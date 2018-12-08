-- ----------------------------------------------------------------
--  TABLE account
-- ----------------------------------------------------------------

CREATE TABLE bank.account
(
   `Account_num`     CHAR(9)
                       CHARACTER SET utf8
                       COLLATE utf8_general_ci
                       NOT NULL,
   `Assn`            CHAR(7)
                       CHARACTER SET utf8
                       COLLATE utf8_general_ci
                       NOT NULL,
   `Ussn`            CHAR(7)
                       CHARACTER SET utf8
                       COLLATE utf8_general_ci
                       NOT NULL,
   `Account_type`    VARCHAR(10)
                       CHARACTER SET utf8
                       COLLATE utf8_general_ci
                       NOT NULL,
   `Created_date`    DATE NULL DEFAULT NULL,
   `Money`           INT(11) NULL DEFAULT 0,
   PRIMARY KEY(`Account_num`),
   CONSTRAINT account_ibfk_2 FOREIGN KEY(`Ussn`)
      REFERENCES user (`Usr_ssn`) ON UPDATE CASCADE ON DELETE CASCADE,
   CONSTRAINT account_ibfk_1 FOREIGN KEY(`Assn`)
      REFERENCES administrator (`Adm_ssn`)
         ON UPDATE CASCADE
         ON DELETE CASCADE
)
ENGINE INNODB
COLLATE 'utf8_general_ci'
ROW_FORMAT DEFAULT;


