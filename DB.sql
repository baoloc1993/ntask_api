-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.9.4-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for ntask_api
CREATE DATABASE IF NOT EXISTS `ntask_api` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `ntask_api`;

-- Dumping structure for table ntask_api.authority
CREATE TABLE IF NOT EXISTS `authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jdeu5vgpb8k5ptsqhrvamuad2` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.authority: ~2 rows (approximately)
DELETE FROM `authority`;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `name`) VALUES
	(1, 'sysadmin', NULL, NULL, NULL, 'ROLE_ADMIN'),
	(2, 'sysadmin', NULL, NULL, NULL, 'ROLE_USER');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;

-- Dumping structure for table ntask_api.bookmark_event
CREATE TABLE IF NOT EXISTS `bookmark_event` (
  `user_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`event_id`),
  KEY `FKp3eyf5e2yn4q6s7bptjr2730j` (`event_id`),
  CONSTRAINT `FKb3l4mwa8lmg3xi7ufgrd74wcn` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKp3eyf5e2yn4q6s7bptjr2730j` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.bookmark_event: ~0 rows (approximately)
DELETE FROM `bookmark_event`;
/*!40000 ALTER TABLE `bookmark_event` DISABLE KEYS */;
INSERT INTO `bookmark_event` (`user_id`, `event_id`) VALUES
	(3, 8);
/*!40000 ALTER TABLE `bookmark_event` ENABLE KEYS */;

-- Dumping structure for table ntask_api.event
CREATE TABLE IF NOT EXISTS `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `end_at` datetime(6) DEFAULT NULL,
  `exp` text DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_at` datetime(6) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.event: ~4 rows (approximately)
DELETE FROM `event`;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `end_at`, `exp`, `name`, `start_at`, `status`) VALUES
	(8, 'kbitdatten@gmail.com', '2022-11-22 15:52:21.398929', 'kbitdatten@gmail.com', '2022-11-25 16:13:52.783120', '3', '2022-11-22 16:00:00.000000', NULL, '3', '2022-11-22 15:52:00.000000', 0),
	(15, 'kbitdatten@gmail.com', '2022-11-23 09:42:57.985301', 'kbitdatten@gmail.com', '2022-11-23 09:42:57.985301', 'giang sinh', '2022-11-25 17:00:00.000000', NULL, 'su kien giang sinh', '2022-11-23 17:00:00.000000', 0),
	(49, 'heymanbo99@gmail.com', '2022-12-06 07:57:00.176711', 'heymanbo99@gmail.com', '2022-12-06 07:57:00.176711', 'test', '2022-12-14 17:00:00.000000', NULL, 'test', '2022-12-06 07:56:00.000000', 0),
	(62, 'kbitdatten@gmail.com', '2022-12-09 10:22:03.911708', 'kbitdatten@gmail.com', '2022-12-09 10:22:03.911708', 'su kien giang sinh', '2022-12-11 17:00:00.000000', NULL, 'su kien giang sinh', '2022-12-08 17:00:00.000000', 0);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;

-- Dumping structure for table ntask_api.message
CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `content` text DEFAULT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `from_id` bigint(20) DEFAULT NULL,
  `room_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkn6rt8591aaepiuacwuggj556` (`from_id`),
  KEY `FKagwdojfjm745d1v2re0r6ha74` (`room_id`),
  CONSTRAINT `FKagwdojfjm745d1v2re0r6ha74` FOREIGN KEY (`room_id`) REFERENCES `event` (`id`),
  CONSTRAINT `FKkn6rt8591aaepiuacwuggj556` FOREIGN KEY (`from_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.message: ~0 rows (approximately)
DELETE FROM `message`;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;

-- Dumping structure for table ntask_api.notification_event
CREATE TABLE IF NOT EXISTS `notification_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `readed` bit(1) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `deleted_noti` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5vf8f9bdocxeeoui2ph451biy` (`event_id`),
  KEY `FKr6utaup8thwun160y26cxyx4e` (`user_id`),
  CONSTRAINT `FK5vf8f9bdocxeeoui2ph451biy` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`),
  CONSTRAINT `FKr6utaup8thwun160y26cxyx4e` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.notification_event: ~73 rows (approximately)
