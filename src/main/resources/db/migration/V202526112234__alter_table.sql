-- TABELA ESTOQUE
ALTER TABLE estoque
MODIFY COLUMN quantidade_total DECIMAL(18, 2) CHECK (quantidade_total >= 0),
MODIFY COLUMN quantidade_disponivel DECIMAL(18, 2),
MODIFY COLUMN reservado DECIMAL(18, 2) DEFAULT 0;

-- TABELA HISTORICO_ESTOQUE
ALTER TABLE historico_estoque
MODIFY COLUMN quantidade DECIMAL(18, 2) NOT NULL,
MODIFY COLUMN quantidade_atual DECIMAL(18, 2);

ALTER TABLE agendamento_produto
MODIFY COLUMN quantidade_utilizada DECIMAL(18, 2) NOT NULL CHECK (quantidade_utilizada >= 0),
MODIFY COLUMN quantidade_reservada DECIMAL(18, 2) NOT NULL CHECK (quantidade_reservada >= 0);