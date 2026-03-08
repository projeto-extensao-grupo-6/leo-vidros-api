-- ===========================================================================
-- V202526030800 - Cria tabelas de orçamento e insere status de orçamento
-- ===========================================================================

-- Status do tipo ORCAMENTO
INSERT INTO status (tipo, nome) VALUES
('ORCAMENTO', 'RASCUNHO'),
('ORCAMENTO', 'ENVIADO'),
('ORCAMENTO', 'EM ANALISE'),
('ORCAMENTO', 'APROVADO'),
('ORCAMENTO', 'RECUSADO'),
('ORCAMENTO', 'EXPIRADO');

-- Tabela principal de orçamento
CREATE TABLE orcamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pedido_id INT NOT NULL,
    cliente_id INT NOT NULL,
    status_id INT,
    numero_orcamento VARCHAR(50),
    data_orcamento DATE NOT NULL,
    observacoes TEXT,
    prazo_instalacao VARCHAR(100),
    garantia VARCHAR(100),
    forma_pagamento VARCHAR(255),
    valor_subtotal DECIMAL(18,2) DEFAULT 0,
    valor_desconto DECIMAL(18,2) DEFAULT 0,
    valor_total DECIMAL(18,2) DEFAULT 0,
    pdf_path VARCHAR(500),
    ativo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (status_id) REFERENCES status(id)
);

-- Tabela de itens do orçamento
CREATE TABLE orcamento_item (
    id INT PRIMARY KEY AUTO_INCREMENT,
    orcamento_id INT NOT NULL,
    produto_id INT,
    descricao TEXT NOT NULL,
    quantidade DECIMAL(18,5) NOT NULL,
    preco_unitario DECIMAL(18,5) NOT NULL,
    desconto DECIMAL(18,2) DEFAULT 0,
    subtotal DECIMAL(18,2) AS (quantidade * preco_unitario - desconto) STORED,
    observacao TEXT,
    ordem INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (orcamento_id) REFERENCES orcamento(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);
