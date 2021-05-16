[MYSQL优化 笔记 较全](http://jjssobk.gitee.io/my-web/2021/01/24/mysql优化/)

[MYSQL优化 笔记2 较全](https://github.com/RingoTangs/LearningNote/blob/master/MySQL/MySQL.md)

[MySQL之索引](https://blog.csdn.net/single_0910/article/details/113828569)

[MySQL之EXPLAIN](https://blog.csdn.net/single_0910/article/details/113836467)

[MySQL之优化案例](https://blog.csdn.net/single_0910/article/details/113844814)



单表索引优化几个原则

- [x] 最左前缀法则
- [x] 索引列上不计算

- [x] 范围之后全失效

- [x] 覆盖索引多使用

- [x] 使用不等会失效
- [x] 使用NULL值要小心

- [x] 模糊查询%加右边
- [x] 覆盖索引多使用



## 索引相关题目

**假设index(a,b,c)**

| Where语句                                               | 索引是否被使用                             |
| ------------------------------------------------------- | ------------------------------------------ |
| where a = 3                                             | Y，使用到a                                 |
| where a = 3 and b = 5                                   | Y，使用到a，b                              |
| where a = 3 and b = 5                                   | Y，使用到a，b，c                           |
| where b = 3 或者 where b = 3 and c = 4 或者 where c = 4 | N，没有用到a字段                           |
| where a = 3 and c = 5                                   | 使用到a，但是没有用到c，因为b断了          |
| where a = 3 and b > 4 and c = 5                         | 使用到a，b，但是没有用到c，因为c在范围之后 |
| where a = 3 and b like 'kk%' and c = 4                  | Y，a，b，c都用到                           |
| where a = 3 and b like '%kk' and c = 4                  | 只用到a                                    |
| where a = 3 and b like '%kk%' and c = 4                 | 只用到a                                    |
| where a = 3 and b like 'k%kk%' and c = 4                | Y，a，b，c都用到                           |





## 面试题分析

```sql
/* 创建表 */
CREATE TABLE `test03`(
`id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
`c1` CHAR(10),
`c2` CHAR(10),
`c3` CHAR(10),
`c4` CHAR(10),
`c5` CHAR(10)
);

/* 插入数据 */
INSERT INTO `test03`(`c1`,`c2`,`c3`,`c4`,`c5`) VALUES('a1','a2','a3','a4','a5');
INSERT INTO `test03`(`c1`,`c2`,`c3`,`c4`,`c5`) VALUES('b1','b22','b3','b4','b5');
INSERT INTO `test03`(`c1`,`c2`,`c3`,`c4`,`c5`) VALUES('c1','c2','c3','c4','c5');
INSERT INTO `test03`(`c1`,`c2`,`c3`,`c4`,`c5`) VALUES('d1','d2','d3','d4','d5');
INSERT INTO `test03`(`c1`,`c2`,`c3`,`c4`,`c5`) VALUES('e1','e2','e3','e4','e5');

/* 创建复合索引 */
CREATE INDEX idx_test03_c1234 ON `test03`(`c1`,`c2`,`c3`,`c4`);
```

题目

```sql
/* 最好索引怎么创建的，就怎么用，按照顺序使用，避免让MySQL再自己去翻译一次 */

/* 1.全值匹配 用到索引c1 c2 c3 c4全字段 */
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c2` = 'a2' AND `c3` = 'a3' AND `c4` = 'a4';

/* 2.用到索引c1 c2 c3 c4全字段 MySQL的查询优化器会优化SQL语句的顺序*/
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c2` = 'a2' AND `c4` = 'a4' AND `c3` = 'a3';

/* 3.用到索引c1 c2 c3 c4全字段 MySQL的查询优化器会优化SQL语句的顺序*/
EXPLAIN SELECT * FROM `test03` WHERE `c4` = 'a4' AND `c3` = 'a3' AND `c2` = 'a2' AND `c1` = 'a1';

/* 4.用到索引c1 c2 c3字段，c4字段失效，范围之后全失效 */
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c2` = 'a2' AND `c3` > 'a3' AND `c4` = 'a4';

/* 5.用到索引c1 c2 c3 c4全字段 MySQL的查询优化器会优化SQL语句的顺序*/
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c2` = 'a2' AND `c4` > 'a4' AND `c3` = 'a3';

/* 
   6.用到了索引c1 c2 c3三个字段, c1和c2两个字段用于查找,  c3字段用于排序了但是没有统计到key_len中，c4字段失效
*/
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c2` = 'a2' AND `c4` = 'a4' ORDER BY `c3`;

/* 7.用到了索引c1 c2 c3三个字段，c1和c2两个字段用于查找, c3字段用于排序了但是没有统计到key_len中*/
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c2` = 'a2' ORDER BY `c3`;

/* 
   8.用到了索引c1 c2两个字段，c4失效，c1和c2两个字段用于查找，c4字段排序产生了Using filesort说明排序没有用到c4字段 
*/
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c2` = 'a2' ORDER BY `c4`;

/* 9.用到了索引c1 c2 c3三个字段，c1用于查找，c2和c3用于排序 */
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c5` = 'a5' ORDER BY `c2`, `c3`;

/* 10.用到了c1一个字段，c1用于查找，c3和c2两个字段索引失效，产生了Using filesort */
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c5` = 'a5' ORDER BY `c3`, `c2`;

/* 11.用到了c1 c2 c3三个字段，c1 c2用于查找，c2 c3用于排序 */
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND  `c2` = 'a2' ORDER BY c2, c3;

/* 12.用到了c1 c2 c3三个字段，c1 c2用于查找，c2 c3用于排序 */
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND  `c2` = 'a2' AND `c5` = 'a5' ORDER BY c2, c3;

/* 
   13.用到了c1 c2 c3三个字段，c1 c2用于查找，c2 c3用于排序 没有产生Using filesort 
      因为之前c2这个字段已经确定了是'a2'了，这是一个常量，再去ORDER BY c3,c2 这时候c2已经不用排序了！
      所以没有产生Using filesort 和(10)进行对比学习！
*/
EXPLAIN SELECT * FROM `test03` WHERE `c1` = 'a1' AND `c2` = 'a2' AND `c5` = 'a5' ORDER BY c3, c2;



/* GROUP BY 表面上是叫做分组，但是分组之前必定排序。 */

/* 14.用到c1 c2 c3三个字段，c1用于查找，c2 c3用于排序，c4失效 */
EXPLAIN SELECT c2,c3 FROM `test03` WHERE `c1` = 'a1' AND `c4` = 'a4' GROUP BY `c2`,`c3`;

/* 15.用到c1这一个字段，c4失效，c2和c3排序失效产生了Using filesort */
EXPLAIN SELECT c2,c3 FROM `test03` WHERE `c1` = 'a1' AND `c4` = 'a4' GROUP BY `c3`,`c2`;
GROUP BY基本上都需要进行排序，索引优化几乎和ORDER BY一致，但是GROUP BY会有临时表的产生。
```





开启慢查询日志

```sql
mysql> show variables like 'long_query_time';
+-----------------+-----------+
| Variable_name   | Value     |
+-----------------+-----------+
| long_query_time | 10.000000 |
+-----------------+-----------+
1 row in set (0.00 sec)

mysql> set global long_query_time = 3;
Query OK, 0 rows affected (0.00 sec)

mysql> show variables like 'long_query_time';
+-----------------+-----------+
| Variable_name   | Value     |
+-----------------+-----------+
| long_query_time | 10.000000 |
+-----------------+-----------+
1 row in set (0.00 sec)
```



```sql
mysql> show variables like 'long_query_time';
+-----------------+----------+
| Variable_name   | Value    |
+-----------------+----------+
| long_query_time | 3.000000 |
+-----------------+----------+
1 row in set (0.00 sec)

```



```sql
mysql> show variable like  'slow%';
ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'variable like  'slow%'' at line 1
mysql> show variables like  'slow%';
+---------------------+----------------------------------------+
| Variable_name       | Value                                  |
+---------------------+----------------------------------------+
| slow_launch_time    | 2                                      |
| slow_query_log      | ON                                     |
| slow_query_log_file | /var/lib/mysql/VM-0-16-centos-slow.log |
+---------------------+----------------------------------------+
3 rows in set (0.00 sec)

mysql> select sleep(4);
+----------+
| sleep(4) |
+----------+
|        0 |
+----------+
1 row in set (4.00 sec)

mysql> show variables like  'slow%';
+---------------------+----------------------------------------+
| Variable_name       | Value                                  |
+---------------------+----------------------------------------+
| slow_launch_time    | 2                                      |
| slow_query_log      | ON                                     |
| slow_query_log_file | /var/lib/mysql/VM-0-16-centos-slow.log |
+---------------------+----------------------------------------+
3 rows in set (0.00 sec)

mysql> show global status like '%slow_queries%';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| Slow_queries  | 1     |
+---------------+-------+
1 row in set (0.00 sec)

```



```sh
Tcp port: 3306  Unix socket: /var/lib/mysql/mysql.sock
Time                 Id Command    Argument
# Time: 2021-02-21T10:31:48.768397Z
# User@Host: root[root] @ localhost []  Id:    46
# Query_time: 4.001882  Lock_time: 0.000000 Rows_sent: 1  Rows_examined: 0
SET timestamp=1613903508;
select sleep(4);

```



mysqldumpslow 

```sh
得到返回记录集最多的10个SQL。
mysqldumpslow -s r -t 10 /database/mysql/mysql06_slow.log
得到访问次数最多的10个SQL
mysqldumpslow -s c -t 10 /database/mysql/mysql06_slow.log
```



批量插入

```sql
CREATE TABLE `dept` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`deptName` VARCHAR(30) DEFAULT NULL,
`address` VARCHAR(40) DEFAULT NULL,
ceo INT NULL ,
PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
CREATE TABLE `emp` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`empno` INT NOT NULL ,
`name` VARCHAR(20) DEFAULT NULL,
`age` INT(3) DEFAULT NULL,
`deptId` INT(11) DEFAULT NULL,
PRIMARY KEY (`id`)
#CONSTRAINT `fk_dept_id` FOREIGN KEY (`deptId`) REFERENCES `t_dept` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```



在执行创建函数之前，首先请保证 log_bin_trust_function_creators 参数为 1，即 on 开启状态。
否则会报错：



```sql
查询：show variables like 'log_bin_trust_function_creators';
设置：set global log_bin_trust_function_creators=1;
```



```
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'zL@123456' WITH GRANT OPTION;
```



批量插入数据

```sql
###################################################################
#随机产生字符串
DELIMITER $$
CREATE FUNCTION rand_string(n INT) RETURNS VARCHAR(255)
BEGIN
DECLARE chars_str VARCHAR(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFJHIJKLMNOPQRSTUVWXYZ';
DECLARE return_str VARCHAR(255) DEFAULT '';
DECLARE i INT DEFAULT 0;
WHILE i < n DO
SET return_str =CONCAT(return_str,SUBSTRING(chars_str,FLOOR(1+RAND()*52),1));
SET i = i + 1;
END WHILE;
RETURN return_str;
END $$
#如果要删除函数，则执行：drop function rand_string;

###################################################################
#用于随机产生多少到多少的编号
DELIMITER $$
CREATE FUNCTION rand_num (from_num INT ,to_num INT) RETURNS INT(11)
BEGIN
DECLARE i INT DEFAULT 0;
SET i = FLOOR(from_num +RAND()*(to_num -from_num+1)) ;
RETURN i;
END$$
#如果要删除函数：drop function rand_num;

###################################################################
#创建往 emp 表中插入数据的存储过程
DELIMITER $$
CREATE PROCEDURE insert_emp( START INT , max_num INT )
BEGIN
DECLARE i INT DEFAULT 0;
#set autocommit =0 把 autocommit 设置成 0
SET autocommit = 0;
REPEAT
SET i = i + 1;
INSERT INTO emp (empno, NAME ,age ,deptid ) VALUES ((START+i) ,rand_string(6) ,
rand_num(30,50),rand_num(1,10000));
UNTIL i = max_num
END REPEAT;
COMMIT;
END$$

#删除
# DELIMITER ;
# drop PROCEDURE insert_emp;

###################################################################
#执行存储过程，往 dept 表添加随机数据
DELIMITER $$
CREATE PROCEDURE `insert_dept`( max_num INT )
BEGIN
DECLARE i INT DEFAULT 0;
SET autocommit = 0;
REPEAT
SET i = i + 1;
INSERT INTO dept ( deptname,address,ceo ) VALUES (rand_string(8),rand_string(10),rand_num(1,500000));
UNTIL i = max_num
END REPEAT;
COMMIT;
END$$
#删除
# DELIMITER ;
# drop PROCEDURE insert_dept;
```



添加数据到部门表

```sql
#执行存储过程，往 dept 表添加 1 万条数据
DELIMITER ;
CALL insert_dept(10000);
```



添加数据到员工表

```sql
#执行存储过程，往 emp 表添加 50 万条数据
DELIMITER ;
CALL insert_emp(100000,500000);
```

