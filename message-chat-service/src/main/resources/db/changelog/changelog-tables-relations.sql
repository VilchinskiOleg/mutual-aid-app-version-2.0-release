--liquibase formatted sql

--changeset dialog-by-member-relations:1
ALTER TABLE dialog_by_member
    ADD CONSTRAINT dialog_fkey FOREIGN KEY (dialog_id) REFERENCES dialog (id),
    ADD CONSTRAINT member_fkey FOREIGN KEY (member_id) REFERENCES member (id);

--changeset member-info-relations:1
ALTER TABLE member_info
    ADD CONSTRAINT member_fkey FOREIGN KEY (id) REFERENCES member (id);

--changeset message-relations:1
ALTER TABLE message
    ADD CONSTRAINT dialog_fkey FOREIGN KEY (dialog_id) REFERENCES dialog (id),
    ADD CONSTRAINT author_fkey FOREIGN KEY (author_id) REFERENCES member (id);