-- =============================================================
-- Migration: Refatoração do status de pedido para novo fluxo
-- Data: 2026-05-11
-- Objetivo: Substituir status genéricos (ATIVO, FINALIZADO...)
--           por status granulares que refletem o fluxo real do serviço.
-- =============================================================

-- 1. Adicionar novos status de pedido
INSERT INTO status (tipo, nome)
SELECT tipo, nome FROM (VALUES
    ('PEDIDO', 'AGUARDANDO AGENDA DE ORÇAMENTO'),
    ('PEDIDO', 'ORÇAMENTO AGENDADO'),
    ('PEDIDO', 'ANÁLISE DO ORÇAMENTO'),
    ('PEDIDO', 'ORÇAMENTO APROVADO'),
    ('PEDIDO', 'AGUARDANDO AGENDA DE SERVIÇO/INSTALAÇÃO'),
    ('PEDIDO', 'SERVIÇO AGENDADO'),
    ('PEDIDO', 'AGENDAMENTO EM EXECUÇÃO'),
    ('PEDIDO', 'CONCLUÍDO')
) AS novos(tipo, nome)
WHERE NOT EXISTS (
    SELECT 1 FROM status WHERE status.tipo = novos.tipo AND status.nome = novos.nome
);

-- 2. Pedidos concluídos (etapa CONCLUÍDO ou agendamento SERVICO concluído) → CONCLUÍDO
UPDATE pedido p
INNER JOIN servico s ON s.pedido_id = p.id
INNER JOIN agendamento a ON a.servico_id = s.id AND a.tipo = 'SERVICO'
INNER JOIN status st_ag ON st_ag.id = a.status_id AND st_ag.nome IN ('CONCLUÍDO', 'CONCLUIDO')
SET p.status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'CONCLUÍDO'),
    p.ativo = FALSE,
    s.ativo = FALSE;

-- Pedidos com etapa CONCLUÍDO no serviço → CONCLUÍDO
UPDATE pedido p
INNER JOIN servico s ON s.pedido_id = p.id
INNER JOIN etapa e ON e.id = s.etapa_id AND e.nome IN ('CONCLUÍDO', 'CONCLUIDO')
SET p.status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'CONCLUÍDO'),
    p.ativo = FALSE,
    s.ativo = FALSE
WHERE p.status_id NOT IN (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'CONCLUÍDO');

-- 3. Pedidos com agendamento SERVICO EM ANDAMENTO → AGENDAMENTO EM EXECUÇÃO
UPDATE pedido p
INNER JOIN servico s ON s.pedido_id = p.id
INNER JOIN agendamento a ON a.servico_id = s.id
INNER JOIN status st_ag ON st_ag.id = a.status_id AND st_ag.nome = 'EM ANDAMENTO'
SET p.status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'AGENDAMENTO EM EXECUÇÃO')
WHERE p.status_id IN (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome IN ('ATIVO', 'PENDENTE', 'EM ANDAMENTO', 'FINALIZADO', 'CANCELADO'));

-- 4. Pedidos com agendamento SERVICO PENDENTE → SERVIÇO AGENDADO
UPDATE pedido p
INNER JOIN servico s ON s.pedido_id = p.id
INNER JOIN agendamento a ON a.servico_id = s.id AND a.tipo = 'SERVICO'
INNER JOIN status st_ag ON st_ag.id = a.status_id AND st_ag.nome = 'PENDENTE'
SET p.status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'SERVIÇO AGENDADO')
WHERE p.status_id IN (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome IN ('ATIVO', 'PENDENTE', 'EM ANDAMENTO'));

-- 5. Pedidos com agendamento ORCAMENTO PENDENTE → ORÇAMENTO AGENDADO
UPDATE pedido p
INNER JOIN servico s ON s.pedido_id = p.id
INNER JOIN agendamento a ON a.servico_id = s.id AND a.tipo = 'ORCAMENTO'
INNER JOIN status st_ag ON st_ag.id = a.status_id AND st_ag.nome = 'PENDENTE'
SET p.status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'ORÇAMENTO AGENDADO')
WHERE p.status_id IN (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome IN ('ATIVO', 'PENDENTE', 'EM ANDAMENTO'));

-- 6. Demais pedidos ATIVO/PENDENTE/EM ANDAMENTO sem agendamento → AGUARDANDO AGENDA DE ORÇAMENTO
UPDATE pedido
SET status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'AGUARDANDO AGENDA DE ORÇAMENTO')
WHERE status_id IN (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome IN ('ATIVO', 'PENDENTE', 'EM ANDAMENTO'));

-- 7. Pedidos FINALIZADO/INATIVO/CANCELADO remanescentes → CONCLUÍDO
UPDATE pedido
SET status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'CONCLUÍDO')
WHERE status_id IN (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome IN ('FINALIZADO', 'INATIVO', 'CANCELADO'));

-- 8. Remover status de pedido obsoletos
DELETE FROM status
WHERE tipo = 'PEDIDO'
  AND nome IN ('ATIVO', 'EM ANDAMENTO', 'FINALIZADO', 'PENDENTE', 'CANCELADO', 'INATIVO');
