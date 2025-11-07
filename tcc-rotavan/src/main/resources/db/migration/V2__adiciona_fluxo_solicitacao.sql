-- Adiciona a coluna que causou o erro na tabela Crianca
ALTER TABLE crianca
ADD COLUMN tipo_servico VARCHAR(255);

-- Cria a nova tabela Solicitacao (do seu guia)
CREATE TABLE solicitacao (
    id BINARY(16) NOT NULL,
    fk_dependente BINARY(16),
    fk_motorista BINARY(16),
    status VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_Solicitacao_Dependente FOREIGN KEY (fk_dependente) REFERENCES crianca(id),
    CONSTRAINT FK_Solicitacao_Motorista FOREIGN KEY (fk_motorista) REFERENCES motoristas(id)
);

-- Adiciona a coluna fk_solicitacao na tabela Rota (do seu guia)
ALTER TABLE rotas
ADD COLUMN fk_solicitacao BINARY(16),
ADD CONSTRAINT FK_Rota_Solicitacao FOREIGN KEY (fk_solicitacao) REFERENCES solicitacao(id) ON DELETE CASCADE;