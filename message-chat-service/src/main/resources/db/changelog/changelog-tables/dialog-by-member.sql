--liquibase formatted sql

--changeset dialog-by-member:1
CREATE TABLE dialog_by_member (
                        dialog_id INTEGER,
                        member_id INTEGER,
                        member_joined_to_dialog_at TIMESTAMP,

                        CONSTRAINT d_by_m_pkey PRIMARY KEY (dialog_id, member_id)
);