DELETE FROM `notification_event`;
/*!40000 ALTER TABLE `notification_event` DISABLE KEYS */;
INSERT INTO `notification_event` (`id`, `content`, `created_date`, `deleted`, `readed`, `type`, `event_id`, `user_id`, `deleted_noti`) VALUES
	(1, 'tạo event: 4 thành công', '2022-11-30 17:12:38.096000', b'1', b'1', 4, NULL, 3, b'1'),
	(7, 'xóa event: 4 thành công', '2022-11-30 17:33:45.603000', b'1', b'1', 6, NULL, 3, b'1'),
	(8, 'xóa event: 3 thành công', '2022-11-30 17:34:02.893000', b'1', b'1', 6, NULL, 3, b'1'),
	(9, 'tạo event: 5 thành công', '2022-11-30 17:36:36.506000', b'1', b'1', 4, NULL, 3, b'1'),
	(10, 'xóa event: 5 thành công', '2022-11-30 17:37:07.227000', b'1', b'1', 6, NULL, 3, b'1'),
	(11, 'tạo event: 6 thành công', '2022-11-30 17:38:35.188000', b'1', b'1', 4, NULL, 3, b'1'),
	(12, 'xóa event: 6 thành công', '2022-11-30 17:38:52.745000', b'1', b'1', 6, NULL, 3, b'1'),
	(13, 'tạo event: su kien test thành công', '2022-12-01 09:10:38.472000', b'1', b'1', 4, NULL, 3, b'1'),
	(14, 'xóa event: su kien test thành công', '2022-12-01 09:11:08.982000', b'1', b'1', 6, NULL, 3, b'1'),
	(15, 'tạo event: su kien test thành công', '2022-12-01 09:50:33.021000', b'1', b'1', 4, NULL, 3, b'1'),
	(16, 'tạo event: su kien test thành công', '2022-12-01 10:24:55.371000', b'1', b'1', 4, NULL, 3, b'1'),
	(17, 'xóa event: su kien test thành công', '2022-12-01 10:25:28.500000', b'1', b'1', 6, NULL, 3, b'1'),
	(18, 'tạo event: su kien test thành công', '2022-12-01 10:26:06.003000', b'1', b'1', 4, NULL, 3, b'1'),
	(19, 'tạo event: su kien test thành công', '2022-12-01 10:27:09.097000', b'1', b'1', 4, NULL, 3, b'1'),
	(20, 'xóa event: su kien test thành công', '2022-12-01 10:28:08.578000', b'1', b'1', 6, NULL, 3, b'1'),
	(21, 'tạo event: 2 thành công', '2022-12-01 10:39:25.231000', b'1', b'1', 4, NULL, 3, b'1'),
	(22, 'xóa event: 2 thành công', '2022-12-01 10:39:31.668000', b'1', b'1', 6, NULL, 3, b'1'),
	(23, 'tạo event: 3 thành công', '2022-12-01 10:39:37.609000', b'1', b'1', 4, NULL, 3, b'1'),
	(24, 'xóa event: 3 thành công', '2022-12-01 10:39:41.263000', b'1', b'1', 6, NULL, 3, b'1'),
	(26, 'Xóa event: su kien test thành công', '2022-12-02 15:06:13.201000', b'1', b'1', 6, NULL, 3, b'1'),
	(27, 'Xóa event: 3 thành công', '2022-12-02 15:06:20.850000', b'1', b'1', 6, NULL, 3, b'1'),
	(28, 'Xóa event: 2 thành công', '2022-12-02 15:06:24.174000', b'1', b'1', 6, NULL, 3, b'1'),
	(30, 'Tạo event: 1 thành công', '2022-12-02 15:16:33.134000', b'1', b'1', 4, NULL, 3, b'1'),
	(31, 'Tạo event: abc thành công', '2022-12-06 07:25:41.143000', b'1', b'1', 4, NULL, 3, b'1'),
	(32, 'Xóa event: abc thành công', '2022-12-06 07:26:51.273000', b'1', b'1', 6, NULL, 3, b'1'),
	(33, 'Tạo event: 3 thành công', '2022-12-06 07:27:02.879000', b'1', b'1', 4, NULL, 3, b'1'),
	(34, 'Xóa event: 3 thành công', '2022-12-06 07:29:06.808000', b'1', b'1', 6, NULL, 3, b'1'),
	(35, 'Tạo event: 3 thành công', '2022-12-06 07:29:11.602000', b'1', b'1', 4, NULL, 3, b'1'),
	(36, 'Xóa event: 3 thành công', '2022-12-06 07:31:11.924000', b'1', b'1', 6, NULL, 3, b'1'),
	(37, 'Tạo event: 3 thành công', '2022-12-06 07:31:20.936000', b'1', b'1', 4, NULL, 3, b'1'),
	(38, 'Xóa event: 3 thành công', '2022-12-06 07:33:43.826000', b'1', b'1', 6, NULL, 3, b'1'),
	(39, 'Tạo event: 3 thành công', '2022-12-06 07:34:11.403000', b'1', b'1', 4, NULL, 3, b'1'),
	(40, 'Tạo event: 4 thành công', '2022-12-06 07:46:14.976000', b'1', b'1', 4, NULL, 4, b'1'),
	(41, 'Tạo event: 5 thành công', '2022-12-06 07:46:37.778000', b'1', b'1', 4, NULL, 4, b'1'),
	(42, 'Tạo event: shhss thành công', '2022-12-06 07:51:38.519000', b'1', b'1', 4, NULL, 4, b'1'),
	(43, 'Tạo event: 3 thành công', '2022-12-06 07:52:09.629000', b'1', b'1', 4, NULL, 3, b'1'),
	(44, 'Tạo event: 4 thành công', '2022-12-06 07:52:21.034000', b'1', b'1', 4, NULL, 3, b'1'),
	(45, 'Tạo event: test thành công', '2022-12-06 07:57:00.185000', b'0', b'1', 4, NULL, 4, b'1'),
	(46, 'Tạo event: su kien test thành công', '2022-12-07 10:10:51.414000', b'1', b'1', 4, NULL, 3, b'1'),
	(47, 'Cập nhật event: su kien test thành công', '2022-12-07 17:24:07.887000', b'1', b'1', 5, NULL, 3, b'1'),
	(48, 'Tạo event: su kien giang sinh thành công', '2022-12-08 07:11:58.373000', b'1', b'1', 4, NULL, 3, b'1'),
	(49, 'Tạo event: su kien giang sinh thành công', '2022-12-09 03:28:01.718000', b'1', b'1', 4, NULL, 3, b'1'),
	(50, 'Xóa event: su kien giang sinh thành công', '2022-12-09 03:29:27.621000', b'1', b'1', 6, NULL, 3, b'1'),
	(51, 'Xóa event: su kien test thành công', '2022-12-09 03:29:32.185000', b'1', b'1', 6, NULL, 3, b'1'),
	(52, 'Xóa event: su kien test thành công', '2022-12-09 03:29:37.835000', b'1', b'1', 6, NULL, 3, b'1'),
	(53, 'Xóa event: su kien test thành công', '2022-12-09 03:29:41.135000', b'1', b'1', 6, NULL, 3, b'1'),
	(54, 'Xóa event: 1 thành công', '2022-12-09 03:29:45.820000', b'1', b'1', 6, NULL, 3, b'1'),
	(55, 'Xóa event: 3 thành công', '2022-12-09 03:29:48.591000', b'1', b'1', 6, NULL, 3, b'1'),
	(56, 'Xóa event: 3 thành công', '2022-12-09 03:29:52.655000', b'1', b'1', 6, NULL, 3, b'1'),
	(57, 'Xóa event: 4 thành công', '2022-12-09 03:29:55.749000', b'1', b'1', 6, NULL, 3, b'1'),
	(58, 'Tạo event: su kien giang sinh thành công', '2022-12-09 05:15:54.998000', b'1', b'1', 4, NULL, 3, b'1'),
	(59, 'Xóa event: su kien giang sinh thành công', '2022-12-09 06:59:42.649000', b'1', b'1', 6, NULL, 3, b'1'),
	(60, 'Tạo event: su kien giang sinh thành công', '2022-12-09 07:00:58.400000', b'1', b'1', 4, NULL, 3, b'1'),
	(61, 'Xóa event: su kien giang sinh thành công', '2022-12-09 07:16:05.240000', b'1', b'1', 6, NULL, 3, b'1'),
	(62, 'Tạo event: su kien giang sinh thành công', '2022-12-09 07:17:04.265000', b'1', b'1', 4, NULL, 3, b'0'),
	(63, 'Xóa event: su kien giang sinh thành công', '2022-12-09 07:56:30.802000', b'1', b'1', 6, NULL, 3, b'0'),
	(64, 'Tạo event: su kien giang sinh thành công', '2022-12-09 07:56:36.267000', b'1', b'1', 4, NULL, 3, b'1'),
	(65, 'Tạo event: su kien giang sinh thành công', '2022-12-09 07:59:17.338000', b'1', b'1', 4, NULL, 3, b'1'),
	(66, 'Xóa event: su kien giang sinh thành công', '2022-12-09 08:00:00.938000', b'1', b'1', 6, NULL, 3, b'1'),
	(67, 'Xóa event: su kien giang sinh thành công', '2022-12-09 08:00:04.732000', b'1', b'1', 6, NULL, 3, b'1'),
	(68, 'Tạo event: su kien giang sinh thành công', '2022-12-09 08:00:48.779000', b'1', b'1', 4, NULL, 3, b'1'),
	(69, 'Tạo event: su kien giang sinh thành công', '2022-12-09 08:00:51.144000', b'1', b'1', 4, NULL, 3, b'0'),
	(70, 'Tạo event: su kien giang sinh thành công', '2022-12-09 09:40:37.251000', b'1', b'1', 4, NULL, 3, b'1'),
	(71, 'Tạo event: su kien giang sinh thành công', '2022-12-09 10:08:37.822000', b'1', b'1', 4, NULL, 3, b'1'),
	(72, 'Tạo event: su kien giang sinh thành công', '2022-12-09 10:22:04.054000', b'0', b'1', 4, NULL, 3, b'1'),
	(73, 'Xóa event: su kien giang sinh thành công', '2022-12-09 16:46:06.453000', b'1', b'1', 6, NULL, 3, b'1'),
	(74, 'Xóa event: su kien giang sinh thành công', '2022-12-09 16:46:47.511000', b'1', b'1', 6, NULL, 3, b'1'),
	(75, 'Xóa event: su kien giang sinh thành công', '2022-12-09 16:48:05.831000', b'1', b'1', 6, NULL, 3, b'1'),
	(76, 'Xóa event: su kien giang sinh thành công', '2022-12-09 16:48:09.729000', b'1', b'0', 6, NULL, 3, b'0'),
	(77, 'Xóa event: su kien giang sinh thành công', '2022-12-09 16:48:15.069000', b'1', b'1', 6, NULL, 3, b'1'),
	(79, 'Xóa event: 4 thành công', '2022-12-10 10:32:19.920000', b'1', b'1', 6, NULL, 4, b'1'),
	(80, 'Xóa event: 5 thành công', '2022-12-10 10:32:28.101000', b'1', b'1', 6, NULL, 4, b'1'),
	(81, 'Xóa event: shhss thành công', '2022-12-10 10:32:32.151000', b'1', b'1', 6, NULL, 4, b'1');
