# 外键约束

作用:用来确定表与表之间的联系[1:1 1:N  N:N 自关联]

**使用表级添加**或者使用**alter命令**.

使用到的关键字:foreign key ... references 表(列)

## 外键特点

1. 可以为null
2. 保证参数的完整性.外键列的值一定存在于引用的表中的某个列的值.
3. 外键始终是在多的一方,所以在进行删除的时候,先删除多的一方,再删除一的一方.保证参数的完整性.

## 一对一

~~~sql
drop table t_wife;
drop table t_husband;
create table t_husband(
	id int(7) primary key auto_increment,
    name varchar(20) not null
);

create table t_wife(
	id int(7) primary key auto_increment,
    name varchar(20) not null,
    husband_id int(7),
    constraint t_wife_husband_id_fk foreign key(husband_id) references t_husband(id)
);
-- 插入数据
insert into t_husband(name) values('tom');
insert into t_wife(name,husband_id) values('凤姐',last_insert_id());
~~~



## 关于安全删除

删除表的语法:DROP TABLE 表名;

一旦存在了外键引用的关系的表,在删除的时候,一定要引用的表.

会先删除多的一方,再删除一的一方;



## sql脚本文件

顺序:先删除 - 创建表 - 插入数据 - 目的:可以防止后期出现数据混乱的时候,可以无限恢复

在cmd中引用脚本文件 - source sql脚本文件的绝对路径



1 - MYSQL客户端的命令不要加分号.

2 - sql脚本的文件的绝对路径中不要包含中文,特殊字符.



## 一对多

~~~sql
--先删除多的一方
drop table t_ord;
drop table t_customer;
create table t_customer(
	id int(7) primary key,
    cname varchar(20) not null
);

create table t_ord(
	id int(7) primary key,
    ordno varchar(20) unique not null,
    cid int(7),
    constraint t_ord_cid_fk foreign key(cid) references t_customer(id) on delete cascade
);

--插入insert
insert into t_customer(id,cname) values(1,'tom');
insert into t_customer(id,cname) values(2,'jack');

insert into t_ord(id,ordno,cid) values(1,'1001',1);
insert into t_ord(id,ordno,cid) values(2,'1002',2);
insert into t_ord(id,ordno,cid) values(3,'1003',2);

--先删除所有id = 1在t_ord中的子记录
delete from t_ord where cid = 1;
--删除delete
delete from t_customer where id = 1;
~~~



## 查看约束

~~~sql
select @@FOREIGN_KEY_CHECKS;//查看约束的状态 1代表的是约束处于启动状态.0代表的暂时失效状态

--删除外键约束
mysql>alter table t_ord drop foreign key t_ord_cid_fk;

--使外键约束失效
mysql>set FOREIGN_KEY_CHECKS = 0;

--外键约束生效
mysql>set FOREIGN_KEY_CHECKS = 1;
原因是:mysql轻量级的

create table t_test(
	id int(7) primary key,
    name varchar(20),
    uno varchar(20)
);
insert into t_test(id,name) values(1,'tom'); 
alter table t_test modify column uno varchar(20) not null;//不 ok

~~~

## 多对多

~~~sql
drop table sc;
drop table t_student;
drop table t_course;
create table t_student(
	id int(7) primary key,
    sname varchar(20)
);
create table t_course(
	id int(7) primary key,
    cname varchar(20)
);

create table sc(
	sid int(7),
    cid int(7),
    constraint sc_pk primary key(sid,cid),
    constraint sc_sid_fk foreign key(sid) references t_student(id),
    constraint sc_cid_fk foreign key(cid) references t_course(id)
);
~~~



## 级联操作

级联删除

 ~~~sql
constraint t_ord_cid_fk foreign key(cid) references t_customer(id) ON DELETE CASCASE;
delete from t_customer where id = 1;//观察t_ord表中引用了cid=1的记录是否也删除了.
 ~~~

dao层 - 直接和数据库进行交互

