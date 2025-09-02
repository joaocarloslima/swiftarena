-- AULA 1
INSERT INTO lesson (id, number) VALUES (1, 1);
INSERT INTO lesson (id, number) VALUES (2, 2);
INSERT INTO lesson (id, number) VALUES (3, 3);
INSERT INTO lesson (id, number) VALUES (4, 4);
INSERT INTO lesson (id, number) VALUES (5, 5);

-- MISSÃO 1 (soma simples)
INSERT INTO mission (id, title, challenge, lesson_id, description_markdown) VALUES
    (1, 'Soma de Dois Números', false, 1,
     '# Soma de dois números

     Leia dois números inteiros do usuário e imprima a soma.

     ### Entrada
     Dois inteiros, um por linha.

     ### Saída
     A soma dos dois valores.
     ');

-- TESTES DA MISSÃO 1
INSERT INTO test_case (id, mission_id, input, expected_output) VALUES
                                                                   (1, 1, '3\n4', '7'),
                                                                   (2, 1, '10\n-5', '5'),
                                                                   (3, 1, '0\n0', '0');

-- MISSÃO CHALLENGE (desafio)
INSERT INTO mission (id, title, challenge, lesson_id, description_markdown) VALUES
    (2, 'Desafio: Produto de Três Números', true, 1,
     '# Desafio: Produto de três números

     Leia três números inteiros e imprima o produto entre eles.

     ### Entrada
     Três números inteiros, um por linha.

     ### Saída
     O produto dos três valores.
     ');

-- TESTES DO CHALLENGE
INSERT INTO test_case (id, mission_id, input, expected_output) VALUES
                                                                   (4, 2, '2\n3\n4', '24'),
                                                                   (5, 2, '1\n-1\n5', '-5'),
                                                                   (6, 2, '0\n7\n8', '0');