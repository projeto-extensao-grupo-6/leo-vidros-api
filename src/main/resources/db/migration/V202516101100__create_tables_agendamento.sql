CREATE TABLE etapa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    id_etapa INT,
    id_status INT,
    valor_total DECIMAL(18,2),
    ativo BOOLEAN DEFAULT TRUE COMMENT 'Indica se o pedido está ativo',
    observacao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (id_etapa) REFERENCES etapa(id),
    FOREIGN KEY (id_status) REFERENCES status(id),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

CREATE TABLE agendamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT,
    id_endereco INT,
    id_status INT,
    tipo ENUM('ORCAMENTO','SERVICO') NOT NULL,
    data_agendamento TIMESTAMP NOT NULL,
    observacao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_status) REFERENCES status(id),
    FOREIGN KEY (id_endereco) REFERENCES endereco(id)
);

CREATE TABLE agendamento_funcionario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_agendamento INT NOT NULL,
    id_funcionario INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do vínculo',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do vínculo',
    FOREIGN KEY (id_agendamento) REFERENCES agendamento(id),
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id)
);