~~~java
public interface ICustomerDao{
    //级联操作 - 根据客户id进行删除,并且如果该客户存在订单信息
    //则也要将客户的所有的订单信息也要删除
    void delById(int id);
}

public interface IOrderDao{
    
    //根据订单的外键cid[客户的id进行删除删除]
    void delByCustomerId(int cid);
}
~~~

service层 - 业务逻辑代码 - 调用dao中的.

~~~java
public class CustomerService Implements ICustomerService{
    
    private ICustomerDao customerDao = new CustomerDaoImpl();
    
    private IOrderDao orderDao = new OrderDaoImpl();
    
    //级联操作 - 根据客户id进行删除,并且如果该客户存在订单信息
    //则也要将客户的所有的订单信息也要删除
    @Override
    public void delById(int id){//id代表的就是客户id
        orderDao.delByCustomerId(id);//先删除多的一方
        customerDao.delById(id);
    }
}
~~~



# DML

DML - Data Manipulating Language - 数据操纵语言 - insert update delete



## 插入INSERT

* 插入所有列 - 每列的值都是手动进行插入

  插入的值的顺序以及个数一定要和列保持一致.

  ~~~sql
  INSERT INTO 表名 VALUES(值1,值2,值3,值4,值5);
  
  mysql>insert into t_husband values(4,'jack');//ok id的值就是4
  mysql>insert into t_husband values(null,'jack');//ok
  mysql>insert into t_husband values('jack');//error - 少掉一个列
  ~~~

* 插入指定列的指定值 - 推荐 - 可读性强

  ~~~sql
  INSERT INTO 表名(列1,列2,列3) VALUES(列值1,列值2,列值3);
  
  mysql>insert into t_husband(name) values('cang');
  ~~~



## 数据库优化

在多线程的环境中,如果批量插入大量的数据的时候,有可能会导致数据库进入到阻塞状态.执行INSERT语句是需要

时间的,必须等到所有的数据全部插入完毕之后,才会响应给客户端的.就会使用delayed进行延迟插入.

无需等待所有的数据全部插入完毕之后才响应.会立即响应.DB自己会在空闲的时间,再慢慢将大量的数据插入到

数据库中.

~~~SQL
INSERT DELAYED INTO 表名 VALUES(值1,值2,值3);

mysql>insert delayed into t_husband(name) values('岳云鹏');
~~~



## 批量插入

~~~sql
INSERT INTO 表名(列名1,列名N) VALUES(列值1,列值N),(列值2,列值N),(列值3,列值N);

mysql>insert into t_husband(name) values('王静静'),('小P霞'),('生蚝');
~~~

SQL语句的执行过程 - 先经过DB的Server进行编译和解释的.



## 删除记录 - DELETE

删除的时候一定要考虑外键约束的存在!

应该先删除子记录,再删除父记录.

* 清空整张表

  ~~~sql
  DELETE FROM 表名;
  ~~~

* 根据条件进行删除

  sql中是使用where语句来作为条件的连接的.

  ~~~sql
  DELETE FROM 表名 WHERE 条件;
  
  mysql>delete from tb_husband where name = 'tom';
  ~~~

## 笔试题

DDL命令truncate和DML命令delete的区别.

**truncate是用来清空整张表的,不能和where语句结合使用.并且删除完毕之后,将不会回滚.**

~~~sql
mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> delete from t_wife;
Query OK, 1 row affected (0.00 sec)

mysql> select * from t_wife;
Empty set (0.00 sec)

mysql> rollback;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_wife;
+----+--------+------------+
| id | name   | husband_id |
+----+--------+------------+
|  1 | 凤姐   |          1 |
+----+--------+------------+
1 row in set (0.00 sec)

mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> truncate t_wife;
Query OK, 0 rows affected (0.02 sec)

mysql> select * from t_wife;
Empty set (0.00 sec)

mysql> rollback;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_wife;
Empty set (0.00 sec)
~~~