/*!40000 ALTER TABLE `notification_event` ENABLE KEYS */;

-- Dumping structure for table ntask_api.notification_task
CREATE TABLE IF NOT EXISTS `notification_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `readed` bit(1) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `task` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `deleted_noti` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb695ihw0anrhc1nhtp7xmatie` (`task`),
  KEY `FKieom2iyuyhxd2h2kdjtlnwv3k` (`user_id`),
  CONSTRAINT `FKb695ihw0anrhc1nhtp7xmatie` FOREIGN KEY (`task`) REFERENCES `t_task` (`id`),
  CONSTRAINT `FKieom2iyuyhxd2h2kdjtlnwv3k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.notification_task: ~59 rows (approximately)
DELETE FROM `notification_task`;
/*!40000 ALTER TABLE `notification_task` DISABLE KEYS */;
INSERT INTO `notification_task` (`id`, `content`, `created_date`, `deleted`, `readed`, `type`, `task`, `user_id`, `deleted_noti`) VALUES
	(5, 'tạo task: 1 thành công', '2022-11-30 16:37:33.678000', b'1', b'1', 1, NULL, 3, b'1'),
	(6, 'cập nhật task: 1 thành công', '2022-11-30 16:42:33.755000', b'1', b'1', 2, NULL, 3, b'1'),
	(7, 'xóa task: 1 thành công', '2022-11-30 16:43:08.370000', b'1', b'1', 3, NULL, 3, b'1'),
	(8, 'tạo task: 2 thành công', '2022-11-30 16:44:01.828000', b'1', b'1', 1, NULL, 3, b'1'),
	(9, 'cập nhật task: cong viec 2 thành công', '2022-12-01 09:17:24.802000', b'1', b'1', 2, NULL, 3, b'1'),
	(10, 'Tạo task: 3 thành công', '2022-12-02 08:37:37.572000', b'1', b'1', 1, NULL, 3, b'1'),
	(11, 'Tạo task: 4 thành công', '2022-12-02 08:38:00.845000', b'1', b'1', 1, NULL, 3, b'1'),
	(12, 'Tạo task: 5 thành công', '2022-12-02 08:38:23.502000', b'1', b'1', 1, NULL, 3, b'1'),
	(13, 'Tạo task: 1 thành công', '2022-12-02 16:14:16.460000', b'1', b'1', 1, NULL, 3, b'1'),
	(14, 'Tạo task: 2 thành công', '2022-12-02 16:14:30.856000', b'1', b'1', 1, NULL, 3, b'1'),
	(15, 'Tạo task: 3 thành công', '2022-12-02 16:18:45.490000', b'1', b'1', 1, NULL, 3, b'1'),
	(16, 'Tạo task: 4 thành công', '2022-12-02 16:18:56.160000', b'1', b'1', 1, NULL, 3, b'1'),
	(17, 'Tạo task: 5 thành công', '2022-12-02 16:19:10.409000', b'1', b'1', 1, NULL, 3, b'1'),
	(18, 'Cập nhật task: 1 thành công', '2022-12-02 16:45:41.245000', b'1', b'1', 2, NULL, 3, b'1'),
	(19, 'Cập nhật task: 1 thành công', '2022-12-02 16:48:58.109000', b'1', b'1', 2, NULL, 3, b'1'),
	(20, 'Cập nhật task: 2 thành công', '2022-12-04 06:53:37.020000', b'1', b'1', 2, NULL, 3, b'1'),
	(21, 'Cập nhật task: 3 thành công', '2022-12-04 06:55:24.676000', b'1', b'1', 2, NULL, 3, b'1'),
	(22, 'Tạo task: ahgdjasgdjasgdasgdashdgadgasdgasdasdgasjdhdgasgd thành công', '2022-12-04 08:45:46.898000', b'1', b'1', 1, NULL, 3, b'1'),
	(23, 'Tạo task: cong viec 1 thành công', '2022-12-06 09:23:48.086000', b'1', b'1', 1, NULL, 3, b'1'),
	(24, 'Cập nhật task: cong viec 1 thành công', '2022-12-06 09:24:00.143000', b'1', b'1', 2, NULL, 3, b'1'),
	(25, 'Cập nhật task: cong viec 1 thành công', '2022-12-06 09:25:34.179000', b'1', b'1', 2, NULL, 3, b'1'),
	(26, 'Cập nhật task: cong viec 1 thành công', '2022-12-06 09:28:59.841000', b'1', b'1', 2, NULL, 3, b'1'),
	(27, 'Tạo task: a thành công', '2022-12-07 09:29:59.906000', b'0', b'1', 1, 41, 4, b'0'),
	(28, 'Cập nhật task: a thành công', '2022-12-07 09:30:09.978000', b'0', b'1', 2, NULL, 4, b'1'),
	(29, 'Cập nhật task: a thành công', '2022-12-07 09:30:24.309000', b'0', b'1', 2, NULL, 4, b'1'),
	(30, 'Cập nhật task: a thành công', '2022-12-07 09:30:38.532000', b'0', b'1', 2, NULL, 4, b'1'),
	(31, 'Cập nhật task: a thành công', '2022-12-07 09:30:44.528000', b'0', b'1', 2, NULL, 4, b'1'),
	(32, 'Cập nhật task: a thành công', '2022-12-07 09:30:52.334000', b'0', b'1', 2, NULL, 4, b'1'),
	(33, 'Cập nhật task: a thành công', '2022-12-07 09:30:59.243000', b'0', b'1', 2, NULL, 4, b'1'),
	(34, 'Tạo task: 1 thành công', '2022-12-07 17:24:19.774000', b'1', b'1', 1, NULL, 3, b'1'),
	(35, 'Cập nhật task: 1 thành công', '2022-12-07 17:24:22.974000', b'1', b'1', 2, NULL, 3, b'1'),
	(36, 'Tạo task: 2 thành công', '2022-12-07 17:24:33.845000', b'1', b'1', 1, NULL, 3, b'1'),
	(37, 'Tạo task: phat bieu khai mac thành công', '2022-12-08 07:18:54.037000', b'1', b'1', 1, NULL, 3, b'1'),
	(38, 'Tạo task: chuong trinh ca nhac thành công', '2022-12-08 07:24:55.919000', b'1', b'1', 1, NULL, 3, b'1'),
	(39, 'Tạo task: phat qua thành công', '2022-12-08 07:26:10.777000', b'1', b'1', 1, NULL, 3, b'1'),
	(40, 'Tạo task: choi tro choi thành công', '2022-12-08 07:27:07.256000', b'1', b'1', 1, NULL, 3, b'1'),
	(41, 'Tạo task: phat bieu be mac thành công', '2022-12-08 07:27:58.675000', b'1', b'1', 1, NULL, 3, b'1'),
	(42, 'Tạo task: MC phat bieu thành công', '2022-12-08 07:34:18.770000', b'1', b'1', 1, NULL, 3, b'1'),
	(43, 'Tạo task: boi ban thành công', '2022-12-08 07:36:14.187000', b'1', b'1', 1, NULL, 3, b'1'),
	(44, 'Tạo task: bung nuoc thành công', '2022-12-08 07:36:32.201000', b'1', b'1', 1, NULL, 3, b'1'),
	(45, 'Cập nhật task: phat bieu khai mac thành công', '2022-12-08 07:46:34.350000', b'1', b'1', 2, NULL, 3, b'1'),
	(46, 'Cập nhật task: phat bieu khai mac thành công', '2022-12-08 09:32:37.943000', b'1', b'1', 2, NULL, 3, b'1'),
	(47, 'Cập nhật task: phat bieu khai mac thành công', '2022-12-08 09:32:48.211000', b'1', b'1', 2, NULL, 3, b'1'),
	(48, 'Cập nhật task: phat bieu khai mac thành công', '2022-12-08 09:33:29.262000', b'1', b'1', 2, NULL, 3, b'1'),
	(49, 'Cập nhật task: phat bieu khai mac thành công', '2022-12-08 09:33:48.404000', b'1', b'1', 2, NULL, 3, b'1'),
	(50, 'Cập nhật task: chuong trinh ca nhac thành công', '2022-12-08 09:34:05.472000', b'1', b'1', 2, NULL, 3, b'1'),
	(51, 'Cập nhật task: chuong trinh ca nhac thành công', '2022-12-08 09:39:24.885000', b'1', b'1', 2, NULL, 3, b'1'),
	(52, 'Cập nhật task: phat bieu khai mac thành công', '2022-12-08 09:41:32.608000', b'1', b'1', 2, NULL, 3, b'1'),
	(53, 'Cập nhật task: phat qua thành công', '2022-12-08 09:41:42.039000', b'1', b'1', 2, NULL, 3, b'1'),
	(54, 'Cập nhật task: phat bieu be mac thành công', '2022-12-08 09:56:13.157000', b'1', b'1', 2, NULL, 3, b'1'),
	(55, 'Cập nhật task: phat bieu be mac thành công', '2022-12-08 10:34:21.539000', b'1', b'1', 2, NULL, 3, b'1'),
	(56, 'Tạo task: phat bieu be mac 2 thành công', '2022-12-08 14:57:18.921000', b'1', b'1', 1, NULL, 3, b'1'),
	(57, 'Cập nhật task: boi ban thành công', '2022-12-08 16:58:37.036000', b'1', b'1', 2, NULL, 3, b'1'),
	(58, 'Cập nhật task: bung nuoc thành công', '2022-12-08 17:04:01.374000', b'1', b'1', 2, NULL, 3, b'1'),
	(59, 'Cập nhật task: bung nuoc thành công', '2022-12-08 17:05:00.555000', b'1', b'1', 2, NULL, 3, b'1'),
	(60, 'Cập nhật task: bung nuoc thành công', '2022-12-08 17:19:15.938000', b'1', b'1', 2, NULL, 3, b'1'),
	(61, 'Cập nhật task: bung nuoc thành công', '2022-12-10 09:14:24.224000', b'0', b'1', 2, NULL, 4, b'1'),
	(62, 'Cập nhật task: boi ban thành công', '2022-12-11 07:04:17.594000', b'0', b'1', 2, NULL, 4, b'1'),
	(63, 'Cập nhật task: bung nuoc thành công', '2022-12-11 07:04:39.785000', b'0', b'0', 2, 51, 4, b'0');
/*!40000 ALTER TABLE `notification_task` ENABLE KEYS */;

-- Dumping structure for table ntask_api.t_task
CREATE TABLE IF NOT EXISTS `t_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `end_at` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_at` datetime(6) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `is_show` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK4fa7mheipuuei4gpiid1wsryt` (`event_id`),
  CONSTRAINT `FK4fa7mheipuuei4gpiid1wsryt` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.t_task: ~14 rows (approximately)
