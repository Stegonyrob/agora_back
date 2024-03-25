INSERT INTO users (username, password, email) VALUES ('admin', '$2a$10$4S7iF8T4UdU86lu/kTY2WOIQ8Q7fYAYTjeOPLZH74C2Gyd7P/PDhu', 'admin@gmail.com');
INSERT INTO users (username, password, email) VALUES ('user1', '$2a$10$Gj7XQGLifS79T.qeyHTCA.dKhs3nrAmJVJ.i5G2cB9R2.mLELHPlK$2a$10$Gj7XQGLifS79T.qeyHTCA.dKhs3nrAmJVJ.i5G2cB9R2.mLELHPlK', 'admin@gmail.com');
INSERT INTO users (username, password, email) VALUES ('user2', '$2a$10$ye/zFlMvCCx7tff6ExhUs.JjNUwrCUdFsu.N27GvpKnztMlg/gScS', 'admin@gmail.com');
INSERT INTO users (username, password, email) VALUES ('user3', '$2a$10$7Vg3cAi0IJ4e/WvJnpRNxuaUXtLriNqenUd6ygjRSXgGjsP8i9doi', 'admin@gmail.com');
INSERT INTO roles (id_role, name) VALUES (default, "ROLE_ADMIN");
INSERT INTO roles (id_role, name) VALUES (default, "ROLE_USER");
INSERT INTO roles_users (role_id, user_id) VALUES (1,1);
INSERT INTO roles_users (role_id, user_id) VALUES (2,2);
INSERT INTO roles_users (role_id, user_id) VALUES (2,3);
INSERT INTO roles_users (role_id, user_id) VALUES (2,4);

INSERT INTO posts (title, message, user_id) VALUES ('Post 1 de Admin', 'Este es el primer post del administrador.', 1);
INSERT INTO posts (title, message, user_id) VALUES ('Post 2 de Admin', 'Este es el segundo post del administrador.', 1);
INSERT INTO posts (title, message, user_id) VALUES ('Post 3 de Admin', 'Este es el tercer post del administrador.', 1);

INSERT INTO posts (title, message, user_id) VALUES ('Post 1 de User1', 'Este es el primer post del usuario 1.', 2);
INSERT INTO posts (title, message, user_id) VALUES ('Post 2 de User1', 'Este es el segundo post del usuario 1.', 2);
INSERT INTO posts (title, message, user_id) VALUES ('Post 3 de User1', 'Este es el tercer post del usuario 1.', 2);

INSERT INTO posts (title, message, user_id) VALUES ('Post 1 de User2', 'Este es el primer post del usuario 2.', 3);
INSERT INTO posts (title, message, user_id) VALUES ('Post 2 de User2', 'Este es el segundo post del usuario 2.', 3);
INSERT INTO posts (title, message, user_id) VALUES ('Post 3 de User2', 'Este es el tercer post del usuario 2.', 3);

INSERT INTO posts (title, message, user_id) VALUES ('Post 1 de User3', 'Este es el primer post del usuario 3.', 4);
INSERT INTO posts (title, message, user_id) VALUES ('Post 2 de User3', 'Este es el segundo post del usuario 3.', 4);
INSERT INTO posts (title, message, user_id) VALUES ('Post 3 de User3', 'Este es el tercer post del usuario 3.', 4);