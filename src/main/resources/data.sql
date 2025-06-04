/*Users*/
INSERT INTO
  users (id_user, username, password, email)
VALUES
  (
    default,
    'admin',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'admin@gmail.com'
  );

INSERT INTO
  users (id_user, username, password, email)
VALUES
  (
    default,
    'user1',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user1@gmail.com'
  );

INSERT INTO
  users (id_user, username, password, email)
VALUES
  (
    default,
    'user2',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user2@gmail.com'
  );

INSERT INTO
  users (id_user, username, password, email)
VALUES
  (
    default,
    'user3',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user3@gmail.com'
  );

INSERT INTO
  users (id_user, username, password, email)
VALUES
  (
    default,
    'user4',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user3@gmail.com'
  );

INSERT INTO
  users (id_user, username, password, email)
VALUES
  (
    default,
    'user5',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user3@gmail.com'
  );

INSERT INTO
  users (id_user, username, password, email)
VALUES
  (
    default,
    'user6',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user3@gmail.com'
  );

/*Profiles*/
INSERT INTO
  profiles (
    id_profile,
    user_id,
    first_Name,
    last_Name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city,
    favorite
  )
VALUES
  (
    default,
    1,
    'Ivan',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city',
    false
  );

INSERT INTO
  profiles (
    id_profile,
    user_id,
    first_Name,
    last_Name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city,
    favorite
  )
VALUES
  (
    default,
    2,
    'Stella',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'Gijon',
    false
  );

INSERT INTO
  profiles (
    id_profile,
    user_id,
    first_Name,
    last_Name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city,
    favorite
  )
VALUES
  (
    default,
    3,
    'Name3',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city',
    false
  );

INSERT INTO
  profiles (
    id_profile,
    user_id,
    first_Name,
    last_Name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city,
    favorite
  )
VALUES
  (
    default,
    4,
    'Name4',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city',
    false
  );

INSERT INTO
  profiles (
    id_profile,
    user_id,
    first_Name,
    last_Name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city,
    favorite
  )
VALUES
  (
    default,
    5,
    'Name5',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city',
    false
  );

INSERT INTO
  profiles (
    id_profile,
    user_id,
    first_Name,
    last_Name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city,
    favorite
  )
VALUES
  (
    default,
    6,
    'Name5',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city',
    false
  );

INSERT INTO
  profiles (
    id_profile,
    user_id,
    first_Name,
    last_Name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city,
    favorite
  )
VALUES
  (
    default,
    7,
    'Name7',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city',
    false
  );

-- Inserciones en la tabla 'roles'
INSERT INTO
  roles (id_role, name)
VALUES
  (default, 'ROLE_ADMIN');

INSERT INTO
  roles (id_role, name)
VALUES
  (default, 'ROLE_USER');

-- Asociación entre usuarios y roles
INSERT INTO
  roles_users (role_id, user_id)
VALUES
  (1, 1);

INSERT INTO
  roles_users (role_id, user_id)
VALUES
  (2, 2);

INSERT INTO
  roles_users (role_id, user_id)
VALUES
  (2, 3);

INSERT INTO
  roles_users (role_id, user_id)
VALUES
  (2, 4);

INSERT INTO
  roles_users (role_id, user_id)
VALUES
  (2, 5);

INSERT INTO
  roles_users (role_id, user_id)
VALUES
  (2, 6);

INSERT INTO
  roles_users (role_id, user_id)
VALUES
  (2, 7);

-- Inserciones en la tabla 'posts'
INSERT INTO
  posts (title, message, user_id, creation_date, archived)
