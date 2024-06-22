-- Inserciones en la tabla 'users'
-- INSERT INTO users (username, password, email, first_name, first_last_name, second_last_name, address, city, province, postal_code, number_phone, relationship) 
-- VALUES 
--   ('admin', '$2a$10$4S7iF8T4UdU86lu/kTY2WOIQ8Q7fYAYTjeOPLZH74C2Gyd7P/PDhu', 'admin@gmail.com', '', '', '', '', '', '', '', '', ''),
--   ('user1', '$2a$10$Gj7XQGLifS79T.qeyHTCA.dKhs3nrAmJVJ.i5G2cB9R2.mLELHPlK', 'user1@gmail.com', '', '', '', '', '', '', '', '', ''),
--   ('user2', '$2a$10$ye/zFlMvCCx7tff6ExhUs.JjNUwrCUdFsu.N27GvpKnztMlg/gScS', 'user2@gmail.com', '', '', '', '', '', '', '', '', ''),
--   ('user3', '$2a$10$7Vg3cAi0IJ4e/WvJnpRNxuaUXtLriNqenUd6ygjRSXgGjsP8i9doi', 'user3@gmail.com', '', '', '', '', '', '', '', '', '');

/*Users*/
INSERT INTO users(id_user, username, password, email) VALUES (default, 'admin', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO', 'admin@gmail.com');
INSERT INTO users(id_user, username, password, email) VALUES (default, 'user1', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO', 'user1@gmail.com');
INSERT INTO users(id_user, username, password, email) VALUES (default, 'user2', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO', 'user2@gmail.com');
INSERT INTO users(id_user, username, password, email) VALUES (default, 'user3', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO', 'user3@gmail.com');

/*Profiles*/

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 1, 'Stella', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'Gijon', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 2, 'Name2', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 3, 'Name3', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 4, 'Name4', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

-- Inserciones en la tabla 'roles'
INSERT INTO roles (id_role, name) VALUES (default, 'ROLE_ADMIN');
INSERT INTO roles (id_role, name) VALUES (default, 'ROLE_USER');

-- Asociación entre usuarios y roles
INSERT INTO roles_users (role_id, user_id) VALUES (1, 1);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 2);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 3);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 4);

-- Inserciones en la tabla 'posts'
INSERT INTO posts (title, message, user_id) VALUES 
  ('Post 1', 'Este es el primer post.', 1),
  ('Post 2', 'Este es el segundo post.', 1),
  ('Post 3', 'Este es el tercer post.', 1);

-- Inserciones en la tabla 'replies'
INSERT INTO replies (message, user_id, post_id) VALUES
  ('Respuesta 1 del admin', 1, 1),
  ('Respuesta 2 del user1', 2, 1),
  ('Respuesta 3 del admin', 1, 2),
  ('Respuesta 4 del user2', 3, 2),
  ('Respuesta 5 del user3', 4, 3),
  ('Respuesta 6 del admin', 1, 3);

-- -- Inserciones en la tabla 'favorites'
-- INSERT INTO favorites (user_id, reply_id) VALUES
--   (2, 1),
--   (3, 1),
--   (4, 1),
--   (1, 2),
--   (3, 3),
--   (4, 5);
