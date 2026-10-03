import re

with open('./resource_setup/postgres_data.sql', 'r') as f:
    content = f.read()

content = content.replace(
    'like_count integer,',
    'like_count integer,\n    dislike_count integer,'
)
content = content.replace(
    'COPY catalog.pms_review (id, product_id, member_id, parent_id, rating, content, like_count, status, created_time)',
    'COPY catalog.pms_review (id, product_id, member_id, parent_id, rating, content, like_count, dislike_count, status, created_time)'
)
content = content.replace(
    'COPY public.pms_review (id, product_id, member_id, parent_id, rating, content, like_count, status, created_time)',
    'COPY public.pms_review (id, product_id, member_id, parent_id, rating, content, like_count, dislike_count, status, created_time)'
)
with open('./resource_setup/postgres_data.sql', 'w') as f:
    f.write(content)
