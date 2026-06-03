-- 1. Criando o usuário (Primeiro Acesso)
INSERT INTO funcionarios (matricula, nome, departamento, funcao, pass) 
VALUES ('12345678', 'João da Silva', 'CTI', 'Técnico de TI', '12345678');

-- 2. Ajustes de Ponto (A nossa tabela tem AUTO_INCREMENT, então não precisa de ID aqui)
INSERT INTO frequencia_ajustes (matriculaFrequencia, dataEsquecida, horaSugerida, justificativa, status) 
VALUES ('05566880', '2026-05-25', '13:00:00', 'Esqueci de registrar a volta do almoço no CTI.', 'PENDENTE');

INSERT INTO frequencia_ajustes (matriculaFrequencia, dataEsquecida, horaSugerida, justificativa, status) 
VALUES ('12345678', '2026-05-26', '08:00:00', 'Leitor de QR Code reiniciando.', 'PENDENTE');

INSERT INTO frequencia_ajustes (matriculaFrequencia, dataEsquecida, horaSugerida, justificativa, status) 
VALUES ('98765432', '2026-05-26', '17:30:00', 'Manutenção externa.', 'PENDENTE');

-- 3. Batidas Físicas (A tabela legada aceita max 7 caracteres, então usamos HH:MM)
-- Dia 26: Joãozinho bateu os 4 horários (OK)
INSERT INTO frequencia (id, matriculaFrequencia, data, hora) VALUES ('1', '12345678', '2026-05-26', '08:00');
INSERT INTO frequencia (id, matriculaFrequencia, data, hora) VALUES ('2', '12345678', '2026-05-26', '12:00');
INSERT INTO frequencia (id, matriculaFrequencia, data, hora) VALUES ('3', '12345678', '2026-05-26', '13:30');
INSERT INTO frequencia (id, matriculaFrequencia, data, hora) VALUES ('4', '12345678', '2026-05-26', '17:30');

-- Dia 27: Joãozinho esqueceu a volta do almoço (Inconsistente)
INSERT INTO frequencia (id, matriculaFrequencia, data, hora) VALUES ('5', '12345678', '2026-05-27', '08:15');
INSERT INTO frequencia (id, matriculaFrequencia, data, hora) VALUES ('6', '12345678', '2026-05-27', '12:05');
INSERT INTO frequencia (id, matriculaFrequencia, data, hora) VALUES ('7', '12345678', '2026-05-27', '18:10');