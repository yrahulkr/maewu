# Dump of table cl_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_category`;

CREATE TABLE `cl_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '',
  `code` varchar(12) NOT NULL DEFAULT '',
  `idParent` int(11) DEFAULT '0',
  `rank` int(11) NOT NULL DEFAULT '0',
  `visible` tinyint(1) NOT NULL DEFAULT '1',
  `canHaveCoursesChild` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_category` WRITE;
/*!40000 ALTER TABLE `cl_category` DISABLE KEYS */;

INSERT INTO `cl_category` (`id`, `name`, `code`, `idParent`, `rank`, `visible`, `canHaveCoursesChild`)
VALUES
	(0,'Root','ROOT',NULL,0,0,0),
	(2,'Sciences','SC',0,1,1,1),
	(3,'Economics','ECO',0,2,1,1),
	(4,'Humanities','HUMA',0,3,1,1);

/*!40000 ALTER TABLE `cl_category` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_class
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_class`;

CREATE TABLE `cl_class` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '',
  `class_parent_id` int(11) DEFAULT NULL,
  `class_level` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='classe_id, name, classe_parent_id, classe_level';



# Dump of table cl_config_file
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_config_file`;

CREATE TABLE `cl_config_file` (
  `config_code` varchar(30) NOT NULL DEFAULT '',
  `config_hash` varchar(40) NOT NULL DEFAULT '',
  PRIMARY KEY (`config_code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AVG_ROW_LENGTH=48;

LOCK TABLES `cl_config_file` WRITE;
/*!40000 ALTER TABLE `cl_config_file` DISABLE KEYS */;

INSERT INTO `cl_config_file` (`config_code`, `config_hash`)
VALUES
	('CLMAIN','240546454ad7182dad0a8d9f1bfc9c25'),
	('CLGRP','549184eaee7a64573c00aa5fc5cbd401'),
	('CLCAS','32d68966aa65fff5dc4ff51099498eb6'),
	('CLCRS','55e2e1fba299c4b22e888ff77ec1368e'),
	('CLAUTH','a42a6a9efaff435ee75c469b8eea050b'),
	('CLKCACHE','14c2e11ef130b1c4cf38cf0e196ec2ad'),
	('CLHOME','dbc9ae26faf1f8db266d591a7b6521f2'),
	('CLRSS','f021486de8b856fddf5ee22c79ae69fb'),
	('CLPROFIL','0d2dc935ab479bdf349fb271714d3d54'),
	('CLICAL','6fd8f96a3551363e72a7134b8174162f'),
	('CLMSG','91e1462bfa57f38ddbebda608ef0457a'),
	('CLSSO','8a65583515ba1a0545863553322d8446'),
	('CLDSC','b8a39eea7c997ab3ca84d76b6d32e745'),
	('CLANN','288429920da7eace08a3dfdfeb79517e'),
	('CLDOC','9aed4182dccf9dc95dbbdf7c6b6c9170'),
	('CLQWZ','73956fbb4f9a8945efde0d7e0d0ef8b9'),
	('CLLNP','8086e3f4d1b384b1068f41c2932f3752'),
	('CLWRK','bc1eef71a4f5271e7dd36bad87a1a443'),
	('CLFRM','256be3e852709a0f1be3a8c0b3dc8202'),
	('CLUSR','d5d9d3a0c6b57fc599d12b6571969f2b'),
	('CLWIKI','71640540af456551eabf44781df2568c'),
	('CLCHAT','6a04048b768abd553ee89feca0ba9127');

