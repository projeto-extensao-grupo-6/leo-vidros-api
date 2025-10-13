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
    fk_endereco INT,
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
    telefone VARCHAR(100),
    funcao VARCHAR(100),
    contrato VARCHAR(100),
    ativo BOOLEAN DEFAULT TRUE COMMENT 'Indica se está ativo na empresa',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro'
);

CREATE TABLE solicitacao (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    email VARCHAR(155) NOT NULL,
    telefone VARCHAR(20),
    status ENUM('PENDENTE','APROVADO','RECUSADO') DEFAULT 'PENDENTE' COMMENT 'Status da solicitação',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro'
);

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
    fk_produto INT NOT NULL,
    tipo VARCHAR(100) COMMENT 'Tipo de atributo (Ex: cor, espessura, material)',
    valor VARCHAR(100) COMMENT 'Valor do atributo (Ex: verde, 8mm, alumínio)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do registro',
    FOREIGN KEY (fk_produto) REFERENCES produto(id)
);

CREATE TABLE estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_produto INT NOT NULL,
    quantidade INT CHECK (quantidade >= 0),
    reservado INT DEFAULT 0,
    localizacao VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de entrada no estoque',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (fk_produto) REFERENCES produto(id)
);

CREATE TABLE historico_estoque (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_estoque INT NOT NULL,
    fk_usuario INT,
    tipo_movimentacao ENUM('ENTRADA','SAIDA') NOT NULL COMMENT 'Tipo de movimentação',
    quantidade INT NOT NULL,
    observacao VARCHAR(255),
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data e hora da movimentação',
    FOREIGN KEY (fk_estoque) REFERENCES estoque(id),
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id)
);

CREATE TABLE pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_cliente INT NOT NULL,
    fk_funcionario INT,
    valor_total DECIMAL(18,2),
    status ENUM('PENDENTE','APROVADO','CANCELADO','FINALIZADO') DEFAULT 'PENDENTE' COMMENT 'Status atual do pedido',
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT,
    FOREIGN KEY (fk_cliente) REFERENCES cliente(id),
    FOREIGN KEY (fk_funcionario) REFERENCES funcionario(id)
);

CREATE TABLE agendamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_pedido INT,
    fk_funcionario INT,
    tipo ENUM('ORCAMENTO','SERVICO') NOT NULL,
    data_agendamento TIMESTAMP NOT NULL,
    status ENUM('PENDENTE','CONFIRMADO','CANCELADO','CONCLUIDO') DEFAULT 'PENDENTE' COMMENT 'Status do agendamento',
    observacao TEXT,
    fk_endereco INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (fk_pedido) REFERENCES pedido(id),
    FOREIGN KEY (fk_funcionario) REFERENCES funcionario(id),
    FOREIGN KEY (fk_endereco) REFERENCES endereco(id)
);

CREATE TABLE pedido_funcionario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fk_pedido INT NOT NULL,
    fk_funcionario INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do vínculo',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização do vínculo',
    FOREIGN KEY (fk_pedido) REFERENCES pedido(id),
    FOREIGN KEY (fk_funcionario) REFERENCES funcionario(id)
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
    fk_categoria INT NOT NULL,
    mensagem TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    CONSTRAINT fk_log_categoria foreign key (fk_categoria) REFERENCES categoria(id)
);

INSERT INTO categoria (nome) VALUES
('INFO'),
('ERROR'),
('DEBUG'),
('WARNING'),
('SUCCESS'),
('FATAL');