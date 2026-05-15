-- =========================================================
-- FEItv - Dados de demonstração para prints e apresentação
-- Execute este script APÓS executar script_banco_feitv.sql
-- Banco esperado: feitv
-- =========================================================

-- =========================================================
-- Usuários de demonstração
-- =========================================================

INSERT INTO tb_usuario (nome, email, senha)
VALUES
('Guilherme Gomes', 'guilherme@email.com', '123456'),
('Ana Silva', 'ana@email.com', '123456'),
('Carlos Souza', 'carlos@email.com', '123456')
ON CONFLICT (email) DO NOTHING;

-- =========================================================
-- Playlists de demonstração
-- Usuário principal dos prints: guilherme@email.com
-- =========================================================

INSERT INTO tb_lista (id_usuario, nome, descricao)
SELECT id_usuario, 'Favoritos de Ficção Científica', 'Filmes e séries com viagens no tempo, espaço e tecnologia.'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

INSERT INTO tb_lista (id_usuario, nome, descricao)
SELECT id_usuario, 'Filmes para Rever', 'Produções marcantes para assistir novamente.'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

INSERT INTO tb_lista (id_usuario, nome, descricao)
SELECT id_usuario, 'Animações e Família', 'Conteúdos leves para assistir em família.'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

INSERT INTO tb_lista (id_usuario, nome, descricao)
SELECT id_usuario, 'Séries em Alta', 'Séries populares adicionadas para acompanhar.'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

-- Playlists para outros usuários, para demonstrar separação por usuário
INSERT INTO tb_lista (id_usuario, nome, descricao)
SELECT id_usuario, 'Lista da Ana', 'Playlist pessoal da Ana.'
FROM tb_usuario
WHERE email = 'ana@email.com';

INSERT INTO tb_lista (id_usuario, nome, descricao)
SELECT id_usuario, 'Lista do Carlos', 'Playlist pessoal do Carlos.'
FROM tb_usuario
WHERE email = 'carlos@email.com';

-- =========================================================
-- Vínculos entre playlists e vídeos
-- Usando subqueries para evitar dependência direta dos IDs
-- =========================================================

-- Favoritos de Ficção Científica
INSERT INTO tb_lista_video (id_lista, id_video)
SELECT l.id_lista, v.id_video
FROM tb_lista l, tb_video v, tb_usuario u
WHERE l.id_usuario = u.id_usuario
AND u.email = 'guilherme@email.com'
AND l.nome = 'Favoritos de Ficção Científica'
AND v.titulo IN (
    'Interestelar',
    'A Origem',
    'Matrix',
    'Avatar',
    'Dark',
    'Stranger Things'
)
ON CONFLICT DO NOTHING;

-- Filmes para Rever
INSERT INTO tb_lista_video (id_lista, id_video)
SELECT l.id_lista, v.id_video
FROM tb_lista l, tb_video v, tb_usuario u
WHERE l.id_usuario = u.id_usuario
AND u.email = 'guilherme@email.com'
AND l.nome = 'Filmes para Rever'
AND v.titulo IN (
    'O Poderoso Chefão',
    'Clube da Luta',
    'Forrest Gump',
    'Gladiador',
    'Batman: O Cavaleiro das Trevas'
)
ON CONFLICT DO NOTHING;

-- Animações e Família
INSERT INTO tb_lista_video (id_lista, id_video)
SELECT l.id_lista, v.id_video
FROM tb_lista l, tb_video v, tb_usuario u
WHERE l.id_usuario = u.id_usuario
AND u.email = 'guilherme@email.com'
AND l.nome = 'Animações e Família'
AND v.titulo IN (
    'Toy Story',
    'Divertida Mente'
)
ON CONFLICT DO NOTHING;

-- Séries em Alta
INSERT INTO tb_lista_video (id_lista, id_video)
SELECT l.id_lista, v.id_video
FROM tb_lista l, tb_video v, tb_usuario u
WHERE l.id_usuario = u.id_usuario
AND u.email = 'guilherme@email.com'
AND l.nome = 'Séries em Alta'
AND v.titulo IN (
    'Breaking Bad',
    'Stranger Things',
    'The Last of Us',
    'The Mandalorian'
)
ON CONFLICT DO NOTHING;