DELETE FROM `t_task`;
/*!40000 ALTER TABLE `t_task` DISABLE KEYS */;
INSERT INTO `t_task` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `end_at`, `name`, `start_at`, `status`, `event_id`, `is_show`) VALUES
	(15, 'kbitdatten@gmail.com', '2022-11-23 09:51:17.316767', 'kbitdatten@gmail.com', '2022-11-23 09:51:17.316767', ' cong viec 1', '2022-11-23 18:00:00.000000', 'cong viec 1', '2022-11-23 17:00:00.000000', 0, 15, b'0'),
	(16, 'kbitdatten@gmail.com', '2022-11-23 09:52:05.916112', 'kbitdatten@gmail.com', '2022-11-23 09:52:05.916112', 'cong viec 2', '2022-11-23 19:00:00.000000', 'cong viec 2', '2022-11-23 18:00:00.000000', 0, 15, b'0'),
	(19, 'kbitdatten@gmail.com', '2022-11-23 09:56:47.044225', 'kbitdatten@gmail.com', '2022-11-23 09:56:47.044225', 'cong viec 3', '2022-11-23 19:00:00.000000', 'cong viec 3', '2022-11-23 18:00:00.000000', 0, 15, b'0'),
	(20, 'kbitdatten@gmail.com', '2022-11-23 10:01:45.662410', 'kbitdatten@gmail.com', '2022-11-23 10:01:45.662410', '4\n', '2022-11-24 17:00:00.000000', '4', '2022-11-23 17:00:00.000000', 0, 15, b'0'),
	(41, 'heymanbo99@gmail.com', '2022-12-07 09:29:59.900492', 'heymanbo99@gmail.com', '2022-12-07 09:30:59.244614', 'a', '2022-12-07 17:00:00.000000', 'a', '2022-12-07 09:29:00.000000', 1, 49, NULL),
	(44, 'kbitdatten@gmail.com', '2022-12-08 07:18:54.031319', 'kbitdatten@gmail.com', '2022-12-09 10:22:04.062704', '1h30 phut', '2022-12-08 18:30:00.000000', 'phat bieu khai mac', '2022-12-08 18:00:00.000000', 2, 62, b'1'),
	(45, 'kbitdatten@gmail.com', '2022-12-08 07:24:55.916603', 'kbitdatten@gmail.com', '2022-12-09 10:22:04.063710', '1h', '2022-12-08 19:31:00.000000', 'chuong trinh ca nhac', '2022-12-08 18:30:00.000000', 2, 62, b'1'),
	(46, 'kbitdatten@gmail.com', '2022-12-08 07:26:10.774198', 'kbitdatten@gmail.com', '2022-12-09 10:22:04.063710', '30 phut', '2022-12-08 20:00:00.000000', 'phat qua', '2022-12-08 19:30:00.000000', 1, 62, b'1'),
	(47, 'kbitdatten@gmail.com', '2022-12-08 07:27:07.251809', 'kbitdatten@gmail.com', '2022-12-09 10:22:04.063710', '1h', '2022-12-08 21:00:00.000000', 'choi tro choi', '2022-12-08 20:00:00.000000', 0, 62, b'1'),
	(48, 'kbitdatten@gmail.com', '2022-12-08 07:27:58.669552', 'kbitdatten@gmail.com', '2022-12-09 10:22:04.063710', '1h', '2022-12-08 23:00:00.000000', 'phat bieu be mac', '2022-12-08 22:00:00.000000', 0, 62, b'1'),
	(49, 'kbitdatten@gmail.com', '2022-12-08 07:34:18.767694', 'kbitdatten@gmail.com', '2022-12-09 10:22:04.063710', '', '2022-12-08 23:00:00.000000', 'MC phat bieu', '2022-12-08 22:00:00.000000', 0, 62, b'0'),
	(50, 'kbitdatten@gmail.com', '2022-12-08 07:36:14.183790', 'heymanbo99@gmail.com', '2022-12-11 07:04:17.601777', '4h toi 6h', '2022-12-08 23:00:00.000000', 'boi ban', '2022-12-08 21:00:00.000000', 1, 62, b'0'),
	(51, 'kbitdatten@gmail.com', '2022-12-08 07:36:32.197567', 'heymanbo99@gmail.com', '2022-12-11 07:04:39.789135', '4h toi 6h', '2022-12-08 23:00:00.000000', 'bung nuoc', '2022-12-08 21:00:00.000000', 0, 62, b'0'),
	(52, 'kbitdatten@gmail.com', '2022-12-08 14:57:18.874717', 'kbitdatten@gmail.com', '2022-12-09 10:22:04.064710', '1h', '2022-12-09 00:00:00.000000', 'phat bieu be mac 2', '2022-12-08 23:00:00.000000', 0, 62, b'1');
