DROP DATABASE collabtive;
CREATE DATABASE collabtive;

USE collabtive;


# Dump of table chat
# ------------------------------------------------------------

DROP TABLE IF EXISTS `chat`;

CREATE TABLE `chat` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `time` varchar(255) NOT NULL DEFAULT '',
  `ufrom` varchar(255) NOT NULL DEFAULT '',
  `ufrom_id` int(10) NOT NULL DEFAULT '0',
  `userto` varchar(255) NOT NULL DEFAULT '',
  `userto_id` int(10) NOT NULL DEFAULT '0',
  `text` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table company
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company`;

CREATE TABLE `company` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `email` varchar(255) NOT NULL DEFAULT '',
  `phone` varchar(255) NOT NULL DEFAULT '',
  `address1` varchar(255) NOT NULL DEFAULT '',
  `address2` varchar(255) NOT NULL DEFAULT '',
  `state` varchar(255) NOT NULL DEFAULT '',
  `country` varchar(255) NOT NULL DEFAULT '',
  `logo` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table company_assigned
# ------------------------------------------------------------

DROP TABLE IF EXISTS `company_assigned`;

CREATE TABLE `company_assigned` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `user` int(10) NOT NULL DEFAULT '0',
  `company` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `company` (`company`),
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table files
# ------------------------------------------------------------

DROP TABLE IF EXISTS `files`;

CREATE TABLE `files` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `desc` varchar(255) NOT NULL DEFAULT '',
  `project` int(10) NOT NULL DEFAULT '0',
  `milestone` int(10) NOT NULL DEFAULT '0',
  `user` int(10) NOT NULL DEFAULT '0',
  `tags` varchar(255) NOT NULL DEFAULT '',
  `added` varchar(255) NOT NULL DEFAULT '',
  `datei` varchar(255) NOT NULL DEFAULT '',
  `type` varchar(255) NOT NULL DEFAULT '',
  `title` varchar(255) NOT NULL DEFAULT '',
  `folder` int(10) NOT NULL,
  `visible` text NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `name` (`name`),
  KEY `datei` (`datei`),
  KEY `added` (`added`),
  KEY `project` (`project`),
  KEY `tags` (`tags`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table files_attached
# ------------------------------------------------------------

DROP TABLE IF EXISTS `files_attached`;

CREATE TABLE `files_attached` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `file` int(10) unsigned NOT NULL DEFAULT '0',
  `message` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `file` (`file`,`message`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `log`;

CREATE TABLE `log` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `user` int(10) NOT NULL DEFAULT '0',
  `username` varchar(255) NOT NULL DEFAULT '',
  `name` varchar(255) NOT NULL DEFAULT '',
  `type` varchar(255) NOT NULL DEFAULT '',
  `action` int(1) NOT NULL DEFAULT '0',
  `project` int(10) NOT NULL DEFAULT '0',
  `datum` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  KEY `datum` (`datum`),
  KEY `type` (`type`),
  KEY `action` (`action`),
  FULLTEXT KEY `username` (`username`),
  FULLTEXT KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



INSERT INTO `log` (`ID`, `user`, `username`, `name`, `type`, `action`, `project`, `datum`)
VALUES
  (1,0,'','admin','user',1,0,'1545668085'),
  (4,1,'admin','1','projekt',3,1,'1545669491'),
  (7,1,'admin','1','projekt',3,2,'1545669530'),
  (15,1,'admin','1','projekt',3,4,'1545669778'),
  (12,1,'admin','1','projekt',3,3,'1545669754'),
  (33,1,'admin','1','projekt',3,6,'1545672692'),
  (37,1,'admin','1','projekt',3,5,'1545724500');

# Dump of table messages
# ------------------------------------------------------------

DROP TABLE IF EXISTS `messages`;

CREATE TABLE `messages` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `project` int(10) NOT NULL DEFAULT '0',
  `title` varchar(255) NOT NULL DEFAULT '',
  `text` text NOT NULL,
  `tags` varchar(255) NOT NULL,
  `posted` varchar(255) NOT NULL DEFAULT '',
  `user` int(10) NOT NULL DEFAULT '0',
  `username` varchar(255) NOT NULL DEFAULT '',
  `replyto` int(11) NOT NULL DEFAULT '0',
  `milestone` int(10) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `project` (`project`),
  KEY `user` (`user`),
  KEY `replyto` (`replyto`),
  KEY `tags` (`tags`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table milestones
# ------------------------------------------------------------

DROP TABLE IF EXISTS `milestones`;

CREATE TABLE `milestones` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `project` int(10) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL DEFAULT '',
  `desc` text NOT NULL,
  `start` varchar(255) NOT NULL DEFAULT '',
  `end` varchar(255) NOT NULL DEFAULT '',
  `status` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `name` (`name`),
  KEY `end` (`end`),
  KEY `project` (`project`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table milestones_assigned
# ------------------------------------------------------------

DROP TABLE IF EXISTS `milestones_assigned`;

CREATE TABLE `milestones_assigned` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `user` int(10) NOT NULL DEFAULT '0',
  `milestone` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `user` (`user`),
  KEY `milestone` (`milestone`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table projectfolders
# ------------------------------------------------------------

DROP TABLE IF EXISTS `projectfolders`;

CREATE TABLE `projectfolders` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parent` int(10) unsigned NOT NULL DEFAULT '0',
  `project` int(11) NOT NULL DEFAULT '0',
  `name` text NOT NULL,
  `description` varchar(255) NOT NULL,
  `visible` text NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `project` (`project`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table projekte
# ------------------------------------------------------------

DROP TABLE IF EXISTS `projekte`;

CREATE TABLE `projekte` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `desc` text NOT NULL,
  `start` varchar(255) NOT NULL DEFAULT '',
  `end` varchar(255) NOT NULL DEFAULT '',
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `budget` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `status` (`status`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table projekte_assigned
# ------------------------------------------------------------

DROP TABLE IF EXISTS `projekte_assigned`;

CREATE TABLE `projekte_assigned` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `user` int(10) NOT NULL DEFAULT '0',
  `projekt` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `user` (`user`),
  KEY `projekt` (`projekt`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table roles
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roles`;

CREATE TABLE `roles` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `projects` text NOT NULL,
  `tasks` text NOT NULL,
  `milestones` text NOT NULL,
  `messages` text NOT NULL,
  `files` text NOT NULL,
  `chat` text NOT NULL,
  `timetracker` text NOT NULL,
  `admin` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



INSERT INTO `roles` (`ID`, `name`, `projects`, `tasks`, `milestones`, `messages`, `files`, `chat`, `timetracker`, `admin`)
VALUES
  (1,'Admin','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:4:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:4:\\\"read\\\";i:1;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:1;}'),
  (2,'User','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:5:\\\"close\\\";i:1;s:4:\\\"view\\\";i:1;}','a:4:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:1;}','a:5:{s:3:\\\"add\\\";i:1;s:4:\\\"edit\\\";i:1;s:3:\\\"del\\\";i:1;s:4:\\\"read\\\";i:0;s:4:\\\"view\\\";i:1;}','a:1:{s:3:\\\"add\\\";i:0;}'),
  (3,'Client','a:4:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;}','a:4:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;}','a:4:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;}','a:4:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:5:\\\"close\\\";i:0;}','a:3:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;}','a:1:{s:3:\\\"add\\\";i:0;}','a:4:{s:3:\\\"add\\\";i:0;s:4:\\\"edit\\\";i:0;s:3:\\\"del\\\";i:0;s:4:\\\"read\\\";i:0;}','a:1:{s:3:\\\"add\\\";i:0;}');


# Dump of table roles_assigned
# ------------------------------------------------------------

DROP TABLE IF EXISTS `roles_assigned`;

CREATE TABLE `roles_assigned` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `user` int(10) NOT NULL,
  `role` int(10) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


INSERT INTO `roles_assigned` (`ID`, `user`, `role`)
VALUES
  (1,1,1);



# Dump of table settings
# ------------------------------------------------------------

DROP TABLE IF EXISTS `settings`;

CREATE TABLE `settings` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `settingsKey` varchar(50) NOT NULL,
  `settingsValue` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


INSERT INTO `settings` (`ID`, `settingsKey`, `settingsValue`)
VALUES
  (1,'name','Collabtive'),
  (2,'subtitle','Projectmanagement'),
  (3,'locale','en'),
  (4,'timezone','UTC'),
  (5,'dateformat','d.m.Y'),
  (6,'template','standard'),
  (7,'mailnotify','1'),
  (8,'mailfrom','collabtive@localhost'),
  (9,'mailfromname',''),
  (10,'mailmethod','mail'),
  (11,'mailhost',''),
  (12,'mailuser',''),
  (13,'mailpass',''),
  (14,'rssuser',''),
  (15,'rsspass','');


# Dump of table tasklist
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tasklist`;

CREATE TABLE `tasklist` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `project` int(10) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL DEFAULT '',
  `desc` text NOT NULL,
  `start` varchar(255) NOT NULL DEFAULT '',
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `access` tinyint(4) NOT NULL DEFAULT '0',
  `milestone` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `status` (`status`),
  KEY `milestone` (`milestone`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table tasks
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `start` varchar(255) NOT NULL DEFAULT '',
  `end` varchar(255) NOT NULL DEFAULT '',
  `title` varchar(255) NOT NULL DEFAULT '',
  `text` text NOT NULL,
  `liste` int(10) NOT NULL DEFAULT '0',
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `project` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `liste` (`liste`),
  KEY `status` (`status`),
  KEY `end` (`end`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table tasks_assigned
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tasks_assigned`;

CREATE TABLE `tasks_assigned` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `user` int(10) NOT NULL DEFAULT '0',
  `task` int(10) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `user` (`user`),
  KEY `task` (`task`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table timetracker
# ------------------------------------------------------------

DROP TABLE IF EXISTS `timetracker`;

CREATE TABLE `timetracker` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `user` int(10) NOT NULL DEFAULT '0',
  `project` int(10) NOT NULL DEFAULT '0',
  `task` int(10) NOT NULL DEFAULT '0',
  `comment` text NOT NULL,
  `started` varchar(255) NOT NULL DEFAULT '',
  `ended` varchar(255) NOT NULL DEFAULT '',
  `hours` float NOT NULL DEFAULT '0',
  `pstatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `user` (`user`,`project`,`task`),
  KEY `started` (`started`),
  KEY `ended` (`ended`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  `email` varchar(255) DEFAULT '',
  `tel1` varchar(255) DEFAULT NULL,
  `tel2` varchar(255) DEFAULT NULL,
  `pass` varchar(255) DEFAULT '',
  `company` varchar(255) DEFAULT '',
  `lastlogin` varchar(255) DEFAULT '',
  `zip` varchar(10) DEFAULT NULL,
  `gender` char(1) DEFAULT '',
  `url` varchar(255) DEFAULT '',
  `adress` varchar(255) DEFAULT '',
  `adress2` varchar(255) DEFAULT '',
  `state` varchar(255) DEFAULT '',
  `country` varchar(255) DEFAULT '',
  `tags` varchar(255) DEFAULT '',
  `locale` varchar(6) DEFAULT '',
  `avatar` varchar(255) DEFAULT '',
  `rate` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `name` (`name`),
  KEY `pass` (`pass`),
  KEY `locale` (`locale`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO `user` (`ID`, `name`, `email`, `tel1`, `tel2`, `pass`, `company`, `lastlogin`, `zip`, `gender`, `url`, `adress`, `adress2`, `state`, `country`, `tags`, `locale`, `avatar`, `rate`)
VALUES
  (1,'admin','',NULL,NULL,'d033e22ae348aeb5660fc2140aec35850c4da997','0','1545724500',NULL,'','','','','','','','','','0');

