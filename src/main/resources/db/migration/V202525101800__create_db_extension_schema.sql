CREATE TABLE endereco (
    id INT PRIMARY KEY AUTO_INCREMENT,
    logradouro VARCHAR(200),
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    uf CHAR(2),
    cep VARCHAR(8),
    referencia VARCHAR(200),
    tipo ENUM('RESIDENCIAL','COMERCIAL'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro'
);

CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) UNIQUE,
    email VARCHAR(155) NOT NULL,
    senha VARCHAR(255),
    telefone VARCHAR(20),
    first_login BOOLEAN DEFAULT TRUE,
    endereco_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro'
);

CREATE TABLE cliente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    telefone VARCHAR(20),
    status ENUM('ATIVO','INATIVO') DEFAULT 'ATIVO' COMMENT 'Status atual do cliente',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro'
);

CREATE TABLE funcionario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100),
    telefone VARCHAR(100) UNIQUE,
    funcao VARCHAR(100),
    contrato VARCHAR(100),
    ativo BOOLEAN DEFAULT TRUE COMMENT 'Indica se está ativo na empresa',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro'
);

CREATE TABLE status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo VARCHAR(100) COMMENT 'Tipo de status: PEDIDO, AGENDAMENTO, CADASTRO',
    nome VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE solicitacao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    email VARCHAR(155) NOT NULL,
    telefone VARCHAR(20),
    status_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro',
    FOREIGN KEY (status_id) REFERENCES status(id)
);

CREATE TABLE categoria (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    data_hora DATETIME NOT NULL,
    id_categoria INT NOT NULL,
    mensagem TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    CONSTRAINT fk_log_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);

CREATE TABLE metrica_estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nivel_minimo INT DEFAULT 0,
    nivel_maximo INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de entrada no estoque',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE produto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    unidade_medida VARCHAR(255),
    preco DECIMAL(16, 5),
    metrica_estoque_id INT,
    ativo BOOLEAN DEFAULT TRUE COMMENT 'Define se o produto está ativo no catálogo',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro',
    FOREIGN KEY (metrica_estoque_id) REFERENCES metrica_estoque(id)
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
    quantidade_total INT CHECK (quantidade_total >= 0),
    quantidade_disponivel INT,
    reservado INT DEFAULT 0,
    localizacao VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de entrada no estoque',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

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
    status_id INT,
    valor_total DECIMAL(18,2),
    ativo BOOLEAN DEFAULT TRUE COMMENT 'Indica se o pedido está ativo',
    observacao TEXT,
    forma_pagamento VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (status_id) REFERENCES status(id),
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE servico (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    codigo VARCHAR(255),
    descricao TEXT,
    preco_base DECIMAL(18, 2),
    ativo BOOLEAN DEFAULT TRUE,
    pedido_id INT,
    etapa_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (etapa_id) REFERENCES etapa(id)
);

CREATE TABLE agendamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    servico_id INT,
    endereco_id INT,
    status_id INT,
    tipo ENUM('ORCAMENTO','SERVICO') NOT NULL,
    data_agendamento TIMESTAMP NOT NULL,
    observacao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (servico_id) REFERENCES servico(id),
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

CREATE TABLE agendamento_produto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    agendamento_id INT NOT NULL,
    produto_id INT NOT NULL,
    quantidade_utilizada INT NOT NULL CHECK (quantidade_utilizada >= 0),
    quantidade_reservada INT NOT NULL CHECK (quantidade_reservada >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do vínculo',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do vínculo',
    FOREIGN KEY (agendamento_id) REFERENCES agendamento(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

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

CREATE TABLE historico_estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    estoque_id INT NOT NULL,
    usuario_id INT,
    tipo_movimentacao ENUM('ENTRADA','SAIDA') NOT NULL,
    quantidade DECIMAL(18, 2) NOT NULL,
    quantidade_atual DECIMAL(18, 2),
    observacao VARCHAR(255),
    item_pedido_id INT NULL,
    agendamento_produto_id INT NULL,
    motivo_perda ENUM('QUEBRA','FURTO','VENCIMENTO','OUTRO') NULL,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (estoque_id) REFERENCES estoque(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (item_pedido_id) REFERENCES item_pedido(id),
    FOREIGN KEY (agendamento_produto_id) REFERENCES agendamento_produto(id)
);

ALTER TABLE cliente ADD COLUMN endereco_id INT, ADD CONSTRAINT FOREIGN KEY (endereco_id) REFERENCES endereco(id);
ALTER TABLE usuario ADD CONSTRAINT fk_usuario_endereco FOREIGN KEY (endereco_id) REFERENCES endereco(id);

ALTER TABLE estoque
MODIFY COLUMN quantidade_total DECIMAL(18, 2) CHECK (quantidade_total >= 0),
MODIFY COLUMN quantidade_disponivel DECIMAL(18, 2),
MODIFY COLUMN reservado DECIMAL(18, 2) DEFAULT 0;

ALTER TABLE historico_estoque
MODIFY COLUMN quantidade DECIMAL(18, 2) NOT NULL,
MODIFY COLUMN quantidade_atual DECIMAL(18, 2);

ALTER TABLE agendamento_produto
MODIFY COLUMN quantidade_utilizada DECIMAL(18, 2) NOT NULL CHECK (quantidade_utilizada >= 0),
MODIFY COLUMN quantidade_reservada DECIMAL(18, 2) NOT NULL CHECK (quantidade_reservada >= 0);

ALTER TABLE agendamento ADD COLUMN inicio_agendamento TIMESTAMP NOT NULL;
ALTER TABLE agendamento ADD COLUMN fim_agendamento TIMESTAMP NOT NULL;
ALTER TABLE agendamento MODIFY COLUMN data_agendamento DATE NOT NULL;

ALTER TABLE funcionario ADD COLUMN escala VARCHAR(255);

INSERT INTO etapa (tipo, nome) VALUES
('PEDIDO', 'PENDENTE'),
('PEDIDO', 'AGUARDANDO ORÇAMENTO'),
('PEDIDO', 'ANÁLISE DO ORÇAMENTO'),
('PEDIDO', 'ORÇAMENTO APROVADO'),
('PEDIDO', 'SERVIÇO AGENDADO'),
('PEDIDO', 'SERVIÇO EM EXECUÇÃO'),
('PEDIDO', 'CONCLUÍDO');

---------------------------------------------------------

INSERT INTO status (tipo, nome) VALUES
('AGENDAMENTO', 'PENDENTE'),
('AGENDAMENTO', 'EM ANDAMENTO'),
('AGENDAMENTO', 'CONCLUÍDO');

INSERT INTO status (tipo, nome) VALUES
('SOLICITACAO', 'PENDENTE'),
('SOLICITACAO', 'RECUSADO'),
('SOLICITACAO', 'ACEITO');

INSERT INTO status (tipo, nome) VALUES
('PEDIDO', 'ATIVO'),
('PEDIDO', 'EM ANDAMENTO'),
('PEDIDO', 'FINALIZADO'),
('PEDIDO', 'PENDENTE');

INSERT INTO categoria (nome) VALUES
('INFO'),
('ERROR'),
('DEBUG'),
('WARNING'),
('SUCCESS'),
('FATAL');
