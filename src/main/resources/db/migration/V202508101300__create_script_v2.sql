CREATE TABLE `role` (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único do perfil',
    nome VARCHAR(50) NOT NULL COMMENT 'Nome do perfil (ADMIN, USER, etc.)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação do perfil',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de última atualização'
);

INSERT INTO role (nome) VALUES ('ADMIN'), ('USER');

CREATE TABLE usuario (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID único do usuário',
    nome VARCHAR(100) NOT NULL COMMENT 'Nome completo',
    cpf CHAR(11) NOT NULL UNIQUE COMMENT 'CPF do usuário',
    email VARCHAR(155) NOT NULL COMMENT 'Email de login',
    senha VARCHAR(255) COMMENT 'Senha armazenada como hash',
    telefone VARCHAR(20) COMMENT 'Telefone de contato',
    first_login BOOLEAN DEFAULT TRUE COMMENT 'Indica se é o primeiro login',
    role_id INT COMMENT 'Perfil do usuário',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    CONSTRAINT fk_usuario_role FOREIGN KEY (role_id) REFERENCES role(id)
);

CREATE INDEX idx_usuario_email ON usuario(email);

CREATE TABLE solicitacao (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID da solicitação',
    nome VARCHAR(100) NOT NULL COMMENT 'Nome do solicitante',
    cpf CHAR(11) NOT NULL UNIQUE COMMENT 'CPF do solicitante',
    email VARCHAR(155) NOT NULL COMMENT 'Email do solicitante',
    telefone VARCHAR(20) COMMENT 'Telefone',
    cargo_desejado VARCHAR(100) COMMENT 'Cargo desejado',
    status ENUM('PENDENTE','APROVADO','RECUSADO') DEFAULT 'PENDENTE' COMMENT 'Status da solicitação',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    INDEX(status)
);

CREATE TABLE estoque (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do item em estoque',
    item VARCHAR(100) COMMENT 'Nome do item',
    tipo VARCHAR(100) COMMENT 'Tipo (vidro, alumínio, broca, etc.)',
    descricao VARCHAR(100) COMMENT 'Descrição detalhada',
    preco DECIMAL(18,2) COMMENT 'Preço unitário',
    quantidade INT CHECK (quantidade >= 0) COMMENT 'Quantidade disponível',
    reservado INT DEFAULT 0 COMMENT 'Quantidade reservada',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE INDEX idx_estoque_tipo ON estoque(tipo);

CREATE TABLE movimentacoes_estoque (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID da movimentação',
    fk_item_estoque INT NOT NULL COMMENT 'Item movimentado',
    fk_usuario INT NOT NULL COMMENT 'Usuário responsável',
    tipo_movimentacao ENUM('ENTRADA', 'SAIDA') NOT NULL COMMENT 'Tipo de movimentação',
    quantidade INT NOT NULL CHECK (quantidade > 0) COMMENT 'Quantidade movimentada',
    observacao VARCHAR(255) COMMENT 'Observações',
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data da movimentação',
    FOREIGN KEY (fk_item_estoque) REFERENCES estoque(id),
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id),
    INDEX(fk_item_estoque, data_movimentacao)
);

