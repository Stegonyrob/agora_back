-- Crear tabla para post loves
CREATE TABLE IF NOT EXISTS
    posts_loves (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        post_id BIGINT NOT NULL,
        user_id BIGINT NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        UNIQUE KEY unique_post_user_love (post_id, user_id),
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE,
        FOREIGN KEY (user_id) REFERENCES users (id_user) ON DELETE CASCADE
    );

-- Crear tabla para user favorite events
CREATE TABLE IF NOT EXISTS
    user_favorite_events (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id BIGINT NOT NULL,
        event_id BIGINT NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        UNIQUE KEY unique_user_event_favorite (user_id, event_id),
        FOREIGN KEY (user_id) REFERENCES users (id_user) ON DELETE CASCADE,
        FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
    );

-- Insertar algunos datos de ejemplo
INSERT IGNORE INTO
    posts_loves (post_id, user_id)
VALUES
    (1, 2);

INSERT IGNORE INTO
    posts_loves (post_id, user_id)
VALUES
    (1, 3);

INSERT IGNORE INTO
    posts_loves (post_id, user_id)
VALUES
    (2, 2);

INSERT IGNORE INTO
    user_favorite_events (user_id, event_id)
VALUES
    (2, 1);

INSERT IGNORE INTO
    user_favorite_events (user_id, event_id)
VALUES
    (3, 1);

INSERT IGNORE INTO
    user_favorite_events (user_id, event_id)
VALUES
    (2, 2);