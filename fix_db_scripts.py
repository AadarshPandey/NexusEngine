import re

# 1. V1__baseline.sql
with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql', 'r') as f:
    content = f.read()

content = re.sub(r'CREATE TABLE IF NOT EXISTS pms_comment \([\s\S]*?\);\n', '', content)
content = re.sub(r'CREATE TABLE IF NOT EXISTS pms_comment_reply \([\s\S]*?\);\n', '', content)

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V1__baseline.sql', 'w') as f:
    f.write(content)

# 2. V9__add_foreign_keys.sql
with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V9__add_foreign_keys.sql', 'r') as f:
    content = f.read()

content = re.sub(r'ALTER TABLE pms_comment ADD CONSTRAINT fk_pms_comment_product_id FOREIGN KEY \(product_id\) REFERENCES pms_product\(id\);\n', '', content)
content = re.sub(r'ALTER TABLE pms_comment_reply ADD CONSTRAINT fk_pms_comment_reply_comment_id FOREIGN KEY \(comment_id\) REFERENCES pms_comment\(id\);\n', '', content)

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V9__add_foreign_keys.sql', 'w') as f:
    f.write(content)

# 3. V10__auto_add_foreign_keys.sql
with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V10__auto_add_foreign_keys.sql', 'r') as f:
    content = f.read()

content = re.sub(r'[\s]*IF NOT EXISTS \(SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = \'fk_pms_comment_product_id\'\) THEN\n[\s]*ALTER TABLE pms_comment ADD CONSTRAINT fk_pms_comment_product_id FOREIGN KEY \(product_id\) REFERENCES pms_product\(id\);\n[\s]*END IF;\n', '', content)

content = re.sub(r'[\s]*IF NOT EXISTS \(SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = \'fk_pms_comment_reply_comment_id\'\) THEN\n[\s]*ALTER TABLE pms_comment_reply ADD CONSTRAINT fk_pms_comment_reply_comment_id FOREIGN KEY \(comment_id\) REFERENCES pms_comment\(id\);\n[\s]*END IF;\n', '', content)

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V10__auto_add_foreign_keys.sql', 'w') as f:
    f.write(content)

# 4. V11__move_tables_to_schemas.sql
with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V11__move_tables_to_schemas.sql', 'r') as f:
    content = f.read()

content = re.sub(r'[\s]*IF EXISTS \(SELECT 1 FROM information_schema.tables \n\s*WHERE table_schema=\'public\' AND table_name=\'pms_comment\'\) THEN\n\s*ALTER TABLE public\.pms_comment SET SCHEMA catalog;\n\s*END IF;', '', content)
content = re.sub(r'[\s]*IF EXISTS \(SELECT 1 FROM information_schema.tables \n\s*WHERE table_schema=\'public\' AND table_name=\'pms_comment_reply\'\) THEN\n\s*ALTER TABLE public\.pms_comment_reply SET SCHEMA catalog;\n\s*END IF;', '', content)
content = re.sub(r'\s*AND t\.relname = \'pms_comment\'', '', content)
content = re.sub(r'\s*AND t\.relname = \'pms_comment_reply\'', '', content)

with open('./NexusCore/nexus-data-jpa/src/main/resources/db/migration/V11__move_tables_to_schemas.sql', 'w') as f:
    f.write(content)

# 5. postgres_data.sql
with open('./resource_setup/postgres_data.sql', 'r') as f:
    content = f.read()

# Remove the table definitions for public.pms_comment and public.pms_comment_reply
content = re.sub(r'CREATE TABLE public\.pms_comment \([\s\S]*?\);\n', '', content)
content = re.sub(r'CREATE TABLE public\.pms_comment_reply \([\s\S]*?\);\n', '', content)

# Remove ALTER TABLE OWNER TO statements
content = re.sub(r'ALTER TABLE public\.pms_comment OWNER TO postgres;\n', '', content)
content = re.sub(r'ALTER TABLE public\.pms_comment_reply OWNER TO postgres;\n', '', content)

# Remove sequences
content = re.sub(r'CREATE SEQUENCE public\.pms_comment_id_seq[\s\S]*?OWNED BY public\.pms_comment\.id;\n', '', content)
content = re.sub(r'CREATE SEQUENCE public\.pms_comment_reply_id_seq[\s\S]*?OWNED BY public\.pms_comment_reply\.id;\n', '', content)
content = re.sub(r'ALTER SEQUENCE public\.pms_comment_id_seq OWNER TO postgres;\n', '', content)
content = re.sub(r'ALTER SEQUENCE public\.pms_comment_reply_id_seq OWNER TO postgres;\n', '', content)

# Remove COPY statements and their data blocks
# We know they end with '\.\n'
content = re.sub(r'COPY catalog\.pms_comment \([\s\S]*?\) FROM stdin;\n(?:.*?\n)*?\\\.\n', '', content)
content = re.sub(r'COPY public\.pms_comment \([\s\S]*?\) FROM stdin;\n(?:.*?\n)*?\\\.\n', '', content)
content = re.sub(r'COPY catalog\.pms_comment_reply \([\s\S]*?\) FROM stdin;\n(?:.*?\n)*?\\\.\n', '', content)
content = re.sub(r'COPY public\.pms_comment_reply \([\s\S]*?\) FROM stdin;\n(?:.*?\n)*?\\\.\n', '', content)

with open('./resource_setup/postgres_data.sql', 'w') as f:
    f.write(content)

