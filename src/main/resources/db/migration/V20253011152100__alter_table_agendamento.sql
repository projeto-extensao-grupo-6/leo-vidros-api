ALTER TABLE agendamento ADD COLUMN inicio_agendamento TIMESTAMP NOT NULL;
ALTER TABLE agendamento ADD COLUMN fim_agendamento TIMESTAMP NOT NULL;
ALTER TABLE agendamento MODIFY COLUMN data_agendamento DATE NOT NULL;