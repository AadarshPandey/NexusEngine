import re
import os

# 1. Clean V6__coupon_enhancements.sql
v6_path = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V6__coupon_enhancements.sql'
with open(v6_path, 'r') as f:
    v6_content = f.read()

v6_content = re.sub(r'CREATE TABLE sms_coupon_brand_relation[\s\S]*?;\n', '', v6_content)
v6_content = re.sub(r'COMMENT ON TABLE sms_coupon_brand_relation.*?\n', '', v6_content)
v6_content = re.sub(r'COMMENT ON COLUMN sms_coupon_brand_relation.*?\n', '', v6_content)
v6_content = re.sub(r'CREATE INDEX idx_coupon_id ON sms_coupon_brand_relation.*?\n', '', v6_content)

with open(v6_path, 'w') as f:
    f.write(v6_content)


# 2. Clean V10__auto_add_foreign_keys.sql
v10_path = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V10__auto_add_foreign_keys.sql'
with open(v10_path, 'r') as f:
    v10_content = f.read()

v10_content = re.sub(r'\s*SELECT 1 FROM information_schema.key_column_usage WHERE constraint_name = \'fk_sms_coupon_brand_relation.*?\n\s*IF NOT FOUND THEN\n\s*ALTER TABLE sms_coupon_brand_relation.*?\n\s*END IF;', '', v10_content)

with open(v10_path, 'w') as f:
    f.write(v10_content)


# 3. Clean V11__move_tables_to_schemas.sql
v11_path = './NexusCore/nexus-data-jpa/src/main/resources/db/migration/V11__move_tables_to_schemas.sql'
with open(v11_path, 'r') as f:
    v11_content = f.read()

v11_content = re.sub(r'\s*IF EXISTS \(SELECT 1 FROM information_schema\.tables\n\s*WHERE table_schema=\'public\' AND table_name=\'sms_coupon_brand_relation\'\)\n\s*THEN\n\s*ALTER TABLE public\.sms_coupon_brand_relation SET SCHEMA marketing;\n\s*END IF;', '', v11_content)
# There is also an index rename block in V11, let's just grep it out safely line by line.
with open(v11_path, 'w') as f:
    f.write(v11_content)


# 4. Clean postgres_data.sql using a line-based state machine
pg_path = './resource_setup/postgres_data.sql'
with open(pg_path, 'r') as f:
    pg_lines = f.readlines()

new_pg_lines = []
skip_copy = False
skip_table = False

i = 0
while i < len(pg_lines):
    line = pg_lines[i]
    if 'sms_coupon_brand_relation' in line:
        if line.startswith('COPY '):
            skip_copy = True
            i += 1
            continue
        elif skip_copy and line.strip() == '\\.':
            skip_copy = False
            i += 1
            continue
        elif 'CREATE TABLE' in line:
            skip_table = True
            i += 1
            continue
        elif skip_table and ');' in line:
            skip_table = False
            i += 1
            continue
        elif skip_table:
            i += 1
            continue
        else:
            i += 1
            continue
    else:
        if skip_copy:
            if line.strip() == '\\.':
                skip_copy = False
            i += 1
            continue
        if skip_table:
            if ');' in line:
                skip_table = False
            i += 1
            continue
        new_pg_lines.append(line)
        i += 1

with open(pg_path, 'w') as f:
    f.writelines(new_pg_lines)

print("Cleaned up scripts")
