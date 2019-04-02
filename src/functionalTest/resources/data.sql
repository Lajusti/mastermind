DELETE FROM ATTEMPT;
DELETE FROM GAME;

INSERT INTO GAME (ID, SECRET, MAX_ATTEMPTS) VALUES
    ('END_GAME', 'BLUE:RED:YELLOW:ORANGE', 2),
    ('NOT_ENDED_GAME', 'RED:YELLOW:BLUE:ORANGE', 2),
    ('NOT_ENDED_GAME_TO_SOLVE', 'RED:YELLOW:BLUE:ORANGE', 15),
    ('SOLVED_GAME', 'BLUE:BLUE:RED:RED', 10);

INSERT INTO ATTEMPT (ID, INPUT, FEEDBACK, GAME_ID) VALUES
    (1000, 'BLUE:YELLOW:PURPLE:GREEN', 'BLACK:WHITE', 'END_GAME'),
    (1001, 'BLUE:ORANGE:YELLOW:GREEN', 'BLACK:BLACK:WHITE', 'END_GAME'),
    (1002, 'BLUE:RED:BLUE:RED', 'BLACK:BLACK:WHITE:WHITE', 'SOLVED_GAME'),
    (1003, 'BLUE:BLUE:RED:RED', 'BLACK:BLACK:BLACK:BLACK', 'SOLVED_GAME');