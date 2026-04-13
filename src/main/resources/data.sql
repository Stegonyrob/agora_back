DELETE FROM profiles;

DELETE FROM avatars;

DELETE FROM users;

-- 1. Insertar usuarios desarrolloweb
INSERT INTO
  users (username, password, email, accepted_rules)
VALUES
  (
    'admin',
    '$2a$12$uLtwNo2T3itMQEltgwCmQelweoy1oczfbgtrkmCMDVRsqoRmG66vq',
    'admin@gmail.com',
    true
  ),
  (
    'user1',
    '$2a$12$uLtwNo2T3itMQEltgwCmQelweoy1oczfbgtrkmCMDVRsqoRmG66vq',
    'user1@gmail.com',
    true
  ),
  (
    'user2',
    '$2a$12$uLtwNo2T3itMQEltgwCmQelweoy1oczfbgtrkmCMDVRsqoRmG66vq',
    'user2@gmail.com',
    true
  ),
  (
    'user3',
    '$2a$12$uLtwNo2T3itMQEltgwCmQelweoy1oczfbgtrkmCMDVRsqoRmG66vq',
    'user3@gmail.com',
    true
  ),
  (
    'user4',
    '$2a$12$uLtwNo2T3itMQEltgwCmQelweoy1oczfbgtrkmCMDVRsqoRmG66vq',
    'user4@gmail.com',
    true
  ),
  (
    'user5',
    '$2a$12$uLtwNo2T3itMQEltgwCmQelweoy1oczfbgtrkmCMDVRsqoRmG66vq',
    'user5@gmail.com',
    true
  ),
  (
    'user6',
    '$2a$12$uLtwNo2T3itMQEltgwCmQelweoy1oczfbgtrkmCMDVRsqoRmG66vq',
    'user6@gmail.com',
    true
  );

DELETE FROM avatars;

-- 2. Tabla de Avatares precargados
INSERT INTO
  avatars (
    id,
    image_name,
    image_data,
    preloaded,
    is_default,
    display_name,
    image_url
  )
VALUES
  (
    100000,
    'onron.png',
    NULL,
    true,
    false,
    'Avatar Onron (Admin)',
    '/images/avatars/onron.png'
  ),
  (
    1,
    '1.png',
    NULL,
    true,
    true,
    'Avatar amarillo sonriente',
    '/images/avatars/1.png'
  ),
  (
    2,
    '2.png',
    NULL,
    true,
    false,
    'Avatar marron sonriente',
    '/images/avatars/2.png'
  ),
  (
    3,
    '3.png',
    NULL,
    true,
    false,
    'Avatar azul cielo sonriente',
    '/images/avatars/3.png'
  ),
  (
    4,
    '4.png',
    NULL,
    true,
    false,
    'Avatar verde sonriente',
    '/images/avatars/4.png'
  ),
  (
    5,
    '5.png',
    NULL,
    true,
    false,
    'Avatar amarillo sonriente',
    '/images/avatars/5.png'
  ),
  (
    6,
    '6.png',
    NULL,
    true,
    false,
    'Avatar turquesa sonriente',
    '/images/avatars/6.png'
  ),
  (
    7,
    '7.png',
    NULL,
    true,
    false,
    'Avatar verde claro sonriente',
    '/images/avatars/7.png'
  ),
  (
    8,
    '8.png',
    NULL,
    true,
    false,
    'Avatar azul cielo sonriente 2',
    '/images/avatars/8.png'
  ),
  (
    9,
    '9.png',
    NULL,
    true,
    false,
    'Avatar gris y blanco sonriente',
    '/images/avatars/9.png'
  ),
  (
    10,
    '10.png',
    NULL,
    true,
    false,
    'Avatar lila sonriente',
    '/images/avatars/10.png'
  ),
  (
    11,
    '11.png',
    NULL,
    true,
    false,
    'Avatar azul sonriente',
    '/images/avatars/11.png'
  ),
  (
    12,
    '12.png',
    NULL,
    true,
    false,
    'Avatar amarillo sonriente 2',
    '/images/avatars/12.png'
  ),
  (
    13,
    '13.png',
    NULL,
    true,
    false,
    'Avatar azul sonriente ojos cerrados',
    '/images/avatars/13.png'
  ),
  (
    14,
    '14.png',
    NULL,
    true,
    false,
    'Avatar rosa sonriente',
    '/images/avatars/14.png'
  ),
  (
    15,
    '15.png',
    NULL,
    true,
    false,
    'Avatar marron serio',
    '/images/avatars/15.png'
  ),
  (
    16,
    '16.png',
    NULL,
    true,
    false,
    'Avatar azul cielo serio',
    '/images/avatars/16.png'
  ),
  (
    17,
    '17.png',
    NULL,
    true,
    false,
    'Avatar 17',
    '/images/avatars/17.png'
  ),
  (
    18,
    '18.png',
    NULL,
    true,
    false,
    'Avatar azul feliz',
    '/images/avatars/18.png'
  ),
  (
    19,
    '19.png',
    NULL,
    true,
    false,
    'Avatar amarillo feliz',
    '/images/avatars/19.png'
  ),
  (
    20,
    '20.png',
    NULL,
    true,
    false,
    'Avatar verde sonriente',
    '/images/avatars/20.png'
  ),
  (
    21,
    '21.png',
    NULL,
    true,
    false,
    'Avatar gris sonriente',
    '/images/avatars/21.png'
  ),
  (
    22,
    '22.png',
    NULL,
    true,
    false,
    'Avatar azul cielo mirando a la izquierda',
    '/images/avatars/22.png'
  ),
  (
    23,
    '23.png',
    NULL,
    true,
    false,
    'Avatar azul cielo serio',
    '/images/avatars/23.png'
  ),
  (
    24,
    '24.png',
    NULL,
    true,
    false,
    'Avatar rosa con cuernos sonriente',
    '/images/avatars/24.png'
  ),
  (
    25,
    '25.png',
    NULL,
    true,
    false,
    'Avatar verde y blanco sonriente',
    '/images/avatars/25.png'
  ),
  (
    26,
    '26.png',
    NULL,
    true,
    false,
    'Avatar lila serio',
    '/images/avatars/26.png'
  ),
  (
    27,
    '27.png',
    NULL,
    true,
    false,
    'Avatar azul serio 2',
    '/images/avatars/27.png'
  ),
  (
    28,
    '28.png',
    NULL,
    true,
    false,
    'Avatar verde pensativo',
    '/images/avatars/28.png'
  );

-- 3. Insertar perfiles
INSERT INTO
  profiles (
    id_profile,
    user_id,
    first_name,
    last_name1,
    last_name2,
    username,
    relationship,
    email,
    password,
    confirm_password,
    city,
    country,
    phone,
    avatar_id
  )
