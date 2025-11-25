CREATE TABLE item_pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    estoque_id INT NOT NULL,
    quantidade_solicitada DECIMAL(18, 5) NOT NULL,
    preco_unitario_negociado DECIMAL(18, 5) NOT NULL,
    subtotal DECIMAL(18, 2) AS (quantidade_solicitada * preco_unitario_negociado) STORED,
    observacao TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_item_pedido_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    CONSTRAINT fk_item_pedido_estoque FOREIGN KEY (estoque_id) REFERENCES estoque(id)
);

CREATE TABLE servico (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    descricao TEXT,
    preco_base DECIMAL(18, 2),
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE item_servico (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    servico_id INT NOT NULL,
    quantidade DECIMAL(18, 5) NOT NULL,
    preco_unitario_negociado DECIMAL(18, 2) NOT NULL,
    subtotal DECIMAL(18, 2) AS (quantidade * preco_unitario_negociado) STORED,
    observacao TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (servico_id) REFERENCES servico(id)
);