CREATE TABLE produto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    unidade_medida VARCHAR(255),
    preco DECIMAL(16, 5),
    ativo BOOLEAN DEFAULT TRUE COMMENT 'Define se o produto está ativo no catálogo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro'
);

CREATE TABLE atributo_produto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    produto_id INT NOT NULL,
    tipo VARCHAR(100) COMMENT 'Tipo de atributo (Ex: cor, espessura, material)',
    valor VARCHAR(100) COMMENT 'Valor do atributo (Ex: verde, 8mm, alumínio)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro',
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

CREATE TABLE estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    produto_id INT NOT NULL,
    quantidade INT CHECK (quantidade >= 0),
    reservado INT DEFAULT 0,
    localizacao VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de entrada no estoque',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

CREATE TABLE historico_estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    estoque_id INT NOT NULL,
    usuario_id INT,
    tipo_movimentacao ENUM('ENTRADA','SAIDA') NOT NULL COMMENT 'Tipo de movimentação',
    quantidade INT NOT NULL,
    quantidade_atual INT,
    observacao VARCHAR(255),
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data e hora da movimentação',
    FOREIGN KEY (estoque_id) REFERENCES estoque(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE metrica_estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    produto_id INT NOT NULL,
    nivel_minimo INT DEFAULT 0,
    nivel_maximo INT,
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);