VALUES
  (
    1,
    1,
    'Ivan',
    'Apellido1',
    'SegApellido1',
    'ivanboss',
    'Cool',
    'admin@gmail.com',
    '',
    '',
    'Gijon',
    'España',
    '600000001',
    (
      SELECT
        id
      FROM
        avatars
      WHERE
        image_name = 'onron.png'
    )
  ),
  (
    2,
    2,
    'Stella',
    'Apellido1',
    'SegApellido1',
    'stella',
    'Sister',
    'user1@gmail.com',
    '',
    '',
    'Oviedo',
    'España',
    '600000002',
    (
      SELECT
        id
      FROM
        avatars
      WHERE
        image_name = '1.png'
    )
  ),
  (
    3,
    3,
    'Paula',
    'Apellido1',
    'SegApellido1',
    'userthree',
    'Friend',
    'admin2@gmail.com',
    '',
    '',
    'Avilés',
    'España',
    '600000003',
    (
      SELECT
        id
      FROM
        avatars
      WHERE
        image_name = '6.png'
    )
  ),
  (
    4,
    4,
    'Name3',
    'Apellido1',
    'SegApellido1',
    'userfour',
    'Brother',
    'user3@gmail.com',
    '',
    '',
    'Mieres',
    'España',
    '600000004',
    (
      SELECT
        id
      FROM
        avatars
      WHERE
        image_name = '3.png'
    )
  ),
  (
    5,
    5,
    'Name4',
    'Apellido1',
    'SegApellido1',
    'userfive',
    'Cousin',
    'user4@gmail.com',
    '',
    '',
    'Langreo',
    'España',
    '600000005',
    (
      SELECT
        id
      FROM
        avatars
      WHERE
        image_name = '4.png'
    )
  ),
  (
    6,
    6,
    'Name5',
    'Apellido1',
    'SegApellido1',
    'usersix',
    'Friend',
    'user5@gmail.com',
    '',
    '',
    'Siero',
    'España',
    '600000006',
    (
      SELECT
        id
      FROM
        avatars
      WHERE
        image_name = '5.png'
    )
  );

-- 4. Inserciones en la tabla 'roles'
INSERT INTO
  roles (id_role, name)
VALUES
  (default, 'ROLE_ADMIN');

INSERT INTO
  roles (id_role, name)
VALUES
  (default, 'ROLE_USER');

-- 5. Asociación entre usuarios y roles
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
  (1, 3);

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

-- 6. Inserción de publicaciones
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

-- 7. Inserta comentarios para posts existentes
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

-- 8. Inserciones en la tabla 'replies'
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

-- 9. Insertar los 23 textos (tabla texts)
INSERT INTO
  texts (category, title, message, created_at, archived)
