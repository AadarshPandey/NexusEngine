import re
import sys
import os

def generate_javadoc_for_line(line):
    line = line.strip()
    if not line:
        return ""
    
    if line.startswith("class ") or line.startswith("public class ") or line.startswith("public interface ") or line.startswith("public abstract class "):
        name = line.split("class ")[-1].split("interface ")[-1].split("<")[0].split(" ")[0]
        return f"/**\n * Represents the {name} component.\n * Provides core functionality and operations for {name}.\n */"
    
    if "(" in line and ")" in line:
        if "return" not in line and not line.startswith("void ") and not " void " in line and not line.startswith("public void"):
            has_return = True
        else:
            has_return = False
        
        params_part = line[line.find("(")+1:line.rfind(")")]
        params = []
        if params_part:
            for p in params_part.split(","):
                p = p.strip()
                if p:
                    parts = p.split(" ")
                    if len(parts) >= 2:
                        params.append(parts[-1])
        
        doc = "/**\n     * Executes the operation.\n"
        for p in params:
            doc += f"     * @param {p} the {p}\n"
        if has_return and not (" void " in line or line.startswith("void ")):
            doc += "     * @return the result of the operation\n"
        doc += "     */"
        return doc
        
    else:
        name = line.split(" ")[-1].replace(";", "")
        return f"/**\n     * The {name} property.\n     */"


def process_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # We need to replace /**\s**/ and /**\s** Auto-generated documentation\s**/
    
    # standardize "Auto-generated documentation" to just empty first so we can split easily
    content = re.sub(r'/\*\*\s*\* Auto-generated documentation(?:\s*\* Auto-generated documentation)*\s*\*/', '/** */', content)
    
    parts = re.split(r'/\*\*\s*\*/', content)
    if len(parts) <= 1:
        return False
    
    new_content = parts[0]
    for i in range(1, len(parts)):
        next_part = parts[i]
        
        lines = next_part.split('\n')
        next_code_line = ""
        for line in lines:
            if line.strip() and not line.strip().startswith("@") and not line.strip().startswith("//"):
                next_code_line = line.strip()
                break
        
        javadoc = generate_javadoc_for_line(next_code_line)
        
        if next_code_line and not next_code_line.startswith("public class ") and not next_code_line.startswith("public interface ") and not next_code_line.startswith("class "):
            if not javadoc.startswith("    /**"):
                javadoc = javadoc.replace("/**", "    /**")
                
        if "    /**" in javadoc and ("public class" in next_code_line or "public interface" in next_code_line):
            javadoc = javadoc.replace("    /**", "/**").replace("     *", " *")
            
        new_content += javadoc + next_part

    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(new_content)
    return True

count = 0
for root, dirs, files in os.walk("."):
    for f in files:
        if f.endswith(".java"):
            if process_file(os.path.join(root, f)):
                count += 1
print(f"Processed {count} files")
