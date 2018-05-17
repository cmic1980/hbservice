-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.2.14-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 hb 的数据库结构
CREATE DATABASE IF NOT EXISTS `hb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `hb`;

-- 导出  表 hb.analysis_result 结构
CREATE TABLE IF NOT EXISTS `analysis_result` (
  `analysis_result_id` int(11) NOT NULL AUTO_INCREMENT,
  `analysis_time` datetime NOT NULL,
  `symbol` varchar(50) NOT NULL,
  `s_hour` int(11) NOT NULL,
  `t` float NOT NULL,
  `income` float NOT NULL,
  `scale` float NOT NULL,
  `current` bit(1) NOT NULL DEFAULT b'0',
  `price` float NOT NULL DEFAULT 0,
  PRIMARY KEY (`analysis_result_id`)
) ENGINE=InnoDB AUTO_INCREMENT=342 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 hb.order_item 结构
CREATE TABLE IF NOT EXISTS `order_item` (
  `order_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` float NOT NULL DEFAULT 0,
  `symbol` varchar(50) NOT NULL DEFAULT '0',
  `order_type` varchar(50) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT 0,
  `buy_price` float NOT NULL DEFAULT 0,
  `sell_price` float NOT NULL DEFAULT 0,
  `buy_time` datetime NOT NULL,
  `t` float NOT NULL,
  PRIMARY KEY (`order_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;