/*!40000 ALTER TABLE `cl_config_file` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_cours
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_cours`;

CREATE TABLE `cl_cours` (
  `cours_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(40) DEFAULT NULL,
  `isSourceCourse` tinyint(4) NOT NULL DEFAULT '0',
  `sourceCourseId` int(11) DEFAULT NULL,
  `administrativeNumber` varchar(40) DEFAULT NULL,
  `directory` varchar(20) DEFAULT NULL,
  `dbName` varchar(40) DEFAULT NULL,
  `language` varchar(15) DEFAULT NULL,
  `intitule` varchar(250) DEFAULT NULL,
  `titulaires` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `extLinkName` varchar(30) DEFAULT NULL,
  `extLinkUrl` varchar(180) DEFAULT NULL,
  `visibility` enum('visible','invisible') NOT NULL DEFAULT 'visible',
  `access` enum('public','private','platform') NOT NULL DEFAULT 'public',
  `registration` enum('open','close','validation') NOT NULL DEFAULT 'open',
  `registrationKey` varchar(255) DEFAULT NULL,
  `diskQuota` int(10) unsigned DEFAULT NULL,
  `versionDb` varchar(250) NOT NULL DEFAULT 'NEVER SET',
  `versionClaro` varchar(250) NOT NULL DEFAULT 'NEVER SET',
  `lastVisit` datetime DEFAULT NULL,
  `lastEdit` datetime DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `expirationDate` datetime DEFAULT NULL,
  `defaultProfileId` int(11) NOT NULL,
  `status` enum('enable','pending','disable','trash','date') NOT NULL DEFAULT 'enable',
  `userLimit` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cours_id`),
  KEY `administrativeNumber` (`administrativeNumber`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='data of courses';



# Dump of table cl_course_tool
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_course_tool`;

CREATE TABLE `cl_course_tool` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `claro_label` varchar(8) NOT NULL DEFAULT '',
  `script_url` varchar(255) NOT NULL DEFAULT '',
  `icon` varchar(255) DEFAULT NULL,
  `def_access` enum('ALL','COURSE_MEMBER','GROUP_MEMBER','GROUP_TUTOR','COURSE_ADMIN','PLATFORM_ADMIN') NOT NULL DEFAULT 'ALL',
  `def_rank` int(10) unsigned DEFAULT NULL,
  `add_in_course` enum('MANUAL','AUTOMATIC') NOT NULL DEFAULT 'AUTOMATIC',
  `access_manager` enum('PLATFORM_ADMIN','COURSE_ADMIN') NOT NULL DEFAULT 'COURSE_ADMIN',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claro_label` (`claro_label`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='based definiton of the claroline tool used in each course';

LOCK TABLES `cl_course_tool` WRITE;
/*!40000 ALTER TABLE `cl_course_tool` DISABLE KEYS */;

INSERT INTO `cl_course_tool` (`id`, `claro_label`, `script_url`, `icon`, `def_access`, `def_rank`, `add_in_course`, `access_manager`)
VALUES
	(1,'CLDSC','index.php','icon.png','ALL',1,'AUTOMATIC','COURSE_ADMIN'),
	(2,'CLCAL','agenda.php','icon.png','ALL',2,'AUTOMATIC','COURSE_ADMIN'),
	(3,'CLANN','announcements.php','icon.png','ALL',3,'AUTOMATIC','COURSE_ADMIN'),
	(4,'CLDOC','document.php','icon.png','ALL',4,'AUTOMATIC','COURSE_ADMIN'),
	(5,'CLQWZ','exercise.php','icon.png','ALL',5,'AUTOMATIC','COURSE_ADMIN'),
	(6,'CLLNP','learningPathList.php','icon.png','ALL',6,'AUTOMATIC','COURSE_ADMIN'),
	(7,'CLWRK','work.php','icon.png','ALL',7,'AUTOMATIC','COURSE_ADMIN'),
	(8,'CLFRM','index.php','icon.png','ALL',8,'AUTOMATIC','COURSE_ADMIN'),
	(9,'CLGRP','group.php','icon.png','ALL',9,'AUTOMATIC','COURSE_ADMIN'),
	(10,'CLUSR','user.php','icon.png','ALL',10,'AUTOMATIC','COURSE_ADMIN'),
	(11,'CLWIKI','wiki.php','icon.png','ALL',11,'AUTOMATIC','COURSE_ADMIN'),
	(12,'CLCHAT','index.php','icon.png','ALL',12,'AUTOMATIC','COURSE_ADMIN');

/*!40000 ALTER TABLE `cl_course_tool` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_coursehomepage_portlet
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_coursehomepage_portlet`;

CREATE TABLE `cl_coursehomepage_portlet` (
  `label` varchar(10) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`label`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_coursehomepage_portlet` WRITE;
/*!40000 ALTER TABLE `cl_coursehomepage_portlet` DISABLE KEYS */;

