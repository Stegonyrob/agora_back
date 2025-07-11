/*Users*/
INSERT INTO
  users (username, password, email, accepted_rules)
VALUES
  (
    'admin',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'admin@gmail.com',
    true
  ),
  (
    'user1',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user1@gmail.com',
    true
  ),
  (
    'user2',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user2@gmail.com',
    true
  ),
  (
    'user3',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user3@gmail.com',
    true
  ),
  (
    'user4',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user4@gmail.com',
    true
  ),
  (
    'user5',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user5@gmail.com',
    true
  ),
  (
    'user6',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user6@gmail.com',
    true
  ),
  (
    'user7',
    '$2a$12$xOx5K0CaHRWkRgaZBRHvZ.tcrVC/AeA3sIjCySnHKk6ZEM9kmuIyO',
    'user7@gmail.com',
    true
  );

/*Profiles*/
INSERT INTO
  profiles (
    user_id,
    first_Name,
    last_Name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city
  )
VALUES
  (
    1,
    'Ivan',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city'
  ),
  (
    2,
    'Stella',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'Gijon'
  ),
  (
    3,
    'Name3',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city'
  ),
  (
    4,
    'Name4',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city'
  ),
  (
    5,
    'Name5',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city'
  ),
  (
    6,
    'Name5',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city'
  ),
  (
    7,
    'Name7',
    'Apellido1',
    'SegApellido1',
    'TheBoss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'city'
  );

-- Inserciones en la tabla 'roles'
INSERT INTO
  roles (name)
VALUES
  ('ROLE_ADMIN'),
  ('ROLE_USER');

-- Asociación entre usuarios y roles
INSERT INTO
  roles_users (role_id, user_id)
VALUES
  (1, 1),
  (2, 2),
  (2, 3),
  (2, 4),
  (2, 5),
  (2, 6),
  (2, 7);

-- Inserciones en la tabla 'posts'
INSERT INTO
  posts (title, message, user_id, creation_date, archived)
VALUES
  (
    'Post 1',
    'Comer es un desafío diario para mí. No es solo cuestión de elegir qué quiero comer, sino de superar las barreras que mi cerebro y mi cuerpo me ponen. Tengo selectividad alimentaria por texturas y colores, lo que significa que solo como alimentos que se ajustan a mis estándares de textura y color. La carne debe ser suave y tierna, no puede tener grasa visible ni tener un color demasiado intenso. Las verduras deben ser crujientes y frescas, no pueden estar cocidas ni tener un color demasiado oscuro. Los frutos deben ser dulces y jugosos, no pueden ser ácidos ni tener un color demasiado brillante. Cada comida es un reto, cada bocado una lucha. Me siento aislada en una sociedad que valora la variedad y la experimentación culinaria. Me siento culpable por no poder disfrutar de una comida familiar o compartir un plato con amigos. Me siento frustrada por no poder explicarles a los demás por qué no como ciertas cosas. Me siento sola en mi lucha diaria por encontrar alimentos que se ajusten a mis necesidades. Pero también me siento fuerte por haber aprendido a adaptarme y a encontrar formas de superar mis limitaciones. Me siento orgullosa de haber encontrado una comunidad en línea que entiende mi lucha y me apoya. Comer es un desafío, pero no es imposible. Y aunque sea difícil, vale la pena luchar por encontrar la comida que me hace sentir bien.',
    1,
    '2024-01-10T09:00:00',
    false
  ),
  (
    'Presunción de Competencia: Un Nuevo Paradigma',
    'La presunción de competencia es un principio fundamental que está revolucionando el apoyo a personas autistas. En lugar de asumir déficits, partimos de la base de que toda persona tiene capacidades innatas que pueden desarrollarse. Este enfoque reconoce que las diferencias en la comunicación o el comportamiento no indican menor inteligencia o capacidad. Muchas personas autistas han demostrado que, cuando se les brinda el apoyo adecuado y se respetan sus formas naturales de procesar el mundo, pueden alcanzar logros extraordinarios. La clave está en encontrar los métodos de comunicación y aprendizaje que mejor se adapten a cada persona, en lugar de forzar adaptaciones a métodos neurotípicos. Este paradigma nos invita a ser más creativos, pacientes y respetuosos en nuestro acompañamiento.',
    1,
    '2024-01-11T10:00:00',
    false
  ),
  (
    'Más Allá del Capacitismo: Celebrando la Neurodiversidad',
    'El movimiento de neurodiversidad nos ha enseñado a ver el autismo no como algo que necesita ser "curado", sino como una variación natural del cerebro humano. Los enfoques tradicionales, a menudo capacitistas, se centraban en hacer que las personas autistas se vieran "normales", ignorando el estrés y trauma que esto causaba. Hoy sabemos que es más efectivo y respetuoso adaptar el entorno a las necesidades de cada persona. Esto significa crear espacios sensorialmente seguros, respetar los stims (movimientos autorregulatorios), y valorar formas alternativas de comunicación como el uso de tableros de comunicación o tecnología asistiva. Cuando eliminamos las barreras capacitistas, las personas autistas florecen mostrando sus verdaderas habilidades y talentos únicos.',
    1,
    '2024-01-12T11:00:00',
    false
  ),
  (
    'Post 4',
    'Comer es un desafío diario para mí. No es solo cuestión de elegir qué quiero comer, sino de superar las barreras que mi cerebro y mi cuerpo me ponen. Tengo selectividad alimentaria por texturas y colores, lo que significa que solo como alimentos que se ajustan a mis estándares de textura y color. La carne debe ser suave y tierna, no puede tener grasa visible ni tener un color demasiado intenso. Las verduras deben ser crujientes y frescas, no pueden estar cocidas ni tener un color demasiado oscuro. Los frutos deben ser dulces y jugosos, no pueden ser ácidos ni tener un color demasiado brillante. Cada comida es un reto, cada bocado una lucha. Me siento aislada en una sociedad que valora la variedad y la experimentación culinaria. Me siento culpable por no poder disfrutar de una comida familiar o compartir un plato con amigos. Me siento frustrada por no poder explicarles a los demás por qué no como ciertas cosas. Me siento sola en mi lucha diaria por encontrar alimentos que se ajusten a mis necesidades. Pero también me siento fuerte por haber aprendido a adaptarme y a encontrar formas de superar mis limitaciones. Me siento orgullosa de haber encontrado una comunidad en línea que entiende mi lucha y me apoya. Comer es un desafío, pero no es imposible. Y aunque sea difícil, vale la pena luchar por encontrar la comida que me hace sentir bien.',
    1,
    '2024-01-13T12:00:00',
    false
  ),
  (
    'Comunicación Alternativa y Aumentativa: Rompiendo Barreras',
    'La comunicación no tiene por qué ser verbal para ser válida y poderosa. Los sistemas de comunicación alternativa y aumentativa (CAA) han abierto un mundo de posibilidades para personas autistas que comunican de formas diferentes. Desde pictogramas y tableros de comunicación hasta aplicaciones tecnológicas avanzadas, estas herramientas permiten que las personas expresen sus pensamientos, necesidades y emociones de manera efectiva. Es fundamental entender que alguien que no habla no es alguien que no tiene nada que decir. De hecho, muchas personas autistas no parlantes tienen pensamientos complejos y profundos que comparten a través de la escritura, los símbolos o la tecnología. El respeto por estas formas de comunicación y la paciencia para aprender a usarlas correctamente es clave para construir relaciones auténticas y significativas.',
    1,
    '2024-01-14T13:00:00',
    false
  ),
  (
    'Apoyo Centrado en la Persona: Respetando la Autodeterminación',
    'El enfoque centrado en la persona reconoce que cada individuo autista es único, con sus propios intereses, fortalezas y formas de interactuar con el mundo. Este paradigma se aleja de los modelos "talla única" y abraza la personalización del apoyo. Significa escuchar activamente lo que la persona comunica sobre sus necesidades, respetar sus elecciones cuando es posible, y trabajar CON la persona, no SOBRE ella. Incluye valorar los intereses especiales como recursos de aprendizaje y conexión, respetar las necesidades sensoriales, y entender que los comportamientos repetitivos (stims) son formas naturales de autorregulación. Cuando el apoyo se centra verdaderamente en la persona, vemos un aumento en la autoestima, la autonomía y el bienestar general. Este enfoque nos recuerda que nuestro objetivo no es cambiar a la persona, sino crear las condiciones para que pueda prosperar siendo auténticamente ella misma.',
    1,
    '2024-01-15T14:00:00',
    false
  );

