--liquibase formatted sql

--changeset message:1
CREATE TABLE message (
                        id SERIAL PRIMARY KEY,
                        message_id VARCHAR(36),
                        author_id INTEGER,
                        dialog_id INTEGER,
                        description VARCHAR(255),
                        create_at TIMESTAMP,
                        modify_at TIMESTAMP,

                        CONSTRAINT message_id_uq UNIQUE (message_id)
);