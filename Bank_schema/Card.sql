-- ----------------------------------------------------------------
--  TABLE card
-- ----------------------------------------------------------------

CREATE TABLE bank.card
(
   `Card_num`       CHAR(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
   `Ussn`           CHAR(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
   `Valid_year`     VARCHAR(2)
                      CHARACTER SET utf8
                      COLLATE utf8_general_ci
                      NOT NULL,
   `Valid_month`    VARCHAR(2)
                      CHARACTER SET utf8
                      COLLATE utf8_general_ci
                      NOT NULL,
   `Card_type`      VARCHAR(10)
                      CHARACTER SET utf8
                      COLLATE utf8_general_ci
                      NOT NULL,
   `Limits`         INT(11) NULL DEFAULT NULL,
   PRIMARY KEY(`Card_num`),
   CONSTRAINT card_ibfk_1 FOREIGN KEY(`Ussn`)
      REFERENCES user (`Usr_ssn`) ON UPDATE CASCADE ON DELETE CASCADE
)
ENGINE INNODB
COLLATE 'utf8_general_ci'
ROW_FORMAT DEFAULT;


