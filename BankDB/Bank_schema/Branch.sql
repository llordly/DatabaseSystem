-- ----------------------------------------------------------------
--  TABLE branch
-- ----------------------------------------------------------------

CREATE TABLE bank.branch
(
   `Branch_num`     INT(11) NOT NULL,
   `Branch_name`    VARCHAR(20)
                      CHARACTER SET utf8
                      COLLATE utf8_general_ci
                      NOT NULL,
   `Address`        VARCHAR(30)
                      CHARACTER SET utf8
                      COLLATE utf8_general_ci
                      NOT NULL,
   PRIMARY KEY(`Branch_num`)
)
ENGINE INNODB
COLLATE 'utf8_general_ci'
ROW_FORMAT DEFAULT;