## 更新UPDATE

* 更新整张表 - 不加

  ~~~java
  UPDATE 表名 SET 列名1 = 新的列值,列名2 = 新的列值;
  mysql>update t_customer set cname = 'xxx';
  ~~~

* 根据条件进行更新操作

  ~~~java
  UPDATE 表名 SET 列名1 = 新的列值,列名2 = 新的列值 WHERE 条件;
  
  mysql>update t_customer set cname = 'success' where id = 2;
  ~~~

# 提交模式

在MYSQL中的,默认的提交模式是默认提交.

* 查看当前会话的提交模式 - 自动提交还是手动提交

  ~~~sql
  mysql>show session variables like '%autocommit%';
  +---------------+-------+
  | Variable_name | Value |
  +---------------+-------+
  | autocommit    | ON    |
  +---------------+-------+
  ~~~

* 设置它为手动提交方式:

  ~~~java
  set autocommit = off;
  ~~~

  最终执行完DML操作之后,我们需要commit命令来进行手动提交.

  为什么要设置手动提交方式 - 原因是:如果是默认提交,则无法回滚.

# 事务

**DTL - 数据事务语言**

**事务的定义:**

**就是指一组相关的SQL操作,我们所有的操作都是处在事务中的**

**注意:**

1. 在数据库中,**执行业务的基本单位是事务**,不是以某一条SQL

2. 数据库在默认情况下,事务都是打开的,也就是说它是一直

3. 处在事务中的,一个事务的结束,代表着下一个事务的开启

4. 执行commit或者rollback指令时,会结束当前事务

**作用:用来保证数据的平稳性和可预测性.**

**事务的四大特性(ACID):**

1. atomic,原子性,事务是不可再分割的,要么同时成功,要么同时失败

     ​	     **原子性强调的是"一种状态".**

2. consistency,一致性,事务一旦结束,内存中的数据和数据库中的数据是保持一致的

     ​	      **一致性强调的是"数据的一致性"**

3. isolation,隔离性,事务之间互不干扰,一个事务的结束意味着下一个事务的开启

4. duration,持久性,事务一旦提交,则数据持久化到数据库中,永久保存[**如果发生了灾难,能够用数据库恢复技术来对数据进行恢复**]

## 事务控制语句

1. **BEGIN或START TRANSACTION**；显式地开启一个事务；

2. **COMMIT；**也可以使用COMMIT WORK，不过二者是等价的。COMMIT会提交事务，并使已对数据库进行的所有修改成为永久性的；

3. **ROLLBACK；**有可以使用ROLLBACK WORK，不过二者是等价的。回滚会结束用户的事务，并撤销正在进行的所有未提交的修改；

4. SAVEPOINT identifier；SAVEPOINT允许在事务中创建一个保存点，一个事务中可以有多个SAVEPOINT；

5. RELEASE SAVEPOINT identifier；删除一个事务的保存点，当没有指定的保存点时，执行该语句会抛出一个异常；

6. ROLLBACK TO identifier；把事务回滚到标记点；

7. **SET TRANSACTION；**用来设置事务的隔离级别。InnoDB存储引擎提供事务的隔离级别有READ UNCOMMITTED、READ COMMITTED、REPEATABLE READ和SERIALIZABLE。

## MYSQL 事务处理主要有两种方法

1. 用 BEGIN, ROLLBACK, COMMIT来实现

    1-1. BEGIN 开始一个事务

    ​	**begin或者start transaction**	

    1-2. ROLLBACK 事务回滚
    1-3. COMMIT 事务确认

2. 直接用 SET 来改变 MySQL 的自动提交模式: 

    修改的是仅仅是当前session[代表的是当前的会话窗口,代表当前的连接]的提交模式

    2-1. **SET AUTOCOMMIT= off 禁止自动提交**
    2-2. SET AUTOCOMMIT= on 开启自动提交 - 默认的