VALUES
  (
    'Post 1',
    'Comer es un desafío diario para mí. No es solo cuestión de elegir qué quiero comer, sino de superar las barreras que mi cerebro y mi cuerpo me ponen. Tengo selectividad alimentaria por texturas y colores, lo que significa que solo como alimentos que se ajustan a mis estándares de textura y color. La carne debe ser suave y tierna, no puede tener grasa visible ni tener un color demasiado intenso. Las verduras deben ser crujientes y frescas, no pueden estar cocidas ni tener un color demasiado oscuro. Los frutos deben ser dulces y jugosos, no pueden ser ácidos ni tener un color demasiado brillante. Cada comida es un reto, cada bocado una lucha. Me siento aislada en una sociedad que valora la variedad y la experimentación culinaria. Me siento culpable por no poder disfrutar de una comida familiar o compartir un plato con amigos. Me siento frustrada por no poder explicarles a los demás por qué no como ciertas cosas. Me siento sola en mi lucha diaria por encontrar alimentos que se ajusten a mis necesidades. Pero también me siento fuerte por haber aprendido a adaptarme y a encontrar formas de superar mis limitaciones. Me siento orgullosa de haber encontrado una comunidad en línea que entiende mi lucha y me apoya. Comer es un desafío, pero no es imposible. Y aunque sea difícil, vale la pena luchar por encontrar la comida que me hace sentir bien.',
    1,
    now(),
    false
  ),
  (
    'Post 2',
    'Este es el segundo post. dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.',
    1,
    now(),
    false
  ),
  (
    'Post 3',
    'Este es el tercer post.since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum. ',
    1,
    now(),
    false
  );

-- Inserciones en la tabla 'replies'
INSERT INTO
  replies (message, user_id, post_id)
VALUES
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
INSERT INTO
  texts (id, title, description, image, name_image)
VALUES
  (
    1,
    '¿Quienes Somos?',
    'Somos un centro educativo que trabaja en el ámbito de la neurodiversidad así como en el refuerzo educativo individualizado. Nuestro objetivo principal es el desarrollo de personas respetando su individualidad y concentrándonos en sus potencialidades, priorizando a la persona como elemento clave de la intervención.',
    '../../../public/images/img/edificio.jpg',
    'edificio'
  ),
  (
    2,
    'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;',
    'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.',
    '../../../public/images/img/escritorio.jpg',
    'escritorio'
  ),
  (
    3,
    'Reeducación pedagógica',
    'Apoyo educativo, Intervención en NEE y NEAE, Cuidado especializado por horas, Intervención educativa con familias y en el hogar, Coordinación multidisciplinar con centros educativos y particulares, Somos beneficiarios de las becas de reeducación pedagógica de NEAE y NEE.',
    '../../../public/images/img/niñoPuzzle.jpg',
    'niñoPuzzle'
  ),
  (
    4,
    'Educación Psicomotriz',
    'Sesiones diseñadas para fomentar el desarrollo integral de los niños a través del movimiento y la interacción con el entorno. Se trabaja la coordinación, el equilibrio, la conciencia corporal y otras habilidades motoras fundamentales para el aprendizaje y el bienestar.',
    '../../../public/images/img/niñaFicha.jpg',
    'educacionPsicomotriz'
  ),
  (
    5,
    'Refuerzo de Inglés en Grupos Reducidos',
    'Clases de apoyo para el aprendizaje del inglés, impartidas en grupos pequeños para garantizar una atención más personalizada. Se refuerzan los contenidos curriculares, se trabajan las habilidades comunicativas y se fomenta la confianza en el uso del idioma.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'refuerzoIngles'
  ),
  (
    6,
    'Refuerzo Educativo Especializado en Grupos Pequeños',
    'Apoyo académico individualizado o en grupos muy reducidos para alumnos con necesidades educativas especiales o dificultades de aprendizaje. Se adaptan las estrategias y los materiales para atender las particularidades de cada estudiante y facilitar su progreso.',
    '../../../public/images/img/adolescentesGrupal.jpg',
    'refuerzoEducativo'
  ),
  (
    7,
    'Talleres Temáticos Educativos (Vacaciones)',
    'Programas especiales durante los periodos vacacionales con temáticas diversas y educativas. Estos talleres buscan ofrecer un espacio de aprendizaje lúdico y creativo, explorando diferentes áreas de conocimiento y desarrollando nuevas habilidades de manera divertida.',
    '../../../public/images/img/niñoFichas.jpg',
    'talleresTematicos'
  ),
  (
    8,
    'Taller Juegos de Mesa',
    'Espacio dedicado al aprendizaje y desarrollo a través de los juegos de mesa. Se fomenta el pensamiento estratégico, la resolución de problemas, la cooperación, el respeto por las normas y las habilidades sociales en un ambiente divertido y participativo. Un aspecto fundamental del taller es la gestión del tiempo y la paciencia al esperar el turno de juego, promoviendo la escucha activa y la consideración por los demás participantes.',
    '../../../public/images/img/niñaFicha.jpg',
    'juegosDeMesa'
  ),
  (
    9,
    'Escuela de Familias',
    'Programa de formación y apoyo dirigido a padres y madres. Se ofrecen herramientas, estrategias y recursos para afrontar los retos de la crianza, mejorar la comunicación familiar y promover un desarrollo saludable de los hijos.',
    '../../../public/images/img/niñoPuzzle.jpg',
    'escuelaPadres'
  ),
  (
    10,
    'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;',
    'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.',
    '../../../public/images/img/niñoCascos.jpg',
    'niñoCascos'
  ),
  (
    11,
    'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;',
    'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    12,
    'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;',
    'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    13,
    'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;',
    'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    14,
    'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;',
    'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    15,
    'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae;',
    'Sed euismod, nunc at cursus pellentesque, nisl eros pellentesque quam, a faucibus nisl nunc id nisl.',
    '../../../public/images/img/ivan.jpg',
    'ivan'
  );

