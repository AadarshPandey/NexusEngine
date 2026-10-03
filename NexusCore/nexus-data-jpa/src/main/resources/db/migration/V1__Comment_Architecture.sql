-- 1. Create a temporary column to store the newly created comment ID
ALTER TABLE catalog.pms_comment_reply ADD COLUMN IF NOT EXISTS reply_comment_id BIGINT;

-- 2. Insert all existing replies into the pms_comment table
-- (We use a CTE or PL/pgSQL to do this if there's existing data, but for empty/small DB we can do a simple INSERT)
-- For Postgres, we can do it using a CTE if needed. Assuming there's data:
WITH inserted_comments AS (
  INSERT INTO catalog.pms_comment (product_id, member_nick_name, create_time, content, member_icon, show_status)
  SELECT 
    (SELECT product_id FROM catalog.pms_comment pc WHERE pc.id = pcr.comment_id LIMIT 1),
    pcr.member_nick_name,
    pcr.create_time,
    pcr.content,
    pcr.member_icon,
    1 -- default show status
  FROM catalog.pms_comment_reply pcr
  RETURNING id, content, create_time, member_nick_name
)
UPDATE catalog.pms_comment_reply pcr
SET reply_comment_id = ic.id
FROM inserted_comments ic
WHERE pcr.content = ic.content 
  AND pcr.member_nick_name = ic.member_nick_name 
  AND pcr.create_time = ic.create_time;

-- 3. Drop the old columns from pms_comment_reply
ALTER TABLE catalog.pms_comment_reply 
  DROP COLUMN IF EXISTS member_nick_name,
  DROP COLUMN IF EXISTS member_icon,
  DROP COLUMN IF EXISTS content,
  DROP COLUMN IF EXISTS create_time,
  DROP COLUMN IF EXISTS type;

-- 4. Add constraints
ALTER TABLE catalog.pms_comment_reply
  ADD CONSTRAINT fk_pms_comment_reply_parent
  FOREIGN KEY (comment_id) REFERENCES catalog.pms_comment(id) ON DELETE CASCADE;

ALTER TABLE catalog.pms_comment_reply
  ADD CONSTRAINT fk_pms_comment_reply_child
  FOREIGN KEY (reply_comment_id) REFERENCES catalog.pms_comment(id) ON DELETE CASCADE;