/*!40000 ALTER TABLE `t_task` ENABLE KEYS */;

-- Dumping structure for table ntask_api.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `bio` varchar(255) DEFAULT NULL,
  `eligible_resetting_pw` bit(1) DEFAULT NULL,
  `email` varchar(254) DEFAULT NULL,
  `login` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password_hash` varchar(60) NOT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `notification_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ew1hvam8uwaknuaellwhqchhb` (`login`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.user: ~2 rows (approximately)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `activated`, `activation_key`, `avatar_url`, `bio`, `eligible_resetting_pw`, `email`, `login`, `name`, `password_hash`, `reset_key`, `notification_key`) VALUES
	(3, 'anonymousUser', '2022-11-09 11:57:46.713752', 'kbitdatten@gmail.com', '2022-12-09 03:27:47.348710', b'1', NULL, 'https://i.imgur.com/YOBb785.jpg', NULL, b'0', 'kbitdatten@gmail.com', 'kbitdatten@gmail.com', 'An Huynh admin', '$2a$10$i3tCnMfNPorxZKcF203XAuGKR6kFZb/QRbYyjSRNHaB8FiL6L59uS', NULL, 'd37blvueTKKAseLgQYFTZL:APA91bGLuM9V7QSZzKyOYiRI57SXYlx3bMj5Rt7zvcsT1tU5dPg64C2B1aM2ofai1QGve0MaTfAaFMgW6yupxg6StUbyZrxnp4EM_8yHaensE9TyNFZiQK2eMiFQ-j1Z1aYS4rdp5X6V'),
	(4, 'kbitdatten@gmail.com', '2022-11-09 13:12:56.411543', 'heymanbo99@gmail.com', '2022-12-11 07:00:52.099946', b'1', NULL, 'https://i.imgur.com/WivWKTz.jpg', NULL, b'0', 'heymanbo99@gmail.com', 'heymanbo99@gmail.com', 'An Huynhnns', '$2a$10$BD63L0WaIoX2uW24j0mZ3u1qZn/RtQFHVWyE29fl9mwURF9rp7p8i', NULL, 'd37blvueTKKAseLgQYFTZL:APA91bGLuM9V7QSZzKyOYiRI57SXYlx3bMj5Rt7zvcsT1tU5dPg64C2B1aM2ofai1QGve0MaTfAaFMgW6yupxg6StUbyZrxnp4EM_8yHaensE9TyNFZiQK2eMiFQ-j1Z1aYS4rdp5X6V');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- Dumping structure for table ntask_api.user_authority
CREATE TABLE IF NOT EXISTS `user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  CONSTRAINT `FKpqlsjpkybgos9w2svcri7j8xy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.user_authority: ~2 rows (approximately)
DELETE FROM `user_authority`;
/*!40000 ALTER TABLE `user_authority` DISABLE KEYS */;
INSERT INTO `user_authority` (`user_id`, `authority_name`) VALUES
	(3, 'ROLE_ADMIN'),
	(4, 'ROLE_ADMIN');
/*!40000 ALTER TABLE `user_authority` ENABLE KEYS */;

-- Dumping structure for table ntask_api.user_group
CREATE TABLE IF NOT EXISTS `user_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo4b747rfwdcbwu2y3ty89gh4x` (`event_id`),
  KEY `FK8a92gm313uoimxkb4ap5ieil3` (`role_id`),
  KEY `FK1c1dsw3q36679vaiqwvtv36a6` (`user_id`),
  CONSTRAINT `FK1c1dsw3q36679vaiqwvtv36a6` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK8a92gm313uoimxkb4ap5ieil3` FOREIGN KEY (`role_id`) REFERENCES `authority` (`id`),
  CONSTRAINT `FKo4b747rfwdcbwu2y3ty89gh4x` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.user_group: ~6 rows (approximately)
