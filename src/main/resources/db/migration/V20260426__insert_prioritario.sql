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

INSERT INTO status (tipo, nome) VALUES
                                    ('AGENDAMENTO', 'PENDENTE'),
                                    ('AGENDAMENTO', 'EM ANDAMENTO'),
                                    ('AGENDAMENTO', 'CONCLUÍDO'),
                                    ('AGENDAMENTO', 'CANCELADO');


INSERT INTO status (tipo, nome) VALUES
                                    ('PEDIDO', 'AGUARDANDO AGENDA DE ORÇAMENTO'),
                                    ('PEDIDO', 'ORÇAMENTO AGENDADO'),
                                    ('PEDIDO', 'ANÁLISE DO ORÇAMENTO'),
                                    ('PEDIDO', 'ORÇAMENTO APROVADO'),
                                    ('PEDIDO', 'AGUARDANDO AGENDA DE SERVIÇO/INSTALAÇÃO'),
                                    ('PEDIDO', 'SERVIÇO AGENDADO'),
                                    ('PEDIDO', 'AGENDAMENTO EM EXECUÇÃO'),
                                    ('PEDIDO', 'CONCLUÍDO');


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
