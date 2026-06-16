-- V1_criar_tabela-evento.sql
CREATE TABLE Eventos (
    id BIGSERIAL PRIMARY KEY ,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    identificador VARCHAR(255) NOT NULL UNIQUE ,
    local_evento VARCHAR(255) NOT NULL,
    organizador VARCHAR(255) NOT NULL,
    capacidade INTEGER NOT NULL,
    tipoEvento VARCHAR(50) NOT NULL
)