## 多事务的并发处理机制

原因:多个事务同时操作一个表中的同一行数据,如果这些操作是.修改操作的话,就会产生并发问题,如果不处理,则会造成数据不一致的情况.

数据库可能产生的并发问题包括:

>1. `脏读`
>>是指一个事务正在访问数据,并且对这个数据进行修改,而这种修改
>>还没有提交到数据库中,而另一个事务也访问了这个数据,并且使用了这个数据.
>>解决方法:一个事务在修改数据时,该数据不能被其他事务访问

>2. `不可重复读`
>>是指一个事务多次读取同一条记录,如果此时另一个事务也访问并且修改了该数据,
>>
>>则就会出现多次读取出现数据不一致的情况,原来的
>>数据变成了不可重复读取的数据
>>解决方法:只有在**修改**事务完全提交过后才可以读取到数据

>3. `幻读`
>>是指一个事务修改表中的多行记录,但是此时另一个事务对该表格进行了**插入**数据的操作,
>>
>>则第一个事务会发现表格中会出现没有被修改的行就像发生了幻觉一样
>>解决方法:在一个事务提交数据之前,其他事务不能添加数据

**不可重复读的重点是修改，同样的条件，你读取过的数据，再次读取出来发现值不一样了幻读的重点在于新增或者删除**

## 事务隔离级别

>1. READ_UNCOMMITTED
>>这是事务最低的隔离级别，它充许另外一个事务可以看到这个事务未提交的数据。
>>解决第一类丢失更新的问题，但是会出现脏读、不可重复读.

>2. READ_COMMITTED
>>保证一个事务修改的数据提交后才能被另外一个事务读取，即另外一个事务不能读取该事务未提交的数据。
>>解决第一类丢失更新和脏读的问题，但会出现不可重复读.

>3. REPEATABLE_READ
>>保证一个事务相同条件下前后两次获取的数据是一致的
>>解决第一类丢失更新，脏读、不可重复读.

>4. SERIALIZABLE
>
>>事务被处理为顺序执行。解决所有问题

**提醒：**
**Mysql默认的事务隔离级别为repeatable_read**



## 事务隔离级别的设置

~~~sql
drop table t_account;
create table t_account(
   id int(7) primary key,
   accno varchar(20),
   balance double(7,2)
);
insert into t_account values(1,'1001',450.0);
insert into t_account values(2,'1002',300.0);
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  450.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
~~~

查看MSYQL数据库默认的事务的隔离级别 - 可重复读repeatable-read

~~~sql
mysql>select @@tx_isolation;
+-----------------+
| @@tx_isolation  |
+-----------------+
| REPEATABLE-READ |
+-----------------+
~~~



### READ UNCOMMITTED[读未提交]

演示:脏读的场景 - **READ_UNCOMMITTED[读未提交]**

**一个事务使用到了另外一个事务尚未提交的数据.**

打开A窗口和B窗口 - 设置当前会话的事务的隔离界别为READ_UNCOMMITTED

~~~sql
mysql>set transaction isolation level READ UNCOMMITTED;
~~~

A窗口:

~~~sql
mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  450.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)

mysql> update t_account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0
~~~

B窗口

~~~sql
mysql> start transaction;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  450.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  400.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)
~~~

此时发现B事务中读到了A事务中尚未提交的数据.



A窗口回滚事务

~~~sql
mysql> update t_account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.01 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> rollback;
Query OK, 0 rows affected (0.01 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  450.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)
~~~

B窗口更新数据

~~~sql
mysql> update t_account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  400.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)
~~~

此时对于B事务来说,读出来的400的数据就是一个脏的数据.



### READ COMMITTED 读已提交

一个事务可以读取到另外一个事务已经提交的数据.但是不能读取到另外一个事务尚未提交的数据.

**解决了脏读的问题.**

**可能造成的问题是:不可重复读** - 在B事务读取的时候,另外一个事务A对数据进行了修改操作并且提交了.B事务再次

