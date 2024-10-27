-- пользователи
create TABLE IF NOT EXISTS users (
	id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	CONSTRAINT pk_users PRIMARY KEY (id),
	CONSTRAINT users_email_unique UNIQUE (email)
);
create TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY, -- Уникальный идентификатор записи
    name VARCHAR(50) NOT NULL,                  -- Название категории
    CONSTRAINT pk_stats PRIMARY KEY (id),
    CONSTRAINT categories_unique UNIQUE (name)
);

CREATE TABLE IF NOT exists locations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    lat DECIMAL(9,6) NOT NULL,
    lon DECIMAL(9,6) NOT NULL,
    name varchar(255),
    address varchar(1000),
    CONSTRAINT pk_locations PRIMARY KEY (id),
    CONSTRAINT unique_locations_lat_lon UNIQUE (lat, lon)
);

CREATE TABLE IF NOT exists events (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY,
	annotation varchar(2000) NOT NULL,
	category_id BIGINT NOT NULL,
	initiator_id BIGINT NOT NULL,
	location_id BIGINT NOT NULL,
	paid BOOL NOT NULL,
	request_moderation BOOL NOT NULL,
	title varchar(120) NOT NULL,
	state varchar(9) NOT NULL,
	description varchar(7000) NOT NULL,
	event_date timestamp NOT NULL,
	participant_limit INTEGER NOT NULL,
	created_on timestamp NOT NULL,
	published_on timestamp,
	CONSTRAINT pk_events PRIMARY KEY (id),
	CONSTRAINT events_category_id_fk FOREIGN KEY (category_id) REFERENCES categories(id),
	CONSTRAINT events_users_fk FOREIGN KEY (initiator_id) REFERENCES users(id),
	CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE
);

create TABLE IF NOT EXISTS participation_requests (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY,
	requester_id BIGINT NOT NULL,
	event_id BIGINT NOT NULL,
	status VARCHAR(9) NOT NULL,
	created TIMESTAMP NOT NULL,
	CONSTRAINT pk_participation_requests PRIMARY KEY (id),
	CONSTRAINT fk_participation_requests_requester_id_users_id FOREIGN KEY (requester_id) REFERENCES users(id),
	CONSTRAINT fk_participation_requests_event_id_events_id FOREIGN KEY (event_id) REFERENCES events(id),
	CONSTRAINT unique_participation_requests_requester_id_event_id UNIQUE (requester_id, event_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title varchar(50) NOT NULL,
    pinned boolean NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id bigint NOT NULL,
    event_id bigint NOT NULL,
    CONSTRAINT pk_compilation_events PRIMARY KEY (compilation_id, event_id),
	CONSTRAINT compilations_fk FOREIGN KEY (compilation_id) REFERENCES compilations(id) ON DELETE CASCADE,
	CONSTRAINT events_fk FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float
AS
'
declare
    dist float = 0;
    rad_lat1 float;
    rad_lat2 float;
    theta float;
    rad_theta float;
BEGIN
    IF lat1 = lat2 AND lon1 = lon2
    THEN
        RETURN dist;
    ELSE
        -- переводим градусы широты в радианы
        rad_lat1 = pi() * lat1 / 180;
        -- переводим градусы долготы в радианы
        rad_lat2 = pi() * lat2 / 180;
        -- находим разность долгот
        theta = lon1 - lon2;
        -- переводим градусы в радианы
        rad_theta = pi() * theta / 180;
        -- находим длину ортодромии
        dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

        IF dist > 1
            THEN dist = 1;
        END IF;

        dist = acos(dist);
        -- переводим радианы в градусы
        dist = dist * 180 / pi();
        -- переводим градусы в километры
        dist = dist * 60 * 1.8524;

        RETURN dist;
    END IF;
END;
'
LANGUAGE PLPGSQL;