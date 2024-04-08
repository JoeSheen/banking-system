DROP VIEW IF EXISTS audit_view;

TRUNCATE TABLE jv_commit_property RESTART IDENTITY CASCADE;
TRUNCATE TABLE jv_snapshot RESTART IDENTITY CASCADE;
TRUNCATE TABLE jv_global_id RESTART IDENTITY CASCADE;
TRUNCATE TABLE jv_commit RESTART IDENTITY CASCADE;

CREATE VIEW audit_view
AS
SELECT jv_commit.commit_id,
       jv_commit.author,
       jv_commit.commit_date,
       jv_snapshot.type AS audit_type,
       jv_snapshot.STATE AS audit_state,
       jv_snapshot.changed_properties,
       replace(jv_snapshot.managed_type, 'com.sheen.joe.bankingsystem.entity.', '') as entity_type,
       replace(jv_global_id.local_id, '"', '') AS entity_id,
       bu.first_name,
       bu.last_name
FROM jv_commit
JOIN jv_snapshot ON jv_commit.commit_pk = jv_snapshot.commit_fk
JOIN jv_global_id ON jv_snapshot.global_id_fk = jv_global_id.global_id_pk
LEFT JOIN banking_user bu ON jv_commit.author = bu.username;
