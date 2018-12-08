-- ----------------------------------------------------------------
--  TABLE administrator
-- ----------------------------------------------------------------

CREATE TABLE bank.administrator
(
   `Adm_ssn`    CHAR(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
   `Bnum`       INT(11) NOT NULL,
   `Name`       VARCHAR(15)
                  CHARACTER SET utf8
                  COLLATE utf8_general_ci
                  NOT NULL,
   CONSTRAINT administrator_ibfk_1 FOREIGN KEY(`Bnum`)
      REFERENCES branch (`Branch_num`) ON UPDATE CASCADE ON DELETE CASCADE,
   PRIMARY KEY(`Adm_ssn`)
)
ENGINE INNODB
COLLATE 'utf8_general_ci'
ROW_FORMAT DEFAULT;