查询结果的时候,读到了事务A修改后的结果.造成B在同一个事务中两次读取的结果是不一样的.

~~~sql
mysql>set transaction isolation level READ COMMITTED;
~~~

A事务

~~~sql
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  400.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)

mysql> update t_account set balance = balance + 50 where id = 1;
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> commit;
Query OK, 0 rows affected (0.01 sec)
~~~

B事务:

~~~sql
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  400.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  400.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  450.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)
~~~



### REPEATABLE READ

B事务从开始到提交之前,读取到的结果都是一致的.即使在这中间,其他事务对数据进行了修改和提交.

可重复读.

~~~sql
mysql>set transaction isolation level REPEATABLE READ;
~~~

A事务

~~~sql
set autocommit = off;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  450.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)

mysql> update t_account set balance = balance - 50 where id = 1;
Query OK, 1 row affected (0.00 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> commit;
Query OK, 0 rows affected (0.00 sec)
~~~

B事务

~~~sql
mysql> set autocommit = off;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  450.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  450.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)

mysql> commit;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  400.00 |
|  2 | 1002  |  300.00 |
+----+-------+---------+
2 rows in set (0.00 sec)
~~~

### SERIALIZABLE

锁表 - 串行 - 性能及其低下.

~~~sql
mysql>set transaction isolation level SERIALIZABLE;
~~~



## 总结

|                                | 脏读 | 不可重复读 | 幻读 | 可重复读 |
| ------------------------------ | ---- | ---------- | ---- | -------- |
| READ UNCOMMITTED[读未提交]     | 是   | 是         | 是   | 否       |
| READ COMMITTED[读已提交]       | 否   | 是         | 是   | 否       |
| REPEATABLE COMMITTED[可重复读] | 否   | 否         | 是   | 是       |
| SERIALIZABLE[串行化]           | 否   | 否         | 否   | 否       |



## 幻读

事务A在T1时刻进行修改操作,事务B在T2时刻进行增加操作并且提交了.事务A在T2时刻修改完毕也提交了.

那么会发现了"表中似乎存在未修改的行集" - 事务B偷偷加进去的.就像发生了"幻觉一样".

~~~sql
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  100.00 |
|  2 | 1002  | 3100.00 |
|  3 | 1003  | 4000.00 |
|  4 | 1004  |  900.00 |
|  9 | 1009  |  100.00 |
+----+-------+---------+
5 rows in set (0.00 sec)

mysql> insert into t_account values(10,'1010',1000.0);
ERROR 1205 (HY000): Lock wait timeout exceeded; try restarting transaction
mysql> insert into t_account values(10,'1010',1000.0);
Query OK, 1 row affected (15.61 sec)

mysql> commit;
Query OK, 0 rows affected (0.00 sec)
~~~

~~~sql

