-- ----------------------------------------------------------------
--  TABLE loan
-- ----------------------------------------------------------------

CREATE TABLE bank.loan
(
   `Loan_num`    INT(11) NOT NULL,
   `Ussn`        CHAR(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
   `Amount`      INT(11) NULL DEFAULT NULL,
   CONSTRAINT loan_ibfk_1 FOREIGN KEY(`Ussn`)
      REFERENCES user (`Usr_ssn`) ON UPDATE CASCADE ON DELETE CASCADE,
   PRIMARY KEY(`Loan_num`, `Ussn`)
)
ENGINE INNODB
COLLATE 'utf8_general_ci'
ROW_FORMAT DEFAULT;


