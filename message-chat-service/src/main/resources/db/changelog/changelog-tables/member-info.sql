--liquibase formatted sql

--changeset member-info:1
CREATE TABLE member_info (
                        id INTEGER,
                        firs_name VARCHAR(50),
                        last_name VARCHAR(50),
                        nick_name VARCHAR(50),

                        CONSTRAINT m_info_pkey PRIMARY KEY (id)
);