-- Inserciones en la tabla 'events'
INSERT INTO
  events (
    id,
    title,
    message,
    creation_date,
    archived,
    user_id,
    favorites_count
  )
VALUES
  (
    1,
    'Taller de Juegos de Mesa',
    'Un aspecto fundamental del taller es la gestión del tiempo y la paciencia al esperar el turno de juego, promoviendo la escucha activa y la consideración por los demás participantes.',
    NOW(),
    false,
    1,
    0
  ),
  (
    2,
    'Escuela de Padres',
    'Programa de formación y apoyo dirigido a padres y madres. Se ofrecen herramientas, estrategias y recursos para afrontar los retos de la crianza, mejorar la comunicación familiar y promover un desarrollo saludable de los hijos.',
    NOW(),
    false,
    2,
    0
  );

-- Inserta el Aviso Legal adaptado para el Centro de Apoyo Educativo Especializado Ágora
INSERT INTO
  legal_texts (type, title, content)
VALUES
  (
    'terms',
    'Aviso Legal',
    '
<p>Aviso Legal para el sitio web de Centro de Apoyo Educativo Especializado Ágora</p>
<h2>1.- Introducción.</h2>
<p>
El presente documento tiene como finalidad el establecer y regular las normas de uso del Sitio web de Centro de Apoyo Educativo Especialzado Ágora (en adelante el «Sitio»), entendiendo por Sitio todas las páginas y sus contenidos propiedad de Centro de Apoyo Educativo Especializado Ágora a las cuales se accede a través de su dominio y subdominios.<br>
La utilización del Sitio atribuye la condición de Usuario del mismo e implica la aceptación de todas las condiciones incluidas en el presente Aviso Legal. El Usuario se compromete a leer atentamente el presente Aviso Legal en cada una de las ocasiones en que se proponga utilizar el Sitio, ya que éste y sus condiciones de uso recogidas en el presente Aviso Legal pueden sufrir modificaciones.<br>
Todos los avisos pueden ser revisados en cualquier momento por el usuario a través de nuestra página web.
</p>
<h2>2.- Titularidad del Sitio Web.</h2>
<ul>
<li><strong>Razón social:</strong> Centro de Apoyo Educativo Especializado Ágora</li>
<li><strong>Teléfonos:</strong> +34 693 54 59 93</li>
<li><strong>E-mail:</strong> contacto@agoraeducativo.es</li>
<li><strong>Domicilio:</strong> Calle Nicagua 16 Gijón- Oeste , 33213, Gijón, ASTURIAS</li>
<li><strong>C.I.F:</strong> ........</li>
<li><strong>Datos Registrales:</strong> Inscrito como cetro en ..........</li>
</ul>
<h2>3.- Propiedad intelectual e industrial.</h2>
<p>
Los derechos de propiedad intelectual de este sitio, son titularidad de Centro de Apoyo Educativo Especializado Ágora, o de terceros de los que dispone autorización cuando sea necesaria.<br>
La reproducción, distribución, comercialización o transformación, total o parcial, no autorizadas del contenido del Sitio, constituye una infracción de los derechos de propiedad intelectual de Centro de Apoyo Educativo Especializado Ágora. Igualmente, todas las marcas o signos distintivos de cualquier clase contenidos en el Sitio están protegidos por Ley.<br>
La utilización no autorizada de la información contenida en este Sitio, así como los perjuicios ocasionados en los derechos de propiedad intelectual e industrial de Centro de Apoyo Educativo Especializado Ágora, pueden dar lugar al ejercicio de las acciones que legalmente correspondan y, si procede, a las responsabilidades que de dicho ejercicio se deriven.
</p>
<h2>4.- Exclusión de responsabilidad.</h2>
<p>
El contenido, programas, información y/o consejos expresados en este Sitio deben entenderse como simplemente orientativos. Centro de Apoyo Educativo Especializado Ágora no responde de ninguna forma de la efectividad o exactitud de los mismos, quedando exenta de cualquier responsabilidad contractual o extracontractual con los Usuarios que haga uso de ellos, ya que son éstas las que deberán decidir según su criterio la oportunidad de los mismos.<br>
En este Sitio se pueden publicar contenidos aportados por terceras personas o empresas, Centro de Apoyo Educativo Especializado Ágora no responde de la veracidad y exactitud de los mismos, quedando exenta de cualquier responsabilidad contractual o extracontractual con los Usuarios que hagan uso de ellos.<br>
Centro de Apoyo Educativo Especializado Ágora se reserva el derecho de modificar el contenido del Sitio sin previo aviso y sin ningún tipo de limitación.<br>
Asimismo, declina cualquier responsabilidad por los eventuales daños y perjuicios que puedan ocasionarse por la falta de disponibilidad y/o continuidad de este Sitio y de los servicios que se ofrecen en el.<br>
Centro de Apoyo Educativo Especializado Ágora no garantiza la ausencia de virus ni de otros elementos en la web que puedan producir alteraciones en su sistema informático. Centro de Apoyo Educativo Especializado Ágora declina cualquier responsabilidad contractual o extracontractual con los Usuarios que hagan uso de ello y tuviera perjuicios de cualquier naturaleza ocasionados por virus informáticos o por elementos informáticos de cualquier índole.<br>
Centro de Apoyo Educativo Especializado Ágora declina cualquier responsabilidad por los servicios que eventualmente pudieran prestarse en el Sitio por parte de terceros.<br>
Centro de Apoyo Educativo Especializado Ágora declina cualquier responsabilidad por los servicios y/o información que se preste en otros Sitios enlazados con este. Centro de Apoyo Educativo Especializado Ágora no controla ni ejerce ningún tipo de supervisión en Sitios Webs de terceros. Aconsejamos a los Usuarios de los mismos a actuar con prudencia y consultar las eventuales condiciones legales que se expongan en dichas webs.<br>
Los Usuarios que remitan cualquier tipo de información a Centro de Apoyo Educativo Especializado Ágora se comprometen a que la misma sea veraz y que no vulnere cualquier derecho de terceros ni la legalidad vigente.
</p>
<h2>5.- Condiciones de uso del portal para los usuarios.</h2>
<p>
El acceso al presente Sitio es gratuito salvo en lo relativo al coste de la conexión a través de la red de telecomunicaciones suministrada por el proveedor de acceso contratado por los usuarios.<br>
Queda expresamente prohibido el uso del Sitio con fines lesivos de bienes o intereses de Centro de Apoyo Educativo Especializado Ágora o de terceros o que de cualquier otra forma sobrecarguen, dañen o inutilicen las redes, servidores y demás equipos informáticos (hardware) o productos y aplicaciones informáticas (software) de Centro de Apoyo Educativo Especializado Ágora, o de terceros.<br>
En el caso de que el Usuario tuviera conocimiento de que los Sitios enlazados remiten a páginas cuyos contenidos o servicios son ilícitos, nocivos, denigrantes, violentos o contrarios a la moral le agradeceríamos que se pusiera en contacto con Centro de Apoyo Educativo Especializado Ágora.
</p>
<h2>6.- Política de protección de datos personales.</h2>
<p>
Puede consultar nuestra Política de protección de datos personales en el apartado Política de Privacidad de esta web.
</p>
<h2>7.- Cookies.</h2>
<p>
Las cookies sirven para mejorar el funcionamiento de la web. El Art. 4 del Real Decreto-Ley 13/2012, de 30 de Marzo, modifica la Ley 34/2002, de 11 de julio, de servicios de la sociedad de la información y de comercio electrónico, estableciendo como requisito la obtención del consentimiento informado antes de que se almacene o se acceda a la información del terminal del usuario en ciertos supuestos.<br>
Si usamos otro tipo de cookies que no sean las necesarias, podrá consultar la política de cookies en el enlace correspondiente desde el inicio de la web.
</p>
<h2>8.- Legislación.</h2>
<p>
Centro de Apoyo Educativo Especializado Ágora le informa que el presente Aviso legal se rige en todos y cada uno de sus extremos por la legislación española. El presente contrato está establecido en idioma español.
</p>
<h2>9.- Contacte con nosotros.</h2>
<p>
Si tiene Ud. cualquier pregunta sobre las condiciones reflejadas en este Aviso Legal, o si le gustaría hacer cualquier sugerencia o recomendación, por favor diríjase a nosotros a través del email facilitado en el punto 2º.
</p>
<p><strong>Fecha Actualización:</strong> 2 de Junio de 2025</p>'
  );

