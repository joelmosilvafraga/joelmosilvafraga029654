--Tabela referente ao tipo de partipação de um artista em um album
CREATE TABLE album_artist_role (
    id           BIGSERIAL PRIMARY KEY,
    code         VARCHAR(50)  NOT NULL UNIQUE,
    description  VARCHAR(255) NOT NULL,
    active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
);


--Tabela para armazenamento de artistas
CREATE TABLE artist (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(200) NOT NULL,
  country     VARCHAR(100),
  genre       VARCHAR(100),
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX ix_artist_name ON artist (name);


--Tabela de ligação entre um artista e uma album
CREATE TABLE album_artist (
  album_id   BIGINT NOT NULL,
  artist_id  BIGINT NOT NULL,
  role_id    BIGINT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT pk_album_artist
    PRIMARY KEY (album_id, artist_id),

  CONSTRAINT fk_album_artist_album
    FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE,

  CONSTRAINT fk_album_artist_artist
    FOREIGN KEY (artist_id) REFERENCES artist(id) ON DELETE RESTRICT,

  CONSTRAINT fk_album_artist_role
    FOREIGN KEY (role_id) REFERENCES album_artist_role(id) ON DELETE RESTRICT
);

CREATE INDEX ix_album_artist_artist ON album_artist (artist_id);
CREATE INDEX ix_album_artist_role   ON album_artist (role_id);