VALUES
  (
    'agora',
    '¿Quiénes Somos?',
    'Somos un centro educativo que trabaja de forma integral en el ámbito de la neurodiversidad y el refuerzo educativo individualizado y personalizado. Nuestra misión se cimienta en una profunda comprensión: cada mente es única, con sus propios ritmos, desafíos y, lo más importante, talentos extraordinarios. Lejos de aplicar un modelo de talla única, adoptamos un enfoque inclusivo y diferencial que celebra las distintas formas en que las personas aprenden y procesan la información, reconociendo condiciones como el TDAH, la dislexia, el espectro autista, y otras. Nuestro objetivo principal es el desarrollo pleno de cada persona, respetando intrínsecamente su individualidad y concentrándonos estratégicamente en sus potencialidades. Priorizamos a la persona como el elemento clave de toda intervención educativa. Esto significa que nuestros programas no se limitan a cubrir carencias académicas, sino que están diseñados para fomentar la autoconciencia, la regulación emocional y las habilidades sociales necesarias para una vida autónoma y satisfactoria.

Nuestra Metodología: Respeto y Competencia
La metodología que empleamos se basa en la evaluación continua y la flexibilidad curricular. Cada plan de refuerzo se construye tras un análisis detallado de las fortalezas cognitivas y emocionales del estudiante, utilizando herramientas y estrategias didácticas que se adaptan a su estilo de aprendizaje específico. Implementamos el aprendizaje multisensorial, la gamificación o el uso de apoyos visuales, buscando siempre el canal de comunicación más efectivo. Creemos firmemente que el éxito no se mide solo por las calificaciones, sino por la capacidad de la persona para desenvolverse en el mundo y alcanzar su máximo potencial dentro de su propio marco de bienestar. Al centrarnos en las potencialidades, transformamos lo que a menudo se percibe como una "dificultad" en un punto de partida para una habilidad o un interés especial. Nuestro compromiso es ser un catalizador para la confianza y la autoeficacia.

Compromiso Ético: Por qué No Aplicamos ABA (Análisis de Comportamiento Aplicado)
Como principio rector fundacional de nuestra identidad, hemos tomado la decisión de no aplicar técnicas basadas en el Análisis de Comportamiento Aplicado (ABA), especialmente en su modalidad conductual intensiva. Esta postura ética se alinea con nuestra filosofía de presunción de competencia y respeto incondicional a la identidad personal:

Priorizamos la Autorregulación Interna: El ABA tradicional se centra en la modificación conductual externa mediante refuerzos para ajustarse a normas neurotípicas. Nosotros, en cambio, priorizamos la autorregulación interna y el manejo emocional, respetando la motivación intrínseca de la persona.
Rechazo al Enmascaramiento (Masking): Las terapias enfocadas en la normalización obligan a la persona neurodivergente a enmascarar sus rasgos naturales (como movimientos auto-reguladores o stimming). Este proceso es reportado por la comunidad autista como una fuente de ansiedad crónica y daño a la identidad a largo plazo. Nuestro enfoque valida y respeta las formas propias de regulación de cada individuo.
Enfoque en la Autonomía: El modelo ABA puede enfocar el éxito en la obediencia. Nosotros nos enfocamos en la autonomía, la autodeterminación y la enseñanza de habilidades que permitan a la persona tomar decisiones informadas y expresar sus necesidades, promoviendo su liderazgo sobre su propia vida.

En resumen, optamos por intervenciones educativas y psicopedagógicas que fomenten la competencia, la autoaceptación y la calidad de vida de la persona, en lugar de intentar imponer un estándar de comportamiento neurotípico. Nuestro compromiso es asegurar que cada estudiante se sienta visto, valorado y equipado para trazar su propio camino de forma auténtica.',
    NOW(),
    false
  ),
  (
    'agora',
    'Dónde Estamos',
    'Nos encontramos en la zona Oeste de Gijón, más exactamente en La Calzada en Calle Nicaragua al 16. Nuestros Horarios de atención son de Septiembre a Junio de  Lunes a Viernes de 16:00 a 21:00 , Sabádos de 10:00 a 14:00 , Domingo Cerrado y en periodo de Verano de Lunes a Viernes de 9:00 a 14:00 , Sabados 10:00 a 14:00 Domingo Cerrados. Estamos aquí para ofrecerte el mejor apoyo educativo y acompañamiento personalizado en un entorno accesible y acogedor.',
    NOW(),
    false
  ),
  (
    'services',
    'Nuestros Servicios',
    'Nuestros servicios están meticulosamente diseñados para atender las necesidades únicas de cada estudiante de manera individualizada, asegurando la promoción de su desarrollo integral y bienestar emocional y académico.

Ofrecemos una gama de intervenciones especializadas y programas de apoyo continuo. Entre ellos destacan: la Reeducación Pedagógica, enfocada en optimizar los procesos de aprendizaje y las funciones ejecutivas; y la Educación Psicomotriz, crucial para el desarrollo de la coordinación y la conciencia corporal, elementos fundamentales para el aprendizaje.

Además, proporcionamos Refuerzo de Inglés en grupos reducidos y Refuerzo Educativo Especializado en grupos pequeños para garantizar una atención personalizada y efectiva en materias curriculares.

Para fomentar habilidades sociales y el aprendizaje lúdico, organizamos Talleres Temáticos Educativos durante periodos vacacionales y un Taller de Juegos de Mesa. Reconociendo la importancia del entorno familiar, facilitamos una Escuela de Familias, ofreciendo herramientas y estrategias a los padres para un acompañamiento efectivo en el proceso educativo de sus hijos. Cada servicio está alineado con nuestra filosofía de respeto a la individualidad.',
    NOW(),
    false
  ),
  (
    'services',
    'Reeducación Pedagógica',
    'Apoyo educativo, Intervención en NEE y NEAE, Cuidado especializado por horas, Intervención educativa con familias y en el hogar, Coordinación multidisciplinar con centros educativos y particulares. Somos beneficiarios de las becas de reeducación pedagógica de NEAE y NEE.

Nuestra propuesta de valor se centra en un Apoyo Educativo exhaustivo y una Intervención especializada en Necesidades Específicas de Apoyo Educativo (NEAE) y Necesidades Educativas Especiales (NEE). Abordamos cada caso desde la premisa de la neurodiversidad, entendiendo que la variabilidad cerebral es una riqueza, no una deficiencia. Esto se traduce en planes de intervención diseñados a medida que maximizan las fortalezas y respetan los ritmos individuales de cada estudiante. Ofrecemos Cuidado Especializado por Horas, proporcionando un entorno seguro y estimulante que se adapta a las rutinas familiares. Además, extendemos nuestro compromiso con la Intervención Educativa con Familias y en el Hogar, creando un puente coherente entre el centro y el entorno natural del estudiante.

La Coordinación Multidisciplinar es fundamental, manteniendo una comunicación activa y fluida con centros educativos y otros profesionales particulares para garantizar una visión unificada y efectiva. Es un orgullo ser beneficiarios de las becas de reeducación pedagógica de NEAE y NEE, un reconocimiento que valida la calidad y el enfoque de nuestros programas. Nuestro equipo está altamente capacitado para transformar los desafíos de aprendizaje en oportunidades de desarrollo, promoviendo siempre la autonomía, la confianza y el desarrollo de la identidad de la persona neurodiversa.',
    NOW(),
    false
  ),
  (
    'services',
    'Educación Psicomotriz',
    'Sesiones diseñadas para fomentar el desarrollo integral de los niños a través del movimiento y la interacción con el entorno. Se trabaja la coordinación, el equilibrio, la conciencia corporal y otras habilidades motoras fundamentales para el aprendizaje y el bienestar.

Nuestras sesiones de Educación Psicomotriz están diseñadas con un profundo respeto por el proceso de desarrollo integral y la neurodiversidad de cada niño. Entendemos el movimiento no solo como una actividad física, sino como el lenguaje primario a través del cual el niño se relaciona, aprende y organiza su pensamiento. Por ello, las sesiones fomentan una interacción rica y segura con el entorno, permitiendo que cada niño explore a su propio ritmo. Trabajamos en la mejora de la coordinación motriz fina y gruesa, el equilibrio y, de manera crucial, la conciencia corporal.

Estas habilidades motoras son fundamentales, ya que sientan las bases neurológicas para procesos cognitivos superiores, incluyendo la atención, la lectoescritura y el razonamiento lógico. Más allá del beneficio físico, la psicomotricidad es un espacio de desarrollo emocional donde se potencia la autoestima, la gestión de la frustración y el bienestar general. Nuestro enfoque es siempre lúdico y no directivo, valorando la expresión espontánea y la creatividad del niño. Respetamos la individualidad de su desarrollo sensoriomotor, asegurando que el proceso sea una experiencia de éxito y empoderamiento que refuerce la conexión entre el cuerpo, la emoción y el conocimiento.

',
    NOW(),
    false
  ),
  (
    'services',
    'Refuerzo de Inglés en Grupos Reducidos',
    'Clases de apoyo para el aprendizaje del inglés, impartidas en grupos pequeños para garantizar una atención más personalizada. Se refuerzan los contenidos curriculares, se trabajan las habilidades comunicativas y se fomenta la confianza en el uso del idioma.

El aprendizaje de un segundo idioma, como el inglés, se aborda en nuestro centro con un enfoque altamente sensible a la neurodiversidad y a los distintos estilos de procesamiento lingüístico. Nuestras clases de apoyo se imparten en grupos muy reducidos, lo que no solo garantiza una atención personalizada, sino que también crea un entorno de aprendizaje seguro y de baja presión, esencial para alumnos con diferencias de procesamiento o ansiedad ante el rendimiento. Reforzamos los contenidos curriculares siguiendo una metodología adaptada que incorpora múltiples canales sensoriales, como el aprendizaje visual, kinestésico y auditivo, para asegurar que la información sea accesible para todos.

El objetivo principal va más allá de aprobar un examen: nos concentramos en desarrollar las habilidades comunicativas prácticas (escucha, habla, lectura y escritura) de forma integrada. Ponemos especial énfasis en fomentar la confianza en el uso del idioma. Para un estudiante neurodiverso, el éxito en la comunicación se logra a través de la aceptación de errores como parte natural del aprendizaje. Celebramos cada progreso y utilizamos estrategias lúdicas y basadas en el interés del estudiante para hacer del inglés una herramienta útil y divertida. Promovemos un entorno donde cada voz es valorada, respetando los tiempos de respuesta y la forma de expresión individual.',
    NOW(),
    false
  ),
  (
    'services',
    'Refuerzo Educativo Especializado',
    'Apoyo académico individualizado o en grupos muy reducidos para alumnos con necesidades educativas especiales o dificultades de aprendizaje. Se adaptan las estrategias y los materiales para atender las particularidades de cada estudiante y facilitar su progreso.

Nuestro programa de Apoyo Académico es el núcleo de nuestra filosofía de respeto a la neurodiversidad. Está diseñado específicamente para alumnos con Necesidades Educativas Especiales (NEE) y diversas Dificultades de Aprendizaje (DA), ofreciendo sesiones individuales o en grupos muy pequeños. Partimos de la firme creencia de que cada desafío en el aprendizaje es una señal de que el sistema o la metodología deben adaptarse a la persona, y no al revés. La clave de nuestro éxito reside en la adaptación profunda de estrategias y materiales.

Esto implica ir más allá de la simple ayuda con los deberes; significa construir un perfil de aprendizaje detallado de cada estudiante, identificando sus fortalezas cognitivas y utilizando herramientas compensatorias que promuevan su autonomía. Ya sea mediante apoyos visuales, software adaptado o técnicas de organización basadas en su estilo neurológico, el objetivo es facilitar su progreso académico y su desarrollo personal. Presumimos competencia en cada estudiante, actuando como facilitadores que les enseñan a aprender, a autorregularse y a utilizar sus diferencias como ventajas. Nuestro enfoque es holístico y se centra en el empoderamiento, asegurando que el alumno gane las habilidades metacognitivas necesarias para ser el protagonista activo de su propio aprendizaje.',
    NOW(),
    false
  ),
  (
    'services',
    'Talleres Temáticos Educativos',
    'Programas especiales durante los periodos vacacionales con temáticas diversas y educativas. Estos talleres buscan ofrecer un espacio de aprendizaje lúdico y creativo, explorando diferentes áreas de conocimiento y desarrollando nuevas habilidades de manera divertida.

Los Talleres Temáticos Educativos que ofrecemos durante los periodos vacacionales son una extensión de nuestra filosofía de aprendizaje lúdico y respetuoso con la neurodiversidad. Lejos del formato de la escuela tradicional, estos programas están diseñados para ser un espacio de aprendizaje creativo y desestructurado, donde se minimiza la presión de rendimiento y se maximiza el disfrute. A través de temáticas diversas y atractivas, buscamos explorar diferentes áreas de conocimiento que quizás no se aborden en el currículo estándar, permitiendo a los participantes desarrollar nuevas habilidades en un ambiente relajado.

El enfoque es intrínsecamente inclusivo, utilizando una metodología activa y participativa que se adapta a las distintas formas de interacción y procesamiento. Por ejemplo, un taller de ciencia puede enfocarse en la experimentación práctica para estudiantes kinestésicos, mientras que uno de arte puede ofrecer diferentes medios de expresión para adaptarse a sensibilidades sensoriales. El objetivo principal es que el niño o adolescente asocie el aprendizaje con la diversión, la curiosidad y el éxito personal. Fomentamos la interacción social positiva, el respeto mutuo y la aceptación de las diferencias, asegurando que cada participante pueda expresarse y desarrollarse de manera auténtica.',
    NOW(),
    false
  ),
  (
    'services',
    'Taller Juegos de Mesa',
    'Espacio dedicado al aprendizaje y desarrollo a través de los juegos de mesa. Se fomenta el pensamiento estratégico, la resolución de problemas, la cooperación, el respeto por las normas y las habilidades sociales en un ambiente divertido y participativo.

Nuestro Taller de Juegos de Mesa es una herramienta poderosa y lúdica para el desarrollo de habilidades cognitivas y sociales, totalmente alineado con el respeto a la neurodiversidad. Los juegos de mesa son, por naturaleza, entornos controlados y estructurados que facilitan el entrenamiento de funciones ejecutivas cruciales. Este espacio está dedicado a fomentar el pensamiento estratégico y la resolución de problemas de una manera tangible y motivadora. La naturaleza intrínsecamente divertida y participativa del taller reduce la ansiedad y facilita el engagement en tareas que requieren concentración.

Más allá de lo cognitivo, el taller es un laboratorio social esencial. Promovemos activamente la cooperación (en juegos de equipo) y el respeto por las normas, lo cual es una habilidad vital para la vida y el autocontrol. Además, ofrece un marco seguro para practicar habilidades sociales, como esperar el turno, manejar la frustración de perder y celebrar el éxito ajeno. Para las personas neurodiversas, el juego de mesa ofrece un conjunto de reglas claras y previsibles, un "guion" social que reduce la incertidumbre y facilita la interacción, permitiéndoles practicar y generalizar estas habilidades a otros contextos de forma natural y respetuosa con su forma de ser.',
    NOW(),
    false
  ),
  (
    'services',
    'Escuela de Familias',
    'Programa de formación y apoyo dirigido a padres y madres. Se ofrecen herramientas, estrategias y recursos para afrontar los retos de la crianza, mejorar la comunicación familiar y promover un desarrollo saludable de los hijos.

Nuestra Escuela de Familias es un programa esencial que reconoce a los padres y madres como los principales agentes de cambio y apoyo para sus hijos. El programa está diseñado para ofrecer una formación y apoyo de alta calidad, centrada en la comprensión y el respeto a la neurodiversidad. Entendemos que la crianza de un hijo neurodiverso presenta retos únicos, y nuestra misión es equipar a las familias con las herramientas, estrategias y recursos necesarios para afrontarlos con competencia y amor.

Se abordan temas cruciales como la gestión conductual positiva, la mejora de la comunicación familiar adaptada a los estilos comunicativos específicos del hijo, y el fomento de la autonomía. Nuestro enfoque es siempre empático y sin juicio, creando un espacio de intercambio seguro donde las familias pueden compartir experiencias y sentirse validadas. Al promover una comprensión profunda de las necesidades y fortalezas únicas de sus hijos, ayudamos a los padres a pasar de un modelo centrado en la "corrección" a uno centrado en la validación y el apoyo al desarrollo saludable. El objetivo final es fortalecer la unidad familiar, reducir el estrés parental y asegurar que el hogar sea un entorno de aceptación incondicional y crecimiento.',
    NOW(),
    false
  ),
  (
    'team',
    'Equipo Profesional',
    'Soy un profesional apasionado por la educación y el desarrollo integral de los niños. Con una formación sólida en pedagogía y psicología infantil, he dedicado mi carrera a crear entornos de aprendizaje inclusivos y estimulantes.',
    NOW(),
    false
  ),
  (
    'neurodiversity',
    'Neurodiversidad ¿Qué es?',
    'La neurodiversidad se define como la variabilidad natural del cerebro humano, que abarca una amplia gama de diferencias neurológicas y cognitivas. Esta perspectiva reconoce que las variaciones en el funcionamiento cerebral son normales y valiosas.

La Neurodiversidad es el principio fundamental que guía toda nuestra filosofía y metodología. Se define como la variabilidad natural y deseable del cerebro humano, una perspectiva que abarca y celebra la amplia gama de diferencias neurológicas y cognitivas que existen entre las personas. Esta visión es esencialmente un movimiento por la justicia social que despatologiza las diferencias. Reconocemos que variaciones en el procesamiento de la información, el timing social, la atención o el aprendizaje —como se manifiestan en el Autismo, el TDAH o la Dislexia—, son formas de ser tan normales y válidas como cualquier otra.

Nuestra adopción de esta perspectiva implica un cambio radical: las dificultades no son inherentes a la persona, sino el resultado de la falta de adaptación y accesibilidad en el entorno. Al abrazar la neurodiversidad, afirmamos que estas variaciones en el funcionamiento cerebral no solo son normales, sino valiosas, aportando talentos, perspectivas y soluciones únicas a nuestra comunidad. Nuestro trabajo se enfoca en crear entornos educativos y sociales inclusivos que permitan que la persona neurodivergente prospere, utilice sus fortalezas únicas y desarrolle una identidad positiva y competente.',
    NOW(),
    false
  ),
  (
    'neurodiversity',
    'Nuestra Visión de la Neurodiversidad',
    'Nuestra visión de la neurodiversidad se basa en el respeto y la valoración de las diferencias individuales. Creemos que cada persona tiene un potencial único y que la diversidad cognitiva enriquece nuestras comunidades.

Nuestra visión institucional de la neurodiversidad es un compromiso inquebrantable con el respeto incondicional y la valoración activa de las diferencias individuales. Para nosotros, no se trata solo de ser "tolerantes", sino de presumir competencia y buscar activamente las fortalezas inherentes a cada configuración neurológica. Creemos firmemente que cada persona neurodivergente posee un potencial único y significativo, talentos que a menudo son invisibles en entornos educativos rígidos. Nuestra labor es ser el catalizador que ayude a descubrir y cultivar ese potencial, concentrando nuestra intervención en las habilidades que la persona ya tiene para construir desde una base de éxito.

Afirmamos con convicción que la diversidad cognitiva —esta gama de cerebros que piensan, sienten y aprenden de manera diferente— enriquece inmensamente nuestras comunidades. Esta visión nos impulsa a diseñar estrategias individualizadas que no buscan "normalizar" a la persona, sino crear un entorno que sea flexible, accesible y validante para ella. El respeto a la neurodiversidad es, por lo tanto, la base ética para fomentar una autoestima fuerte y una identidad positiva, asegurando que cada estudiante sepa que su forma de ser no solo es aceptada, sino celebrada.',
    NOW(),
    false
  ),
  (
    'cea',
    'Condición del Espectro Autista',
    'La condición del espectro autista (CEA) es una condición del neurodesarrollo que afecta la comunicación, la interacción social y el comportamiento. Se manifiesta de diversas formas y con diferentes grados de intensidad.

La Condición del Espectro Autista (CEA) se entiende en nuestro centro como una variación del neurodesarrollo que impacta la forma en que una persona se relaciona con el mundo. Es fundamental reconocer que el CEA es un espectro, lo que significa que se manifiesta de innumerables formas y con diferentes grados de intensidad, haciendo que la experiencia de cada persona autista sea única. Si bien tradicionalmente se ha definido por diferencias en la comunicación, la interacción social y los patrones de comportamiento (incluyendo intereses restringidos o comportamientos repetitivos), nuestra aproximación se enfoca en las fortalezas que a menudo acompañan a esta neurotipo.

Estas fortalezas pueden incluir una atención excepcional al detalle, un pensamiento lógico superior, una gran honestidad o la capacidad para una inmersión profunda en temas de interés. Nuestro enfoque se centra en adaptar la comunicación a los estilos de procesamiento autista (por ejemplo, usando apoyos visuales y lenguaje directo), y en enseñar habilidades sociales en el contexto de las normas sociales neurotípicas, siempre respetando la necesidad de espacios y el estilo de interacción preferido del individuo. Abogamos por la autonomía y el respeto a la identidad autista, proporcionando las herramientas necesarias para que puedan navegar el mundo sin presiones de enmascaramiento innecesarias.',
    NOW(),
    false
  ),
  (
    'cea',
    'Evolución de la CEA',
    '

La condición del espectro autista, antiguamente considerada un trastorno (TEA), ha evolucionado en su comprensión y enfoque. Actualmente, se reconoce que cada persona con CEA es única.

La comprensión de la Condición del Espectro Autista (CEA) ha experimentado una evolución fundamental, pasando de ser vista meramente como un Trastorno del Espectro Autista (TEA) —una terminología más antigua y patologizante— a ser reconocida como una condición del neurodesarrollo y una manifestación de la neurodiversidad. Este cambio de enfoque es vital para nuestra práctica. Rechazamos la idea de que el autismo es algo que debe ser "curado" o "normalizado". En su lugar, reconocemos que el autismo es una forma de ser, de percibir y de interactuar.

La clave de nuestra intervención es el reconocimiento de que cada persona con CEA es única. Las estrategias que funcionan para un individuo pueden no ser adecuadas para otro. Por ello, la intervención es siempre personalizada y centrada en los objetivos y el bienestar de la persona autista. Nos enfocamos en enseñar habilidades compensatorias y en crear entornos accesibles, en lugar de intentar modificar el núcleo de la identidad autista. Esta visión respeta la autodeterminación y promueve una identidad autista positiva, donde las fortalezas y los intereses especiales son valorados como recursos.',
    NOW(),
    false
  ),
  (
    'tda_tdh',
    'Trastorno del Déficit de Atención (TDA)',
    'El trastorno del déficit de atención (TDA) es una condición del neurodesarrollo que se caracteriza por dificultades en la atención. Estas dificultades pueden afectar el rendimiento académico y las relaciones interpersonales.

El Trastorno del Déficit de Atención (TDA), sin hiperactividad, se considera una condición del neurodesarrollo caracterizada principalmente por diferencias significativas en la regulación de la atención. Lejos de ser un déficit de capacidad, se entiende como una dificultad para dirigir y mantener la atención de forma consistente, especialmente en tareas que no son intrínsecamente estimulantes o interesantes. Estas diferencias en la función ejecutiva pueden, efectivamente, afectar el rendimiento académico y, en ocasiones, las relaciones interpersonales, debido a despistes o dificultades para seguir instrucciones largas.

Nuestra intervención se basa en la presunción de competencia. Sabemos que el cerebro con TDA es capaz de una hiperconcentración asombrosa cuando el interés es alto. Por lo tanto, nuestro trabajo se enfoca en enseñar estrategias de gestión de la atención y habilidades organizativas que se adapten a este perfil. Utilizamos herramientas como el chunking (dividir tareas grandes), la gamificación, y el uso de señales visuales y checklists. El objetivo es proporcionar al estudiante el "andamiaje" externo necesario para compensar las diferencias en su función ejecutiva, fomentando su autonomía y transformando el desafío atencional en una habilidad de enfoque autodirigido.',
    NOW(),
    false
  ),
  (
    'tda_tdh',
    'Trastorno del Déficit de Atención con Hiperactividad (TDAH)',
    'El TDAH es una condición del neurodesarrollo que se caracteriza por dificultades en la atención, la hiperactividad y la impulsividad. Es importante abordar el TDAH con estrategias adecuadas y un enfoque comprensivo.

El Trastorno por Déficit de Atención e Hiperactividad (TDAH) es una condición del neurodesarrollo que implica diferencias en la atención, la hiperactividad (o inquietud motriz/mental) y la impulsividad. Abordamos el TDAH con un enfoque profundamente comprensivo y respetuoso, reconociendo las fortalezas que a menudo acompañan a este neurotipo, como la energía, la creatividad, el pensamiento rápido y la capacidad de actuar bajo presión. La hiperactividad y la impulsividad no se ven como comportamientos a erradicar, sino como manifestaciones de un sistema nervioso que necesita una estimulación o descarga diferente.

Nuestra intervención se centra en dotar al estudiante de estrategias de autorregulación y herramientas de coaching. Esto incluye técnicas para gestionar la impulsividad mediante pausas estructuradas, el uso de movimiento para optimizar el focus (como el fidgeting discreto) y la enseñanza de habilidades de planificación ejecutiva. Es fundamental que la persona con TDAH se sienta vista y aceptada. Mediante un abordaje integral, que incluye la colaboración con la familia, garantizamos que el estudiante reciba el apoyo necesario para canalizar su energía y creatividad, transformando las diferencias en una fuente de competencia y autoeficacia.',
    NOW(),
    false
  ),
  (
    'learning_difficulties',
    'Dificultades del Aprendizaje',
    'Las dificultades del aprendizaje son condiciones que afectan la capacidad de una persona para adquirir, procesar o utilizar información de manera efectiva. Es fundamental identificar y abordar estas dificultades de manera temprana.

Las Dificultades del Aprendizaje (DA) son condiciones intrínsecas que afectan la manera en que una persona adquiere, procesa o utiliza la información de manera efectiva, afectando áreas como la lectura (dislexia), la escritura (disgrafía) o las matemáticas (discalculia). Desde nuestra perspectiva de neurodiversidad, entendemos que estas no reflejan una falta de inteligencia, sino una diferencia en el cableado neuronal para procesar información específica. Es fundamental identificar y abordar estas dificultades de manera temprana para prevenir el bajo rendimiento, la frustración y la disminución de la autoestima.

Nuestra intervención se basa en un diagnóstico funcional que va más allá de la etiqueta, buscando comprender cómo aprende el estudiante. Desarrollamos programas de reeducación que utilizan metodologías multisensoriales y estructuradas para crear nuevas vías de procesamiento. En lugar de forzar a un estudiante disléxico a leer más, le enseñamos estrategias compensatorias y utilizamos tecnología de apoyo. Nuestro objetivo es que el estudiante comprenda sus diferencias, se sienta competente y aprenda a navegar el sistema educativo utilizando sus fortalezas, asegurando que su capacidad intelectual no se vea limitada por un obstáculo específico en el aprendizaje.

',
    NOW(),
    false
  ),
  (
    'learning_difficulties',
    'Desafíos del Aprendizaje',
    'Los desafíos del aprendizaje son obstáculos que pueden dificultar el proceso educativo de los estudiantes. Estos desafíos pueden ser de naturaleza cognitiva, emocional o social.

Los Desafíos del Aprendizaje representan los obstáculos multifacéticos que pueden dificultar el proceso educativo de cualquier estudiante, con o sin un diagnóstico específico. Reconocemos que estos desafíos pueden ser de naturaleza cognitiva (relacionados con la memoria, la atención o el procesamiento), emocional (como la ansiedad ante los exámenes o la baja autoestima) o social (dificultades en la interacción con compañeros o profesores). Nuestro enfoque es integral y siempre respeta la individualidad del estudiante, entendiendo que un desafío rara vez es puramente académico.

Abordamos estos obstáculos mediante la creación de un entorno de apoyo seguro y a través de la enseñanza de habilidades de afrontamiento y metacognición. Si la ansiedad es el obstáculo, trabajamos en la regulación emocional; si es la organización, en la función ejecutiva. Para los estudiantes neurodiversos, estos desafíos pueden intensificarse debido a la falta de adaptaciones en el sistema. Por ello, trabajamos en la adaptación del entorno y en dotar al estudiante de la confianza y las estrategias necesarias para superar activamente estos obstáculos, fomentando una mentalidad de crecimiento y la resiliencia académica y personal.',
    NOW(),
    false
  ),
  (
    'development_conditions',
    'Condiciones del Desarrollo',
    'Las condiciones del desarrollo son un conjunto de factores que influyen en el crecimiento y la evolución de los niños. Estos factores pueden ser biológicos, psicológicos o sociales.

El término Condiciones del Desarrollo hace referencia al amplio conjunto de factores (biológicos, psicológicos y sociales) que modelan la trayectoria de crecimiento y evolución de los niños. En nuestro centro, entendemos estas condiciones desde una perspectiva de trayectoria individualizada. Los factores biológicos incluyen la dotación genética y las diferencias neurológicas; los psicológicos abarcan el temperamento, la personalidad y las experiencias emocionales; y los sociales se refieren al entorno familiar, escolar y comunitario. Todos interactúan dinámicamente.

Al trabajar bajo el prisma de la neurodiversidad, reconocemos que muchas de las "condiciones" tradicionalmente vistas como "trastornos" son, de hecho, diferencias biológicas que requieren un entorno social y educativo adaptado. Nuestro objetivo es comprender esta compleja interacción de factores. Al individualizar la intervención, nos aseguramos de que el apoyo no se limite a tratar un síntoma, sino a nutrir el desarrollo en todos los ámbitos, potenciando las capacidades de adaptación y la construcción de una identidad fuerte y positiva, respetando profundamente el ritmo y el estilo evolutivo único de cada niño.',
    NOW(),
    false
  ),
  (
    'development_conditions',
    'Importancia de las Condiciones del Desarrollo',
    'La comprensión de las condiciones del desarrollo es esencial para ofrecer un apoyo adecuado a los estudiantes. Es fundamental abordar cada caso de manera individualizada.

La Comprensión profunda de las Condiciones del Desarrollo es, para nuestro equipo, la piedra angular de un apoyo educativo ético y efectivo. No basta con conocer la etiqueta diagnóstica; es esencial entender cómo se manifiestan esas condiciones en la vida diaria de un estudiante y cómo interactúan con su entorno. Esto implica un análisis funcional detallado de los factores biológicos, psicológicos y sociales que influyen en su aprendizaje y bienestar. Reconocemos que una comprensión superficial lleva a intervenciones generalizadas e ineficaces.

Por ello, la premisa de abordar cada caso de manera individualizada es innegociable. No aplicamos protocolos genéricos, sino que diseñamos un Plan de Intervención Personalizado (PIP) que es único para cada estudiante, honrando su estilo cognitivo, sus intereses y sus necesidades. Este enfoque garantiza que el apoyo sea pertinente y motivador. Al basar nuestra práctica en el respeto a la neurodiversidad, aseguramos que las estrategias implementadas no busquen el conformismo, sino la autorregulación y el empoderamiento, permitiendo que cada estudiante avance desde una posición de competencia y autoaceptación.',
    NOW(),
    false
  ),
  (
    'communication',
    'Trastornos de la Comunicación',
    'Los trastornos de la comunicación son condiciones que afectan la capacidad de una persona para comunicarse de manera efectiva. La intervención temprana y el apoyo adecuado son clave.

Los Trastornos de la Comunicación son condiciones que afectan la habilidad de una persona para comprender (lenguaje receptivo) o expresar (lenguaje expresivo) información de manera efectiva, tanto a nivel verbal como no verbal. Estas dificultades, que abarcan desde el desarrollo del lenguaje hasta la fluidez (tartamudez) o la articulación, impactan directamente la capacidad del individuo para participar plenamente en entornos sociales y académicos. En línea con nuestra filosofía, entendemos que estas no son fallas, sino diferencias en el procesamiento del lenguaje.

La Intervención Temprana es crucial, pues optimiza la plasticidad cerebral y sienta las bases para el desarrollo futuro. Nuestro apoyo adecuado se materializa en terapias de lenguaje personalizadas, que respetan el ritmo de cada persona y utilizan métodos de comunicación aumentativa y alternativa (CAA) cuando es necesario. Es fundamental que la intervención no solo se centre en la producción verbal, sino en la capacidad funcional de comunicarse, valorando todas las formas de expresión. Al actuar de forma temprana y respetuosa, mitigamos la frustración comunicativa y fomentamos la confianza y la competencia social del individuo.',
    NOW(),
    false
  ),
  (
    'communication',
    'Importancia de los Trastornos de la Comunicación',
    'La comprensión de los trastornos de la comunicación es esencial para ofrecer un apoyo adecuado a los estudiantes. La correcta identificación y tratamiento puede marcar una gran diferencia.

Una Comprensión exhaustiva de los Trastornos de la Comunicación es esencial para el éxito educativo y social de nuestros estudiantes. La correcta identificación de la naturaleza y el grado del desafío comunicativo es el primer paso para diseñar un programa de apoyo adecuado y verdaderamente individualizado. En nuestro enfoque, valoramos las diferencias en la comunicación como parte de la neurodiversidad, y nuestro objetivo es facilitar la conexión efectiva, no imponer un único estilo comunicativo.

Entendemos que el tratamiento debe ser integral y que el éxito se mide por la mejora en la calidad de vida y las oportunidades de participación del estudiante. La intervención no se aísla, sino que se integra en el entorno educativo y familiar, asegurando la generalización de las habilidades. Una identificación y un apoyo tempranos pueden marcar una gran diferencia no solo en el rendimiento académico, sino en la autoestima, la prevención del bullying y el desarrollo de relaciones significativas. Nos comprometemos a capacitar a cada persona para que su voz, en la forma que sea, sea escuchada y valorada.',
    NOW(),
    false
  );

