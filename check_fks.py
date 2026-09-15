import subprocess

# Get all foreign keys from Postgres
cmd = """docker exec postgres psql -U postgres -d nexuscore -t -c "
SELECT 
    tc.table_name, 
    kcu.column_name, 
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name 
FROM 
    information_schema.table_constraints AS tc 
    JOIN information_schema.key_column_usage AS kcu
      ON tc.constraint_name = kcu.constraint_name
      AND tc.table_schema = kcu.table_schema
    JOIN information_schema.constraint_column_usage AS ccu
      ON ccu.constraint_name = tc.constraint_name
      AND ccu.table_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY';"
"""
output = subprocess.check_output(cmd, shell=True).decode('utf-8')
postgres_fks = []
for line in output.split('\n'):
    if '|' in line:
        parts = [p.strip() for p in line.split('|')]
        postgres_fks.append((parts[0], parts[1], parts[2]))

# Get all _id columns from Java codebase
cmd2 = 'grep -r "@Column(name = \\\".*_id\\\")" NexusCore/nexus-data-jpa/src/main/java/com/nexusengine/core/model/'
output2 = subprocess.check_output(cmd2, shell=True).decode('utf-8')

java_relations = []
import re
for line in output2.split('\n'):
    match = re.search(r'model/([^.]+)\.java:\s*@Column\(name = "([^"]+)"\)', line)
    if match:
        entity = match.group(1)
        column = match.group(2)
        table_name = re.sub(r'(?<!^)(?=[A-Z])', '_', entity).lower()
        java_relations.append((table_name, column))

missing = []
for table, column in java_relations:
    # Check if there is ANY foreign key in postgres for this table and column
    found = False
    for pt, pc, _ in postgres_fks:
        if pt == table and pc == column:
            found = True
            break
    if not found:
        missing.append(f"{table}.{column}")

if missing:
    print("Missing FKs in Postgres:")
    for m in missing:
        print(m)
else:
    print("All _id columns have foreign keys in Postgres!")
