import yaml
import glob
import os

files = glob.glob('NexusCore/**/application*.yml', recursive=True)

for file_path in files:
    with open(file_path, 'r') as f:
        content = f.read()
    
    # Remove the duplicate spring section added at the end
    duplicate_spring = "\nspring:\n  flyway:\n    enabled: true\n    baseline-on-migrate: true\n    baseline-version: 1\n"
    duplicate_spring_no_nl = "\nspring:\n  flyway:\n    enabled: true\n    baseline-on-migrate: true\n    baseline-version: 1"
    
    if duplicate_spring in content:
        content = content.replace(duplicate_spring, "")
    elif duplicate_spring_no_nl in content:
        content = content.replace(duplicate_spring_no_nl, "")
        
    if "flyway:" not in content:
        # We need to insert flyway under the first spring:
        if "spring:" in content:
            lines = content.split('\n')
            new_lines = []
            for line in lines:
                new_lines.append(line)
                if line.startswith("spring:"):
                    new_lines.append("  flyway:")
                    new_lines.append("    enabled: true")
                    new_lines.append("    baseline-on-migrate: true")
                    new_lines.append("    baseline-version: 1")
            content = '\n'.join(new_lines)

    with open(file_path, 'w') as f:
        f.write(content)
