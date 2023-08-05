--liquibase formatted sql

--changeset dialog-index:1
CREATE  UNIQUE  INDEX  dialog_id_idx  ON  dialog USING btree (dialog_id ASC);