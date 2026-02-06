CREATE TABLE IF NOT EXISTS media_object (
  id           BIGSERIAL PRIMARY KEY,
  bucket       VARCHAR(200) NOT NULL,
  object_key   VARCHAR(500) NOT NULL,
  content_type VARCHAR(120),
  size_bytes   BIGINT,
  etag         VARCHAR(64),
  created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_media_bucket_key
  ON media_object(bucket, object_key);


CREATE TABLE IF NOT EXISTS album_media (
  album_id    BIGINT NOT NULL,
  media_id    BIGINT NOT NULL,
  media_type  VARCHAR(40) NOT NULL,
  is_primary  BOOLEAN NOT NULL DEFAULT false,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT pk_album_media PRIMARY KEY (album_id, media_id),
  CONSTRAINT fk_album_media_album FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE,
  CONSTRAINT fk_album_media_media FOREIGN KEY (media_id) REFERENCES media_object(id) ON DELETE RESTRICT
);

CREATE UNIQUE INDEX IF NOT EXISTS ux_album_media_primary_per_type
  ON album_media(album_id, media_type)
  WHERE is_primary = true;

CREATE INDEX IF NOT EXISTS ix_album_media_album_type
  ON album_media(album_id, media_type);
