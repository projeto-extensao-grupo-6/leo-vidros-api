CREATE TABLE produto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE COMMENT 'Define se o produto está ativo no catálogo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro'
);

CREATE TABLE atributo_produto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_produto INT NOT NULL,
    tipo VARCHAR(100) COMMENT 'Tipo de atributo (Ex: cor, espessura, material)',
    valor VARCHAR(100) COMMENT 'Valor do atributo (Ex: verde, 8mm, alumínio)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro',
    FOREIGN KEY (id_produto) REFERENCES produto(id)
);

CREATE TABLE estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_produto INT NOT NULL,
    quantidade INT CHECK (quantidade >= 0),
    reservado INT DEFAULT 0,
    localizacao VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de entrada no estoque',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (id_produto) REFERENCES produto(id)
);

CREATE TABLE historico_estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    id_estoque INT NOT NULL,
    id_usuario INT,
    tipo_movimentacao ENUM('ENTRADA','SAIDA') NOT NULL COMMENT 'Tipo de movimentação',
    quantidade INT NOT NULL,
    observacao VARCHAR(255),
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data e hora da movimentação',
    FOREIGN KEY (id_estoque) REFERENCES estoque(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);