INSERT INTO
  legal_texts (type, title, content)
VALUES
  (
    'privacy',
    'Política de Privacidad',
    '
<h2>Titularidad</h2>
<p>En cumplimiento de las obligaciones establecidas en la Ley Orgánica 3/2018, de 5 de diciembre, de Protección de Datos Personales y garantía de los derechos digitales y del artículo 10 de la Ley 34/2002, de 11 de julio, de Servicios de la Sociedad de la Información y Comercio Electrónico, se hace constar que esta página corresponde a la entidad:</p>
<ul>
<li><strong>Responsable:</strong> CENTRO DE APOYO EDUCATIVO ÁGORA. CIF: ...........</li>
<li><strong>Domicilio:</strong> C/ Calle Nicaragua 16, Gijón-Oeste, 33213, GIJÓN.</li>
<li><strong>Tfno:</strong> 693545993</li>
<li><strong>Mail:</strong> centroeducativoagora@gmail.com</li>
<li><strong>Web:</strong> www.caeagora.es tal vezz   </li>
<li><strong>Inscripción:</strong> Reg. Autonomos ............</li>
<li><strong>Delegado de protección de datos:</strong> PRODAT PRINCIPADO, S.L. – TORRECERREDO, 4 – BAJO – 33012 OVIEDO – 985 11 40 57 – DPDASTURIAS@PRODAT.ES</li>
</ul>
<p>Puedes dirigirte de cualquier forma para comunicarte con nosotros.</p>
<p>Nos reservamos el derecho a modificar o adaptar la presente Política de Privacidad en cualquier momento. Te recomendamos revisar la misma, y si te has registrado y accedes a tu cuenta o perfil, se te informará de las modificaciones.</p>

<h2>¿Quiénes son los colectivos afectados?</h2>
<ul>
<li>Usuarios del sistema interno de información</li>
<li>Contactos de la web o del correo electrónico</li>
<li>Usuarios</li>
<li>Proveedores</li>
<li>Asociados</li>
<li>Contactos redes sociales</li>
<li>Demandantes de empleo</li>
<li>Reclamaciones y quejas</li>
<li>Usuarios del Blog</li>
</ul>

<h2>Usuarios del sistema interno de información</h2>
<p><strong>¿Con qué finalidad tratamos los datos personales que nos facilita?</strong><br>
Tramitar las informaciones que tramite a través del SISTEMA INTERNO DE INFORMACIÓN.</p>
<p>El receptor de las comunicaciones será el Responsable del Sistema Interno de Información, quien respeta al máximo la privacidad de las personas usuarias de la aplicación, tratando sus datos personales con protección y de forma confidencial.</p>
<p>No se van a tomar decisiones automatizadas ni se van a realizar perfiles en relación a la información y datos recabados.</p>
<p><strong>¿Cuál es la legitimación para el tratamiento de sus datos?</strong><br>
El tratamiento de datos personales, en los supuestos de comunicación internos, se entenderá lícito en virtud de lo que disponen los artículos 6.1.c) del Reglamento (UE) 2016/679, 8 de la Ley Orgánica 3/2018, y 11 de la Ley Orgánica 7/2021.</p>
<p><strong>¿Cuánto tiempo conservamos los datos facilitados?</strong><br>
Los datos se conservarán únicamente durante el tiempo imprescindible para decidir sobre la procedencia de iniciar una investigación sobre los hechos. En todo caso, transcurridos 3 meses desde la introducción de los datos deberá procederse a su supresión del sistema salvo que la finalidad de la conservación sea dejar evidencia del funcionamiento del modelo de prevención de la comisión de delitos por la persona jurídica.</p>
<p>En ningún caso serán objeto de tratamiento los datos personales que no sean necesarios para el conocimiento e investigación de la información, procediéndose, en su caso, a su inmediata supresión.</p>
<p><strong>¿A qué destinatarios se pueden comunicar sus datos?</strong><br>
Organizaciones o personas directamente contratadas por el Responsable de Tratamiento para la prestación de servicios vinculados con las finalidades de tratamiento, así como autoridades competentes si fuera necesario.</p>
<p><strong>¿Bajo qué garantías se comunican sus datos?</strong><br>
La aceptación de las presentes condiciones implica la recogida de los datos personales que las personas usuarias introducen en el aplicativo Canal Ético y su consentimiento inequívoco para que sean tratados con la finalidad de gestionar sus denuncias y/o informaciones.</p>

