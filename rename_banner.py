import os
import re

def replace_in_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    original_content = content

    # General replacements
    content = re.sub(r'SmsHomeAdvertise', 'PromotionalBanner', content)
    content = re.sub(r'smsHomeAdvertise', 'promotionalBanner', content)
    content = re.sub(r'sys_banner', 'promotional_banner', content)

    # Specific replacements
    if filepath.endswith('.java'):
        content = re.sub(r'@Table\(name = "promotional_banner", schema = "system"\)', '@Table(name = "promotional_banner", schema = "marketing")', content)
    
    if filepath.endswith('.sql'):
        # For V11, we need to change SCHEMA system to SCHEMA marketing only for promotional_banner
        if 'promotional_banner' in content:
            lines = content.split('\n')
            new_lines = []
            in_banner_block = False
            for line in lines:
                if 'promotional_banner' in line:
                    in_banner_block = True
                
                if in_banner_block and 'SCHEMA system' in line:
                    line = line.replace('SCHEMA system', 'SCHEMA marketing')
                    if 'END $$' in line or ';' in line:
                        pass # just keep track
                
                if in_banner_block and 'nspname = \'system\'' in line:
                    line = line.replace('nspname = \'system\'', 'nspname = \'marketing\'')

                new_lines.append(line)
            content = '\n'.join(new_lines)

    if content != original_content:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk('./NexusCore'):
    if 'target' in root:
        continue
    for file in files:
        if file.endswith(('.java', '.xml', '.yml', '.sql')):
            replace_in_file(os.path.join(root, file))