mysql> use aistar;
Database changed
mysql> begin;
Query OK, 0 rows affected (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |  100.00 |
|  2 | 1002  | 3100.00 |
|  3 | 1003  | 4000.00 |
|  4 | 1004  |  900.00 |
|  9 | 1009  |  100.00 |
+----+-------+---------+
5 rows in set (0.00 sec)

mysql> update t_account set balance = 0;
Query OK, 5 rows affected (0.00 sec)
Rows matched: 5  Changed: 5  Warnings: 0

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |    0.00 |
|  2 | 1002  |    0.00 |
|  3 | 1003  |    0.00 |
|  4 | 1004  |    0.00 |
|  9 | 1009  |    0.00 |
+----+-------+---------+
5 rows in set (0.00 sec)

mysql> commit;
Query OK, 0 rows affected (0.01 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |    0.00 |
|  2 | 1002  |    0.00 |
|  3 | 1003  |    0.00 |
|  4 | 1004  |    0.00 |
|  9 | 1009  |    0.00 |
+----+-------+---------+
5 rows in set (0.00 sec)

mysql> select * from t_account;
+----+-------+---------+
| id | accno | balance |
+----+-------+---------+
|  1 | 1001  |    0.00 |
|  2 | 1002  |    0.00 |
|  3 | 1003  |    0.00 |
|  4 | 1004  |    0.00 |
|  9 | 1009  |    0.00 |
| 10 | 1010  | 1000.00 |
+----+-------+---------+
6 rows in set (0.00 sec)
~~~



## InnoDB引擎的锁机制

**之所以以InnoDB为主介绍锁，是因为InnoDB支持事务，支持行锁和表锁用的比较多，Myisam不支持事务，只支持表锁**

1. **共享锁（S）**：允许一个事务去读一行，阻止其他事务获得相同数据集的排他锁。
   共享锁就是我读的时候，你可以读，但是不能写

2. **排他锁（X)**：允许获得排他锁的事务更新数据，阻止其他事务取得相同数据集的共享读锁和排他写锁。
    排他锁就是我写的时候，你不能读也不能写

3. 意向共享锁（IS）：事务打算给数据行加行共享锁，事务在给一个数据行加共享锁前必须先取得该表的IS锁。

4. 意向排他锁（IX）：事务打算给数据行加行排他锁，事务在给一个数据行加排他锁前必须先取得该表的IX锁。

**说明:**

1. 共享锁和排他锁都是**行锁**，意向锁都是表锁，应用中我们只会使用到共享锁和排他锁**，意向锁是mysql内部使用的，不需要用户干预**。
2. **对于UPDATE、DELETE和INSERT语句，InnoDB会自动给涉及数据集加排他锁（X)**；**对于普通SELECT语句，InnoDB不会加任何锁**，事务可以通过以下语句显示给记录集加共享锁或排他锁。
   共享锁(S)：SELECT * FROM table_name WHERE ... LOCK IN SHARE MODE。
   排他锁(X)：**SELECT * FROM table_name WHERE ... FOR UPDATE。**
3. InnoDB行锁是通过给索引上的索引项加锁来实现的，因此InnoDB这种行锁实现特点意味着：只有通过索引条件检索数据，InnoDB才使用行级锁，否则，InnoDB将使用表锁！

# 视图



## 视图的分类

* 简单视图 - 视图来自于单张表
* 高级视图 - 视图来自于多张表的查询的结果集.

视图是指计算机数据库中的视图，是一个**虚拟表**，其内容由查询定义。同真实的表一样，视图包含一系列带有名称的列和行数据。

但是，视图并不在数据库中以存储的数据值集形式存在。行和列数据来自由定义视图的查询所引用的表，并且在引用视图时动态生成。

## 为啥需要使用视图

关系型数据库中的数据是由一张一张的二维关系表所组成，简单的单表查询只需要遍历一个表，

而复杂的多表查询需要将多个表连接起来进行查询任务。对于复杂的查询事件，每次查询都需要编写MySQL代码效率低下。

为了解决这个问题，数据库提供了视图（view）功能。

## 创建视图

~~~
CREATE VIEW 视图名(列1，列2...)
AS SELECT (列1，列2...)
FROM ...;
~~~

使用视图和使用表完全一样，只需要把视图当成一张表就OK了。视图是一张虚拟表。

~~~sql
--删除视图
drop view t_account_view;
--创建视图,来自于单表的查询
create view t_account_view
as 
select id,balance from t_account;
~~~

## 视图的CRUD操作

* 查询视图 - select * from 视图名;

  ~~~sql
  select * from t_account_view;
  ~~~

* 更新原表肯定会对视图造成影响.

* 更新视图会对原表造成影响.

### 高级视图

不能对高级视图执行DML[insert update delete]操作.

视图的来自于多张表的数据

~~~sql
create view t_customer_v
as 
select c.id cids,c.cname cname,o.* from t_customer c left join t_ord o on c.id = o.cid;
~~~



### 创建表

