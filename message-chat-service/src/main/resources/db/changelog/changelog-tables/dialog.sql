--liquibase formatted sql

--changeset dialog:1
CREATE TABLE dialog (
                        id SERIAL PRIMARY KEY,
                        dialog_id VARCHAR(36),
                        name VARCHAR(50),
                        version BIGINT,
                        status VARCHAR(10),
                        type VARCHAR(10),
                        create_at TIMESTAMP,
                        modify_at TIMESTAMP,
                        create_by_member_id VARCHAR(36),
                        modify_by_member_id VARCHAR(36),

                        CONSTRAINT dialog_id_uq UNIQUE (dialog_id)
);