INSERT INTO `cl_coursehomepage_portlet` (`label`, `name`)
VALUES
	('CLTI','Headlines'),
	('CLCAL','Calendar'),
	('CLANN','Announcements');

/*!40000 ALTER TABLE `cl_coursehomepage_portlet` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_desktop_portlet
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_desktop_portlet`;

CREATE TABLE `cl_desktop_portlet` (
  `label` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `rank` int(11) NOT NULL,
  `visibility` enum('visible','invisible') NOT NULL DEFAULT 'visible',
  `activated` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`label`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_desktop_portlet_data
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_desktop_portlet_data`;

CREATE TABLE `cl_desktop_portlet_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(255) NOT NULL,
  `idUser` int(11) NOT NULL,
  `data` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_dock
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_dock`;

CREATE TABLE `cl_dock` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `module_id` smallint(5) unsigned NOT NULL DEFAULT '0',
  `name` varchar(50) NOT NULL DEFAULT '',
  `rank` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_event_resource
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_event_resource`;

CREATE TABLE `cl_event_resource` (
  `event_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  `tool_id` int(11) NOT NULL,
  `course_code` varchar(40) NOT NULL,
  PRIMARY KEY (`event_id`,`resource_id`,`tool_id`,`course_code`),
  UNIQUE KEY `event_id` (`event_id`,`course_code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_im_message
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_im_message`;

CREATE TABLE `cl_im_message` (
  `message_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sender` int(11) NOT NULL,
  `subject` varchar(100) NOT NULL,
  `message` text NOT NULL,
  `send_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `course` varchar(40) DEFAULT NULL,
  `group` int(11) DEFAULT NULL,
  `tools` char(8) DEFAULT NULL,
  PRIMARY KEY (`message_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_im_message_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_im_message_status`;

CREATE TABLE `cl_im_message_status` (
  `user_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `is_read` tinyint(4) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`,`message_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_im_recipient
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_im_recipient`;

CREATE TABLE `cl_im_recipient` (
  `message_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `sent_to` enum('toUser','toGroup','toCourse','toAll') NOT NULL,
  PRIMARY KEY (`message_id`,`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_log
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_log`;

CREATE TABLE `cl_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_code` varchar(40) DEFAULT NULL,
  `tool_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `ip` varchar(15) DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `type` varchar(60) NOT NULL DEFAULT '',
  `data` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_code`),
  KEY `user_log` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_log` WRITE;
/*!40000 ALTER TABLE `cl_log` DISABLE KEYS */;

INSERT INTO `cl_log` (`id`, `course_code`, `tool_id`, `user_id`, `ip`, `date`, `type`, `data`)
VALUES
	(1,NULL,NULL,1,'172.17.0.1','2018-09-27 15:59:52','PROFILE_UPDATE','a:1:{s:4:\"user\";i:1;}');

/*!40000 ALTER TABLE `cl_log` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_module
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_module`;

CREATE TABLE `cl_module` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `label` varchar(8) NOT NULL DEFAULT '',
  `name` varchar(100) NOT NULL DEFAULT '',
  `activation` enum('activated','desactivated') NOT NULL DEFAULT 'desactivated',
  `type` varchar(10) NOT NULL DEFAULT 'applet',
  `script_url` char(255) NOT NULL DEFAULT 'entry.php',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_module` WRITE;
/*!40000 ALTER TABLE `cl_module` DISABLE KEYS */;

INSERT INTO `cl_module` (`id`, `label`, `name`, `activation`, `type`, `script_url`)
VALUES
	(1,'CLDSC','Course description','activated','tool','index.php'),
	(2,'CLCAL','Agenda','activated','tool','agenda.php'),
	(3,'CLANN','Announcements','activated','tool','announcements.php'),
	(4,'CLDOC','Documents and Links','activated','tool','document.php'),
	(5,'CLQWZ','Exercises','activated','tool','exercise.php'),
	(6,'CLLNP','Learning path','activated','tool','learningPathList.php'),
	(7,'CLWRK','Assignments','activated','tool','work.php'),
	(8,'CLFRM','Forums','activated','tool','index.php'),
	(9,'CLGRP','Groups','activated','tool','group.php'),
	(10,'CLUSR','Users','activated','tool','user.php'),
	(11,'CLWIKI','Wiki','activated','tool','wiki.php'),
	(12,'CLCHAT','Chat','activated','tool','index.php');