-- =========================================================
-- Curtidas de demonstração
-- =========================================================

INSERT INTO tb_curtida (id_usuario, id_video)
SELECT u.id_usuario, v.id_video
FROM tb_usuario u, tb_video v
WHERE u.email = 'guilherme@email.com'
AND v.titulo IN (
    'Interestelar',
    'Matrix',
    'A Origem',
    'Batman: O Cavaleiro das Trevas',
    'Toy Story'
)
ON CONFLICT DO NOTHING;

INSERT INTO tb_curtida (id_usuario, id_video)
SELECT u.id_usuario, v.id_video
FROM tb_usuario u, tb_video v
WHERE u.email = 'ana@email.com'
AND v.titulo IN (
    'Interestelar',
    'Titanic',
    'Divertida Mente'
)
ON CONFLICT DO NOTHING;

INSERT INTO tb_curtida (id_usuario, id_video)
SELECT u.id_usuario, v.id_video
FROM tb_usuario u, tb_video v
WHERE u.email = 'carlos@email.com'
AND v.titulo IN (
    'Matrix',
    'Gladiador',
    'Vingadores: Ultimato'
)
ON CONFLICT DO NOTHING;

-- =========================================================
-- Histórico de buscas de demonstração
-- =========================================================

INSERT INTO tb_historico_busca (id_usuario, termo_busca, data_busca)
SELECT id_usuario, 'matrix', CURRENT_TIMESTAMP - INTERVAL '5 days'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

INSERT INTO tb_historico_busca (id_usuario, termo_busca, data_busca)
SELECT id_usuario, 'interestelar', CURRENT_TIMESTAMP - INTERVAL '4 days'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

INSERT INTO tb_historico_busca (id_usuario, termo_busca, data_busca)
SELECT id_usuario, 'senhor dos anéis', CURRENT_TIMESTAMP - INTERVAL '3 days'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

INSERT INTO tb_historico_busca (id_usuario, termo_busca, data_busca)
SELECT id_usuario, 'toy story', CURRENT_TIMESTAMP - INTERVAL '2 days'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

INSERT INTO tb_historico_busca (id_usuario, termo_busca, data_busca)
SELECT id_usuario, 'breaking bad', CURRENT_TIMESTAMP - INTERVAL '1 day'
FROM tb_usuario
WHERE email = 'guilherme@email.com';

INSERT INTO tb_historico_busca (id_usuario, termo_busca, data_busca)
SELECT id_usuario, 'avatar', CURRENT_TIMESTAMP
FROM tb_usuario
WHERE email = 'guilherme@email.com';

-- =========================================================
-- Consultas de conferência
-- =========================================================

SELECT * FROM tb_usuario ORDER BY id_usuario;

SELECT * FROM tb_video ORDER BY id_video;

SELECT 
    l.id_lista,
    u.nome AS usuario,
    l.nome AS playlist,
    l.descricao
FROM tb_lista l
INNER JOIN tb_usuario u
ON l.id_usuario = u.id_usuario
ORDER BY l.id_lista;

SELECT 
    l.nome AS playlist,
    v.titulo AS video,
    v.genero,
    v.ano_lancamento
FROM tb_lista_video lv
INNER JOIN tb_lista l
ON lv.id_lista = l.id_lista
INNER JOIN tb_video v
ON lv.id_video = v.id_video
ORDER BY l.nome, v.titulo;

SELECT 
    v.titulo,
    COUNT(c.id_usuario) AS total_curtidas
FROM tb_video v
LEFT JOIN tb_curtida c
ON v.id_video = c.id_video
GROUP BY v.titulo
ORDER BY total_curtidas DESC, v.titulo;

SELECT 
    u.nome,
    h.termo_busca,
    h.data_busca
FROM tb_historico_busca h
INNER JOIN tb_usuario u
ON h.id_usuario = u.id_usuario
ORDER BY h.data_busca DESC;