<h2>Contactos de la web o del correo electrónico</h2>
<p><strong>¿Qué datos recopilamos?</strong><br>
Podemos tratar tu IP, sistema operativo, navegador y duración de la visita de forma anónima. Si nos facilitas datos en el formulario de contacto, te identificaremos para poder contactar contigo.</p>
<p><strong>¿Con qué finalidades?</strong><br>
Contestar a tus consultas, solicitudes o peticiones, gestionar el servicio solicitado, información por medios electrónicos, análisis y mejoras en la web.</p>
<p><strong>¿Cuál es la legitimación?</strong><br>
La aceptación y consentimiento del interesado.</p>

<h2>Usuarios</h2>
<p><strong>¿Con qué finalidad tratamos sus datos personales?</strong><br>
Gestionar la relación contractual y la prestación de servicios solicitados, tratar los datos de salud del usuario, realizar trámites administrativos, fiscales y contables.</p>
<p><strong>¿Durante cuánto tiempo?</strong><br>
Mientras dure la prestación del servicio y, tras finalizar, durante un período mínimo de 5 años según la Ley 41/2002.</p>

<h2>Proveedores</h2>
<p><strong>¿Con qué finalidades?</strong><br>
Información por medios electrónicos, gestión administrativa, facturación, transacciones, control y recobro.</p>
<p><strong>¿Legitimación?</strong><br>
Aceptación de una relación contractual o consentimiento.</p>

