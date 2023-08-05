--liquibase formatted sql

--changeset dialog-by-member:1
CREATE TABLE dialog_by_member (
                        dialog_id VARCHAR(36),
                        member_id VARCHAR(36),
                        member_joined_to_dialog_at TIMESTAMP,

                        CONSTRAINT dialog_by_member_pkey PRIMARY KEY (dialog_id, member_id)
);