-- 10. Inserciones en la tabla 'events'
INSERT INTO
  events (
    id,
    title,
    message,
    creation_date,
    event_date,
    event_time,
    archived,
    user_id,
    capacity,
    anonymous_loves
  )
VALUES
  (
    1,
    'Taller de Juegos de Mesa',
    'Un aspecto fundamental del taller es la gestión del tiempo y la paciencia al esperar el turno de juego, promoviendo la escucha activa y la consideración por los demás participantes.',
    '2024-01-10T10:00:00',
    '2024-03-10',
    '18:00:00',
    false,
    1, -- Valor añadido
    30,
    0
  ),
  (
    2,
    'Escuela de Padres',
    'Programa de formación y apoyo dirigido a padres y madres. Se ofrecen herramientas, estrategias y recursos para afrontar los retos de la crianza, mejorar la comunicación familiar y promover un desarrollo saludable de los hijos.',
    '2024-01-15T18:00:00',
    '2024-05-10',
    '18:00:00',
    false,
    1, -- Valor añadido
    40,
    0
  );

-- 11. Inserta el Aviso Legal adaptado para el Centro de Apoyo Educativo Especializado Ágora
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
    '<h2>Política de Cookies</h2>

<p>Este sitio web utiliza tecnologías de almacenamiento para garantizar su correcto funcionamiento. A continuación te explicamos qué cookies y servicios de terceros se utilizan y con qué finalidad.</p>

