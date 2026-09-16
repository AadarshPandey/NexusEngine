-- V24: Remove irrelevant fields from ums_member

ALTER TABLE ums_member DROP COLUMN IF EXISTS job;
ALTER TABLE ums_member DROP COLUMN IF EXISTS city;
