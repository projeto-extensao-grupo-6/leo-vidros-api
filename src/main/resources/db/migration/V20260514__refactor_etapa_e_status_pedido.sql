-- =============================================================
-- Migration: Refatoração de etapa e status do pedido
-- Data: 2026-05-14
-- Objetivo:
--   - etapa: renomear para nomes novos do fluxo
--   - status (PEDIDO): substituir valores de workflow por
--     ciclo de vida simples: ATIVO, INATIVO, CANCELADO
-- =============================================================

-- 1. Adicionar novas etapas
INSERT INTO etapa (tipo, nome)
SELECT tipo, nome FROM (VALUES
    ('PEDIDO', 'AGUARDANDO AGENDA DE ORÇAMENTO'),
    ('PEDIDO', 'ORÇAMENTO AGENDADO'),
    ('PEDIDO', 'AGUARDANDO AGENDA DE SERVIÇO/INSTALAÇÃO'),
    ('PEDIDO', 'AGENDAMENTO EM EXECUÇÃO')
) AS novas(tipo, nome)
WHERE NOT EXISTS (
    SELECT 1 FROM etapa WHERE etapa.tipo = novas.tipo AND etapa.nome = novas.nome
);

-- 2. Migrar serviços com etapa PENDENTE → AGUARDANDO AGENDA DE ORÇAMENTO
UPDATE servico s
INNER JOIN etapa e ON e.id = s.etapa_id AND e.tipo = 'PEDIDO' AND e.nome = 'PENDENTE'
SET s.etapa_id = (SELECT id FROM etapa WHERE tipo = 'PEDIDO' AND nome = 'AGUARDANDO AGENDA DE ORÇAMENTO');

-- 3. Migrar serviços com etapa AGUARDANDO ORÇAMENTO → ORÇAMENTO AGENDADO
UPDATE servico s
INNER JOIN etapa e ON e.id = s.etapa_id AND e.tipo = 'PEDIDO' AND e.nome = 'AGUARDANDO ORÇAMENTO'
SET s.etapa_id = (SELECT id FROM etapa WHERE tipo = 'PEDIDO' AND nome = 'ORÇAMENTO AGENDADO');

-- 4. Migrar serviços com etapa SERVIÇO EM EXECUÇÃO → AGENDAMENTO EM EXECUÇÃO
UPDATE servico s
INNER JOIN etapa e ON e.id = s.etapa_id AND e.tipo = 'PEDIDO' AND e.nome = 'SERVIÇO EM EXECUÇÃO'
SET s.etapa_id = (SELECT id FROM etapa WHERE tipo = 'PEDIDO' AND nome = 'AGENDAMENTO EM EXECUÇÃO');

-- 5. Remover etapas antigas
DELETE FROM etapa
WHERE tipo = 'PEDIDO'
  AND nome IN ('PENDENTE', 'AGUARDANDO ORÇAMENTO', 'SERVIÇO EM EXECUÇÃO', 'REAGENDAR');

-- 6. Adicionar novos status de pedido (ciclo de vida)
INSERT INTO status (tipo, nome)
SELECT tipo, nome FROM (VALUES
    ('PEDIDO', 'ATIVO'),
    ('PEDIDO', 'INATIVO'),
    ('PEDIDO', 'CANCELADO')
) AS novos(tipo, nome)
WHERE NOT EXISTS (
    SELECT 1 FROM status WHERE status.tipo = novos.tipo AND status.nome = novos.nome
);

-- 7. Migrar pedidos com status de workflow concluído → INATIVO
UPDATE pedido
SET status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'INATIVO'),
    ativo = FALSE
WHERE status_id IN (
    SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome IN ('CONCLUÍDO', 'CONCLUIDO')
);

-- 8. Migrar pedidos com status de workflow cancelado → CANCELADO + INATIVO lifecycle
UPDATE pedido
SET status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'CANCELADO'),
    ativo = FALSE
WHERE status_id IN (
    SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome IN ('CANCELADO', 'INATIVO', 'FINALIZADO')
  AND tipo = 'PEDIDO'
);

-- 9. Migrar todos os demais pedidos com status de workflow → ATIVO
UPDATE pedido
SET status_id = (SELECT id FROM status WHERE tipo = 'PEDIDO' AND nome = 'ATIVO')
WHERE status_id IN (
    SELECT id FROM status WHERE tipo = 'PEDIDO'
      AND nome IN (
          'AGUARDANDO AGENDA DE ORÇAMENTO',
          'ORÇAMENTO AGENDADO',
          'ANÁLISE DO ORÇAMENTO',
          'ORÇAMENTO APROVADO',
          'AGUARDANDO AGENDA DE SERVIÇO/INSTALAÇÃO',
          'SERVIÇO AGENDADO',
          'AGENDAMENTO EM EXECUÇÃO',
          'ATIVO',
          'PENDENTE',
          'EM ANDAMENTO'
      )
);

-- 10. Remover status de pedido de workflow obsoletos
DELETE FROM status
WHERE tipo = 'PEDIDO'
  AND nome IN (
      'AGUARDANDO AGENDA DE ORÇAMENTO',
      'ORÇAMENTO AGENDADO',
      'ANÁLISE DO ORÇAMENTO',
      'ORÇAMENTO APROVADO',
      'AGUARDANDO AGENDA DE SERVIÇO/INSTALAÇÃO',
      'SERVIÇO AGENDADO',
      'AGENDAMENTO EM EXECUÇÃO',
      'CONCLUÍDO',
      'CONCLUIDO',
      'FINALIZADO',
      'PENDENTE',
      'EM ANDAMENTO'
  );
