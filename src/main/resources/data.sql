

insert into ROLES (name) values ('ROLE_USER');
insert into ROLES (name) values ('ROLE_MODERATOR');
insert into ROLES (name) values ('ROLE_ADMIN');
--
-- INSERT INTO users (username, email, password, role) VALUES ('nick964', 'nickr964@gmail.com', 'mytestpass', 'ROLE_USER')
--
-- INSERT INTO mygroups (name, group_key, role, icon) VALUES ('nickgroup', 'ABC', 1, '')
--
-- INSERT INTO members (group_id, user_id) VALUES (1, 1)
--
 INSERT INTO question_option (id, option, input_type) VALUES (1, 'O,U', 'radio');
 INSERT INTO question_option (id, option, input_type) VALUES (2, 'Y,N', 'radio');

 INSERT INTO questions (id, text, section, question_type, line_value) VALUES (1, '1st Quarter', 'Game Points', 1, 9.5);
 INSERT INTO questions (id, text, section, question_type, line_value) VALUES (2, '2nd Quarter', 'Game Points', 1, 13.5);
INSERT INTO questions (id, text, section, question_type, line_value) VALUES (3, '3rd Quarter', 'Game Points', 1, 13.5);
INSERT INTO questions (id, text, section, question_type, line_value) VALUES (4, '4th Quarter', 'Game Points', 1, 13.5);
INSERT INTO questions (id, text, section, question_type, line_value) VALUES (5, 'Heads or Tails', 'National Anthem', 1, 13.5);
INSERT INTO questions (id, text, section, question_type, line_value) VALUES (6, 'Winner of Coin Toss', 'National Anthem', 1, 13.5);

 INSERT INTO questions (id, text, section, question_type) VALUES (15, 'First Song "Umbrella"', 'Halftime', 2);
 INSERT INTO questions (id, text, section, question_type) VALUES (16, 'Last Song Diamonds', 'Halftime', 2);
--

INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (1, 1, 1);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (2, 2, 1);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (3, 3, 1);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (4, 4, 1);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (5, 5, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (6, 6, 2);

INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (7, 15, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (8, 16, 2);

--
 --INSERT INTO master_answers (id, question_id, answer) VALUES (1, 1, 'N')
