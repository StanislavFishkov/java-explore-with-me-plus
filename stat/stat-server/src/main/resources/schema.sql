-- Схема для сервиса статистики

-- Создаем таблицу stats
CREATE TABLE stats (
    id SERIAL PRIMARY KEY,                    -- Уникальный идентификатор записи
    app VARCHAR(255) NOT NULL,                -- Название приложения (сервиса), откуда был запрос
    uri VARCHAR(2048) NOT NULL,               -- URI запроса
    ip VARCHAR(45) NOT NULL,                  -- IP-адрес пользователя, который совершил запрос
    timestamp TIMESTAMP NOT NULL              -- Время, когда был совершен запрос
);

-- Создание индекса для быстрого поиска по URI
CREATE INDEX idx_stats_uri ON stats(uri);

-- Создание индекса для быстрого поиска по времени запросов
CREATE INDEX idx_stats_timestamp ON stats(timestamp);