-- Inserta comentarios para posts existentes
INSERT INTO
  comments (
    message,
    creation_date,
    archived,
    post_id,
    user_id
  )
VALUES
  ('Comentario 1', NOW(), false, 1, 1),
  ('Comentario 2', NOW(), false, 1, 2),
  ('Comentario 3', NOW(), false, 2, 3);

-- Inserciones en la tabla 'replies'
-- Inserta replies para comentarios existentes
INSERT INTO
  replies (
    message,
    creation_date,
    archived,
    comment_id,
    user_id
  )
VALUES
  ('Respuesta 1 al comentario 1', NOW(), false, 1, 1),
  ('Respuesta 2 al comentario 1', NOW(), false, 1, 2),
  ('Respuesta 3 al comentario 2', NOW(), false, 2, 3),
  ('Respuesta 4 al comentario 3', NOW(), false, 3, 1);

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
    'Donde Estamos',
    'Nos encontramos en la zona Oeste de Gijón , mas exactamente en La Calzada en Calle Nicaragua al 16.',
    '../../../public/images/img/escritorio.jpg',
    'escritorio'
  ),
  (
    3,
    'Nuestros Servicios',
    'Nuestros servicios están diseñados para atender las necesidades de cada estudiante de manera individualizada, promoviendo su desarrollo integral y bienestar. los cuales son entre otros: Reeducación pedagógica, Educación psicomotriz, Refuerzo de inglés en grupos reducidos, Refuerzo educativo especializado en grupos pequeños, Talleres temáticos educativos (vacaciones), Taller juegos de mesa, Escuela de familias.',
    '../../../public/images/img/escritorio.jpg',
    'escritorio'
  ),
  (
    4,
    'Reeducación pedagógica',
    'Apoyo educativo, Intervención en NEE y NEAE, Cuidado especializado por horas, Intervención educativa con familias y en el hogar, Coordinación multidisciplinar con centros educativos y particulares, Somos beneficiarios de las becas de reeducación pedagógica de NEAE y NEE.',
    '../../../public/images/img/niñoPuzzle.jpg',
    'niñoPuzzle'
  ),
  (
    5,
    'Educación Psicomotriz',
    'Sesiones diseñadas para fomentar el desarrollo integral de los niños a través del movimiento y la interacción con el entorno. Se trabaja la coordinación, el equilibrio, la conciencia corporal y otras habilidades motoras fundamentales para el aprendizaje y el bienestar.',
    '../../../public/images/img/niñaFicha.jpg',
    'educacionPsicomotriz'
  ),
  (
    6,
    'Refuerzo de Inglés en Grupos Reducidos',
    'Clases de apoyo para el aprendizaje del inglés, impartidas en grupos pequeños para garantizar una atención más personalizada. Se refuerzan los contenidos curriculares, se trabajan las habilidades comunicativas y se fomenta la confianza en el uso del idioma.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'refuerzoIngles'
  ),
  (
    7,
    'Refuerzo Educativo Especializado en Grupos Pequeños',
    'Apoyo académico individualizado o en grupos muy reducidos para alumnos con necesidades educativas especiales o dificultades de aprendizaje. Se adaptan las estrategias y los materiales para atender las particularidades de cada estudiante y facilitar su progreso.',
    '../../../public/images/img/adolescentesGrupal.jpg',
    'refuerzoEducativo'
  ),
  (
    8,
    'Talleres Temáticos Educativos (Vacaciones)',
    'Programas especiales durante los periodos vacacionales con temáticas diversas y educativas. Estos talleres buscan ofrecer un espacio de aprendizaje lúdico y creativo, explorando diferentes áreas de conocimiento y desarrollando nuevas habilidades de manera divertida.',
    '../../../public/images/img/niñoFichas.jpg',
    'talleresTematicos'
  ),
  (
    9,
    'Taller Juegos de Mesa',
    'Espacio dedicado al aprendizaje y desarrollo a través de los juegos de mesa. Se fomenta el pensamiento estratégico, la resolución de problemas, la cooperación, el respeto por las normas y las habilidades sociales en un ambiente divertido y participativo. Un aspecto fundamental del taller es la gestión del tiempo y la paciencia al esperar el turno de juego, promoviendo la escucha activa y la consideración por los demás participantes.',
    '../../../public/images/img/niñaFicha.jpg',
    'juegosDeMesa'
  ),
  (
    10,
    'Escuela de Familias',
    'Programa de formación y apoyo dirigido a padres y madres. Se ofrecen herramientas, estrategias y recursos para afrontar los retos de la crianza, mejorar la comunicación familiar y promover un desarrollo saludable de los hijos.',
    '../../../public/images/img/niñoPuzzle.jpg',
    'escuelaPadres'
  ),
  (
    11,
    'Sobre mi',
    'Soy un profesional apasionado por la educación y el desarrollo integral de los niños. Con una formación sólida en pedagogía y psicología infantil, he dedicado mi carrera a crear entornos de aprendizaje inclusivos y estimulantes. Mi enfoque se centra en las necesidades individuales de cada estudiante, promoviendo su bienestar emocional y académico.',
    '../../../public/images/img/ivan.jpg',
    'ivan'
  ),
  (
    12,
    'Neurodiversidad ¿Qué es? ¿Qué Significa?',
    'La nerodiversidad se define como la variabilidad natural del cerebro humano, que abarca una amplia gama de diferencias neurológicas y cognitivas. Esta perspectiva reconoce que las variaciones en el funcionamiento cerebral son normales y valiosas, y que cada individuo aporta una forma única de pensar, aprender y experimentar el mundo.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    13,
    'Nuestra vision de la Neurodiversidad',
    'Nuestra visión de la neurodiversidad se basa en el respeto y la valoración de las diferencias individuales. Creemos que cada persona tiene un potencial único y que la diversidad cognitiva enriquece nuestras comunidades. Nuestro objetivo es crear entornos inclusivos que fomenten el aprendizaje y el desarrollo de todos los estudiantes, independientemente de sus habilidades o desafíos. Creemos fervientemente en PRESUMIR COMPETENCIA, no DISCAPACIDAD, y en que cada persona tiene el derecho a ser comprendida y apoyada en su proceso de aprendizaje.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    14,
    'Condición del espectro autista',
    'La condición del espectro autista (CEA) es una condición del neurodesarrollo que afecta la comunicación, la interacción social y el comportamiento. Se manifiesta de diversas formas y con diferentes grados de intensidad, lo que hace que cada persona con CEA sea única. Es fundamental entender y respetar estas diferencias para ofrecer el apoyo adecuado.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    15,
    'Evolución de la condición de espectro autista',
    'La condición del espectro autista, antiguamente considerada un trastorno (TEA), ha evolucionado en su comprensión y enfoque. Actualmente, se reconoce que cada persona con CEA es única y que sus experiencias y necesidades deben ser respetadas y atendidas de manera individualizada.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    16,
    'Trastorno del déficit de atención sin hiperactividad (TDA)',
    'El trastorno del déficit de atención (TDA) es una condición del neurodesarrollo que se caracteriza por dificultades en la atención. Estas dificultades pueden afectar el rendimiento académico y las relaciones interpersonales. Es importante abordar el TDA con estrategias adecuadas y un enfoque comprensivo.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    17,
    'Trastorno del déficit de atención con hiperactividad (TDAH)',
    'El trastorno del déficit de atención e hiperactividad (TDAH) es una condición del neurodesarrollo que se caracteriza por dificultades en la atención, la hiperactividad y la impulsividad. Estas dificultades pueden afectar el rendimiento académico y las relaciones interpersonales. Es importante abordar el TDAH con estrategias adecuadas y un enfoque comprensivo.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    18,
    'Dificultades del aprendizaje',
    'Las dificultades del aprendizaje son condiciones que afectan la capacidad de una persona para adquirir, procesar o utilizar información de manera efectiva. Estas dificultades pueden manifestarse en diversas áreas, como la lectura, la escritura, el cálculo o la atención. Es fundamental identificar y abordar estas dificultades de manera temprana para ofrecer el apoyo adecuado y facilitar el aprendizaje.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    19,
    'Desafíos del aprendizaje',
    'Los desafíos del aprendizaje son obstáculos que pueden dificultar el proceso educativo de los estudiantes. Estos desafíos pueden ser de naturaleza cognitiva, emocional o social, y es fundamental identificarlos y abordarlos de manera efectiva para garantizar una educación inclusiva y equitativa.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    20,
    'Condiciones del Desarrollo',
    'Las condiciones del desarrollo son un conjunto de factores que influyen en el crecimiento y la evolución de los niños. Estos factores pueden ser biológicos, psicológicos o sociales, y su comprensión es esencial para ofrecer un apoyo adecuado a los estudiantes. Es fundamental abordar cada caso de manera individualizada, teniendo en cuenta las particularidades de cada niño y su contexto.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    21,
    'Importancia de las condiciones del desarrollo',
    'La comprensión de las condiciones del desarrollo es esencial para ofrecer un apoyo adecuado a los estudiantes. Es fundamental abordar cada caso de manera individualizada, teniendo en cuenta las particularidades de cada niño y su contexto.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    22,
    'Trastornos de la comunicación',
    'Los trastornos de la comunicación son condiciones que afectan la capacidad de una persona para comunicarse de manera efectiva. Estos trastornos pueden manifestarse en dificultades para hablar, entender o utilizar el lenguaje, y es fundamental abordarlos con un enfoque comprensivo y respetuoso. La intervención temprana y el apoyo adecuado son clave para ayudar a los niños a desarrollar habilidades comunicativas saludables.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
  ),
  (
    23,
    'Importancia de los trastornos de la comunicación',
    'La comprensión de los trastornos de la comunicación es esencial para ofrecer un apoyo adecuado a los estudiantes. Es fundamental abordar cada caso de manera individualizada, teniendo en cuenta las particularidades de cada niño y su contexto. La correcta identificación y tratamiento de estos trastornos puede marcar una gran diferencia en el desarrollo y bienestar de los niños.',
    '../../../public/images/img/alumnosOrdenador.jpg',
    'alumnosOrdenador'
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
    capacity
  )