* 根据一张表来构建另外一张表

  俩张表都是独立的表.

  保留原表的结构和数据.

  ~~~sql
  create table t_account_copy
  as
  select * from t_account;
  ~~~

* 仅仅保留表的结构,但是不需要原表的数据

  ~~~sql
  create table t_account_copy
  as
  select * from t_account where 1=2;
  ~~~

## 删除视图

drop view 视图名;

## 视图与表数据变更

1. 表格数据变化后，在通过视图检索，得到的结果也同步发生了变化

2. 可以通过视图插入数据，但是只能基于一个基础表进行插入，不能跨表更新数据。

## WITH CHECK OPTION

如果在创建视图的时候制定了“WITH CHECK OPTION”，那么更新数据时不能插入或更新不符合视图限制条件的记录。

~~~sql
drop view t_account_vo;
create view t_account_vo
as
select * from t_account where id = 2
with check option;
~~~

with check option 一定是和where语句条件匹配使用.

作为where的条件列的值是绝对不允许修改的.

# 索引

比如我们要在字典中找某一字，如何才能快速找到呢？那就是通过字典的目录。

对数据库来说，索引的作用就是给‘数据’加目录。创建所以的目的就是为了提高查询速度

## 索引算法

1. btree(二叉树)索引  log<sub>2</sub>N

2. hash(哈希)索引   1

## 优缺点

1. 好处:加快了查询速度(select )

2. 坏处:降低了增,删,改的速度(update/delete/insert),增大了表的文件大小(索引文件甚至可能比数据文件还大)

## 索引类型

1. 普通索引(index)：仅仅是加快了查询速度

2. 唯一索引(unique)：行上的值不能重复

3. 主键索引(primary key)：不能重复

4. 全文索引(fulltext):仅可用于 MyISAM 表，针对较大的数据，生成全文索引很耗时好空间。

5. 组合索引：为了更多的提高mysql效率可建立组合索引，遵循”最左前缀“原则。

## 索引语法

1. 查看某张表上的所有索引

    show index from tableName [\G,如果是在cmd窗口，可以换行];

2. 建立索引

    alter table 表名 add index/unique/fulltext 索引名 (列名) ; ---索引名可不写,不写默认使用列名

    alter table 表名 add primary key(列名) --不要加索引名，因为主键只有一个

3. 删除非主键索引

    alter table 表名 drop index 索引名；

4. 删除主键索引：

    alter table 表名 drop primary key;

5. 全文索引与停止词

    全文索引的用法：match(全文索引名) against('keyword');

## 关于全文索引
关于全文索引的停止词：

全文索引不针对非常频繁的词做索引

如：this,is,you,my等等

全文索引在mysql的默认情况下，对于中文意义不大。

因为英文有空格，标点符号来拆成单词，进而对单词进行索引；

而对于中文，没有空格来隔开单词，mysql无法识别每个中文词。

可以使用sphinx插件来进行全文索引的中文索引。

## 组合索引
(5)复合索引

代码如下:

CREATE TABLE test (

     id INT NOT NULL,
    
     last_name CHAR(30) NOT NULL,
     
     first_name CHAR(30) NOT NULL,
     
     PRIMARY KEY (id),
     
     INDEX name (last_name,first_name)

 );

 name索引是一个对last_name和first_name的索引。索引可以用于为last_name，或者为last_name和first_name在已知范围内指定值的查询。因此，name索引用于下面的查询：

 SELECT * FROM test WHERE last_name='Widenius';

 SELECT * FROM test WHERE last_name='Widenius' AND first_name='Michael';

但是不能用于SELECT * FROM test WHERE first_name='Michael';这是因为MySQL组合索引为“最左前缀”的结果,简单的理解就是只从最左面的开始组合。

## 建立索引的策略

1. 主键列和唯一性列						√
2. 不经常发生改变的列					√
3. 满足以上2个条件,经常作为查询条件的列	√
4. 重复值太多的列						×
5. null值太多的列						×

