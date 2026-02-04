--Inserindo os tipos de papeis que cada artista pode desempenhar em cada album
INSERT INTO album_artist_role (code, description, active, created_at)
VALUES
  ('PRIMARY',        'Artista ou banda principal do álbum',                      true, now()),
  ('FEATURED',       'Participação especial (feat.)',                           true, now()),
  ('GUEST',          'Artista convidado',                                        true, now()),
  ('COLLABORATOR',   'Colaborador geral (participação sem “feat.” formal)',      true, now()),
  ('COMPOSER',       'Compositor das músicas',                                  true, now()),
  ('LYRICIST',       'Autor das letras',                                        true, now()),
  ('ARRANGER',       'Responsável pelos arranjos',                              true, now()),
  ('PRODUCER',       'Produtor musical',                                        true, now()),
  ('EXEC_PRODUCER',  'Produtor executivo',                                      true, now()),
  ('ENGINEER',       'Engenheiro de som',                                       true, now()),
  ('MIXING',         'Responsável pela mixagem',                                true, now()),
  ('MASTERING',      'Responsável pela masterização',                           true, now()),
  ('CONDUCTOR',      'Regente',                                                 true, now()),
  ('ORCHESTRA',      'Orquestra ou grupo instrumental',                          true, now()),
  ('REMIXER',        'Responsável por remix',                                   true, now()),
  ('DJ',             'DJ ou performance eletrônica',                             true, now()),
  ('SOUNDTRACK',     'Participação vinculada a trilha sonora',                   true, now())
ON CONFLICT (code) DO UPDATE
SET
  description = EXCLUDED.description,
  active      = EXCLUDED.active;

--Inserindo os artistas
INSERT INTO artist (name, country, genre, created_at, updated_at)
VALUES

('Legião Urbana', 'Brasil', 'Rock', now(), now()),
('Titãs', 'Brasil', 'Rock', now(), now()),
('Skank', 'Brasil', 'Pop Rock', now(), now()),
('Capital Inicial', 'Brasil', 'Rock', now(), now()),
('Os Paralamas do Sucesso', 'Brasil', 'Rock/Reggae', now(), now()),
('Jota Quest', 'Brasil', 'Pop Rock', now(), now()),
('Charlie Brown Jr.', 'Brasil', 'Rock/Hardcore', now(), now()),
('Barão Vermelho', 'Brasil', 'Rock', now(), now()),
('Cássia Eller', 'Brasil', 'MPB/Rock', now(), now()),
('Marisa Monte', 'Brasil', 'MPB', now(), now()),
('Gilberto Gil', 'Brasil', 'MPB', now(), now()),
('Caetano Veloso', 'Brasil', 'MPB', now(), now()),
('Lulu Santos', 'Brasil', 'Pop Rock', now(), now()),
('Fresno', 'Brasil', 'Emo Rock', now(), now()),
('NX Zero', 'Brasil', 'Pop Rock', now(), now()),
('Metallica', 'USA', 'Heavy Metal', now(), now()),
('Nirvana', 'USA', 'Grunge', now(), now()),
('Pearl Jam', 'USA', 'Rock', now(), now()),
('Stereophonics', 'UK', 'Rock', now(), now()),
('Serj Tankian', 'USA', 'Rock', now(), now()),
('Guns N’ Roses', 'USA', 'Rock', now(), now()),
('Mike Shinoda' , 'USA', 'Rock', now(), now()),
('Red Hot Chili Peppers', 'USA', 'Funk Rock', now(), now()),
('Linkin Park', 'USA', 'Alternative Rock', now(), now()),
('Coldplay', 'UK', 'Alternative Rock', now(), now()),
('U2', 'Ireland', 'Rock', now(), now()),
('Queen', 'UK', 'Rock', now(), now()),
('The Beatles', 'UK', 'Rock', now(), now()),
('Pink Floyd', 'UK', 'Progressive Rock', now(), now()),
('AC/DC', 'Australia', 'Hard Rock', now(), now()),
('Imagine Dragons', 'USA', 'Pop Rock', now(), now()),
('Arctic Monkeys', 'UK', 'Indie Rock', now(), now()),
('Bon Jovi', 'USA', 'Hard Rock', now(), now()),
('Evanescence', 'USA', 'Alternative Metal', now(), now()),
('Djavan', 'Brasil', 'MPB', now(), now()),
('Michael Teló', 'Brasil', 'Sertanejo', now(), now())
