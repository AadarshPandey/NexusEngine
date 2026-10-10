import os
import re
import json

with open("mappings_with_types.json", "r") as f:
    mappings = json.load(f)

model_dir = "NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model"

for filename in os.listdir(model_dir):
    if not filename.endswith(".java"): continue
    filepath = os.path.join(model_dir, filename)
    with open(filepath, "r") as f:
        content = f.read()

    # Identify class name
    class_match = re.search(r'public\s+class\s+([A-Za-z0-9_]+)', content)
    if not class_match:
        continue
    class_name = class_match.group(1)

    # We need to find the mapping for this class.
    # Since we don't have the table name easily here, let's reverse lookup by old_name / new_name from @Table
    table_match = re.search(r'@Table\(\s*name\s*=\s*"([^"]+)"\s*,\s*schema\s*=\s*"([^"]+)"\s*\)', content)
    if not table_match:
        continue
    current_new_name = table_match.group(1)
    
    mapping = None
    for m in mappings.values():
        if m["new_name"] == current_new_name:
            mapping = m
            break
            
    if not mapping:
        continue
        
    # Track if we need imports
    needs_offset_datetime = False
    needs_local_date = False

    # Process fields
    field_pattern = re.compile(r'((?:@[A-Za-z0-9_.]+\s*(?:\([^)]*\))?\s*)*)(private\s+([A-Za-z0-9_<>\.]+)\s+([A-Za-z0-9_]+)\s*(?:=\s*[^;]+)?\s*;)')
    
    new_content = ""
    last_idx = 0
    
    for match in field_pattern.finditer(content):
        start = match.start()
        end = match.end()
        annotations = match.group(1)
        field_decl_full = match.group(2)
        field_type = match.group(3)
        field_name = match.group(4)
        
        # Get old_col
        col_match = re.search(r'@[A-Za-z0-9_.]*Column\([^)]*name\s*=\s*"([^"]+)"[^)]*\)', annotations)
        if col_match:
            new_col = col_match.group(1)
        else:
            # wait, in Phase 1 we renamed them in @Column!
            # so the DB column name is whatever is in @Column, or snake_case of field_name
            # Wait, if we added @Column, it's the NEW column name!
            pass
            
        new_col = col_match.group(1) if col_match else re.sub('(.)([A-Z][a-z]+)', r'\1_\2', field_name).lower()
        new_col = re.sub('([a-z0-9])([A-Z])', r'\1_\2', new_col).lower()
        
        # Find the type mapping from new_col
        col_mapping = None
        for old_col, c_map in mapping.get("columns", {}).items():
            if c_map.get("new_name") == new_col:
                col_mapping = c_map
                break
                
        new_annotations = annotations
        new_field_type = field_type
        
        if col_mapping:
            db_type = col_mapping["type"].lower()
            
            # 1. Boolean flags
            if db_type == "boolean" and field_type in ("Integer", "int", "Short"):
                if "@Convert" not in new_annotations:
                    new_annotations += '    @jakarta.persistence.Convert(converter = com.nexusengine.core.common.converter.IntegerToBooleanConverter.class)\n'
            
            # 2. Smallint
            elif db_type == "smallint" and field_type in ("Integer", "int"):
                if "JdbcTypeCode" not in new_annotations:
                    new_annotations += '    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.SMALLINT)\n'
                    
            # 3. Timestamptz -> OffsetDateTime
            if "timestamptz" in col_mapping.get("full_type", "").lower():
                if field_type in ("LocalDateTime", "Date"):
                    new_field_type = "OffsetDateTime"
                    needs_offset_datetime = True
            
            # 4. Date -> LocalDate
            if "date" in col_mapping.get("full_type", "").lower() and "timestamptz" not in col_mapping.get("full_type", "").lower():
                if field_type in ("LocalDateTime", "Date"):
                    new_field_type = "LocalDate"
                    needs_local_date = True
        
        # replace field_type in decl if changed
        if new_field_type != field_type:
            field_decl_full = field_decl_full.replace(f" {field_type} ", f" {new_field_type} ", 1)
            
        # formatting cleanup
        if new_annotations and not new_annotations.startswith("    ") and not new_annotations.startswith("\t"):
            lines = [("    " + line.strip()) if line.strip() else line for line in new_annotations.split("\n")]
            new_annotations = "\n".join(lines)
            if not new_annotations.endswith("\n"):
                new_annotations += "\n"
        elif new_annotations:
            # ensure exact one newline at the end
            new_annotations = new_annotations.rstrip() + "\n"
            
        new_content += content[last_idx:start] + new_annotations + field_decl_full
        last_idx = end

    new_content += content[last_idx:]
    
    # Add imports if needed
    if needs_offset_datetime:
        if "java.time.OffsetDateTime;" not in new_content:
            new_content = new_content.replace("import java.io.Serializable;", "import java.io.Serializable;\nimport java.time.OffsetDateTime;")
    if needs_local_date:
        if "java.time.LocalDate;" not in new_content:
            new_content = new_content.replace("import java.io.Serializable;", "import java.io.Serializable;\nimport java.time.LocalDate;")
            
    # Add missing @Version field
    if class_name in ["PmsProduct", "PmsSkuStock", "OmsOrder", "SmsCoupon"]:
        if "@Version" not in new_content:
            version_str = '\n    @jakarta.persistence.Version\n    private Integer version;\n'
            # Insert before the closing brace
            last_brace_idx = new_content.rfind('}')
            if last_brace_idx != -1:
                new_content = new_content[:last_brace_idx] + version_str + new_content[last_brace_idx:]
                
    with open(filepath, "w") as f:
        f.write(new_content)
        
    print(f"Processed {filename}")