<h3>1. ¿Qué son las cookies?</h3>
<p>Las cookies son pequeños archivos de texto que los sitios web almacenan en tu dispositivo. Otros mecanismos similares como <strong>sessionStorage</strong> cumplen la misma función pero se eliminan al cerrar el navegador.</p>

<h3>2. Cookies propias</h3>
<p>Ágora Centro Educativo <strong>no almacena cookies propias persistentes</strong>. La sesión de usuario (identificador, token de acceso, rol) se gestiona exclusivamente mediante <strong>sessionStorage</strong>, que se elimina automáticamente al cerrar el navegador y no es accesible por terceros.</p>

<h3>3. Cookies y servicios de terceros</h3>
<p>Para ofrecer ciertas funcionalidades, este sitio carga servicios externos que pueden establecer sus propias cookies:</p>

<h4>3.1 Google OAuth 2.0 (Inicio de sesión con Google)</h4>
<p>Utilizamos el SDK de Google Identity Services (<code>accounts.google.com</code>) para permitir el inicio de sesión con tu cuenta de Google. Este servicio puede establecer cookies de sesión de autenticación en tu navegador (<code>G_AUTHUSER_H</code>, <code>OSID</code>, <code>__Secure-1PSID</code>). Consulta la <a href="https://policies.google.com/privacy" target="_blank" rel="noopener noreferrer">Política de Privacidad de Google</a>.</p>

