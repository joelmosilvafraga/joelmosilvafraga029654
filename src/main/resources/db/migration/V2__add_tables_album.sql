--Tabela referente a tipo de album
CREATE TABLE album_type (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(30) UNIQUE NOT NULL,
  description VARCHAR(150) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

--Tabela para armazenameto de album
CREATE TABLE album (
  id            BIGSERIAL PRIMARY KEY,
  title         VARCHAR(250) NOT NULL,
  release_year  INT,
  album_type_id BIGINT NOT NULL,
  genre         VARCHAR(100),
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT ck_album_release_year
    CHECK (release_year IS NULL OR (release_year BETWEEN 1900 AND 3000)),

  CONSTRAINT fk_album_album_type
    FOREIGN KEY (album_type_id) REFERENCES album_type(id),

  CONSTRAINT uk_album_title_year UNIQUE (title, release_year, album_type_id)
);
CREATE INDEX ix_album_title ON album (title);
CREATE INDEX ix_album_release_year ON album (release_year);
CREATE INDEX ix_album_album_type_id ON album (album_type_id);