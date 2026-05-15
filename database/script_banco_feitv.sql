-- =========================================================
-- FEItv - Script de criação do banco de dados
-- Banco esperado: feitv
-- PostgreSQL
-- =========================================================

-- Este script recria as tabelas principais do sistema.
-- Ele apaga tabelas existentes para evitar conflito durante testes.

DROP TABLE IF EXISTS tb_historico_busca CASCADE;
DROP TABLE IF EXISTS tb_lista_video CASCADE;
DROP TABLE IF EXISTS tb_curtida CASCADE;
DROP TABLE IF EXISTS tb_lista CASCADE;
DROP TABLE IF EXISTS tb_video CASCADE;
DROP TABLE IF EXISTS tb_usuario CASCADE;

-- =========================================================
-- Tabela de usuários
-- =========================================================

CREATE TABLE tb_usuario (
    id_usuario SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL
);

-- =========================================================
-- Tabela de vídeos
-- tipo_video permite diferenciar Filme e Série no Java
-- =========================================================

CREATE TABLE tb_video (
    id_video SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    genero VARCHAR(80),
    ano_lancamento INT,
    tipo_video VARCHAR(20) NOT NULL CHECK (tipo_video IN ('FILME', 'SERIE'))
);

-- =========================================================
-- Tabela de curtidas
-- Um usuário só pode curtir o mesmo vídeo uma vez
-- =========================================================

CREATE TABLE tb_curtida (
    id_usuario INT NOT NULL,
    id_video INT NOT NULL,
    PRIMARY KEY (id_usuario, id_video),
    FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_video) REFERENCES tb_video(id_video) ON DELETE CASCADE
);

-- =========================================================
-- Tabela de playlists/listas de reprodução
-- Cada playlist pertence a um usuário
-- =========================================================

CREATE TABLE tb_lista (
    id_lista SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario) ON DELETE CASCADE
);

-- =========================================================
-- Tabela associativa playlist x vídeo
-- Representa relacionamento N:N
-- =========================================================

CREATE TABLE tb_lista_video (
    id_lista INT NOT NULL,
    id_video INT NOT NULL,
    PRIMARY KEY (id_lista, id_video),
    FOREIGN KEY (id_lista) REFERENCES tb_lista(id_lista) ON DELETE CASCADE,
    FOREIGN KEY (id_video) REFERENCES tb_video(id_video) ON DELETE CASCADE
);

-- =========================================================
-- Histórico de buscas
-- Cada busca fica vinculada ao usuário logado
-- =========================================================

CREATE TABLE tb_historico_busca (
    id_historico SERIAL PRIMARY KEY,
    id_usuario INT NOT NULL,
    termo_busca VARCHAR(150) NOT NULL,
    data_busca TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario) ON DELETE CASCADE
);

-- =========================================================
-- Catálogo inicial de vídeos
-- =========================================================

INSERT INTO tb_video (titulo, descricao, genero, ano_lancamento, tipo_video) VALUES
('Interestelar', 'Um grupo de astronautas viaja por um buraco de minhoca em busca de um novo lar para a humanidade.', 'Ficção Científica', 2014, 'FILME'),
('A Origem', 'Um ladrão especializado em roubar segredos através dos sonhos recebe uma missão quase impossível.', 'Ficção Científica', 2010, 'FILME'),
('Matrix', 'Um programador descobre que a realidade em que vive é uma simulação controlada por máquinas.', 'Ficção Científica', 1999, 'FILME'),
('O Senhor dos Anéis: A Sociedade do Anel', 'Um hobbit recebe a missão de destruir um anel poderoso antes que ele caia nas mãos do mal.', 'Fantasia', 2001, 'FILME'),
('O Senhor dos Anéis: As Duas Torres', 'A jornada continua enquanto a guerra pela Terra-média se intensifica.', 'Fantasia', 2002, 'FILME'),
('O Senhor dos Anéis: O Retorno do Rei', 'A batalha final pela Terra-média acontece enquanto Frodo se aproxima de Mordor.', 'Fantasia', 2003, 'FILME'),
('Cidade de Deus', 'A história do crescimento do crime organizado em uma comunidade do Rio de Janeiro.', 'Drama', 2002, 'FILME'),
('Tropa de Elite', 'Um capitão do BOPE enfrenta dilemas pessoais e profissionais no combate ao crime no Rio de Janeiro.', 'Ação', 2007, 'FILME'),
('Clube da Luta', 'Um homem insatisfeito com sua vida cria um clube secreto que foge do controle.', 'Drama', 1999, 'FILME'),
('Forrest Gump', 'Um homem simples participa de eventos marcantes da história americana enquanto busca seu amor de infância.', 'Drama', 1994, 'FILME'),
('O Poderoso Chefão', 'A trajetória da família Corleone dentro do crime organizado nos Estados Unidos.', 'Crime', 1972, 'FILME'),
('Batman: O Cavaleiro das Trevas', 'Batman enfrenta o Coringa, um criminoso caótico que ameaça Gotham City.', 'Ação', 2008, 'FILME'),
('Gladiador', 'Um general romano traído busca vingança após perder sua família e sua posição.', 'Ação', 2000, 'FILME'),
('Titanic', 'Um romance nasce durante a viagem inaugural do navio Titanic.', 'Romance', 1997, 'FILME'),
('Avatar', 'Um ex-fuzileiro participa de uma missão em Pandora e se envolve com a cultura local.', 'Ficção Científica', 2009, 'FILME'),
('Vingadores: Ultimato', 'Os heróis sobreviventes tentam reverter os danos causados por Thanos.', 'Ação', 2019, 'FILME'),
('Homem-Aranha: Sem Volta Para Casa', 'Peter Parker enfrenta consequências após sua identidade ser revelada ao mundo.', 'Ação', 2021, 'FILME'),
('Toy Story', 'Brinquedos ganham vida quando os humanos não estão por perto.', 'Animação', 1995, 'FILME'),
('Divertida Mente', 'As emoções de uma garota tentam guiá-la durante uma fase difícil de mudanças.', 'Animação', 2015, 'FILME'),
('Parasita', 'Uma família pobre se infiltra na vida de uma família rica, gerando consequências inesperadas.', 'Suspense', 2019, 'FILME'),
('Breaking Bad', 'Um professor de química passa a produzir metanfetamina após descobrir uma doença grave.', 'Drama', 2008, 'SERIE'),
('Stranger Things', 'Crianças enfrentam eventos sobrenaturais em uma pequena cidade.', 'Ficção Científica', 2016, 'SERIE'),
('The Last of Us', 'Sobreviventes atravessam um mundo pós-apocalíptico após uma infecção global.', 'Drama', 2023, 'SERIE'),
('Dark', 'O desaparecimento de uma criança revela segredos envolvendo viagens no tempo.', 'Ficção Científica', 2017, 'SERIE'),
('The Mandalorian', 'Um caçador de recompensas viaja pela galáxia protegendo uma criança misteriosa.', 'Ficção Científica', 2019, 'SERIE');

-- =========================================================
-- Usuário opcional para testes
-- O sistema também permite cadastrar usuários pela interface.
-- =========================================================

INSERT INTO tb_usuario (nome, email, senha) VALUES
('Usuário Teste', 'teste@email.com', '123456');

-- =========================================================
-- Consultas úteis para validação
-- =========================================================

SELECT * FROM tb_usuario;
SELECT * FROM tb_video ORDER BY id_video;
SELECT * FROM tb_lista;
SELECT * FROM tb_lista_video;
SELECT * FROM tb_curtida;
SELECT * FROM tb_historico_busca;