<h4>3.2 Google Maps (visor de ubicaciones)</h4>
<p>En determinadas páginas del sitio se muestra un mapa integrado de Google Maps para indicar nuestra ubicación física. Este mapa se carga como contenido embebido desde <code>google.com</code> y puede establecer cookies de preferencias y consentimiento (<code>CONSENT</code>, <code>SOCS</code>, <code>NID</code>). Consulta la <a href="https://policies.google.com/privacy" target="_blank" rel="noopener noreferrer">Política de Privacidad de Google</a>.</p>

<h4>3.3 Font Awesome (Cloudflare CDN)</h4>
<p>Los iconos del sitio se sirven desde la CDN de Cloudflare (<code>cdnjs.cloudflare.com</code>). Cloudflare puede establecer la cookie técnica <code>__cf_bm</code> con fines de seguridad y gestión de tráfico (bot management). No se utiliza para rastreo publicitario. Consulta la <a href="https://www.cloudflare.com/es-es/privacypolicy/" target="_blank" rel="noopener noreferrer">Política de Privacidad de Cloudflare</a>.</p>

<h3>4. Verificación antispam</h3>
<p>La verificación antispam de los formularios del sitio se realiza mediante un sistema propio de desafíos matemáticos y lógicos, sin intervención de servicios externos ni instalación de cookies.</p>

