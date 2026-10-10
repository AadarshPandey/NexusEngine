import os
import re

model_dir = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model"

for root, _, files in os.walk(model_dir):
    for file in files:
        if file.endswith(".java") and file != "IntegerToBooleanConverter.java":
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
            
            # Find all fields that are `private Integer something;` with `@Column(name = "is_...")`
            # We can just look for `@Column(name = "is_.*")\n    private Integer`
            
            # Wait, there might be annotations between @Column and private Integer
            # Let's match line by line
            
            lines = content.split('\n')
            new_lines = []
            modified = False
            
            needs_convert_import = False
            needs_converter_import = False
            
            i = 0
            while i < len(lines):
                line = lines[i]
                if re.search(r'@Column\(name = "is_[^"]+"', line):
                    # Check next few lines for `private Integer`
                    for j in range(i, min(i+5, len(lines))):
                        if "private Integer" in lines[j]:
                            # This field needs a converter!
                            # Add @Convert above @Column
                            new_lines.append('    @jakarta.persistence.Convert(converter = com.nexusengine.core.model.converter.IntegerToBooleanConverter.class)')
                            modified = True
                            break
                new_lines.append(line)
                i += 1
            
            if modified:
                with open(filepath, "w") as f:
                    f.write('\n'.join(new_lines))
                print(f"Fixed {file}")