/*!40000 ALTER TABLE `cl_module` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_module_contexts
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_module_contexts`;

CREATE TABLE `cl_module_contexts` (
  `module_id` int(10) unsigned NOT NULL,
  `context` varchar(60) NOT NULL DEFAULT 'course',
  PRIMARY KEY (`module_id`,`context`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_module_contexts` WRITE;
/*!40000 ALTER TABLE `cl_module_contexts` DISABLE KEYS */;

INSERT INTO `cl_module_contexts` (`module_id`, `context`)
VALUES
	(1,'course'),
	(2,'course'),
	(3,'course'),
	(4,'course'),
	(4,'group'),
	(5,'course'),
	(6,'course'),
	(7,'course'),
	(8,'course'),
	(8,'group'),
	(9,'course'),
	(10,'course'),
	(11,'course'),
	(11,'group'),
	(12,'course'),
	(12,'group');

/*!40000 ALTER TABLE `cl_module_contexts` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_module_info
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_module_info`;

CREATE TABLE `cl_module_info` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT,
  `module_id` smallint(6) NOT NULL DEFAULT '0',
  `version` varchar(10) NOT NULL DEFAULT '',
  `author` varchar(50) DEFAULT NULL,
  `author_email` varchar(100) DEFAULT NULL,
  `author_website` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `license` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_module_info` WRITE;
/*!40000 ALTER TABLE `cl_module_info` DISABLE KEYS */;

INSERT INTO `cl_module_info` (`id`, `module_id`, `version`, `author`, `author_email`, `author_website`, `description`, `website`, `license`)
VALUES
	(1,1,'1.9','Claro team','devteam@claroline.net','http://www.claroline.net/','\n  ','http://wiki.claroline.net/index.php/CLDSC','GPL'),
	(2,2,'3.0','Claro team','devteam@claroline.net','http://www.claroline.net/','\n  ','http://wiki.claroline.net/index.php/CLCAL','GPL'),
	(3,3,'3.0','Claro team','devteam@claroline.net','http://www.claroline.net/','\n  ','http://wiki.claroline.net/index.php/CLANN','GPL'),
	(4,4,'4.0','Claro team','devteam@claroline.net','http://www.claroline.net/','\n     This tool is an original tool of claroline\n     It\'s able to store and manage local ressoures like file, url.\n     Can  manage upload, zip, images, url, subdirectory\n     Ca edit html files\n  ','http://wiki.claroline.net/index.php/CLDOC','GPL'),
	(5,5,'1.8','Claro team','devteam@claroline.net','http://www.claroline.net/','\n  ','http://wiki.claroline.net/index.php/CLQWZ','GPL'),
	(6,6,'1.0','Claro team','devteam@claroline.net','http://www.claroline.net/','\n  ','http://www.claroline.net/wiki/CLLNP/','GPL'),
	(7,7,'1.8','Claro team','devteam@claroline.net','http://www.claroline.net/','\n  ','','GPL'),
	(8,8,'1.8','Claro team','devteam@claroline.net','http://www.claroline.net/','\n  ','http://wiki.claroline.net/index.php/CLFRM','GPL'),
	(9,9,'1.8','Claro team','devteam@claroline.net','http://www.claroline.net/','\n        This tool allows group-based activities and group management in Claroline\n    ','http://wiki.claroline.net/index.php/CLGRP','GPL'),
	(10,10,'4.0','Claro team','devteam@claroline.net','http://www.claroline.net/','\n  ','http://wiki.claroline.net/index.php/CLUSR','GPL'),
	(11,11,'2.0','Frederic Minne','zefredz@claroline.net','http://wiki.claroline.net/index.php/CLWIKI','\n     This is the original Wiki tool for the Claroline platform. It allows\n     online collaborative edition of web pages using a simplified Wiki\n     syntax based on Olivier Meunier\'s wiki2xhtml renderer from the\n     Dotclear blog project.\n  ','http://wiki.claroline.net/index.php/CLWIKI','GPL'),
	(12,12,'1.0','Sebastien Piraux','seb@claroline.net','http://www.claroline.net','\n    \n  ','','GPL');

