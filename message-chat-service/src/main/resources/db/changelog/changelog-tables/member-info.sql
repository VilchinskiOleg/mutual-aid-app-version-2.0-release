--liquibase formatted sql

--changeset member-info:1
CREATE TABLE member_info (
                        id INTEGER PRIMARY KEY m_info_pkey,
                        firs_name VARCHAR(50),
                        last_name VARCHAR(50),
                        nick_name VARCHAR(50)
);