DELETE FROM `user_group`;
/*!40000 ALTER TABLE `user_group` DISABLE KEYS */;
INSERT INTO `user_group` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `event_id`, `role_id`, `user_id`) VALUES
	(27, 'heymanbo99@gmail.com', '2022-11-25 16:23:43.420338', 'heymanbo99@gmail.com', '2022-11-25 16:23:43.420338', 8, 1, 4),
	(90, 'heymanbo99@gmail.com', '2022-12-06 07:57:00.184088', 'heymanbo99@gmail.com', '2022-12-06 07:57:00.184088', 49, 1, 4),
	(107, 'heymanbo99@gmail.com', '2022-12-07 09:29:30.390579', 'heymanbo99@gmail.com', '2022-12-07 09:29:30.390579', 49, 2, 3),
	(152, 'kbitdatten@gmail.com', '2022-12-09 10:22:03.977963', 'kbitdatten@gmail.com', '2022-12-09 10:22:03.977963', 62, 1, 3),
	(153, 'kbitdatten@gmail.com', '2022-12-09 10:22:03.981152', 'kbitdatten@gmail.com', '2022-12-09 10:22:03.981152', 62, 2, 4),
	(154, 'kbitdatten@gmail.com', '2022-12-09 10:22:03.982177', 'kbitdatten@gmail.com', '2022-12-09 10:22:03.982177', 62, 1, 3);