/*!40000 ALTER TABLE `cl_module_info` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_notify
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_notify`;

CREATE TABLE `cl_notify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_code` varchar(40) NOT NULL DEFAULT '0',
  `tool_id` int(11) NOT NULL DEFAULT '0',
  `ressource_id` varchar(255) NOT NULL DEFAULT '0',
  `group_id` int(11) NOT NULL DEFAULT '0',
  `user_id` int(11) NOT NULL DEFAULT '0',
  `date` datetime DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_code`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_property_definition
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_property_definition`;

CREATE TABLE `cl_property_definition` (
  `propertyId` varchar(50) NOT NULL DEFAULT '',
  `contextScope` varchar(10) NOT NULL DEFAULT '',
  `label` varchar(50) NOT NULL DEFAULT '',
  `type` varchar(10) NOT NULL DEFAULT '',
  `defaultValue` varchar(255) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `required` tinyint(1) NOT NULL DEFAULT '0',
  `rank` int(10) unsigned NOT NULL DEFAULT '0',
  `acceptedValue` text NOT NULL,
  PRIMARY KEY (`contextScope`,`propertyId`),
  KEY `rank` (`rank`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_rel_class_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_rel_class_user`;

CREATE TABLE `cl_rel_class_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `class_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `class_id` (`class_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_rel_course_category
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_rel_course_category`;

CREATE TABLE `cl_rel_course_category` (
  `courseId` int(11) NOT NULL,
  `categoryId` int(11) NOT NULL,
  `rootCourse` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`courseId`,`categoryId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_rel_course_class
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_rel_course_class`;

CREATE TABLE `cl_rel_course_class` (
  `courseId` varchar(40) NOT NULL,
  `classId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`courseId`,`classId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_rel_course_portlet
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_rel_course_portlet`;

CREATE TABLE `cl_rel_course_portlet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `courseId` int(11) NOT NULL,
  `rank` int(11) NOT NULL,
  `label` varchar(255) NOT NULL,
  `visible` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `courseId` (`courseId`,`label`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_rel_course_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_rel_course_user`;

CREATE TABLE `cl_rel_course_user` (
  `code_cours` varchar(40) NOT NULL DEFAULT '0',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0',
  `profile_id` int(11) NOT NULL,
  `role` varchar(60) DEFAULT NULL,
  `team` int(11) NOT NULL DEFAULT '0',
  `tutor` int(11) NOT NULL DEFAULT '0',
  `count_user_enrol` int(11) NOT NULL DEFAULT '0',
  `count_class_enrol` int(11) NOT NULL DEFAULT '0',
  `isPending` tinyint(4) NOT NULL DEFAULT '0',
  `isCourseManager` tinyint(4) NOT NULL DEFAULT '0',
  `enrollment_date` datetime DEFAULT NULL,
  PRIMARY KEY (`code_cours`,`user_id`),
  KEY `isCourseManager` (`isCourseManager`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_right_action
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_right_action`;

CREATE TABLE `cl_right_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) DEFAULT '',
  `tool_id` int(11) DEFAULT NULL,
  `rank` int(11) DEFAULT '0',
  `type` enum('COURSE','PLATFORM') NOT NULL DEFAULT 'COURSE',
  PRIMARY KEY (`id`),
  KEY `tool_id` (`tool_id`),
  KEY `type` (`type`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_right_action` WRITE;
/*!40000 ALTER TABLE `cl_right_action` DISABLE KEYS */;

INSERT INTO `cl_right_action` (`id`, `name`, `description`, `tool_id`, `rank`, `type`)
VALUES
	(1,'read','',1,0,'COURSE'),
	(2,'edit','',1,0,'COURSE'),
	(3,'read','',2,0,'COURSE'),
	(4,'edit','',2,0,'COURSE'),
	(5,'read','',3,0,'COURSE'),
	(6,'edit','',3,0,'COURSE'),
	(7,'read','',4,0,'COURSE'),
	(8,'edit','',4,0,'COURSE'),
	(9,'read','',5,0,'COURSE'),
	(10,'edit','',5,0,'COURSE'),
	(11,'read','',6,0,'COURSE'),
	(12,'edit','',6,0,'COURSE'),
	(13,'read','',7,0,'COURSE'),
	(14,'edit','',7,0,'COURSE'),
	(15,'read','',8,0,'COURSE'),
	(16,'edit','',8,0,'COURSE'),
	(17,'read','',9,0,'COURSE'),
	(18,'edit','',9,0,'COURSE'),
	(19,'read','',10,0,'COURSE'),
	(20,'edit','',10,0,'COURSE'),
	(21,'read','',11,0,'COURSE'),
	(22,'edit','',11,0,'COURSE'),
	(23,'read','',12,0,'COURSE'),
	(24,'edit','',12,0,'COURSE');

