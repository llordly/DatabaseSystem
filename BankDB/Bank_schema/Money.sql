-- ----------------------------------------------------------------
--  TABLE money
-- ----------------------------------------------------------------

CREATE TABLE bank.money
(
   `Number`        INT(11) NOT NULL,
   `Anum`          CHAR(9) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
   `Amount`        INT(11) NULL DEFAULT NULL,
   `Money_type`    VARCHAR(11)
                     CHARACTER SET utf8
                     COLLATE utf8_general_ci
                     NOT NULL,
   CONSTRAINT money_ibfk_1 FOREIGN KEY(`Anum`)
      REFERENCES account (`Account_num`) ON UPDATE CASCADE ON DELETE CASCADE,
   PRIMARY KEY(`Number`, `Anum`)
)
ENGINE INNODB
COLLATE 'utf8_general_ci'
ROW_FORMAT DEFAULT;


