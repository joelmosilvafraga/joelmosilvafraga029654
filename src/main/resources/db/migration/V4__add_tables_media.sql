--Tabela de armazenamento de metadados
CREATE TABLE media_object (
  id                BIGSERIAL PRIMARY KEY,
  bucket            VARCHAR(100) NOT NULL,
  object_key        VARCHAR(400) NOT NULL UNIQUE,
  original_filename VARCHAR(255),
  content_type      VARCHAR(100) NOT NULL,
  size_bytes        BIGINT NOT NULL,
  etag              VARCHAR(128),
  created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT ck_media_size CHECK (size_bytes >= 0)
);
CREATE INDEX ix_media_bucket_key ON media_object (bucket, object_key);

--Tabela de armazenamento de relacionamento de imagens
CREATE TABLE album_media (
  album_id    BIGINT NOT NULL,
  media_id    BIGINT NOT NULL,
  type        VARCHAR(30) NOT NULL,
  is_primary  BOOLEAN NOT NULL DEFAULT false,
  sort_order  INT NOT NULL DEFAULT 0,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT pk_album_media PRIMARY KEY (album_id, media_id),
  CONSTRAINT fk_album_media_album FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE,
  CONSTRAINT fk_album_media_media FOREIGN KEY (media_id) REFERENCES media_object(id) ON DELETE RESTRICT,

  CONSTRAINT ck_album_media_type CHECK (type IN ('COVER','THUMBNAIL','BACK_COVER','BOOKLET','OTHER')),
  CONSTRAINT ck_album_media_sort CHECK (sort_order >= 0)
);
CREATE UNIQUE INDEX ux_album_media_primary_per_type
  ON album_media(album_id, type)
  WHERE is_primary = true;
CREATE INDEX ix_album_media_album_type
  ON album_media(album_id, type);