/*!40000 ALTER TABLE `cl_right_action` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_right_profile
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_right_profile`;

CREATE TABLE `cl_right_profile` (
  `profile_id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('COURSE','PLATFORM') NOT NULL DEFAULT 'COURSE',
  `name` varchar(255) NOT NULL DEFAULT '',
  `label` varchar(50) NOT NULL DEFAULT '',
  `description` varchar(255) DEFAULT '',
  `courseManager` tinyint(4) DEFAULT '0',
  `mailingList` tinyint(4) DEFAULT '0',
  `userlistPublic` tinyint(4) DEFAULT '0',
  `groupTutor` tinyint(4) DEFAULT '0',
  `locked` tinyint(4) DEFAULT '0',
  `required` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`profile_id`),
  KEY `type` (`type`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_right_profile` WRITE;
/*!40000 ALTER TABLE `cl_right_profile` DISABLE KEYS */;

INSERT INTO `cl_right_profile` (`profile_id`, `type`, `name`, `label`, `description`, `courseManager`, `mailingList`, `userlistPublic`, `groupTutor`, `locked`, `required`)
VALUES
	(1,'COURSE','Anonymous','anonymous','Course visitor (the user has no account on the platform)',0,1,1,0,0,1),
	(2,'COURSE','Guest','guest','Course visitor (the user has an account on the platform, but is not enrolled in the course)',0,1,1,0,0,1),
	(3,'COURSE','User','user','Course member (the user is actually enrolled in the course)',0,1,1,0,0,1),
	(4,'COURSE','Manager','manager','Course Administrator',1,1,1,0,1,1);

/*!40000 ALTER TABLE `cl_right_profile` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_right_rel_profile_action
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_right_rel_profile_action`;

CREATE TABLE `cl_right_rel_profile_action` (
  `profile_id` int(11) NOT NULL,
  `action_id` int(11) NOT NULL,
  `courseId` varchar(40) NOT NULL DEFAULT '',
  `value` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`profile_id`,`action_id`,`courseId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_right_rel_profile_action` WRITE;
/*!40000 ALTER TABLE `cl_right_rel_profile_action` DISABLE KEYS */;

INSERT INTO `cl_right_rel_profile_action` (`profile_id`, `action_id`, `courseId`, `value`)
VALUES
	(1,24,'',0),
	(2,24,'',0),
	(3,24,'',0),
	(4,24,'',1),
	(1,23,'',1),
	(2,23,'',1),
	(3,23,'',1),
	(4,23,'',1),
	(1,21,'',1),
	(1,22,'',0),
	(2,21,'',1),
	(2,22,'',0),
	(3,21,'',1),
	(3,22,'',0),
	(4,21,'',1),
	(1,20,'',0),
	(1,19,'',1),
	(1,18,'',0),
	(2,20,'',0),
	(2,19,'',1),
	(3,20,'',0),
	(3,19,'',1),
	(4,22,'',1),
	(1,17,'',1),
	(1,16,'',0),
	(1,15,'',1),
	(2,18,'',0),
	(2,17,'',1),
	(3,18,'',0),
	(3,17,'',1),
	(3,16,'',0),
	(4,20,'',1),
	(4,19,'',1),
	(4,18,'',1),
	(4,17,'',1),
	(1,14,'',0),
	(1,13,'',1),
	(2,16,'',0),
	(2,15,'',1),
	(2,14,'',0),
	(3,15,'',1),
	(3,14,'',0),
	(3,13,'',1),
	(4,16,'',1),
	(4,15,'',1),
	(4,14,'',1),
	(4,13,'',1),
	(1,12,'',0),
	(1,11,'',1),
	(2,13,'',1),
	(2,12,'',0),
	(2,11,'',1),
	(3,12,'',0),
	(3,11,'',1),
	(4,12,'',1),
	(4,11,'',1),
	(1,10,'',0),
	(1,9,'',1),
	(2,10,'',0),
	(2,9,'',1),
	(3,10,'',0),
	(3,9,'',1),
	(4,10,'',1),
	(4,9,'',1),
	(1,8,'',0),
	(1,7,'',1),
	(2,8,'',0),
	(2,7,'',1),
	(3,8,'',0),
	(3,7,'',1),
	(4,8,'',1),
	(4,7,'',1),
	(1,6,'',0),
	(1,5,'',1),
	(2,6,'',0),
	(2,5,'',1),
	(3,6,'',0),
	(3,5,'',1),
	(4,6,'',1),
	(4,5,'',1),
	(1,4,'',0),
	(1,3,'',1),
	(2,4,'',0),
	(2,3,'',1),
	(3,4,'',0),
	(3,3,'',1),
	(4,4,'',1),
	(4,3,'',1),
	(1,2,'',0),
	(1,1,'',1),
	(2,2,'',0),
	(2,1,'',1),
	(3,2,'',0),
	(3,1,'',1),
	(4,2,'',1),
	(4,1,'',1);

