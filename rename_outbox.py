import os
import re

def replace_in_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    original_content = content
    
    # 1. Update application*.yml flyway schemas
    if filepath.endswith('.yml'):
        content = re.sub(r'migration,catalog,orders,users,marketing,ai,system,integration', 
                         r'migration,catalog,orders,users,marketing,ai,system,messaging', content)
        content = re.sub(r'migration,pms,orders,users,marketing,ai,system,integration', 
                         r'migration,pms,orders,users,marketing,ai,system,messaging', content)

    # 2. Update SQL files
    if filepath.endswith('.sql'):
        # V1__baseline.sql
        content = re.sub(r'CREATE TABLE IF NOT EXISTS outbox_event', 'CREATE TABLE IF NOT EXISTS staged_event', content)
        # V11__move_tables_to_schemas.sql
        content = re.sub(r'SCHEMA IF NOT EXISTS integration', 'SCHEMA IF NOT EXISTS messaging', content)
        content = re.sub(r'public\.outbox_event', 'public.staged_event', content)
        content = re.sub(r"table_name='outbox_event'", "table_name='staged_event'", content)
        content = re.sub(r"relname = 'outbox_event'", "relname = 'staged_event'", content)
        content = re.sub(r"nspname = 'integration'", "nspname = 'messaging'", content)
        content = re.sub(r"SCHEMA integration", "SCHEMA messaging", content)

    # 3. Update Java files
    if filepath.endswith('.java'):
        content = re.sub(r'@Table\(name = "outbox_event", schema = "integration"\)', '@Table(name = "staged_event", schema = "messaging")', content)
        content = re.sub(r'OutboxEvent', 'StagedEvent', content)
        content = re.sub(r'outbox_event', 'staged_event', content)
        content = re.sub(r'outboxEvent', 'stagedEvent', content)

    if content != original_content:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk('./NexusCore'):
    # skip target dirs
    if 'target' in root:
        continue
    for file in files:
        if file.endswith(('.java', '.xml', '.yml', '.sql')):
            replace_in_file(os.path.join(root, file))
