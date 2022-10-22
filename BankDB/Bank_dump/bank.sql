-- MySQL dump 10.17  Distrib 10.3.11-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: Bank
-- ------------------------------------------------------
-- Server version	10.3.11-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `Account_num` char(9) NOT NULL,
  `Assn` char(7) NOT NULL,
  `Ussn` char(7) NOT NULL,
  `Account_type` varchar(10) NOT NULL,
  `Created_date` date DEFAULT NULL,
  `Money` int(11) DEFAULT 0,
  PRIMARY KEY (`Account_num`),
  KEY `account_ibfk_1` (`Assn`),
  KEY `account_ibfk_2` (`Ussn`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`Assn`) REFERENCES `administrator` (`Adm_ssn`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `account_ibfk_2` FOREIGN KEY (`Ussn`) REFERENCES `user` (`Usr_ssn`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('187494896','1925723','1987465','Saving','2010-09-19',80000000),('350026695','1240273','1498756','Saving','2015-12-18',9000000),('401154137','3412406','1987465','Saving','2017-02-26',2000000),('402040615','3058274','3159846','Checking','2008-05-27',3000000),('433981826','3259672','1978654','Saving','2015-07-25',120000),('468224575','1250427','1978654','Checking','2014-01-16',20000000),('499370096','1203958','3548645','Checking','2012-04-23',450000),('615610355','3450182','1734234','Checking','2017-09-24',50000),('688224380','1925723','1798465','Saving','2013-11-20',3000000),('688435915','1246207','3512897','Checking','2016-02-22',250000),('737807670','1240273','1498756','Saving','2017-04-17',30000000),('806644214','1250427','3215948','Saving','2015-07-15',10000000),('868220435','1246207','1734234','Checking','2018-07-21',100000),('902357946','3461023','3598462','Checking','2007-07-14',5000000);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `Adm_ssn` char(7) NOT NULL,
  `Bnum` int(11) NOT NULL,
  `Name` varchar(15) NOT NULL,
  PRIMARY KEY (`Adm_ssn`),
  KEY `administrator_ibfk_1` (`Bnum`),
  CONSTRAINT `administrator_ibfk_1` FOREIGN KEY (`Bnum`) REFERENCES `branch` (`Branch_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES ('1203958',6,'Mason\r'),('1240273',9,'Noah\r'),('1246207',7,'Lucas\r'),('1250427',10,'Liam\r'),('1925723',8,'Logan\r'),('3058274',2,'Isabella\r'),('3259672',3,'Olivia\r'),('3412406',1,'Ava\r'),('3450182',4,'Emma\r'),('3461023',5,'Sophia\r');
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `branch` (
  `Branch_num` int(11) NOT NULL,
  `Branch_name` varchar(20) NOT NULL,
  `Address` varchar(30) NOT NULL,
  PRIMARY KEY (`Branch_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES (1,'Head','Seoul\r'),(2,'Incheon_branch','Incheon\r'),(3,'Busan_branch','Busan\r'),(4,'Ulsan_branch','Ulsan\r'),(5,'Daejeon_branch','Daejeon\r'),(6,'Gwangju_branch','Gwangju\r'),(7,'Pohang_branch','Pohang\r'),(8,'Dokdo_branch','Dokdo\r'),(9,'Jaejudo_branch','Jaejudo\r'),(10,'Daegu_branch','Daegu\r');
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card`
--

DROP TABLE IF EXISTS `card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `card` (
  `Card_num` char(10) NOT NULL,
  `Ussn` char(7) NOT NULL,
  `Valid_year` varchar(2) NOT NULL,
  `Valid_month` varchar(2) NOT NULL,
  `Card_type` varchar(10) NOT NULL,
  `Limits` int(11) DEFAULT NULL,
  PRIMARY KEY (`Card_num`),
  KEY `Ussn` (`Ussn`),
  CONSTRAINT `card_ibfk_1` FOREIGN KEY (`Ussn`) REFERENCES `user` (`Usr_ssn`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card` DISABLE KEYS */;
INSERT INTO `card` VALUES ('3442765003','1978654','24','3','Credit',40000000),('3768280369','1734234','23','7','Check',10000000),('7005517958','3159846','19','1','Credit',100000000),('8062169863','3598462','20','12','Check',3000000),('8371231154','1987465','22','5','Check',2000000);
/*!40000 ALTER TABLE `card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `loan` (
  `Loan_num` int(11) NOT NULL,
  `Ussn` char(7) NOT NULL,
  `Amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`Loan_num`,`Ussn`),
  KEY `loan_ibfk_1` (`Ussn`),
  CONSTRAINT `loan_ibfk_1` FOREIGN KEY (`Ussn`) REFERENCES `user` (`Usr_ssn`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loan`
--

LOCK TABLES `loan` WRITE;
/*!40000 ALTER TABLE `loan` DISABLE KEYS */;
INSERT INTO `loan` VALUES (1,'3512897',1000000),(2,'3215948',2000000),(3,'1498756',3000000),(4,'1798465',10000000),(5,'1734234',20000000);
/*!40000 ALTER TABLE `loan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `money`
--

DROP TABLE IF EXISTS `money`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `money` (
  `Number` int(11) NOT NULL,
  `Anum` char(9) NOT NULL,
  `Amount` int(11) DEFAULT NULL,
  `Money_type` varchar(11) NOT NULL,
  PRIMARY KEY (`Number`,`Anum`),
  KEY `money_ibfk_1` (`Anum`),
  CONSTRAINT `money_ibfk_1` FOREIGN KEY (`Anum`) REFERENCES `account` (`Account_num`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `money`
--

LOCK TABLES `money` WRITE;
/*!40000 ALTER TABLE `money` DISABLE KEYS */;
INSERT INTO `money` VALUES (1,'187494896',180000,'Remittance\r'),(1,'350026695',190000,'Deposit\r'),(1,'433981826',170000,'Deposit\r'),(1,'468224575',200000,'Deposit\r'),(1,'499370096',140000,'Withdraw\r'),(1,'615610355',100000,'Deposit\r'),(1,'688435915',120000,'Deposit\r'),(1,'737807670',130000,'Deposit\r'),(1,'806644214',230000,'Deposit\r'),(2,'187494896',210000,'Deposit\r'),(2,'350026695',220000,'Withdraw\r'),(2,'468224575',70000,'Withdraw\r'),(2,'499370096',90000,'Withdraw\r'),(2,'615610355',150000,'Deposit\r'),(2,'688435915',110000,'Deposit\r'),(2,'737807670',50000,'Withdraw\r'),(2,'806644214',60000,'Withdraw\r'),(3,'187494896',240000,'Withdraw\r'),(3,'350026695',80000,'Remittanced'),(3,'615610355',160000,'Remittance\r');
/*!40000 ALTER TABLE `money` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `Usr_ssn` char(7) NOT NULL,
  `Fname` varchar(15) NOT NULL,
  `Lname` varchar(15) NOT NULL,
  `Phone_num` varchar(11) DEFAULT NULL,
  `Sex` char(1) DEFAULT NULL,
  `Birth_date` date DEFAULT NULL,
  PRIMARY KEY (`Usr_ssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1498756','Jeong','Eunho','1074293712','M','1997-12-11'),('1734234','Ryu','Jibeom','1093976864','M','1998-07-15'),('1798465','Choi','Yechan','1034867302','M','1988-12-20'),('1978654','Kim','Jongwon','1023853049','M','1995-06-12'),('1987465','Song','Jeongkun','1053750382','M','1996-04-21'),('3159846','Park','Hyejeong','1095736291','F','1998-03-17'),('3215948','Nam','Hyebin','1088343829','F','1975-01-12'),('3512897','Heo','Eunjin','1092847563','F','1989-05-13'),('3548645','Jeong','Yerin','1029584620','F','1987-03-26'),('3598462','Hong','Yujin','1039586730','F','1998-04-30');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-12-10 18:21:53