<h3>5. Cookies analíticas o publicitarias</h3>
<p>Este sitio web <strong>no utiliza cookies analíticas ni publicitarias</strong>. No hay integración con Google Analytics, Facebook Pixel ni ningún otro sistema de seguimiento de usuarios.</p>

<h3>6. ¿Cómo controlar las cookies?</h3>
<p>Puedes configurar tu navegador para bloquear o eliminar cookies en cualquier momento. Ten en cuenta que bloquear las cookies de servicios como Google OAuth puede impedir el inicio de sesión con Google.</p>
<ul>
  <li><a href="https://support.google.com/chrome/answer/95647" target="_blank" rel="noopener noreferrer">Google Chrome</a></li>
  <li><a href="https://support.mozilla.org/es/kb/habilitar-y-deshabilitar-cookies-sitios-web-rastrear-preferencias" target="_blank" rel="noopener noreferrer">Mozilla Firefox</a></li>
  <li><a href="https://support.apple.com/es-es/guide/safari/sfri11471/mac" target="_blank" rel="noopener noreferrer">Safari</a></li>
  <li><a href="https://support.microsoft.com/es-es/microsoft-edge/eliminar-las-cookies-en-microsoft-edge-63947406-40ac-c3b8-57b9-2a946a29ae09" target="_blank" rel="noopener noreferrer">Microsoft Edge</a></li>
