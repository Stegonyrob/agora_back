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
INSERT INTO users(id_user, username, password, email) VALUES (default, 'user4', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO', 'user3@gmail.com');
INSERT INTO users(id_user, username, password, email) VALUES (default, 'user5', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO', 'user3@gmail.com');
INSERT INTO users(id_user, username, password, email) VALUES (default, 'user6', '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO', 'user3@gmail.com');

/*Profiles*/

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 1, 'Ivan', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 2, 'Stella', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'Gijon', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 3, 'Name3', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 4, 'Name4', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 5, 'Name5', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 6, 'Name5', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

INSERT INTO profiles(id_profile, user_id, first_Name, last_Name1, last_name2, username, relationship, email, password, confirm_password, city, favorite) VALUES (default, 7, 'Name7', 'Apellido1', 'SegApellido1', 'TheBoss', 'Cool', 'admin@gmail.com', '', '', 'city', false);

-- Inserciones en la tabla 'roles'
INSERT INTO roles (id_role, name) VALUES (default, 'ROLE_ADMIN'); 
INSERT INTO roles (id_role, name) VALUES (default, 'ROLE_USER');

-- Asociación entre usuarios y roles
INSERT INTO roles_users (role_id, user_id) VALUES (1, 1);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 2);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 3);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 4);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 5);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 6);
INSERT INTO roles_users (role_id, user_id) VALUES (2, 7);

-- Inserciones en la tabla 'posts'
INSERT INTO posts (title, message, user_id, creation_date, archived) VALUES 
  ('Post 1', 'Comer es un desafío diario para mí. No es solo cuestión de elegir qué quiero comer, sino de superar las barreras que mi cerebro y mi cuerpo me ponen. Tengo selectividad alimentaria por texturas y colores, lo que significa que solo como alimentos que se ajustan a mis estándares de textura y color. La carne debe ser suave y tierna, no puede tener grasa visible ni tener un color demasiado intenso. Las verduras deben ser crujientes y frescas, no pueden estar cocidas ni tener un color demasiado oscuro. Los frutos deben ser dulces y jugosos, no pueden ser ácidos ni tener un color demasiado brillante. Cada comida es un reto, cada bocado una lucha. Me siento aislada en una sociedad que valora la variedad y la experimentación culinaria. Me siento culpable por no poder disfrutar de una comida familiar o compartir un plato con amigos. Me siento frustrada por no poder explicarles a los demás por qué no como ciertas cosas. Me siento sola en mi lucha diaria por encontrar alimentos que se ajusten a mis necesidades. Pero también me siento fuerte por haber aprendido a adaptarme y a encontrar formas de superar mis limitaciones. Me siento orgullosa de haber encontrado una comunidad en línea que entiende mi lucha y me apoya. Comer es un desafío, pero no es imposible. Y aunque sea difícil, vale la pena luchar por encontrar la comida que me hace sentir bien.', 1, now(), false),
  ('Post 2', 'Este es el segundo post. dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.', 1, now(), false),
  ('Post 3', 'Este es el tercer post.since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. ', 1, now(),false);

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

-- Inserciones en la tabla 'texts'
INSERT INTO texts (id, title, description, image, name_image) VALUES
  (1, '¿Quienes Somos?', 'Somos un centro educativo que trabaja en el ámbito de la neurodiversidad así como en el refuerzo educativo individualizado. Nuestro objetivo principal es el desarrollo de personas respetando su individualidad y concentrándonos en sus potencialidades, priorizando a la persona como elemento clave de la intervención.', '../../../public/images/img/edificio.jpg', 'edificio'),
  (2, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;', 'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.', '../../../public/images/img/escritorio.jpg', 'escritorio'),
  (3, 'Reeducación pedagógica', 'Apoyo educativo, Intervención en NEE y NEAE, Cuidado especializado por horas, Intervención educativa con familias y en el hogar, Coordinación multidisciplinar con centros educativos y particulares, Somos beneficiarios de las becas de reeducación pedagógica de NEAE y NEE.', '../../../public/images/img/niñoPuzzle.jpg', 'niñoPuzzle'),
  (4, 'Educación Psicomotriz', 'Sesiones diseñadas para fomentar el desarrollo integral de los niños a través del movimiento y la interacción con el entorno. Se trabaja la coordinación, el equilibrio, la conciencia corporal y otras habilidades motoras fundamentales para el aprendizaje y el bienestar.', '../../../public/images/img/niñaFicha.jpg', 'educacionPsicomotriz'),
  (5, 'Refuerzo de Inglés en Grupos Reducidos', 'Clases de apoyo para el aprendizaje del inglés, impartidas en grupos pequeños para garantizar una atención más personalizada. Se refuerzan los contenidos curriculares, se trabajan las habilidades comunicativas y se fomenta la confianza en el uso del idioma.', '../../../public/images/img/alumnosOrdenador.jpg', 'refuerzoIngles'),
  (6, 'Refuerzo Educativo Especializado en Grupos Pequeños', 'Apoyo académico individualizado o en grupos muy reducidos para alumnos con necesidades educativas especiales o dificultades de aprendizaje. Se adaptan las estrategias y los materiales para atender las particularidades de cada estudiante y facilitar su progreso.', '../../../public/images/img/adolescentesGrupal.jpg', 'refuerzoEducativo'),
  (7, 'Talleres Temáticos Educativos (Vacaciones)', 'Programas especiales durante los periodos vacacionales con temáticas diversas y educativas. Estos talleres buscan ofrecer un espacio de aprendizaje lúdico y creativo, explorando diferentes áreas de conocimiento y desarrollando nuevas habilidades de manera divertida.', '../../../public/images/img/niñoFichas.jpg', 'talleresTematicos'),
  (8, 'Taller Juegos de Mesa', 'Espacio dedicado al aprendizaje y desarrollo a través de los juegos de mesa. Se fomenta el pensamiento estratégico, la resolución de problemas, la cooperación, el respeto por las normas y las habilidades sociales en un ambiente divertido y participativo. Un aspecto fundamental del taller es la gestión del tiempo y la paciencia al esperar el turno de juego, promoviendo la escucha activa y la consideración por los demás participantes.', '../../../public/images/img/niñaFicha.jpg', 'juegosDeMesa'),
  (9, 'Escuela de Padres', 'Programa de formación y apoyo dirigido a padres y madres. Se ofrecen herramientas, estrategias y recursos para afrontar los retos de la crianza, mejorar la comunicación familiar y promover un desarrollo saludable de los hijos.', '../../../public/images/img/niñoPuzzle.jpg', 'escuelaPadres'),
  (10, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;', 'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.', '../../../public/images/img/niñoCascos.jpg', 'niñoCascos'),
  (11, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;', 'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.', '../../../public/images/img/alumnosOrdenador.jpg', 'alumnosOrdenador'),
  (12, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;', 'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.', '../../../public/images/img/alumnosOrdenador.jpg', 'alumnosOrdenador'),
  (13, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;', 'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.', '../../../public/images/img/alumnosOrdenador.jpg', 'alumnosOrdenador'),
  (14, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;', 'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.', '../../../public/images/img/alumnosOrdenador.jpg', 'alumnosOrdenador'),
  (15, 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;', 'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.', '../../../public/images/img/ivan.jpg', 'ivan');