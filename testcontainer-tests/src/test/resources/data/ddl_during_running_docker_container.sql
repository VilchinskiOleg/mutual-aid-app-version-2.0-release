CREATE TABLE member_items_no_increment (
     id INTEGER NOT NULL,
     member_id VARCHAR(128),
     first_name VARCHAR(128),
     last_name VARCHAR(128),
     PRIMARY KEY (id)
);

INSERT INTO member_items_no_increment VALUES (1, 'member_id', 'first_name_test_1', 'last_name_test_1')