/*!40000 ALTER TABLE `user_group` ENABLE KEYS */;

-- Dumping structure for table ntask_api.user_task
CREATE TABLE IF NOT EXISTS `user_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) NOT NULL,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `task_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4ru0lxiqdfh4d6jrk14po9n6v` (`task_id`),
  KEY `FKr2jik008e3jx6r1fal5e9aq1n` (`user_id`),
  CONSTRAINT `FK4ru0lxiqdfh4d6jrk14po9n6v` FOREIGN KEY (`task_id`) REFERENCES `t_task` (`id`),
  CONSTRAINT `FKr2jik008e3jx6r1fal5e9aq1n` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table ntask_api.user_task: ~3 rows (approximately)
DELETE FROM `user_task`;
/*!40000 ALTER TABLE `user_task` DISABLE KEYS */;
INSERT INTO `user_task` (`id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `task_id`, `user_id`) VALUES
	(47, 'heymanbo99@gmail.com', '2022-12-07 09:30:59.239615', 'heymanbo99@gmail.com', '2022-12-07 09:30:59.239615', 41, 3),
	(55, 'heymanbo99@gmail.com', '2022-12-11 07:04:17.573951', 'heymanbo99@gmail.com', '2022-12-11 07:04:17.573951', 50, 4),
	(56, 'heymanbo99@gmail.com', '2022-12-11 07:04:39.780117', 'heymanbo99@gmail.com', '2022-12-11 07:04:39.780117', 51, 4);
/*!40000 ALTER TABLE `user_task` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
