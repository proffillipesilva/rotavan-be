-- 1. Tabela de Usuários (Base para Responsável e Motorista)
CREATE TABLE users (
    id BINARY(16) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    picture VARCHAR(255),
    fcm_token VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY UK_user_email (email)
);

-- 2. Tabela de Responsáveis
CREATE TABLE responsaveis (
    id BINARY(16) NOT NULL,
    nome_responsavel VARCHAR(255),
    cpf_responsavel VARCHAR(255),
    telefone VARCHAR(255),
    endereco VARCHAR(255),
    endereco_casa VARCHAR(255),
    user_id BINARY(16),
    PRIMARY KEY (id),
    CONSTRAINT FK_Responsaveis_User FOREIGN KEY (user_id) REFERENCES users(id) -- <-- CORRIGIDO
);

-- 3. Tabela de Motoristas
CREATE TABLE motoristas (
    id BINARY(16) NOT NULL,
    nome_motorista VARCHAR(255),
    cnh VARCHAR(255),
    val_cnh DATE,
    pix VARCHAR(255),
    cpf VARCHAR(255),
    user_id BINARY(16),
    PRIMARY KEY (id),
    CONSTRAINT FK_Motoristas_User FOREIGN KEY (user_id) REFERENCES users(id) -- <-- CORRIGIDO
);

-- 4. Tabela de Escolas
CREATE TABLE escolas (
    id BINARY(16) NOT NULL,
    nome VARCHAR(255),
    endereco VARCHAR(255),
    cnpj VARCHAR(255),
    telefone VARCHAR(255),      -- <-- ADICIONE ESTA LINHA
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    PRIMARY KEY (id)
);

-- 5. Tabela de Criança
CREATE TABLE crianca (
    id BINARY(16) NOT NULL,
    nome VARCHAR(255),
    endereco VARCHAR(255),
    data_nascimento DATE,
    periodo VARCHAR(50),
    nivel_escolar VARCHAR(100),
    telefone VARCHAR(255),      -- <-- ADICIONE ESTA LINHA
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    responsavel_id BINARY(16),
    escola_id BINARY(16),
    PRIMARY KEY (id),
    CONSTRAINT FK_Crianca_Responsavel FOREIGN KEY (responsavel_id) REFERENCES responsaveis(id),
    CONSTRAINT FK_Crianca_Escola FOREIGN KEY (escola_id) REFERENCES escolas(id)
);

-- 6. Tabela de Veículo
CREATE TABLE veiculos (
    id BINARY(16) NOT NULL,
    placa VARCHAR(255) NOT NULL,
    modelo VARCHAR(255),
    ano INT,
    capacidade INT,
    fk_motorista BINARY(16), -- <-- CORRIGIDO
    PRIMARY KEY (id),
    UNIQUE KEY UK_veiculo_placa (placa),
    CONSTRAINT FK_Veiculo_Motorista FOREIGN KEY (fk_motorista) REFERENCES motoristas(id) -- <-- CORRIGIDO
);

-- 7. Tabela de Contrato
CREATE TABLE contratos (
    id BINARY(16) NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    valor_mensal DECIMAL(19, 2),
    status VARCHAR(50),
    fk_motorista BINARY(16),    -- <-- CORRIGIDO
    fk_responsavel BINARY(16),  -- <-- CORRIGIDO
    PRIMARY KEY (id),
    CONSTRAINT FK_Contrato_Motorista FOREIGN KEY (fk_motorista) REFERENCES motoristas(id),
    CONSTRAINT FK_Contrato_Responsavel FOREIGN KEY (fk_responsavel) REFERENCES responsaveis(id)
);

-- 8. Tabela de Rotas (Sem a fk_solicitacao, que será adicionada no V2)
CREATE TABLE rotas (
    id BINARY(16) NOT NULL,
    nome_rota VARCHAR(255),
    descricao VARCHAR(255),
    distancia_km DECIMAL(19, 2),
    tempo_estimado_min INT,
    motorista_id BINARY(16),
    PRIMARY KEY (id),
    CONSTRAINT FK_Rota_Motorista FOREIGN KEY (motorista_id) REFERENCES motoristas(id)
);

-- 9. Tabela de Ponto
CREATE TABLE pontos (
    id BINARY(16) NOT NULL,
    nome_ponto VARCHAR(255),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    ordem INT,
    fk_rota BINARY(16),         -- <-- CORRIGIDO
    PRIMARY KEY (id),
    CONSTRAINT FK_Pontos_Rota FOREIGN KEY (fk_rota) REFERENCES rotas(id) -- <-- CORRIGIDO
);

-- 10. Tabela de Viagem
-- 10. Tabela de Viagem
CREATE TABLE viagens (
    id BINARY(16) NOT NULL,
    data_viagem DATE NOT NULL,
    hora_saida TIME NOT NULL,        -- <-- ADICIONADO
    hora_chegada TIME,               -- <-- ADICIONADO
    status VARCHAR(50),
    tipo_viagem VARCHAR(50),         -- <-- Verifique se você ainda usa este campo no Java
    fk_dependente BINARY(16) NOT NULL, -- <-- ADICIONADO
    fk_motorista BINARY(16) NOT NULL,  -- <-- ADICIONADO
    fk_rota BINARY(16) NOT NULL,       -- <-- RENOMEADO (era rota_id)
    PRIMARY KEY (id),
    CONSTRAINT FK_Viagens_Rota FOREIGN KEY (fk_rota) REFERENCES rotas(id), -- <-- RENOMEADO
    CONSTRAINT FK_Viagens_Crianca FOREIGN KEY (fk_dependente) REFERENCES crianca(id), -- <-- ADICIONADO
    CONSTRAINT FK_Viagens_Motorista FOREIGN KEY (fk_motorista) REFERENCES motoristas(id) -- <-- ADICIONADO
);
);