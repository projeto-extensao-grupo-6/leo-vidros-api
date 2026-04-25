-- =============================================================
-- Leo Vidros — Dados iniciais (seed)
-- Gerado em: 2026-04-25
-- Executar após schema.sql
-- =============================================================

INSERT INTO categoria (nome) VALUES
                                 ('INFO'),
                                 ('ERROR'),
                                 ('DEBUG'),
                                 ('WARNING'),
                                 ('SUCCESS'),
                                 ('FATAL');

INSERT INTO etapa (tipo, nome) VALUES
                                   ('PEDIDO', 'PENDENTE'),
                                   ('PEDIDO', 'AGUARDANDO ORÇAMENTO'),
                                   ('PEDIDO', 'ANÁLISE DO ORÇAMENTO'),
                                   ('PEDIDO', 'ORÇAMENTO APROVADO'),
                                   ('PEDIDO', 'SERVIÇO AGENDADO'),
                                   ('PEDIDO', 'SERVIÇO EM EXECUÇÃO'),
                                   ('PEDIDO', 'CONCLUÍDO'),
                                   ('PEDIDO', 'REAGENDAR');


INSERT INTO status (tipo, nome) VALUES
                                    ('AGENDAMENTO', 'PENDENTE'),
                                    ('AGENDAMENTO', 'EM ANDAMENTO'),
                                    ('AGENDAMENTO', 'CONCLUÍDO'),
                                    ('AGENDAMENTO', 'CANCELADO');


INSERT INTO status (tipo, nome) VALUES
                                    ('PEDIDO', 'ATIVO'),
                                    ('PEDIDO', 'EM ANDAMENTO'),
                                    ('PEDIDO', 'FINALIZADO'),
                                    ('PEDIDO', 'PENDENTE'),
                                    ('PEDIDO', 'CANCELADO');


INSERT INTO status (tipo, nome) VALUES
                                    ('SOLICITACAO', 'PENDENTE'),
                                    ('SOLICITACAO', 'ACEITO'),
                                    ('SOLICITACAO', 'RECUSADO');


INSERT INTO status (tipo, nome) VALUES
                                    ('ORCAMENTO', 'RASCUNHO'),
                                    ('ORCAMENTO', 'ENVIADO'),
                                    ('ORCAMENTO', 'EM ANALISE'),
                                    ('ORCAMENTO', 'APROVADO'),
                                    ('ORCAMENTO', 'RECUSADO'),
                                    ('ORCAMENTO', 'EXPIRADO');
