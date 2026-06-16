-- V1_criar_tabela-evento.sql
CREATE TABLE Eventos (
    id BIGSERIAL PRIMARY KEY ,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255),
    inicio DATE NOT NULL,
    fim DATE,
    identificador VARCHAR(255) NOT NULL,
    local VARCHAR(255) NOT NULL,
    organizador VARCHAR(255),
    tipoEvento VARCHAR(50) NOT NULL
)