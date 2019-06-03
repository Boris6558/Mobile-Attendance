/*
Navicat MySQL Data Transfer

Source Server         : VPS
Source Server Version : 50548
Source Host           : 209.250.248.95:3306
Source Database       : Check_on_school

Target Server Type    : MYSQL
Target Server Version : 50548
File Encoding         : 65001

Date: 2019-03-20 17:02:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ask_for_leave
-- ----------------------------
DROP TABLE IF EXISTS `ask_for_leave`;
CREATE TABLE `ask_for_leave` (
  `stu_number` int(10) NOT NULL COMMENT '学号',
  `leave_classes_num` char(8) NOT NULL COMMENT '要请假的教学班',
  `leave_status` char(10) NOT NULL COMMENT '请假状态 通过和不通过 待审核',
  KEY `stu_number` (`stu_number`),
  KEY `leave_classes_num` (`leave_classes_num`),
  CONSTRAINT `ask_for_leave_ibfk_1` FOREIGN KEY (`stu_number`) REFERENCES `students` (`stu_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ask_for_leave_ibfk_2` FOREIGN KEY (`leave_classes_num`) REFERENCES `classes` (`classes_number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of ask_for_leave
-- ----------------------------

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes` (
  `classes_number` char(8) NOT NULL COMMENT '教學班編號',
  `course_code` char(8) NOT NULL COMMENT '課程代碼',
  `job_number` int(4) NOT NULL COMMENT '任課教師工號',
  `week` char(1) NOT NULL COMMENT '星期几',
  `schooltime` char(12) NOT NULL COMMENT '上課時間 如\r\n\r\n1-10:30  代表星期一十點半',
  `school_year_semester` char(12) NOT NULL COMMENT '學年學期 如2019-2020-1',
  `longitude` double(10,7) NOT NULL COMMENT '經度 记录当前可是的经纬度',
  `latitude` double(10,7) NOT NULL COMMENT '緯度',
  PRIMARY KEY (`classes_number`,`course_code`,`job_number`,`week`),
  KEY `course_code` (`course_code`),
  KEY `job_number` (`job_number`),
  KEY `school_year_semester` (`school_year_semester`),
  KEY `schooltime` (`schooltime`),
  KEY `longitude` (`longitude`) USING BTREE,
  KEY `latitude` (`latitude`) USING BTREE,
  CONSTRAINT `classes_ibfk_1` FOREIGN KEY (`course_code`) REFERENCES `course` (`course_code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `classes_ibfk_2` FOREIGN KEY (`job_number`) REFERENCES `teachers` (`job_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `classes_ibfk_3` FOREIGN KEY (`school_year_semester`) REFERENCES `school_year` (`school_year_semester`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of classes
-- ----------------------------
INSERT INTO `classes` VALUES ('AJL', 'NN0042', '1470', '4', '17:00-18:20', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('AJL02', 'NN0042', '1470', '5', '10:40-12:00', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BGQ', 'SH3002', '856', '2', '10:40-12:00', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BGQ01', 'SH3002', '856', '5', '15:30-16:50', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BGZ', 'SH3001', '1591', '2', '17:00-18:20', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BGZ01', 'SH3001', '1591', '2', '17:00-18:20', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BHR', 'SI3003', '1331', '1', '10:40-12:00', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BHR01', 'SI3003', '1331', '2', '10:40-12:00', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BMF', 'SW3015', '1712', '1', '15:30-16:50', '2018-2019-1', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BMF01', 'SW3015', '1712', '3', '10:40-12:00', '2018-2019-1', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BOU', 'SS3012', '1950', '3', '15:30-16:50', '2018-2019-1', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('BOU01', 'SS3012', '1950', '5', '13:20-14:40', '2018-2019-1', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('HG', 'SS3004', '978', '1', '09:00-10:20', '2018-2019-1', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('HG01', 'SS3004', '978', '2', '17:00-18:20', '2018-2019-1', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('IM', 'SS2031', '1953', '3', '17:00-18:20', '2018-2019-1', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('IM01', 'SS2031', '1953', '4', '17:00-18:20', '2018-2019-1', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('JH', 'SI4002', '978', '2', '09:00-10:20', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('JH', 'SI4002', '978', '3', '09:00-10:20', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('MI', 'SS3014', '1592', '4', '10:40-12:00', '2018-2019-2', '0.0000000', '0.0000000');
INSERT INTO `classes` VALUES ('MI01', 'SS3014', '1592', '5', '09:00-10:20', '2018-2019-2', '0.0000000', '0.0000000');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `course_code` char(8) NOT NULL COMMENT '課程代碼',
  `course_name` char(255) NOT NULL COMMENT '課程名稱',
  PRIMARY KEY (`course_code`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('CC0002', '计算机二级MS Office高级应用');
INSERT INTO `course` VALUES ('CC1005', 'C语言程序设计II');
INSERT INTO `course` VALUES ('CC2004', '计算机通信与网络');
INSERT INTO `course` VALUES ('CC2006', 'Python数据分析');
INSERT INTO `course` VALUES ('CC2101', '数据挖掘实战');
INSERT INTO `course` VALUES ('CD3005', 'Android应用软件开发');
INSERT INTO `course` VALUES ('CE3001', 'Linux操作系统应用');
INSERT INTO `course` VALUES ('CO1006', 'C++面向对象基础');
INSERT INTO `course` VALUES ('CW1002', '网页UI设计基础');
INSERT INTO `course` VALUES ('CW3007', '软件测试基础');
INSERT INTO `course` VALUES ('CW3015', 'JAVA SSH框架及应用开发');
INSERT INTO `course` VALUES ('CW3101', 'JAVA程序设计实训');
INSERT INTO `course` VALUES ('DD0012', '中国书法艺术');
INSERT INTO `course` VALUES ('DS2017', 'photoshop软件应用');
INSERT INTO `course` VALUES ('GA1016', '三维建模');
INSERT INTO `course` VALUES ('GE3006', '形式与政策III');
INSERT INTO `course` VALUES ('NN0042', '机器学习');
INSERT INTO `course` VALUES ('SH3001', '算法分析');
INSERT INTO `course` VALUES ('SH3002', '非结构化数据库入门');
INSERT INTO `course` VALUES ('SH3005', 'android高级编程');
INSERT INTO `course` VALUES ('SI2010', 'bootstrap前端设计与开发');
INSERT INTO `course` VALUES ('SI3003', '电子支付与网上银行');
INSERT INTO `course` VALUES ('SI3004', '商业银行管理');
INSERT INTO `course` VALUES ('SI3005', '金融电子商务');
INSERT INTO `course` VALUES ('SI4002', '信用评分模型技术与应用');
INSERT INTO `course` VALUES ('SS2031', '编程规范');
INSERT INTO `course` VALUES ('SS3004', '软件工程');
INSERT INTO `course` VALUES ('SS3005', 'XML企业级应用');
INSERT INTO `course` VALUES ('SS3012', 'oracle应用技术');
INSERT INTO `course` VALUES ('SS3014', '云计算基础与使用技术');
INSERT INTO `course` VALUES ('SW3015', '分布式计算系统');
INSERT INTO `course` VALUES ('SW3103', '网页设计项目');
INSERT INTO `course` VALUES ('ZX0021', '从爱因斯坦到霍金的宇宙');
INSERT INTO `course` VALUES ('ZX0052', '汽车之旅');

-- ----------------------------
-- Table structure for jingweidu
-- ----------------------------
DROP TABLE IF EXISTS `jingweidu`;
CREATE TABLE `jingweidu` (
  `jingdu` char(255) NOT NULL,
  `weidu` char(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of jingweidu
-- ----------------------------
INSERT INTO `jingweidu` VALUES ('113.496559', '23.448475');
INSERT INTO `jingweidu` VALUES ('0.0', '23.448608');
INSERT INTO `jingweidu` VALUES ('113.496489', '23.448631');
INSERT INTO `jingweidu` VALUES ('113.496304', '23.448672');

-- ----------------------------
-- Table structure for school_year
-- ----------------------------
DROP TABLE IF EXISTS `school_year`;
CREATE TABLE `school_year` (
  `number` int(11) NOT NULL AUTO_INCREMENT COMMENT '編號',
  `school_year_semester` char(12) NOT NULL COMMENT '學年學期 如2019-2020-1',
  PRIMARY KEY (`number`,`school_year_semester`),
  KEY `school_year_semester` (`school_year_semester`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of school_year
-- ----------------------------
INSERT INTO `school_year` VALUES ('1', '2018-2019-1');
INSERT INTO `school_year` VALUES ('2', '2018-2019-2');

-- ----------------------------
-- Table structure for schooltimetable
-- ----------------------------
DROP TABLE IF EXISTS `schooltimetable`;
CREATE TABLE `schooltimetable` (
  `week_1` int(2) NOT NULL COMMENT '星期幾',
  `mo_1_2` char(12) NOT NULL COMMENT '早上1-2節的上課時間',
  `mo_3-4` char(12) NOT NULL,
  `af_5_6` char(12) NOT NULL COMMENT '下午5-6節上課時間',
  `af_7_8` char(12) NOT NULL,
  `af_9_10` char(12) NOT NULL,
  `af_11_12` char(12) NOT NULL,
  `af_13_14` char(12) NOT NULL,
  `af_15_16` char(12) NOT NULL,
  PRIMARY KEY (`week_1`),
  KEY `week_1` (`week_1`,`mo_1_2`,`mo_3-4`,`af_5_6`,`af_7_8`,`af_9_10`,`af_11_12`,`af_13_14`,`af_15_16`),
  KEY `mo_1_2` (`mo_1_2`,`mo_3-4`,`af_5_6`,`af_7_8`,`af_9_10`,`af_11_12`,`af_13_14`,`af_15_16`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of schooltimetable
-- ----------------------------
INSERT INTO `schooltimetable` VALUES ('1', '09:00-10:20', '10:40-12:00', '12:30-13:50', '14:00-15:20', '15:30-16:50', '17:00-18:20', '19:00-20:20', '20:30-21:50');
INSERT INTO `schooltimetable` VALUES ('2', '09:00-10:20', '10:40-12:00', '12:30-13:50', '14:00-15:20', '15:30-16:50', '17:00-18:20', '19:00-20:20', '20:30-21:50');
INSERT INTO `schooltimetable` VALUES ('3', '09:00-10:20', '10:40-12:00', '12:30-13:50', '14:00-15:20', '15:30-16:50', '17:00-18:20', '19:00-20:20', '20:30-21:50');
INSERT INTO `schooltimetable` VALUES ('4', '09:00-10:20', '10:40-12:00', '12:30-13:50', '14:00-15:20', '15:30-16:50', '17:00-18:20', '19:00-20:20', '20:30-21:50');
INSERT INTO `schooltimetable` VALUES ('5', '09:00-10:20', '10:40-12:00', '13:20-14:40', '14:50-16:10', '16:20-17:40', '17:50-19:10', '19:20-20:40', '20:50-22:10');

-- ----------------------------
-- Table structure for stu_timetable
-- ----------------------------
DROP TABLE IF EXISTS `stu_timetable`;
CREATE TABLE `stu_timetable` (
  `stu_number` int(10) NOT NULL COMMENT '學號',
  `classes_number` char(8) NOT NULL COMMENT '所在的教學班  ',
  `sign_in_time` char(12) NOT NULL COMMENT '在這個教學班的簽到時間 如1-10:30  代表星期一十點半',
  `longitude` double(10,7) NOT NULL COMMENT '經度',
  `latitude` double(10,7) NOT NULL COMMENT '緯度',
  `school_year_semester` char(12) NOT NULL COMMENT '學年學期 如2019-2020-1',
  `classroom` char(10) NOT NULL COMMENT '教室',
  PRIMARY KEY (`stu_number`,`classes_number`,`classroom`),
  KEY `school_year_semester` (`school_year_semester`) USING BTREE,
  KEY `sign_in_time` (`sign_in_time`) USING BTREE,
  KEY `stunumber_classesnumber` (`stu_number`,`classes_number`) USING BTREE,
  KEY `stu_number` (`stu_number`) USING BTREE,
  KEY `classes_number` (`classes_number`) USING BTREE,
  CONSTRAINT `stu_timetable_ibfk_1` FOREIGN KEY (`stu_number`) REFERENCES `students` (`stu_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stu_timetable_ibfk_2` FOREIGN KEY (`classes_number`) REFERENCES `classes` (`classes_number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stu_timetable_ibfk_3` FOREIGN KEY (`school_year_semester`) REFERENCES `school_year` (`school_year_semester`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stu_timetable_ibfk_4` FOREIGN KEY (`sign_in_time`) REFERENCES `classes` (`schooltime`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=gb2312 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of stu_timetable
-- ----------------------------
INSERT INTO `stu_timetable` VALUES ('1640129111', 'AJL', '17:00-18:20', '113.4957180', '23.4488790', '2018-2019-2', 'U101');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'AJL02', '10:40-12:00', '0.0000000', '0.0000000', '2018-2019-2', 'S208');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'BGQ', '10:40-12:00', '0.0000000', '0.0000000', '2018-2019-2', 'A301');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'BGQ01', '15:30-16:50', '0.0000000', '0.0000000', '2018-2019-2', 'T303');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'BGZ', '17:00-18:20', '0.0000000', '0.0000000', '2018-2019-2', 'T502');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'BGZ01', '17:00-18:20', '0.0000000', '0.0000000', '2018-2019-2', 'U208');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'BHR', '10:40-12:00', '0.0000000', '0.0000000', '2018-2019-2', 'U405');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'BHR01', '10:40-12:00', '0.0000000', '0.0000000', '2018-2019-2', 'S2203');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'JH', '09:00-10:20', '0.0000000', '0.0000000', '2018-2019-2', 'T405');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'JH', '09:00-10:20', '0.0000000', '0.0000000', '2018-2019-2', 'U605');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'MI', '10:40-12:00', '0.0000000', '0.0000000', '2018-2019-2', 'C103');
INSERT INTO `stu_timetable` VALUES ('1640129111', 'MI01', '09:00-10:20', '0.0000000', '0.0000000', '2018-2019-2', 'S2603');

-- ----------------------------
-- Table structure for students
-- ----------------------------
DROP TABLE IF EXISTS `students`;
CREATE TABLE `students` (
  `stu_number` int(10) NOT NULL COMMENT '學號',
  `name` char(10) NOT NULL COMMENT '名字',
  `SIM` char(30) NOT NULL COMMENT 'SIM信息',
  PRIMARY KEY (`stu_number`),
  KEY `stu_number` (`stu_number`) USING BTREE,
  KEY `name` (`name`) USING BTREE,
  KEY `SIM` (`SIM`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of students
-- ----------------------------
INSERT INTO `students` VALUES ('1111111111', '张柏芝', '');
INSERT INTO `students` VALUES ('1640129111', '刘斌', '89860117851096109464');
INSERT INTO `students` VALUES ('1640129164', '李铭琪', '');
INSERT INTO `students` VALUES ('1640129222', '刘少奇', '');
INSERT INTO `students` VALUES ('1640129518', '郭沣瑶', '');
INSERT INTO `students` VALUES ('1640129555', '张三丰', '');
INSERT INTO `students` VALUES ('1640129666', '派大星', '');
INSERT INTO `students` VALUES ('1640129999', '孙中山', '');

-- ----------------------------
-- Table structure for teachers
-- ----------------------------
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE `teachers` (
  `job_number` int(4) NOT NULL COMMENT '工號',
  `name` char(10) NOT NULL COMMENT '名字',
  `phone` char(12) NOT NULL COMMENT '电话',
  PRIMARY KEY (`job_number`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312;

-- ----------------------------
-- Records of teachers
-- ----------------------------
INSERT INTO `teachers` VALUES ('379', '邓宁宁', '13926108070');
INSERT INTO `teachers` VALUES ('590', '聂常红', '15018760260');
INSERT INTO `teachers` VALUES ('768', '肖佳', '13926104135');
INSERT INTO `teachers` VALUES ('856', '陈宁穗', '13802906540');
INSERT INTO `teachers` VALUES ('978', '薛建民', '13926195653');
INSERT INTO `teachers` VALUES ('1139', '刘建新', '13928790381');
INSERT INTO `teachers` VALUES ('1331', '钟迅科', '13580300838');
INSERT INTO `teachers` VALUES ('1468', '蒋慧勇', '13926230196');
INSERT INTO `teachers` VALUES ('1470', '苏进胜', '13926120296');
INSERT INTO `teachers` VALUES ('1527', '查俊峰', '13925160896');
INSERT INTO `teachers` VALUES ('1591', '陈坚强', '13822157445');
INSERT INTO `teachers` VALUES ('1592', '潘正军', '13928748182');
INSERT INTO `teachers` VALUES ('1712', '陈立军', '15814809798');
INSERT INTO `teachers` VALUES ('1950', '高仑', '18819826025');
INSERT INTO `teachers` VALUES ('1953', '张莉娜', '13926188637');
