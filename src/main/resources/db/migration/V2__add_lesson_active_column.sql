ALTER TABLE lesson
    ADD active BOOLEAN;

UPDATE lesson
    SET active = FALSE
    WHERE active IS NULL;

ALTER TABLE lesson
    ALTER COLUMN active SET NOT NULL;