<h2>Asociados</h2>
<p><strong>¿Con qué finalidades?</strong><br>
Organización de actuaciones para los fines de la asociación, gestión interna y legal, convocatoria de juntas, cobro de cuotas, declaración de impuestos.</p>
<p><strong>¿Legitimación?</strong><br>
Base legal contractual y aceptación de estatutos.</p>

<h2>Contactos redes sociales</h2>
<p><strong>¿Con qué finalidades?</strong><br>
Contestar consultas, gestionar servicios, crear comunidad de seguidores.</p>
<p><strong>¿Legitimación?</strong><br>
Aceptación de una relación contractual en la red social correspondiente y conforme a sus políticas de privacidad.</p>

<h2>Demandantes de empleo</h2>
<p><strong>¿Con qué finalidades?</strong><br>
Organización de procesos de selección, entrevistas, cesión a empresas colaboradoras si hay consentimiento.</p>
<p><strong>¿Legitimación?</strong><br>
Consentimiento inequívoco al enviar el CV.</p>

<h2>Reclamaciones y quejas</h2>
<p><strong>¿Con qué finalidades?</strong><br>
Recogida y trámite de quejas y reclamaciones.</p>
<p><strong>¿Legitimación?</strong><br>
Cumplimiento de fines legales y consentimiento del interesado.</p>

