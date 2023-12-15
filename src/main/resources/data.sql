

insert into roles (name) values ('ROLE_USER');
insert into roles (name) values ('ROLE_MODERATOR');
insert into roles (name) values ('ROLE_ADMIN');
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
INSERT INTO questions (id, text, section, question_type) VALUES (7, 'First Song "Umbrella"', 'Halftime', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (8, 'Last Song Diamonds', 'Halftime', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (9, 'Will Mary J. Blige Perform First?', 'Yes or No Propositions', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (10, 'Will Eminem Sing “Lose Yourself”?', 'Yes or No Propositions', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (11, 'Will Snoop Dogg Smoke and Sing “The Next Episode”?', 'Yes or No Propositions', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (12, 'Will Kendrick Lamar Sing “HUMBLE”?', 'Yes or No Propositions', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (13, 'Will Dr. Dre Sing “California Love”?', 'Yes or No Propositions', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (14, 'Will the First Commercial Break Contain a Car Commercial?', 'Yes or No Propositions', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (15, 'Will a Cheetos Commercial Appear Before a Pringles Commercial?', 'Yes or No Propositions', 2);
INSERT INTO questions (id, text, section, question_type) VALUES (16, 'Will a Captain Morgan Commercial Appear Before a BMW Commercial?', 'Yes or No Propositions', 2);


INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (1, 1, 1);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (2, 2, 1);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (3, 3, 1);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (4, 4, 1);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (5, 5, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (6, 6, 2);

INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (7, 7, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (8, 8, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (9, 9, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (10, 10, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (11, 11, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (12, 12, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (13, 13, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (14, 14, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (15, 15, 2);
INSERT INTO question_option_relation (id, question_id, question_option_id) VALUES (16, 16, 2);

INSERT INTO configsetup (id, rule, enabled) values (1, 'game_started', false)
--
 --INSERT INTO master_answers (id, question_id, answer) VALUES (1, 1, 'N')
