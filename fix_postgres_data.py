import re

with open('./resource_setup/postgres_data.sql', 'r') as f:
    content = f.read()

# Replace CREATE TABLE lines
# We know the columns are product_attribute VARCHAR(255) and read_count INTEGER
content = re.sub(r'product_attribute character varying\(255\),?', '', content)
content = re.sub(r'read_count integer,', 'dislike_count integer,', content)

# Also match if they are slightly different formatted
content = re.sub(r'product_attribute character varying\(255\) DEFAULT NULL::character varying,?', '', content)
content = re.sub(r'read_count integer DEFAULT 0,', 'dislike_count integer DEFAULT 0,', content)

# For COPY statements
content = content.replace(
    'COPY catalog.pms_comment (id, product_id, member_nick_name, product_name, star, member_ip, create_time, show_status, product_attribute, likes_count, read_count, pics, member_icon, reply_count, content)',
    'COPY catalog.pms_comment (id, product_id, member_nick_name, product_name, star, member_ip, create_time, show_status, likes_count, dislike_count, pics, member_icon, reply_count, content)'
)
content = content.replace(
    'COPY public.pms_comment (id, product_id, member_nick_name, product_name, star, member_ip, create_time, show_status, product_attribute, likes_count, read_count, pics, member_icon, reply_count, content)',
    'COPY public.pms_comment (id, product_id, member_nick_name, product_name, star, member_ip, create_time, show_status, likes_count, dislike_count, pics, member_icon, reply_count, content)'
)

with open('./resource_setup/postgres_data.sql', 'w') as f:
    f.write(content)
