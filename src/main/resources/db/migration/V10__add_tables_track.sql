CREATE TABLE track (
  id               BIGSERIAL PRIMARY KEY,
  album_id          BIGINT NOT NULL,
  track_number      INT NOT NULL,
  title             VARCHAR(250) NOT NULL,
  duration_seconds  INT,
  created_at        TIMESTAMPTZ NOT NULL DEFAULT now(),

  CONSTRAINT fk_track_album FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE,

  CONSTRAINT ck_track_number CHECK (track_number > 0),
  CONSTRAINT ck_track_duration CHECK (duration_seconds IS NULL OR duration_seconds >= 0),

  CONSTRAINT ux_track_album_number UNIQUE (album_id, track_number)
);

CREATE INDEX ix_track_album ON track (album_id);
