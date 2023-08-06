--liquibase formatted sql

--changeset member:1
CREATE TABLE member (
                        id SERIAL PRIMARY KEY,
                        profile_id VARCHAR(36),

                        CONSTRAINT profile_id_uq UNIQUE (profile_id)
);