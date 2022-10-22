-- ----------------------------------------------------------------
--  TABLE user
-- ----------------------------------------------------------------

CREATE TABLE bank.user
(
   `Usr_ssn`       CHAR(7) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
   `Fname`         VARCHAR(15)
                     CHARACTER SET utf8
                     COLLATE utf8_general_ci
                     NOT NULL,
   `Lname`         VARCHAR(15)
                     CHARACTER SET utf8
                     COLLATE utf8_general_ci
                     NOT NULL,
   `Phone_num`     VARCHAR(11)
                     CHARACTER SET utf8
                     COLLATE utf8_general_ci
                     NULL
                     DEFAULT NULL,
   `Sex`           CHAR(1)
                     CHARACTER SET utf8
                     COLLATE utf8_general_ci
                     NULL
                     DEFAULT NULL,
   `Birth_date`    DATE NULL DEFAULT NULL,
   PRIMARY KEY(`Usr_ssn`)
)
ENGINE INNODB
COLLATE 'utf8_general_ci'
ROW_FORMAT DEFAULT;