/*!40000 ALTER TABLE `cl_right_rel_profile_action` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_sso
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_sso`;

CREATE TABLE `cl_sso` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cookie` varchar(255) NOT NULL DEFAULT '',
  `rec_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `user_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_tracking_event
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_tracking_event`;

CREATE TABLE `cl_tracking_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_code` varchar(40) DEFAULT NULL,
  `tool_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `type` varchar(60) NOT NULL DEFAULT '',
  `data` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_code`),
  KEY `user_tracking` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_upgrade_status
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_upgrade_status`;

CREATE TABLE `cl_upgrade_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cid` varchar(40) NOT NULL,
  `claro_label` varchar(8) DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table cl_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_user`;

CREATE TABLE `cl_user` (
  `user_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `nom` varchar(60) DEFAULT NULL,
  `prenom` varchar(60) DEFAULT NULL,
  `username` varchar(255) DEFAULT 'empty',
  `password` varchar(50) DEFAULT 'empty',
  `language` varchar(15) DEFAULT NULL,
  `authSource` varchar(50) DEFAULT 'claroline',
  `email` varchar(255) DEFAULT NULL,
  `officialCode` varchar(255) DEFAULT NULL,
  `officialEmail` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(30) DEFAULT NULL,
  `pictureUri` varchar(250) DEFAULT NULL,
  `creatorId` int(11) unsigned DEFAULT NULL,
  `isPlatformAdmin` tinyint(4) DEFAULT '0',
  `isCourseCreator` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `loginpass` (`username`,`password`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_user` WRITE;
/*!40000 ALTER TABLE `cl_user` DISABLE KEYS */;

INSERT INTO `cl_user` (`user_id`, `nom`, `prenom`, `username`, `password`, `language`, `authSource`, `email`, `officialCode`, `officialEmail`, `phoneNumber`, `pictureUri`, `creatorId`, `isPlatformAdmin`, `isCourseCreator`)
VALUES
	(1,'Doe','John','admin','admin','','claroline','jdoe@mydomain.net','','','','',1,1,0);

/*!40000 ALTER TABLE `cl_user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table cl_user_property
# ------------------------------------------------------------

DROP TABLE IF EXISTS `cl_user_property`;

CREATE TABLE `cl_user_property` (
  `userId` int(10) unsigned NOT NULL DEFAULT '0',
  `propertyId` varchar(255) NOT NULL DEFAULT '',
  `propertyValue` varchar(255) NOT NULL DEFAULT '',
  `scope` varchar(45) NOT NULL DEFAULT '',
  PRIMARY KEY (`scope`,`propertyId`,`userId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

LOCK TABLES `cl_user_property` WRITE;
/*!40000 ALTER TABLE `cl_user_property` DISABLE KEYS */;

INSERT INTO `cl_user_property` (`userId`, `propertyId`, `propertyValue`, `scope`)
VALUES
	(1,'skype','','');

/*!40000 ALTER TABLE `cl_user_property` ENABLE KEYS */;
UNLOCK TABLES;