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
  `symbol` varchar(20) NOT NULL DEFAULT '0',
  `s_hour` int(11) NOT NULL DEFAULT 0,
  `t` float NOT NULL DEFAULT 0,
  `income` float NOT NULL DEFAULT 0,
  `scale` float NOT NULL DEFAULT 0,
  `current` bit(1) NOT NULL DEFAULT b'0',
  `price` float NOT NULL DEFAULT 0,
  `amount` float NOT NULL DEFAULT 0,
  `vol` float NOT NULL DEFAULT 0,
  `days` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`analysis_result_id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 hb.daily_trade 结构
CREATE TABLE IF NOT EXISTS `daily_trade` (
  `daily_trade_id` int(11) NOT NULL AUTO_INCREMENT,
  `balance` int(11) NOT NULL DEFAULT 0 COMMENT '买入数量',
  `symbol` varchar(20) NOT NULL,
  `exec_time` varchar(5) NOT NULL COMMENT '执行时间，格式为HH:mm',
  `t` float NOT NULL COMMENT '期望收益率百分数，比如期望收益率1.2%，这个值应该是1.2',
  `buy_order_id` int(11) DEFAULT NULL COMMENT '买单id，挂买单成功后更新到该字段',
  `buy_price` float DEFAULT NULL,
  `sell_order_id` int(11) DEFAULT NULL COMMENT '卖单id，挂卖单成功后更新到该字段',
  `amount` float DEFAULT NULL COMMENT '币种成交量',
  `vol` float DEFAULT NULL COMMENT 'ETH成交量',
  `status` int(11) NOT NULL COMMENT '当前状态',
  PRIMARY KEY (`daily_trade_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
-- 导出  表 hb.order_item 结构
CREATE TABLE `order_item` (
	`order_item_id` INT(11) NOT NULL AUTO_INCREMENT,
	`amount` FLOAT NOT NULL DEFAULT '0',
	`symbol` VARCHAR(20) NOT NULL DEFAULT '0',
	`order_type` VARCHAR(20) NOT NULL DEFAULT '0',
	`status` INT(11) NOT NULL DEFAULT '0',
	`buy_price` DECIMAL(16,8) NOT NULL DEFAULT '0',
	`sell_price` DECIMAL(16,8) NOT NULL DEFAULT '0',
	`buy_time` DATETIME NOT NULL,
	`t` FLOAT NOT NULL DEFAULT '0',
	`order_id` BIGINT(20) NOT NULL DEFAULT '0',
	PRIMARY KEY (`order_item_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5
;


-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