<h2>¿Incluimos datos personales de terceras personas?</h2>
<p>No, salvo que nos aportes datos de terceros y hayas informado y solicitado su consentimiento previamente.</p>

<h2>¿Y datos de menores?</h2>
<p>No tratamos datos de menores de 14 años. Abstente de facilitarlos si no tienes esa edad.</p>

<h2>¿Realizaremos comunicaciones por medios electrónicos?</h2>
<p>Sólo para gestionar tu solicitud o si has autorizado comunicaciones comerciales.</p>

<h2>¿Qué medidas de seguridad aplicamos?</h2>
<p>Hemos adoptado un nivel óptimo de protección de los Datos Personales y todos los medios técnicos a nuestro alcance para evitar la pérdida, mal uso, alteración, acceso no autorizado y robo de los Datos Personales.</p>

<h2>¿A qué destinatarios se comunicarán tus datos?</h2>
<p>Tus datos no se cederán a terceros salvo obligación legal. En caso de compra o pago, tus datos se cederán a la plataforma correspondiente con la máxima seguridad.</p>

<h2>¿Qué Derechos tienes?</h2>
<ul>
<li>A saber si estamos tratando tus datos o no.</li>
<li>A acceder a tus datos personales.</li>
<li>A solicitar la rectificación o supresión de tus datos.</li>
<li>A solicitar la limitación del tratamiento.</li>
<li>A portar tus datos.</li>
<li>A presentar una reclamación ante la Agencia Española de Protección de Datos.</li>
<li>A revocar el consentimiento en cualquier momento.</li>
</ul>
<p>Si modificas algún dato, comunícalo para mantenerlos actualizados.</p>

<h2>¿Quieres un formulario para el ejercicio de Derechos?</h2>
<p>Tenemos formularios para el ejercicio de tus derechos, pídenoslos por email o usa los de la Agencia Española de Protección de Datos. Los formularios deben ir firmados electrónicamente o acompañados de fotocopia del DNI.</p>

<h2>¿Cuánto tardamos en contestarte al Ejercicio de Derechos?</h2>
<p>Como máximo en un mes desde tu solicitud, y dos meses si el tema es muy complejo.</p>

<h2>¿Tratamos cookies?</h2>
<p>Si usamos otro tipo de cookies que no sean las necesarias, podrás consultar la política de cookies en el enlace correspondiente desde el inicio de nuestra web.</p>

<h2>¿Durante cuánto tiempo vamos a mantener tus datos personales?</h2>
<p>Mientras sigas vinculado con nosotros y durante los plazos legalmente previstos tras la desvinculación.</p>

<p><strong>Fecha Actualización:</strong> 2 de Junio de 2025</p>'
  );

INSERT INTO
  legal_texts (type, title, content, updated_at)
VALUES
  (
    'cookies',
    'Política de Cookies',
    '<h1>Política de Cookies</h1>
  <p>
    Actualmente, este sitio web <strong>no utiliza cookies propias ni de terceros</strong> para recoger información personal del usuario, salvo aquellas estrictamente necesarias para el funcionamiento técnico de la página (por ejemplo, para el correcto funcionamiento del login y la navegación).<br><br>
    No se emplean herramientas de analítica, publicidad, ni servicios de terceros que instalen cookies en el navegador del usuario.<br><br>
    En caso de que en el futuro se implementen nuevas funcionalidades que requieran el uso de cookies, esta política será actualizada para informar detalladamente sobre el tipo de cookies utilizadas, su finalidad y la forma de gestionarlas.<br><br>
    Si tienes cualquier duda sobre nuestra política de cookies, puedes contactar con nosotros en centroeducativoagora@gmail.com.
  </p>',
    NOW()
  );