CREATE TABLE tipos_servicos (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do tipo de serviço',
    nome VARCHAR(50) COMMENT 'Nome do serviço',
    descricao VARCHAR(100) COMMENT 'Descrição detalhada',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE tipos_vidros (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do tipo de vidro',
    nome VARCHAR(30) COMMENT 'Nome do vidro',
    descricao VARCHAR(100) COMMENT 'Descrição detalhada',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE enderecos (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do endereço',
    cidade VARCHAR(50) COMMENT 'Cidade',
    estado VARCHAR(50) COMMENT 'Estado',
    uf VARCHAR(2) COMMENT 'UF',
    logradouro VARCHAR(200) COMMENT 'Rua/avenida',
    numero VARCHAR(10) COMMENT 'Número',
    complemento VARCHAR(50) COMMENT 'Complemento',
    cep VARCHAR(8) COMMENT 'CEP',
    referencia VARCHAR(200) COMMENT 'Referência',
    tipo_endereco VARCHAR(200) COMMENT 'Ex.: residencial/comercial',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE telefones (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do telefone',
    numero VARCHAR(12) COMMENT 'Número do telefone',
    tipo ENUM('celular','fixo','comercial') COMMENT 'Tipo do telefone',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização'
);

CREATE TABLE clientes (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do cliente',
    nome VARCHAR(150) COMMENT 'Nome do cliente',
    email VARCHAR(100) COMMENT 'Email do cliente',
    status VARCHAR(100) COMMENT 'Status do cliente',
    fk_endereco INT COMMENT 'Endereço',
    fk_telefone INT COMMENT 'Telefone principal',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (fk_endereco) REFERENCES enderecos(id),
    FOREIGN KEY (fk_telefone) REFERENCES telefones(id)
);

CREATE TABLE servicos (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do serviço',
    data_servico TIMESTAMP COMMENT 'Data prevista',
    descricao VARCHAR(200) COMMENT 'Descrição do serviço',
    altura DECIMAL(4,2) COMMENT 'Altura do vidro/material',
    largura DECIMAL(4,2) COMMENT 'Largura do vidro/material',
    quantidade_vidro INT COMMENT 'Quantidade de vidro',
    fk_tipo_servico INT COMMENT 'Tipo do serviço',
    fk_tipo_vidro INT COMMENT 'Tipo do vidro',
    fk_endereco INT COMMENT 'Endereço do serviço',
    fk_funcionario INT COMMENT 'Funcionário responsável',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (fk_tipo_servico) REFERENCES tipos_servicos(id),
    FOREIGN KEY (fk_tipo_vidro) REFERENCES tipos_vidros(id),
    FOREIGN KEY (fk_endereco) REFERENCES enderecos(id),
    FOREIGN KEY (fk_funcionario) REFERENCES usuario(id)
);

CREATE TABLE clientes_servicos (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do vínculo cliente-serviço',
    fk_cliente INT NOT NULL COMMENT 'Cliente',
    fk_servico INT NOT NULL COMMENT 'Serviço',
    status_contrato ENUM('ATIVO','FINALIZADO','CANCELADO') DEFAULT 'ATIVO' COMMENT 'Status do contrato',
    observacao TEXT COMMENT 'Observações sobre o contrato',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (fk_cliente) REFERENCES clientes(id),
    FOREIGN KEY (fk_servico) REFERENCES servicos(id),
    INDEX(fk_cliente, status_contrato)
);

CREATE TABLE agendamento (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do agendamento',
    fk_servico INT COMMENT 'Serviço vinculado (NULL se orçamento)',
    tipo ENUM('ORCAMENTO','SERVICO') NOT NULL COMMENT 'Tipo: orçamento ou serviço',
    fk_usuario INT NOT NULL COMMENT 'Usuário que agendou',
    data_agendamento TIMESTAMP NOT NULL COMMENT 'Data agendada',
    status ENUM('PENDENTE','CONFIRMADO','CANCELADO','EM_EXECUCAO','CONCLUIDO') DEFAULT 'PENDENTE' COMMENT 'Status do agendamento',
    observacao TEXT COMMENT 'Observações adicionais',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (fk_servico) REFERENCES servicos(id),
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id),
    INDEX(fk_servico, status)
);

CREATE TABLE agendamento_materiais (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do material reservado para agendamento',
    fk_agendamento INT NOT NULL COMMENT 'Agendamento relacionado',
    fk_estoque INT NOT NULL COMMENT 'Item de estoque',
    quantidade_reservada INT NOT NULL COMMENT 'Quantidade reservada',
    status ENUM('RESERVADO','UTILIZADO','FALTANTE') DEFAULT 'RESERVADO' COMMENT 'Status da reserva',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data de criação',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Data de atualização',
    FOREIGN KEY (fk_agendamento) REFERENCES agendamento(id),
    FOREIGN KEY (fk_estoque) REFERENCES estoque(id)
);

CREATE TABLE alerta_estoque (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID do alerta de estoque',
    fk_estoque INT NOT NULL COMMENT 'Item relacionado',
    fk_agendamento INT NOT NULL COMMENT 'Agendamento que gerou o alerta',
    quantidade_necessaria INT NOT NULL COMMENT 'Quantidade necessária para o agendamento',
    mensagem VARCHAR(255) COMMENT 'Mensagem do alerta',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Data do alerta',
    FOREIGN KEY (fk_estoque) REFERENCES estoque(id),
    FOREIGN KEY (fk_agendamento) REFERENCES agendamento(id)
);