</ul>

<h3>7. Contacto</h3>
<p>Para consultas sobre nuestra política de cookies: <a href="mailto:centroeducativoagora@gmail.com">centroeducativoagora@gmail.com</a></p>',
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

-- 12. Inserciones en la tabla 'tags'
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

INSERT INTO
  event_images (image_name, image_path, event_id)
VALUES
  (
    'adolescentesGrupal.jpg',
    '/temp_images/adolescentesGrupal.jpg',
    1
  ),
  (
    'alumnosOrdenador.jpg',
    '/temp_images/alumnosOrdenador.jpg',
    1
  ),
  ('cubos.jpg', '/temp_images/cubos.jpg', 2),
  (
    'diccionario.jpg',
    '/temp_images/diccionario.jpg',
    2
  );

-- Desarrollo
-- Inserciones para post_images con imágenes de prueba
-- Estas son imágenes JPEG mínimas válidas de 8x8 píxeles para desarrollo/testing
INSERT INTO
  post_images (image_name, image_path, is_main_image, post_id)
VALUES
  -- Imágenes para Post 1: "Selectividad Alimentaria" (PLACEHOLDERS VÁLIDOS)
  (
    'adolescentesGrupal.jpg',
    '/temp_images/adolescentesGrupal.jpg',
    true,
    1
  ),
  (
    'alumnosOrdenador.jpg',
    '/temp_images/alumnosOrdenador.jpg',
    false,
    1
  ),
  -- Imágenes para Post 2: "Presunción de Competencia" (PLACEHOLDERS VÁLIDOS)
  (
    'niñoCascos.jpg',
    '/temp_images/niñoCascos.jpg',
    true,
    2
  ),
  (
    'niñoFichas.jpg',
    '/temp_images/niñoFichas.jpg',
    false,
    2
  ),
  -- Imágenes para Post 3: "Comunicación Aumentativa y Alternativa" (PLACEHOLDERS VÁLIDOS)
  (
    'escritorio.jpg',
    '/temp_images/escritorio.jpg',
    true,
    3
  ),
  (
    'niñoFichas.jpg',
    '/temp_images/niñoFichas.jpg',
    false,
    3
  ),
  -- Imágenes para Post 4: "Experiencias Personales con Selectividad Alimentaria" (PLACEHOLDERS VÁLIDOS)
  (
    'niñoCascos.jpg',
    '/temp_images/niñoCascos.jpg',
    true,
    4
  ),
  (
    'niñoFichas.jpg',
    '/temp_images/niñoFichas.jpg',
    false,
    4
  ),
  -- Imágenes para Post 5: "Desenmascarando Mitos sobre el Autismo" (PLACEHOLDERS VÁLIDOS)
  (
    'niñoPuzzle.jpg',
    '/temp_images/niñoPuzzle.jpg',
    true,
    5
  ),
  (
    'niñoFichas.jpg',
    '/temp_images/niñoFichas.jpg',
    false,
    5
  ),
  -- Imágenes para Post 6: "Apoyo Centrado en la Persona" (PLACEHOLDERS VÁLIDOS)
  (
    'niñoPuzzle.jpg',
    '/temp_images/niñoPuzzle.jpg',
    true,
    6
  ),
  (
    'escritorio.jpg',
    '/temp_images/escritorio.jpg',
    false,
    6
  );

-- Inserciones para la tabla 'text_images'
INSERT INTO
  text_images (image_name, image_path, created_at, text_id)
VALUES
  (
    'fachada.jpg',
    '/temp_images/fachada.jpg',
    NOW(),
    1
  ),
  (
    'escritorio.jpg',
    '/temp_images/escritorio.jpg',
    NOW(),
    2
  ),
  (
    'escritorio.jpg',
    '/temp_images/escritorio.jpg',
    NOW(),
    3
  ),
  (
    'niñoPuzzle.jpg',
    '/temp_images/niñoPuzzle.jpg',
    NOW(),
    4
  ),
  (
    'ninaFicha.jpg',
    '/temp_images/ninaFicha.jpg',
    NOW(),
    5
  ),
  (
    'alumnosOrdenador.jpg',
    '/temp_images/alumnosOrdenador.jpg',
    NOW(),
    6
  ),
  (
    'adolescentesGrupal.jpg',
    '/temp_images/adolescentesGrupal.jpg',
    NOW(),
    7
  ),
  (
    'niñoFichas.jpg',
    '/temp_images/niñoFichas.jpg',
    NOW(),
    8
  ),
  (
    'ninaFicha.jpg',
    '/temp_images/ninaFicha.jpg',
    NOW(),
    9
  ),
  (
    'niñoPuzzle.jpg',
    '/temp_images/niñoPuzzle.jpg',
    NOW(),
    10
  ),
  ('ivan.jpg', '/temp_images/ivan.jpg', NOW(), 11),
  (
    'alumnosOrdenador.jpg',
    '/temp_images/alumnosOrdenador.jpg',
    NOW(),
    12
  ),
  (
    'libros.jpg',
    '/temp_images/libros.jpg',
    NOW(),
    13
  ),
  (
    'niñoCascos.jpg',
    '/temp_images/niñoCascos.jpg',
    NOW(),
    14
  ),
  (
    'pintando.jpg',
    '/temp_images/pintando.jpg',
    NOW(),
    15
  ),
  ('abaco.jpg', '/temp_images/abaco.jpg', NOW(), 16),
  (
    'niñoCuento.jpg',
    '/temp_images/niñoCuento.jpg',
    NOW(),
    17
  ),
  (
    'leyendo.jpg',
    '/temp_images/leyendo.jpg',
    NOW(),
    18
  ),
  ('cubos.jpg', '/temp_images/cubos.jpg', NOW(), 19),
  (
    'alumnosOrdenador.jpg',
    '/temp_images/alumnosOrdenador.jpg',
    NOW(),
    20
  ),
  (
    'adolescentesGrupal.jpg',
    '/temp_images/adolescentesGrupal.jpg',
    NOW(),
    21
  ),
  (
    'escritorio.jpg',
    '/temp_images/escritorio.jpg',
    NOW(),
    22
  ),
  (
    'alumnosOrdenador.jpg',
    '/temp_images/alumnosOrdenador.jpg',
    NOW(),
    23
  );