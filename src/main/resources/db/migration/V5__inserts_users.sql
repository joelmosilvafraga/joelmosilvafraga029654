--Inserindo tabela com usuarios e senha padrão 123456
INSERT INTO app_user (username, password_hash, enabled)
VALUES
  ('administrador',   '$2a$10$5igV0e7hIs0nKScuO50lJezI1uHPhcPLFuji4qN4FSij3FsZPuJfW', true),
  ('usuario_gestor', '$2a$10$5igV0e7hIs0nKScuO50lJezI1uHPhcPLFuji4qN4FSij3FsZPuJfW', true),
  ('usuario_editor',  '$2a$10$5igV0e7hIs0nKScuO50lJezI1uHPhcPLFuji4qN4FSij3FsZPuJfW', true),
  ('usuario_1',    '$2a$10$5igV0e7hIs0nKScuO50lJezI1uHPhcPLFuji4qN4FSij3FsZPuJfW', true),
  ('usuario_2',  '$2a$10$5igV0e7hIs0nKScuO50lJezI1uHPhcPLFuji4qN4FSij3FsZPuJfW', true)
ON CONFLICT (username) DO NOTHING;

--Inserindo os perfis de usuários disponíveis
INSERT INTO role (code, description)
VALUES
  ('ADMIN',  'Acesso total ao sistema'),
  ('MANAGER','Gerencia artistas e álbuns'),
  ('EDITOR', 'Pode criar e editar conteúdos'),
  ('USER',   'Acesso básico de leitura'),
  ('VIEWER', 'Somente consulta')
ON CONFLICT (code) DO NOTHING;

--Inserindos os perfis relacionados a cada usuário criado
INSERT INTO user_role (user_id, role_id)
SELECT
  u.id  AS user_id,
  r.id  AS role_id
FROM app_user u
JOIN role r
  ON r.code =
    CASE u.username
      WHEN 'administrador'           THEN 'ADMIN'
      WHEN 'usuario_gestor'          THEN 'MANAGER'
      WHEN 'usuario_editor'          THEN 'EDITOR'
      WHEN 'usuario_1'               THEN 'USER'
      WHEN 'usuario_2'               THEN 'USER'
    END
ON CONFLICT (user_id, role_id) DO NOTHING;
