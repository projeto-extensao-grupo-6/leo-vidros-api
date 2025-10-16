CREATE TABLE etapa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(100),
    nome VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cliente_id INT,
    etapa_id INT,
    status_id INT,
    valor_total DECIMAL(18,2),
    ativo BOOLEAN DEFAULT TRUE COMMENT 'Indica se o pedido está ativo',
    observacao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (etapa_id) REFERENCES etapa(id),
    FOREIGN KEY (status_id) REFERENCES status(id),
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE agendamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT,
    endereco_id INT,
    status_id INT,
    tipo ENUM('ORCAMENTO','SERVICO') NOT NULL,
    data_agendamento TIMESTAMP NOT NULL,
    observacao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (status_id) REFERENCES status(id),
    FOREIGN KEY (endereco_id) REFERENCES endereco(id)
);

CREATE TABLE agendamento_funcionario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    agendamento_id INT NOT NULL,
    funcionario_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do vínculo',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do vínculo',
    FOREIGN KEY (agendamento_id) REFERENCES agendamento(id),
    FOREIGN KEY (funcionario_id) REFERENCES funcionario(id)
);

INSERT INTO status (tipo, nome) VALUES
('PEDIDO', 'PENDENTE'),
('PEDIDO', 'AGUARDANDO ORÇAMENTO'),
('PEDIDO', 'ANÁLISE DO ORÇAMENTO'),
('PEDIDO', 'ORÇAMENTO APROVADO'),
('PEDIDO', 'SERVIÇO AGENDADO'),
('PEDIDO', 'EM EXECUÇÃO'),
('PEDIDO', 'CONCLUÍDO');

INSERT INTO status (tipo, nome) VALUES
('AGENDAMENTO', 'PENDENTE'),
('AGENDAMENTO', 'EM ANDAMENTO'),
('AGENDAMENTO', 'CONCLUÍDO');

INSERT INTO status (tipo, nome) VALUES
('SOLICITACAO', 'PENDENTE'),
('SOLICITACAO', 'RECUSADO'),
('SOLICITACAO', 'ACEITO');