-- insert into roles (name) values ('ROLE_USER');
-- insert into roles (name) values ('ROLE_MODERATOR');
-- insert into roles (name) values ('ROLE_ADMIN');
--
-- INSERT INTO users (username, email, password, role) VALUES ('nick964', 'nickr964@gmail.com', 'mytestpass', 'ROLE_USER')
--
-- INSERT INTO mygroups (name, group_key, role, icon) VALUES ('nickgroup', 'ABC', 1, '')
--
-- INSERT INTO members (group_id, user_id) VALUES (1, 1)
--
-- INSERT INTO question_option (option, input_type) VALUES ('O,U', 'radio')
-- INSERT INTO question_option (option, input_type) VALUES ('Y,N', 'radio')
--
 INSERT INTO questions (id, text, section, question_type, line_value) VALUES (1, '1st Quarter', 'Game Points', 1, 9.5)
-- INSERT INTO questions (text, section, question_type, line_value) VALUES ('2nd Quarter', 'Game Points', 1, 9.5)
-- INSERT INTO questions (text, section, question_type) VALUES ('First Song "Umbrella"', 'Halftime', 2)
-- INSERT INTO questions (text, section, question_type) VALUES ('Last Song Diamonds', 'Halftime', 2)
--
-- INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (1, 1, 1)
-- INSERT INTO question_option_relation (question_id, question_option_id) VALUES (2, 1)
-- INSERT INTO question_option_relation (question_id, question_option_id) VALUES (3, 2)
-- INSERT INTO question_option_relation (question_id, question_option_id) VALUES (4, 2)
--
 INSERT INTO master_answers (id, question_id, answer) VALUES (1, 1, 'N')