VALUES
  (
    1,
    'Taller de Juegos de Mesa',
    'Un aspecto fundamental del taller es la gestión del tiempo y la paciencia al esperar el turno de juego, promoviendo la escucha activa y la consideración por los demás participantes.',
    '2024-01-10T10:00:00',
    false,
    1,
    30
  ),
  (
    2,
    'Escuela de Padres',
    'Programa de formación y apoyo dirigido a padres y madres. Se ofrecen herramientas, estrategias y recursos para afrontar los retos de la crianza, mejorar la comunicación familiar y promover un desarrollo saludable de los hijos.',
    '2024-01-15T18:00:00',
    false,
    1,
    40
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

INSERT INTO
  legal_texts (type, title, content, updated_at)
VALUES
  (
    'blog-rules',
    'Reglas de la Comunidad Ágora',
    '<div class="rules-content">
  <h2>Bienvenido a la Comunidad Ágora</h2>
  <p>Estas reglas están diseñadas para mantener un ambiente seguro, respetuoso y enriquecedor para todos los miembros de nuestra comunidad educativa.</p>

  <h3>Normas de Convivencia</h3>
  <ul class="rules-list">
    <li>
      <strong>Respeto y tolerancia:</strong> Mantén siempre un trato respetuoso hacia todos los miembros de la comunidad. No se tolerarán insultos, amenazas o cualquier forma de acoso.
    </li>
    <li>
      <strong>Prohibición de lenguaje soez:</strong> Está prohibido el uso de lenguaje vulgar, obsceno o inapropiado en cualquier contexto dentro de la plataforma.
    </li>
    <li>
      <strong>Cero tolerancia al racismo:</strong> No se permitirán comentarios, publicaciones o cualquier tipo de contenido que promueva discriminación racial o étnica.
    </li>
    <li>
      <strong>Prohibición de xenofobia:</strong> Está estrictamente prohibido cualquier contenido que manifieste rechazo, hostilidad o discriminación hacia personas de otros países o culturas.
    </li>
    <li>
      <strong>No discriminación:</strong> Prohibido cualquier tipo de discriminación por motivos de género, orientación sexual, religión, discapacidad o cualquier otra característica personal.
    </li>
    <li>
      <strong>Contenido educativo apropiado:</strong> Todo el contenido compartido debe ser apropiado para un entorno educativo y relacionado con los temas de apoyo especializado y desarrollo personal.
    </li>
    <li>
      <strong>No spam ni contenido comercial:</strong> Está prohibido el envío repetitivo de mensajes o contenido comercial no autorizado.
    </li>
    <li>
      <strong>Veracidad de la información:</strong> Comparte información veraz y basada en fuentes confiables. Evita la difusión de noticias falsas o información engañosa.
    </li>
    <li>
      <strong>Privacidad y confidencialidad:</strong> Respeta la privacidad de otros usuarios. No compartas información personal de terceros sin su consentimiento. Mantén la confidencialidad de los casos y situaciones personales compartidas.
    </li>
    <li>
      <strong>Uso responsable y constructivo:</strong> Utiliza la plataforma de manera responsable, contribuyendo positivamente al crecimiento y bienestar de la comunidad educativa.
    </li>
    <li>
      <strong>Apoyo y empatía:</strong> Fomenta un ambiente de apoyo mutuo, comprensión y empatía, especialmente hacia aquellos que pueden estar enfrentando desafíos educativos o personales.
    </li>
    <li>
      <strong>Contenido apropiado para menores:</strong> Dado nuestro enfoque educativo, todo el contenido debe ser apropiado para menores de edad y familias.
    </li>
  </ul>

  <h3>Protección de Datos Personales - RGPD</h3>
  <div class="gdpr-section">
    <p>
      <strong>Cumplimiento del RGPD:</strong> En conformidad con el Reglamento General de Protección de Datos (RGPD) de la Unión Europea (Reglamento UE 2016/679), 
      te informamos que como usuario de nuestra plataforma tienes los siguientes derechos:
    </p>
    <ul>
      <li><strong>Derecho de acceso:</strong> Puedes solicitar información sobre qué datos personales procesamos sobre ti</li>
      <li><strong>Derecho de rectificación:</strong> Puedes solicitar la corrección de información incorrecta o incompleta</li>
      <li><strong>Derecho de supresión:</strong> Puedes solicitar la eliminación de tus datos personales</li>
      <li><strong>Derecho de limitación:</strong> Puedes solicitar que limitemos el tratamiento de tus datos</li>
      <li><strong>Derecho a la portabilidad:</strong> Puedes solicitar recibir tus datos en un formato estructurado y de uso común</li>
      <li><strong>Derecho de oposición:</strong> Puedes oponerte al tratamiento de tus datos en determinadas circunstancias</li>
      <li><strong>Derecho a no ser objeto de decisiones automatizadas:</strong> Incluido el perfilado automatizado</li>
    </ul>
    <p>
      <strong>Ejercicio de derechos:</strong> Para ejercer cualquiera de estos derechos, puedes contactarnos a través de:
    </p>
    <ul>
      <li>Email: contacto@agoraeducativo.es</li>
      <li>Teléfono: +34 693 54 59 93</li>
      <li>Dirección postal: Calle Nicagua 16 Gijón-Oeste, 33213, Gijón, ASTURIAS</li>
    </ul>
    <p>
      <strong>Plazo de respuesta:</strong> Nos comprometemos a responder a tu solicitud en un plazo máximo de 30 días naturales desde la recepción de la misma.
    </p>
    <p>
      <strong>Autoridad de control:</strong> Tienes derecho a presentar una reclamación ante la Agencia Española de Protección de Datos (AEPD) si consideras que tus derechos no han sido respetados.
    </p>
  </div>

  <h3>Normativa Específica para Entorno Educativo</h3>
  <div class="educational-section">
    <ul>
      <li>
        <strong>Protección de menores:</strong> Se aplicarán medidas especiales de protección para usuarios menores de edad, incluyendo moderación adicional de contenido.
      </li>
      <li>
        <strong>Colaboración con familias:</strong> Fomentamos la participación e involucración de las familias en el proceso educativo a través de la plataforma.
      </li>
      <li>
        <strong>Confidencialidad profesional:</strong> Los profesionales del centro mantienen la confidencialidad de la información compartida en el contexto de su trabajo.
      </li>
      <li>
        <strong>Recursos educativos:</strong> El contenido compartido debe alinearse con los objetivos educativos y terapéuticos del centro.
      </li>
    </ul>
  </div>

  <h3>Consecuencias por Incumplimiento</h3>
  <div class="consequences-section">
    <p>El incumplimiento de estas reglas puede resultar en las siguientes medidas disciplinarias:</p>
    <ul>
      <li><strong>Primera infracción:</strong> Advertencia oficial y orientación sobre las normas</li>
      <li><strong>Infracciones repetidas:</strong> Suspensión temporal de la cuenta (3-30 días)</li>
      <li><strong>Infracciones graves:</strong> Suspensión de funcionalidades específicas</li>
      <li><strong>Infracciones muy graves:</strong> Eliminación permanente de la cuenta</li>
      <li><strong>Casos extremos:</strong> Comunicación a las autoridades competentes si procede</li>
    </ul>
    <p>
      La gravedad de las medidas dependerá de la naturaleza de la infracción, su impacto en la comunidad y la reincidencia del usuario.
    </p>
  </div>

  <h3>Proceso de Moderación y Apelaciones</h3>
  <div class="moderation-section">
    <p>
      <strong>Moderación:</strong> Nuestro equipo de moderación revisa el contenido reportado y aplica las medidas necesarias.
    </p>
    <p>
      <strong>Derecho de apelación:</strong> Si consideras que una medida disciplinaria ha sido injusta, puedes presentar una apelación en un plazo de 15 días a través de contacto@agoraeducativo.es.
    </p>
  </div>

  <div class="footer">
    <p>
      <strong>Actualizaciones:</strong> Estas reglas pueden ser actualizadas periódicamente para adaptarse a nuevas necesidades o cambios normativos. 
      Te notificaremos sobre cualquier cambio significativo con al menos 15 días de antelación.
    </p>
    <p>
      <strong>Aceptación:</strong> Al registrarte en la plataforma del Centro de Apoyo Educativo Especializado Ágora, aceptas cumplir con todas estas normas 
      y contribuir a mantener un ambiente educativo sano, seguro y constructivo para toda la comunidad.
    </p>
    <p>
      <strong>Contacto:</strong> Para cualquier consulta sobre estas reglas o para reportar incumplimientos, contacta con nosotros a través de 
      nuestros canales oficiales de comunicación.
    </p>
  </div>
</div>
<p><strong>Fecha de última actualización:</strong> 26 de Junio de 2025</p>',
    '2025-06-26 00:00:00'
  );

-- Avatares precargados
INSERT INTO
  avatars (
    image_name,
    image_data,
    preloaded,
    is_default,
    display_name,
    image_url
  )
VALUES
  (
    '1.png',
    NULL,
    true,
    true,
    'Avatar amarillo sonriente',
    '/images/avatars/1.png'
  ),
  (
    '2.png',
    NULL,
    true,
    false,
    'Avatar marron sonriente',
    '/images/avatars/2.png'
  ),
  (
    '3.png',
    NULL,
    true,
    false,
    'Avatar avatar azul cielo sonriente',
    '/images/avatars/3.png'
  ),
  (
    '4.png',
    NULL,
    true,
    false,
    'Avatar verde sonriente',
    '/images/avatars/4.png'
  ),
  (
    '5.png',
    NULL,
    true,
    false,
    'Avatar 5amarillo sonriente',
    '/images/avatars/5.png'
  ),
  (
    '6.png',
    NULL,
    true,
    false,
    'Avatar turquesa sonriente',
    '/images/avatars/6.png'
  ),
  (
    '7.png',
    NULL,
    true,
    false,
    'Avatar verde claro sonriente',
    '/images/avatars/7.png'
  ),
  (
    '8.png',
    NULL,
    true,
    false,
    'Avatar azul cielo sonriente 2',
    '/images/avatars/8.png'
  ),
  (
    '9.png',
    NULL,
    true,
    false,
    'Avatar girs y blanco sonriente',
    '/images/avatars/9.png'
  ),
  (
    '10.png',
    NULL,
    true,
    false,
    'Avatar lila sonriente',
    '/images/avatars/10.png'
  ),
  (
    '11.png',
    NULL,
    true,
    false,
    'Avatar azul sonriente',
    '/images/avatars/11.png'
  ),
  (
    '12.png',
    NULL,
    true,
    false,
    'Avatar amarillo sonriente 2',
    '/images/avatars/12.png'
  ),
  (
    '13.png',
    NULL,
    true,
    false,
    'Avatar azul sonriente ojos cerrados',
    '/images/avatars/13.png'
  ),
  (
    '14.png',
    NULL,
    true,
    false,
    'Avatar rosa sonriente',
    '/images/avatars/14.png'
  ),
  (
    '15.png',
    NULL,
    true,
    false,
    'Avatar marron serio',
    '/images/avatars/15.png'
  ),
  (
    '16.png',
    NULL,
    true,
    false,
    'Avatar azul cielo serio',
    '/images/avatars/16.png'
  ),
  (
    '17.png',
    NULL,
    true,
    false,
    'Avatar 17',
    '/images/avatars/17.png'
  ),
  (
    '18.png',
    NULL,
    true,
    false,
    'Avatar azul feliz',
    '/images/avatars/18.png'
  ),
  (
    '19.png',
    NULL,
    true,
    false,
    'Avatar amarillo feliz',
    '/images/avatars/19.png'
  ),
  (
    '20.png',
    NULL,
    true,
    false,
    'Avatar verde sonriente',
    '/images/avatars/20.png'
  ),
  (
    '21.png',
    NULL,
    true,
    false,
    'Avatar gris sonriente',
    '/images/avatars/21.png'
  ),
  (
    '22.png',
    NULL,
    true,
    false,
    'Avatar azul cielo mirando a la izquierda',
    '/images/avatars/22.png'
  ),
  (
    '23.png',
    NULL,
    true,
    false,
    'Avatar azul cielo serio',
    '/images/avatars/23.png'
  ),
  (
    '24.png',
    NULL,
    true,
    false,
    'Avatar rosa con cuernos sonriente',
    '/images/avatars/24.png'
  ),
  (
    '25.png',
    NULL,
    true,
    false,
    'Avatar verde y blanco sonriente',
    '/images/avatars/25.png'
  ),
  (
    '26.png',
    NULL,
    true,
    false,
    'Avatar lila serio',
    '/images/avatars/26.png'
  ),
  (
    '27.png',
    NULL,
    true,
    false,
    'Avatar azul serio 2',
    '/images/avatars/27.png'
  ),
  (
    '28.png',
    NULL,
    true,
    false,
    'Avatar verde pensativo',
    '/images/avatars/28.png'
  ),
  (
    'onron.png',
    NULL,
    true,
    false,
    'Avatar Onron (Admin)',
    '/images/avatars/onron.png'
  );

-- Inserciones en la tabla 'tags'
INSERT INTO
  tags (id, name, archived)
VALUES
  -- Tags básicos ya existentes
  (1, 'Educativo', false),
  (2, 'Talleres', false),
  (3, 'Padres', false),
  (4, 'Formación', false),
  (5, 'Juegos', false),
  (6, 'Apoyo', false),
  (7, 'Infantil', false),
  (8, 'Desarrollo', false),
  -- Tags relacionados con neurodiversidad y condiciones
  (9, 'Neurodiversidad', false),
  (10, 'Autismo', false),
  (11, 'CEA', false), -- Condición del Espectro Autista
  (12, 'TDAH', false),
  (13, 'TDA', false),
  (14, 'NEE', false), -- Necesidades Educativas Especiales
  (15, 'NEAE', false), -- Necesidades Específicas de Apoyo Educativo
  (16, 'Dificultades Aprendizaje', false),
  (17, 'Trastornos Comunicación', false),
  (18, 'Condiciones Desarrollo', false),
  -- Tags de servicios específicos
  (19, 'Reeducación Pedagógica', false),
  (20, 'Educación Psicomotriz', false),
  (21, 'Refuerzo Inglés', false),
  (22, 'Refuerzo Educativo', false),
  (23, 'Talleres Temáticos', false),
  (24, 'Escuela Familias', false),
  (25, 'Juegos Mesa', false),
  (26, 'Vacaciones', false),
  -- Tags de metodología y enfoques
  (27, 'Individualizado', false),
  (28, 'Grupos Reducidos', false),
  (29, 'Especializado', false),
  (30, 'Inclusivo', false),
  (31, 'Coordinación Multidisciplinar', false),
  (32, 'Intervención Familiar', false),
  (33, 'Atención Personalizada', false),
  -- Tags de habilidades y competencias
  (34, 'Habilidades Sociales', false),
  (35, 'Comunicación', false),
  (36, 'Pensamiento Estratégico', false),
  (37, 'Resolución Problemas', false),
  (38, 'Cooperación', false),
  (39, 'Paciencia', false),
  (40, 'Escucha Activa', false),
  (41, 'Coordinación', false),
  (42, 'Equilibrio', false),
  (43, 'Conciencia Corporal', false),
  (44, 'Motricidad', false),
  -- Tags de áreas de desarrollo
  (45, 'Cognitivo', false),
  (46, 'Emocional', false),
  (47, 'Social', false),
  (48, 'Motor', false),
  (49, 'Académico', false),
  (50, 'Bienestar', false),
  (51, 'Autoestima', false),
  (52, 'Autonomía', false),
  -- Tags de edades y grupos
  (53, 'Preescolar', false),
  (54, 'Primaria', false),
  (55, 'Secundaria', false),
  (56, 'Adolescentes', false),
  (57, 'Adultos', false),
  (58, 'Familias', false),
  -- Tags de actividades específicas
  (59, 'Lectura', false),
  (60, 'Escritura', false),
  (61, 'Cálculo', false),
  (62, 'Atención', false),
  (63, 'Concentración', false),
  (64, 'Memoria', false),
  (65, 'Lenguaje', false),
  (66, 'Habla', false),
  (67, 'Comprensión', false),
  -- Tags de temas específicos del blog/posts
  (68, 'Selectividad Alimentaria', false),
  (69, 'Texturas', false),
  (70, 'Colores', false),
  (71, 'Sensibilidad Sensorial', false),
  (72, 'Adaptación', false),
  (73, 'Superación', false),
  (74, 'Comunidad', false),
  (75, 'Apoyo Mutuo', false),
  (76, 'Experiencias Personales', false),
  (77, 'Testimonios', false),
  -- Tags de metodología profesional
  (78, 'Evaluación', false),
  (79, 'Diagnóstico', false),
  (80, 'Intervención', false),
  (81, 'Seguimiento', false),
  (82, 'Progreso', false),
  (83, 'Adaptaciones Curriculares', false),
  (84, 'Estrategias', false),
  (85, 'Recursos', false),
  (86, 'Herramientas', false),
  -- Tags de contextos
  (87, 'Hogar', false),
  (88, 'Escuela', false),
  (89, 'Centro Educativo', false),
  (90, 'Aula', false),
  (91, 'Exterior', false),
  (92, 'Presencial', false),
  (93, 'Online', false),
  -- Tags de eventos y actividades grupales
  (94, 'Eventos', false),
  (95, 'Actividades Grupales', false),
  (96, 'Participación', false),
  (97, 'Interacción', false),
  (98, 'Convivencia', false),
  (99, 'Respeto', false),
  (100, 'Normas', false),
  -- Tags adicionales específicos del centro
  (101, 'Gijón', false),
  (102, 'Asturias', false),
  (103, 'Centro Ágora', false),
  (104, 'Profesional', false),
  (105, 'Calidad', false),
  (106, 'Excelencia', false),
  (107, 'Innovación', false),
  (108, 'Creatividad', false),
  (109, 'Diversión', false),
  (110, 'Lúdico', false);

-- Inserciones en la tabla 'event_tag' (relación entre eventos y tags)
-- Evento 1: "Taller de Juegos de Mesa" (id=1) con tags Educativo, Talleres, Juegos, Infantil
INSERT INTO
  event_tag (event_id, tag_id)
VALUES
  (1, 1), -- Educativo
  (1, 2), -- Talleres
  (1, 5), -- Juegos
  (1, 7);

-- Infantil
-- Evento 2: "Escuela de Padres" (id=2) con tags Padres, Formación, Apoyo, Desarrollo
INSERT INTO
  event_tag (event_id, tag_id)
VALUES
  (2, 3), -- Padres
  (2, 4), -- Formación
  (2, 6), -- Apoyo
  (2, 8);

-- Desarrollo
-- Nota: Las imágenes de eventos se almacenan como LONGBLOB en la tabla event_images.
-- Estas inserciones son ejemplos conceptuales ya que los datos LONGBLOB deben cargarse
-- desde archivos reales usando aplicaciones específicas o scripts de migración.
-- 
-- Las siguientes inserciones muestran la estructura, pero los valores de image_data
-- deben ser reemplazados por datos binarios reales de imágenes.
-- Ejemplo de estructura para event_images:
-- INSERT INTO event_images (image_name, image_data, event_id)
-- VALUES 
--   ('juegos_mesa_1.jpg', LOAD_FILE('/path/to/juegos_mesa_1.jpg'), 1),
--   ('juegos_mesa_2.jpg', LOAD_FILE('/path/to/juegos_mesa_2.jpg'), 1),
--   ('escuela_padres_1.jpg', LOAD_FILE('/path/to/escuela_padres_1.jpg'), 2),
--   ('escuela_padres_2.jpg', LOAD_FILE('/path/to/escuela_padres_2.jpg'), 2);
-- INSERCIONES REALES para event_images con imágenes de prueba
-- Estas son imágenes JPEG mínimas válidas de 8x8 píxeles para desarrollo/testing
INSERT INTO
  event_images (image_name, image_data, event_id)
VALUES
  -- Imágenes para Evento 1: "Taller de Juegos de Mesa"
  (
    'taller_juegos_mesa_1.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    1
  ),
  (
    'taller_juegos_mesa_2.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    1
  ),
  -- Imágenes para Evento 2: "Escuela de Padres"
  (
    'escuela_padres_1.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    2
  ),
  (
    'escuela_padres_2.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    2
  );

-- Inserciones para post_tag (relación entre posts y tags)
-- Asignando tags relevantes basándose en el contenido actualizado de cada post
INSERT INTO
  post_tag (post_id, tag_id)
VALUES
  -- Post 1: "Post 1" - Sobre selectividad alimentaria, texturas, colores, desafíos personales
  (1, 68), -- Selectividad Alimentaria
  (1, 69), -- Texturas
  (1, 70), -- Colores
  (1, 71), -- Sensibilidad Sensorial
  (1, 72), -- Adaptación
  (1, 73), -- Superación
  (1, 74), -- Comunidad
  (1, 75), -- Apoyo Mutuo
  (1, 76), -- Experiencias Personales
  (1, 77), -- Testimonios
  (1, 46), -- Emocional
  (1, 50), -- Bienestar
  (1, 51), -- Autoestima
  -- Post 2: "Presunción de Competencia: Un Nuevo Paradigma"
  (2, 9), -- Neurodiversidad
  (2, 10), -- Autismo
  (2, 30), -- Inclusivo
  (2, 35), -- Comunicación
  (2, 72), -- Adaptación
  (2, 85), -- Recursos
  (2, 84), -- Estrategias
  (2, 104), -- Profesional
  (2, 107), -- Innovación
  (2, 8), -- Desarrollo
  (2, 45), -- Cognitivo
  (2, 52), -- Autonomía
  -- Post 3: "Más Allá del Capacitismo: Celebrando la Neurodiversidad"
  (3, 9), -- Neurodiversidad
  (3, 10), -- Autismo
  (3, 30), -- Inclusivo
  (3, 71), -- Sensibilidad Sensorial
  (3, 35), -- Comunicación
  (3, 86), -- Herramientas
  (3, 99), -- Respeto
  (3, 50), -- Bienestar
  (3, 51), -- Autoestima
  (3, 74), -- Comunidad
  (3, 107), -- Innovación
  -- Post 4: "Post 4" - Sobre selectividad alimentaria (mismo contenido que Post 1)
  (4, 68), -- Selectividad Alimentaria
  (4, 69), -- Texturas
  (4, 70), -- Colores
  (4, 71), -- Sensibilidad Sensorial
  (4, 72), -- Adaptación
  (4, 73), -- Superación
  (4, 74), -- Comunidad
  (4, 75), -- Apoyo Mutuo
  (4, 76), -- Experiencias Personales
  (4, 77), -- Testimonios
  (4, 46), -- Emocional
  (4, 50), -- Bienestar
  (4, 52), -- Autonomía
  -- Post 5: "Comunicación Alternativa y Aumentativa: Rompiendo Barreras"
  (5, 10), -- Autismo
  (5, 35), -- Comunicación
  (5, 17), -- Trastornos Comunicación
  (5, 86), -- Herramientas
  (5, 84), -- Estrategias
  (5, 85), -- Recursos
  (5, 30), -- Inclusivo
  (5, 99), -- Respeto
  (5, 72), -- Adaptación
  (5, 107), -- Innovación
  (5, 52), -- Autonomía
  (5, 74), -- Comunidad
  -- Post 6: "Apoyo Centrado en la Persona: Respetando la Autodeterminación"
  (6, 10), -- Autismo
  (6, 9), -- Neurodiversidad
  (6, 33), -- Atención Personalizada
  (6, 27), -- Individualizado
  (6, 52), -- Autonomía
  (6, 51), -- Autoestima
  (6, 50), -- Bienestar
  (6, 99), -- Respeto
  (6, 72), -- Adaptación
  (6, 84), -- Estrategias
  (6, 30), -- Inclusivo
  (6, 74), -- Comunidad
  (6, 8);

-- Desarrollo
-- Inserciones para post_images con imágenes de prueba
-- Estas son imágenes JPEG mínimas válidas de 8x8 píxeles para desarrollo/testing
INSERT INTO
  post_images (image_name, image_data, is_main_image, post_id)
VALUES
  -- Imágenes para Post 1: "Selectividad Alimentaria"
  (
    'selectividad_alimentaria_1.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    true,
    1
  ),
  (
    'selectividad_alimentaria_2.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    false,
    1
  ),
  -- Imágenes para Post 2: "Presunción de Competencia"
  (
    'presuncion_competencia_1.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    true,
    2
  ),
  (
    'presuncion_competencia_2.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    false,
    2
  ),
  -- Imágenes para Post 3: "Comunicación Aumentativa y Alternativa"
  (
    'comunicacion_caa_1.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    true,
    3
  ),
  -- Imágenes para Post 4: "Experiencias Personales con Selectividad Alimentaria"
  (
    'experiencias_alimentarias_1.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    true,
    4
  ),
  (
    'experiencias_alimentarias_2.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    false,
    4
  ),
  -- Imágenes para Post 5: "Desenmascarando Mitos sobre el Autismo"
  (
    'mitos_autismo_1.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    true,
    5
  ),
  -- Imágenes para Post 6: "Apoyo Centrado en la Persona"
  (
    'apoyo_centrado_persona_1.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    true,
    6
  ),
  (
    'apoyo_centrado_persona_2.jpg',
    UNHEX(
      'FFD8FFE000104A46494600010101006000600000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC0001108000800080301220002110103110FFD'
    ),
    false,
    6
  );