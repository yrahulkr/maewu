# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.5.62-0ubuntu0.14.04.1)
# Database: ppma
# Generation Time: 2018-11-22 08:35:23 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table Entry
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Entry`;

CREATE TABLE `Entry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `comment` text,
  `username` varchar(255) DEFAULT NULL,
  `encryptedPassword` blob,
  `viewCount` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table EntryHasTag
# ------------------------------------------------------------

DROP TABLE IF EXISTS `EntryHasTag`;

CREATE TABLE `EntryHasTag` (
  `entryId` int(11) NOT NULL,
  `tagId` int(11) NOT NULL,
  PRIMARY KEY (`entryId`,`tagId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table Setting
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Setting`;

CREATE TABLE `Setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Setting` WRITE;
/*!40000 ALTER TABLE `Setting` DISABLE KEYS */;

INSERT INTO `Setting` (`id`, `name`, `value`)
VALUES
	(1,'force_ssl','0'),
	(2,'recent_entries_widget_count','10'),
	(3,'recent_entries_widget_enabled','1'),
	(4,'recent_entries_widget_position','2'),
	(5,'most_viewed_entries_widget_count','10'),
	(6,'most_viewed_entries_widget_enabled','1'),
	(7,'most_viewed_entries_widget_position','1'),
	(8,'tag_cloud_widget_position','0'),
	(9,'pagination_page_size_entries','10'),
	(10,'pagination_page_size_tags','10');

/*!40000 ALTER TABLE `Setting` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Tag
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Tag`;

CREATE TABLE `Tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table User
# ------------------------------------------------------------

DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `encryptionKey` blob,
  `isAdmin` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;

INSERT INTO `User` (`id`, `username`, `password`, `salt`, `encryptionKey`, `isAdmin`)
VALUES
	(1,'admin','3f053eb5c1c0e16d0747abf52fc5bddd14166354','6696420919d122ad8602243424247550',X'F96970A1AEC8B58A64F80EA0891C7CAEFBC2F86F2569CA6302AEBDCF01E5891BC552ECC47C4766F0',1);

/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
