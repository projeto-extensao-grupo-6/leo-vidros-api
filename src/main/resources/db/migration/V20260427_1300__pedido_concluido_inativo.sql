INSERT INTO status (tipo, nome)
SELECT 'PEDIDO', 'INATIVO'
WHERE NOT EXISTS (
    SELECT 1
    FROM status
    WHERE tipo = 'PEDIDO' AND nome = 'INATIVO'
);

UPDATE pedido p
JOIN status st_inativo
  ON st_inativo.tipo = 'PEDIDO'
 AND st_inativo.nome = 'INATIVO'
JOIN status st_finalizado
  ON st_finalizado.id = p.status_id
SET p.status_id = st_inativo.id,
    p.ativo = FALSE
WHERE st_finalizado.tipo = 'PEDIDO'
  AND st_finalizado.nome = 'FINALIZADO';

UPDATE pedido p
JOIN servico s
  ON s.pedido_id = p.id
JOIN etapa e
  ON e.id = s.etapa_id
JOIN status st_inativo
  ON st_inativo.tipo = 'PEDIDO'
 AND st_inativo.nome = 'INATIVO'
SET p.status_id = st_inativo.id,
    p.ativo = FALSE,
    s.ativo = FALSE
WHERE e.tipo = 'PEDIDO'
  AND e.nome IN ('CONCLUIDO', 'CONCLUÍDO');

UPDATE pedido p
JOIN servico s
  ON s.pedido_id = p.id
JOIN agendamento a
  ON a.servico_id = s.id
 AND a.tipo = 'SERVICO'
JOIN status st_ag
  ON st_ag.id = a.status_id
JOIN status st_inativo
  ON st_inativo.tipo = 'PEDIDO'
 AND st_inativo.nome = 'INATIVO'
JOIN etapa e_concluido
  ON e_concluido.tipo = 'PEDIDO'
 AND e_concluido.nome IN ('CONCLUIDO', 'CONCLUÍDO')
SET p.status_id = st_inativo.id,
    p.ativo = FALSE,
    s.etapa_id = e_concluido.id,
    s.ativo = FALSE
WHERE st_ag.nome IN ('CONCLUIDO', 'CONCLUÍDO');
