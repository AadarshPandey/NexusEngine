import os
import re

def to_camel_case(s):
    return s[0].lower() + s[1:]

def convert_to_record(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    # Must have @Data, @Getter, or @Setter to be a DTO
    if not re.search(r'@(Data|Getter|Setter)', content):
        return
        
    # Skip if it has 'extends'
    if ' extends ' in content:
        return
        
    # Skip if it has nested classes
    if 'public static class' in content or 'public class ' in content.split('public class ')[1:]:
        return
        
    class_match = re.search(r'public class (\w+)(?:<.*?>)?\s*(?:implements\s+[\w,\s<>]+)?\s*\{', content)
    if not class_match:
        return
        
    class_name = class_match.group(1)
    class_start = class_match.start()
    class_end = class_match.end()
    
    body = content[class_end:content.rfind('}')]
    
    fields = []
    field_regex = r'((?:\s*@[^\n]+)*)\s*(?:private|protected)\s+([\w<>\[\]\?]+)\s+(\w+)\s*(?:=[^;]+)?;'
    
    def replace_field(m):
        annotations = re.sub(r'\s+', ' ', m.group(1)).strip()
        type_name = m.group(2)
        field_name = m.group(3)
        if annotations:
            fields.append(f"{annotations} {type_name} {field_name}")
        else:
            fields.append(f"{type_name} {field_name}")
        return ''
        
    new_body = re.sub(field_regex, replace_field, body)
    new_body_clean = re.sub(r'^\s*$\n', '', new_body, flags=re.MULTILINE)
    
    record_params = ",\n    ".join(fields)
    
    # Imports
    content = re.sub(r'import lombok\.(Data|Getter|Setter|EqualsAndHashCode|NoArgsConstructor|AllArgsConstructor|Builder);\n', '', content)
    if 'import lombok.Builder;' not in content:
        content = re.sub(r'(package .*?;\n)', r'\1\nimport lombok.Builder;\n', content)
        
    if new_body_clean.strip():
        new_class_def = f"@Builder(toBuilder = true)\npublic record {class_name}(\n    {record_params}\n) {{" + new_body_clean + "\n}"
    else:
        new_class_def = f"@Builder(toBuilder = true)\npublic record {class_name}(\n    {record_params}\n) {{}}"
        
    new_content = content[:class_start] + new_class_def
    
    with open(filepath, 'w') as f:
        f.write(new_content)
    print(f"Converted {filepath}")

def process_directory(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                convert_to_record(os.path.join(root, file))

if __name__ == '__main__':
    process_directory('./NexusCore/nexus-admin/src/main/java/com/nexusengine/core/dto')
    process_directory('./NexusCore/nexus-ai/src/main/java/com/nexusengine/core/ai/dto')
    process_directory('./NexusCore/nexus-portal/src/main/java/com/nexusengine/core/portal/domain')
