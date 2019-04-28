# 锁

悲观锁[mysql中自动给DML加上悲观锁.就是mysql数据库中的行锁]和乐观锁[不是真的加上"锁"]



**乐观锁**

业务逻辑代码希望内存中的数据和实际磁盘中的数据是一致 .

**用代码来实现判断内存中的数据和磁盘中的数据是否是一致 .**

**mysql数据库的默认的事务的隔离级别 - 可重复读.**

应用场景:订单表[id,ordno,price,status]

status列 - 订单的状态 - **未下单的1,**已下单的2,已删除的0.

进行删除操作的时候,实际上并不是真的进行delete操作的,而是更改状态的.比如将订单的status修改状态为0.

显示的时候,只需要判断status的状态是否不为0,不为0的订单才显示.



2   1001 100.0  1

A事务 - 查询订单信息 - 即将想要修改未下单的订单的信息 id = 2[未下单的订单];

mysql>select ....

mysql>update .... set....  where id = 2;

B事务 - 查询订单 - 进行更新操作 - id = 2

mysql>update ... set status = 0 where id = 2;

mysql>commit;



A事务

mysql>select ... where id = 2;//status = 仍然为1

在B事务commit之后,A事务

mysql>update .... set....  where id = 2;// 0



需要A事务的sql应该写成这样

mysql>update .... set ...  where id = 2 and status = 1;

然后执行完sql语句之后,判断受影响的行数.如果受影响的行数为0,则说明数据库已经被其他事务更改了,否则修改成功.



# sleep和wait的区别

* 共同点

  都是让当前的线程进入到一种阻塞状态.

* 打破阻塞状态的条件不一样
  * sleep - 睡眠时间到,自动唤醒.
  * wait - 需要其他线程唤醒的.notify和notifyAll

# 模拟数据

模拟100万条数据

~~~sql
-- 先创建内存表[批量插入的速度比较] - 存储引擎 Memory
drop table memory_test;
create table memory_test(
	id int(7) primary key auto_increment,
    uid int(7),
    cid int(7),
    tid int(7)
) engine=Memory default charset=utf8;
~~~

~~~sql
-- 通过存储过程来进行插入
delimiter $$
create procedure memory_test_pro()
begin
	declare i int(7);
	set i = 0;
	while i<1000000 do
		insert into memory_test(uid,cid,tid) 						values(round(rand()*10000+1),round(rand()*10000+1),round(rand()*10000+1));
        set i = i + 1;
    end while;   
end $$    
delimiter ;
                                                                                       -- 调用存储过程
call memory_test_pro();                                                                 
~~~

-- 因为如果直接将大批量的数据直接插入到普通表中,性能会很低

-- 策略:先将数据保存到内存表中,然后再从内存表中把数据迁移到普通表中

~~~sql
drop table innodb_test;
create table innodb_test(
	id int(7) primary key auto_increment,
    uid int(7),
    cid int(7),
    tid int(7)
) engine=InnoDb default charset=utf8;
~~~

~~~sql
insert into innodb_test(select id,uid,cid,tid from memory_test);
~~~

# 索引

比如我们要在字典中找某一字，如何才能快速找到呢？那就是通过字典的目录。

对数据库来说，索引的作用就是给‘数据’加目录。创建所以的目的就是为了提高查询速度

## 索引算法

1. btree(二叉树)索引  log<sub>2</sub>N
2. hash(哈希)索引   1
3. B+Tree

## 优缺点

1. 好处:加快了查询速度(select )
2. 坏处:降低了增,删,改的速度(update/delete/insert),增大了表的文件大小(索引文件甚至可能比数据文件还大)

## 索引类型

1. 普通索引(index)：仅仅是加快了查询速度
2. 唯一索引(unique)：行上的值不能重复
3. 主键索引(primary key)：不能重复
4. 全文索引(fulltext):仅可用于 MyISAM 表，针对较大的数据，生成全文索引很耗时好空间。
5. 组合索引[覆盖索引]：为了更多的提高mysql效率可建立组合索引，遵循”最左前缀“原则。

## 索引语法

## 创建索引总览

~~~sql
CREATE TABLE table_name(
    [col_name data type]
	[unique|fulltext][index|key] [index_name](col_name[length]) [asc|desc]
)

-- 唯一索引
-- 第一种方式 - 添加唯一性约束 - 自动添加唯一索引
-- 第二种方式
create table tt01(
	id int(7),
    name varchar(20),
    -- 如果没有设置索引的名称,那么索引的名称默认和列名是一致的
    -- col_name(长度) - 只有varchar类型的,才会有length的概念
    -- 长度越大,区分度越明显.
    unique uq_index(name(3))
);

-- 长度和区分度的一个说明
-- name: 李三   李三子  李三姐   李四哥  李小龙三 李
-- 比如如果name(1) where name = '李'

~~~

1.  unique|fulltext为可选参数，分别表示唯一索引、全文索引
2. index和key为同义词，两者作用相同，用来指定创建索引
3. col_name为需要创建索引的字段列，该列必须从数据表中该定义的多个列中选择
4. index_name指定索引的名称，为可选参数，如果不指定，默认col_name为索引值
5. length为可选参数，表示索引的长度，只有字符串类型的字段才能指定索引长度
6. asc或desc指定升序或降序的索引值存储


## 索引使用方式

1. 查看某张表上的所有索引

   show index from tableName [\G,如果是在cmd窗口，可以换行];

2. 建立索引

   alter table 表名 add index/unique/fulltext 索引名 (列名) ; ---索引名可不写,不写默认使用列名

   CREATE INDEX  索引名 ON 表名(列值)

   alter table 表名 add primary key(列名) --不要加索引名，因为主键只有一个

3. 删除非主键索引

   alter table 表名 drop index 索引名；

4. 删除主键索引：

   alter table 表名 drop primary key;

## 查看查询是否使用到了索引

~~~sql
mysql>explain select id from innodb where id = 2222;
~~~



## 组合索引

(5)复合索引

代码如下:

~~~sql
mysql>create index ut_index on innodb_test(uid,cid);
~~~



## 索引失效情况

索引type从优到差：System-->**const-->eq_ref-->ref-->ref_or_null-->index_merge-->unique_subquery-->index_subquery-->**range-->index-->all

1. **如果索引了多列，要遵守最左前缀法则。**所谓最左前列，指的是查询从索引的最左前列开始，并且不跳过索引中的列

   总结:1. select 查询的列 要是索引列

   ​	2. 基于第一个要求.满足最左前缀法则,查询的列应该就是复合索引列.

   ~~~sql
   mysql>explain select uid,cid from innodb_test where uid = 2222;//using index
   mysql>explain select uid,cid from innodb_test where uid = 2222 or cid = 1000;//using index 
   
   //using index - 索引检索不回表
   //using where - 索引检索回表 - type - all - 全表扫描 - 索引相当于是失败的
   mysql>explain select * from innodb_test where cid = 2222;
   
   -- 查询的select后面跟的列中出现了非索引列tid
   -- 查询的列- 不能出现非索引列
   mysql>explain select uid,cid,tid from innodb_test where uid = 222;//Extra直接null
   
   -- Innodb存储引擎中是允许同时出现多种索引
   -- 复合索引(uid,cid)
   -- 聚集索引(id主键索引)
   mysql>explain select id,uid,cid from innodb_test where uid = 222;//use index
   
   -- 第一阶段是不会使用到索引的
   -- 但是经过mysql中的索引解释器会帮助我们进行纠正.
   -- 创建索引的顺序是uid,cid - 永远只会认识从最左边开始的索引列uid
   -- 实际不需要纠正的情况:uid=?   或者  uid=? and cid=?
   -- 但是直接使用cid =? 或者 cid=? and uid=?
   mysql>explain select uid from innodb_test where cid = 2222;
   mysql>explain select uid from innodb_test where cid = 2222 and uid = 1111;
   ~~~

2. **当MySQL一旦估计检查的行数可能会”太多”，范围查找优化将不会被使用。**

   ~~~sql
   mysql>explain select uid from xxx where uid<101111111111111;
   ~~~

3. **索引列不应该作为表达式的一部分，即也不能在索引列上使用函数 **???

   ~~~sql
   mysql>select uid from xxx where uid/10>1000;//跟DB版本..
   mysql>select uid from xxx where uid>10*1000;//推荐使用的
   mysql>select uid from xxx where round(uid)/10>10000;//不要在索引列加上函数使用
   ~~~

4. **尽量借用覆盖索引**，减少select * from …语句使用

5. **慎用left join语句,避免创建临时表** 使用left join语句的时候，避免出现创建临时表。尽量不要用left join，分而治之。非要使用的时候，要询问自己是不是真要必须要使用。

6. **谨防where子句中的OR**。where语句使用or，且没有使用覆盖索引,会进行全表扫描。应该尽量避免这样OR语句。尽量使用UNION代替OR

## 索引和排序

1. 检查的行数过多，且没有使用覆盖索引。第3句，虽然跟第2句一样，order by使用了索引最左前列uid，但依然使用**了filesort方式排[性能是及其低下]**序，因为status并不在索引中，所以没办法只扫描索引。

2. 使用了不同的索引，MySQL每回只采用一个索引.第4句,order by出现二个索引，分别是uid_fuid和聚集索引(pk)

3. 对索引列同时使用了ASC和DESC。 通过where语句将order by中索引列转为常量，则除外。第5句,和第6句在order by子句中，都出现了ASC和DESC排序,但是第5句却使用了filesort方式排序,是因为第6句where uid取出排序需要的数据,MySQL将其转为常量,它的ref列为const。

4. where语句与order by语句，使用了不同的索引。

   ~~~sql
   mysql>explain select uid from innodb_test where uid=2222 order by id;//出现filesort
   ~~~

5. where语句或者ORDER BY语句中索引列使用了表达式，包括函数表达式。参见第8，9句

6. where 语句与ORDER BY语句组合满足最左前缀，但where语句中使用了条件查询。查见第10句,虽然where与order by构成了索引最左有缀的条件，但是where子句中使用的是条件查询。

7. order by子句中加入了非索引列,且非索引列不在where子句中。

8. order by或者它与where组合没有满足索引**最左前列**。参见第11句和12句,where与order by组合，不满足索引最左前列. （uid, fsex)跳过了fuid

9. 当使用left join，使用右边的表字段排序。参见第13句，尽管user.uid是pk，依然会使用filesort排序。

   参考:http://www.cnblogs.com/wangxusummer/p/5329813.html

## 建立索引的策略

1. 主键列和唯一性列						√
2. 不经常发生改变的列					√
3. 满足以上2个条件,经常作为查询条件的列	√
4. 重复值太多的列						×
5. null值太多的列						×



## 总结

* 索引的分类
* 索引的底层 - B+Tree,存储形式key[列值]-value[表中行记录的物理地址]
* 索引失效的情况
* order by中如何正确使用索引. - order by where同时出现 - 列都应是同一种索引列,并且满足最左原则.
* 特别注意非索引列的出现.
* 推荐创建复合索引.
* 哪些列不适合创建索引.



# Limit子句

## Limit语法

Limit子句可以被用于强制 SELECT 语句返回指定的记录数。Limit接受一个或两个数字参数。

参数必须是一个整数常量。如果给定两个参数，第一个参数指定第一个返回记录行的偏移量，

第二个参数指定返回记录行的最大数目。

`//初始记录行的偏移量是 0(而不是 1):`

**mysql> SELECT * FROM table LIMIT 5,10; //检索记录行6-15**

`//如果只给定一个参数，它表示返回最大的记录行数目。换句话说，LIMIT n 等价于 LIMIT 0,n`

**mysql> SELECT * FROM table LIMIT 5; //检索前 5 个记录行**

## Limit的效率高？

常说的Limit的执行效率高，是对于一种特定条件下来说的：即数据库的数量很大，但是只需要查询一部分数据的情况。

高效率的原理是：`避免全表扫描，提高查询效率`。

比如：每个用户的email是唯一的，如果用户使用email作为用户名登陆的话，就需要查询出email对应的一条记录。

SELECT * FROM t_user WHERE email=?;

上面的语句实现了查询email对应的一条用户信息，但是由于email这一列没有加索引，会导致全表扫描，效率会很低。

**SELECT * FROM t_user WHERE email=? LIMIT 1;**

加上LIMIT 1，`只要找到了对应的一条记录，就不会继续向下扫描了`，效率会大大提高。

## Limit的效率低？

在一种情况下，使用limit效率低，那就是：只使用limit来查询语句，**并且偏移量特别大的情况**

做以下实验：

**语句1：**
select * from table limit 150000,1000;

**语句2:**
select * from table while id>=150000 limit 1000;//最常见的

语句1为0.2077秒；语句2为0.0063秒

两条语句的时间比是：语句1/语句2＝32.968

比较以上的数据时，我们可以发现采用where...limit....性能基本稳定，受偏移量和行数的影响不大，而单纯采用limit的话，受偏移量的影响很大，当偏移量大到一定后性能开始大幅下降。不过在数据量不大的情况下，两者的区别不大。

**所以应当先使用where等查询语句，配合limit使用，效率才高**

ps：在sql语句中，limt关键字是最后才用到的。以下条件的出现顺序一般是：where->group by->having-order by->limit

## 练习

* 分页参数pageNow当前页,pageSize每页显示多少条,总的条数pageRows

  pageNow和pageSize - > 数据搞出来.

  找规律找出s_emp

  1页 显示5条 - 第一页 pageNow = 1    limit m,pageSize

* 找出在Asia工作的员工的第2行到第4行记录

  ~~~sql
  mysql>select r.name,e.first_name from s_emp e join s_dept d
  on e.dept_id = d.id join s_region r on r.id = d.region_id where r.name = 'Asia'
  limit 1,3;
  ~~~

  

## 分页应用

通过limit可以实现分页功能。

假设 pageSize表示每页要显示的条数，pageNumber表示页码，那么 返回第pageNumber页，每页条数为pageSize的sql语句：

代码示例:

语句3：**select * from student limit (pageNumber-1)*pageSize,pageSize**

# 函数

## 语法

delimiter 自定义符号　<font color='red'>- 如果函数体只有一条语句, begin和end可以省略, 同时delimiter也可以省略</font>

create function 函数名(形参列表) returns 返回类型　　-- <font color='red'>注意是returns</font>

begin

　　函数体　<font color='red'>-- 函数内定义的变量如：set @x = 1; 变量x为全局变量，在函数外面也可以使用</font>

　　返回值

end

自定义符号

delimiter ;



### 注意点

方法：
	1. 必须有返回值
	2. 返回值指定类型 RETURNS
	3. 返回值通过     return 
## 示例

~~~SQL
-- 自定义函数
delimiter $$
create function myfun3(ia int, ib int) returns int
begin
    return ia + ib;
end
$$
delimiter ;

-- 调用函数
select myfun3(20,30)
~~~

## 查看函数

1. show function status [like 'pattern']; -- 查看所有自定义函数, 自定义函数只能在本数据库使用。

2. show create function 函数名; -- 查看函数创建语句

## 删除函数
drop function 函数名;



# 存储过程

SQL语句需要先编译然后执行，而存储过程（Stored Procedure）是一组为了完成特定功能的SQL语句集，经编译后存储在数据库中，用户通过指定存储过程的名字并给定参数（如果该存储过程带有参数）来调用执行它。

存储过程是可编程的函数，在数据库中创建并保存，可以由SQL语句和控制结构组成。当想要在不同的应用程序或平台上执行相同的函数，或者封装特定功能时，存储过程是非常有用的。数据库中的存储过程可以看做是对编程中面向对象方法的模拟，它允许控制数据的访问方式



## 存储优点

* **增强SQL语言的功能和灵活性**：存储过程可以用控制语句编写，有很强的灵活性，可以完成复杂的判断和较复杂的运算。

* **标准组件式编程**：存储过程被创建后，可以在程序中被多次调用，而不必重新编写该存储过程的SQL语句。而且数据库专业人员可以随时对存储过程进行修改，对应用程序源代码毫无影响。

* **较快的执行速度**：如果某一操作包含大量的Transaction-SQL代码或分别被多次执行，那么存储过程要比批处理的执行速度快很多。因为存储过程是预编译的。在首次运行一个存储过程时查询，优化器对其进行分析优化，并且给出最终被存储在系统表中的执行计划。而批处理的Transaction-SQL语句在每次运行时都要进行编译和优化，速度相对要慢一些。

* 减少网络流量：针对同一个数据库对象的操作（如查询、修改），如果这一操作所涉及的Transaction-SQL语句被组织进存储过程，那么当在客户计算机上调用该存储过程时，网络中传送的只是该调用语句，从而大大减少网络流量并降低了网络负载。

* 作为一种安全机制来充分利用：通过对执行某一存储过程的权限进行限制，能够实现对相应的数据的访问权限的限制，避免了非授权用户对数据的访问，保证了数据的安全。



## Mysql存储过程

存储过程是数据库的一个重要的功能，MySQL 5.0以前并不支持存储过程，
这使得MySQL在应用上大打折扣。好在MySQL 5.0开始支持存储过程，
这样即可以大大提高数据库的处理速度，同时也可以提高数据库编程的灵活性。



## 存储过程和函数的区别

* 函数必须指定返回类型,但是存储过程不需要
* 函数必须使用return+返回值,但是存储过程可以没有返回结果.

## 存储过程语法

~~~sql
CREATE PROCEDURE 过程名([[IN|OUT|INOUT] 参数名 数据类型[,[IN|OUT|INOUT] 参数名 数据类型…]]) [特性 ...] 过程体

--实例
DELIMITER //
  CREATE PROCEDURE myproc(OUT s int)
    BEGIN
      SELECT COUNT(*) INTO s FROM students;
    END
    //
DELIMITER ;
~~~



### 语法说明

* 分隔符

  MySQL默认以";"为分隔符，如果没有声明分割符，则编译器会把存储过程当成SQL语句进行处理，因此编译过程会报错，所以要事先用“DELIMITER //”声明当前段分隔符，让编译器把两个"//"之间的内容当做存储过程的代码，不会执行这些代码；“DELIMITER ;”的意为把分隔符还原。

* 过程体

  过程体的开始与结束使用BEGIN与END进行标识



## 简单示例

1. 定义存储过程 - 查询员工的平均薪资

   ~~~sql
   mysql>delimiter $$
   	  create procedure s_emp_salary_avg_pro()
   	  begin
   	  	select avg(salary) from s_emp;
   	  end $$
   	  delimiter ;
   ~~~

2. 调用存储过程

   ~~~sql
   call s_emp_salary_avg_pro();
   ~~~

3. 删除存储过程

   ~~~sql
   drop procedure s_emp_salary_avg_pro;
   ~~~

## 存储过程参数

- IN 输入参数：表示调用者向过程传入值（传入值可以是字面量或变量） 
- OUT 输出参数：表示过程向调用者传出值(可以返回多个值)（传出值只能是变量） 
- INOUT 输入输出参数：既表示调用者向过程传入值，又表示过程向调用者传出值（值只能是变量）

IN参数

```sql
delimiter $$
create procedure in_param(in p_in int)
begin
	select p_in;
	set p_in = 2;--局部变量
	select p_in;
	set @p_in2 = 10;--全局变量
end$$
delimiter ;

set @p_in = 1;
call in_param(@p_in);
select @p_in; # 的结果还是1
select @p_in2;
```

OUT参数

```java
delimiter $$
create procedure out_param(out p_in int)
begin
	select p_in; --#因为out是向调用者输出参数，不接收输入的参数，所以存储过程里的p_out为null
	set p_in = 2;
	select p_in;
	set @p_in2 = 10;
end$$
delimiter ;

set @p_out = 1;
call out_param(@p_out);
select @p_out;//调用了out_param存储过程，输出参数，改变了p_out变量的值
```

## 控制语句

- if..then - else..

  ```sql
  delimiter $$
  create procedure if_pro(in param int)
  begin
  	if param=0 then
  		select 1;
  	end if;
  	if param=2 then
  		select 2;
  	else
  		select 3;
  	end if;
  end $$
  delimiter ;
  
  set @n = 2;
  call if_pro(n);
  ```

- case语句

  ```sql
  delimiter $$
  create procedure case_pro(in param int)
  begin
  	# 仅仅是声明了一个局部变量
  	# delcare 变量名 类型(长度) [default 默认值]
  	declare var int;
  	set var = param + 1;
  	case var
  	when 1 then
  		select 1;
  	when 2 then
  	when 3 then
  		select 2;
  	else
  		select 3;
  	end case;
  end $$
  delimiter ;
  
  call case_pro(1);
  ```

- **循环语句** - java中的while语句

  ```java
  delimiter $$
  create procedure while_pro()
  begin
  	# 声明一个局部变量
  	declare vars int;
  	set vars  = 0;
  	while vars<6 do
          select vars;
  		set vars = vars+1;
  	end while;
  end $$
  delimiter ;
  
  call while_pro();
  ```

- repeat..end repeat - java中的do..while...

  执行操作后,加查结果

  ```java
  delimiter $$
  create procedure repeat_pro()
  begin
  	declare v int;
  	set v=0;
  	repeat
  		set v = v + 1;
  		select v;
  	until v>0
      end repeat;
   end $$
   delimiter ;
  call repeat_pro();
  ```

  

- **loop....endloop** - break跳出指定的标签...

  loop **循环不需要初始条件**，这点和 while 循环相似，同时和 repeat 循环一样不需要结束条件, leave 语句的意义是离开循环。

  ```sql
  delimiter $$
  create procedure loop_pro()
  begin
  	declare v int;
  	set v = 0;
  	loop_lable01:LOOP
  		select v;
  		set v = v +1;
  		if v>=2 then
  		# 跳出loop块
  		LEAVE loop_lable01;
  		end if;
  	end LOOP;
  	end $$
  delimiter ;
  call loop_pro()
  ```

- ITERATE迭代

  ```sql
  -- ITERATE
  DELIMITER //
    CREATE PROCEDURE proc8()
    BEGIN
      DECLARE v INT;
      SET v=0;
      LOOP_LABLE:LOOP
        IF v=3 THEN
          SET v=v+1;
          ITERATE LOOP_LABLE;
        END IF;
        INSERT INTO t VALUES(v);
        SET v=v+1;
        IF v>=5 THEN
          LEAVE LOOP_LABLE;
        END IF;
      END LOOP;
    END;
    //
  DELIMITER ;
  ```

  ## 将结果返回出来

  ```java
  delimiter $$
  create procedure orders_pro(in id int(20),out orno varchar(20),out pp double(7,3))
  begin
  	select orderno into orno from orders where id = id;
  	select price into pp from orders where id = id;
  end $$
  delimiter ;
  call orders_pro(3,@no,@p);
  ```

  ```sql
  delimiter $$
  create procedure orders_pro1(in id int(20),out orno varchar(20),out pp double(7,3))
  begin
  	select orderno,price into orno,pp from orders where id = id;
  end $$
  delimiter ;
  call orders_pro1(3,@no1,@pp);
  ```

  

  ### 实例

  -- 根据id查询员工的信息,并且将员工的first_name和salary返回出来.

  ~~~msql
  delimiter $$
  create procedure s_emp_pro(in ids int(7),out fname varchar(25),out sal float(11,2))
  begin
  	select first_name into fname from s_emp where id = ids;
  	select salary into sal from s_emp where id = ids;
  end $$
  delimiter ;
  
  call s_emp_pro(1,@fname,@sal);
  select @fname;
  select @sal;
  ~~~

  ~~~mysql
  delimiter $$
  create procedure s_emp_pro2(in ids int(7),out fname varchar(25),out sal float(11,2))
  begin
  	select first_name,salary into fname,sal from s_emp where id = ids;
  end $$
  delimiter ;
  
  call s_emp_pro2(1,@fname,@sal);
  select @fname;
  select @sal;
  
  ~~~

  

  ## 带事务

  ```sql
  delimiter $$
  create procedure tx_pro(in ids int(20),in pp double(7,3),out res varchar(20))
  begin
  	start TRANSACTION;
  	update orders set price = pp + price where id = ids;
  	if ids!=3 then
  		set res = "更新失败";
  		ROLLBACK;
  	else
  		insert into orders(orderno,price) values('1006',100.0);
  		set res = "更新成功";
  		COMMIT;
  	end if;
  end $$
  delimiter ;
  call tx_pro(3,200.0,@res);
  select @res;
  ```


## 示例代码

转账的业务:

~~~mysql
delimiter $$
create procedure transfer_acc_pro(in status int(7),in srcano varchar(20),in targetano varchar(20),in money double(7,2),out res varchar(20))
begin
	# 开启事务
	START TRANSACTION;
	
	# 它不会自动提交
	UPDATE t_account SET balance = balance - money WHERE accno = srcano;
	
	if status = 1 then
		set res = "更新失败!";
		select res;
		ROLLBACK;
	else
		UPDATE t_account SET balance = balance + money WHERE accno = targetano;
		set res = "转账成功!";
		select res;
	end if;
	COMMIT;
end $$
delimiter ;
set @no1 = '1009';
set @no2 = '1010';
call transfer_acc_pro(1,@no1,@no2,100.0,@res);

~~~



# 游标

MySQL检索操作返回一组称为结果集的行 ,这组返回的行都是与SQL语句相匹配的行（零行或多行）。使用简单的SELECT语句，例如，没有办法得到第一行、下一行或前10行，也不存在每次一行地处理所有行的简单方法（相对于成批地处理它们）。
有时，需要在检索出来的行中前进或后退一行或多行。这就是使用游标的原因。  



## 使用游标

**使用游标涉及几个明确的步骤。**

1.  在能够使用游标前，必须声明（定义）它。这个过程实际上没有检索数据，它只是定义要使用的SELECT语句。
2.  一旦声明后，必须打开游标 open 以供使用。这个过程用前面定义的SELECT语句把数据实际检索出来。
3.  对于填有数据的游标，根据需要取出（检索）各行。
4.  在结束游标使用时，必须关闭游标  close 

​      隐含关闭 如果你不明确关闭游标， MySQL将会在到达END语
​      句时自动关闭它。 

游标只能用于存储过程 不像多数DBMS， MySQL游标只能用于存储过程（和函数）。 

* 打开 游标 

```sql
open cursor;
```

* 关闭游标

```
close cursor;
```

* 获得游标中的数据

```
fetch names_cursor into v_name;
```

```sql
drop PROCEDURE cs_pro;
delimiter $$
create procedure cs_pro()
begin
	declare v varchar(100);
	declare v_i int(10) default 1;
	DECLARE done INT DEFAULT 0;
	# 将查询出来的所有的行记录保存到游标中
	declare cs cursor for select ordno from t_ord;
	# 将所有的行数赋值给了变量done - 利用select语句来进行赋值操作
	# select.... into ...
	select count(*) from t_ord into done;
	select done;
	select v_i;
	# 打开游标
	open cs;
  while done>=v_i do
  		# 将当前游标对应的值赋值给了变量v
		fetch cs into v;
		select v;
		set v_i = v_i + 1;
	end while;
	# 关闭游标
	close cs;
end $$
delimiter ;

call cs_pro();
```

## 游标练习

1. 查询出20部门所有员工的姓名	
2. 把查询20部门到的员工的姓名 放在一张新表中



# 触发器

在MySQL Server里面也就是对某一个表的一定的操作，触发某种条件（Insert,Update,Delete 等），从而自动执行的一段程序。从这种意义上讲触发器是一个特殊的存储过程。



## 分类

1. **AFTER**触发器 - 在触发条件之后去执行
2. BEFORE触发器 - 在触发条件之前去执行 

## 语法

~~~sql
DROP TRIGGER 触发器名称;
CREATE TRIGGER 自定义名称
触发时机  触发事件(insert,delete,update) ON 触发事件所在的表名
FOR EACH ROW
触发需要执行的逻辑;
~~~

~~~sql
create table trigger_tbl(select * from t_account where 1= 2);//保留表的结构
~~~

## 案例

* 向 t_account表中删除任意一条记录的时候,需要将该记录保存到trigger_tbl表中

  ~~~mysql
  drop trigger acc_tri;
  delimiter $$
  create trigger acc_tri
  after delete on t_account
  for each row
  begin
  	# 关于值的获取
  	# 如果表中有记录,则用old.列名
  	# 如果表中没有记录,则用new.列名
  	insert into trigger_tbl values(old.id,old.accno,old.balance);
  end $$
  delimiter ;
  ~~~

   t_account表中删除任意一条记录的时候都会触发该触发器

  ~~~sql
  mysql>delete from t_account where accno = '1010';
  ~~~

* 级联删除客户它的订单信息,根据客户的订单信息进行删除操作.

  ~~~mysql
  drop trigger cus_tri;
  delimiter $$
  create trigger cus_tri;
  before delete on t_customer;
  for each row
  begin
  	# 删除客户之前需要删除该客户所有的订单信息
  	delete from t_ord where cid = old.id
  end $$
  delimiter ;
  ~~~

  ~~~sql
  mysql>delete from t_customer where id = 1;
  ~~~

* mysql中是不允许check约束的.

  比如插入t_account表,balance列不能小于0

  ~~~mysql
  drop trigger acc_tri_ba;
  delimiter $$
  create trigger acc_tri_ba
  before insert on t_account
  for each row
  begin
  	declare msg varchar(20);
  	if new.balance < 0 then
  		set msg = "余额不能为负数";
  		# 触发器中不需要返回结果
  		# select msg;
  		# 如果给定的列不满足,则给定一个默认值的处理方式
  		# 如果抛出异常,那么仍然会插入成功,并且使用我们在触发器中给定的默认值.
  		set new.balance = 10.0;
  		# 抛出一个异常.则插入失败,并且显示失败的信息
  		SIGNAL SQLSTATE 'HY000' SET MESSAGE_TEXT = msg;
  	else
  		set msg = "插入成功";
  		# select msg;
  	end if;
  end $$
  delimiter ;
  ~~~

  ~~~mysq
  mysql>insert into t_account values(10,'1002',-1);
  ~~~

  



# 数据库优化

## 如何选择服务器的类型？

MySQL服务器配置窗口中各个参数的含义如下。 【Server Configuration Type】该选项用于设置服务器的类型。单击该选项右侧的向下按钮， 即可看到包括3个选项。

**3个选项的具体含义如**下：

1. Development Machine(开发机器)：该选项代表典型个人用桌面工作站。假定机器上运行 着多个桌面应用程序。将MySQL服务器配置成使用最少的系统资源。
2. Server Machine (服务器)：该选项代表服务器，MySQL服务器可以同其它应用程序一起 运行，例如FTP、email和web服务器。MySQL服务器配置成使用适当比例的系统资源。
3. DedicatedMySQL Server Machine (专用 MySQL 服务器）：该选项代表只运行MySQL服务的服务器。假定运行没有运行其它应用程序。MySQL服务器配置成使用所有可用系统资源。作为初学者，建议选择【DevelopmentMachine】（开发者机器）选项，这样占用系统的资源 比较少。

## MySQL中如何使用特殊字符？

诸如单引号（’），双引号（"），反斜线（)等符号，这些符号在MySQL中不能直接输入 使用，否则会产生意料之外的结果。在MySQL中，这些特殊字符称为转义字符，在输入时需要 以反斜线符号（’’）开头，所以在使用单引号和双引号时应分别输入（’）或者（")，输入反 斜线时应该输入（)，其他特殊字符还有回车符（ )，换行符（ )，制表符（ab)，退格 符（)等。在向数据库中插入这些特殊字符时，一定要进行转义处理。

1. MySQL如何执行区分大小写的字符串比较？


	在Windows平台下，MySQL是不区分大小的，因此字符串比较函数也不区分大小写。如果 想执行区分大小写的比较，可以在字符串前面添加BINARY关键字。例如默认情况下，’a’=‘A’返回结果为1，如果使用BINARY关键字，BINARY’a’=‘A’结果为0,在区分大小写的情况下，’a’与’A’并不相同。
4. MySQL语句优化技巧

  MySQL数据库性能的优化是MySQL数据库发展的必经之路，MySQL数据库性能的优化也是MySQL数据库前进的见证，下面介绍下MySQL语句优化的一些小技巧：
  1. 应尽量避免在 where 子句中使用!=或<>操作符，否则将引擎放弃使用索引而进行全表扫描。
  2. 对查询进行优化，应尽量避免全表扫描，首先应考虑在where及order by涉及的列上建立索引。
  3. 应尽量避免在where子句中对字段进行null值判断，否则将导致引擎放弃使用索引而进行全表扫描，如：

      select id from t where num is null
      	可以在num上设置默认值0，确保表中num列没有null值，然后这样查询：
      	select id from t where num=0
  4. 尽量避免在where子句中使用or来连接条件，否则将导致引擎放弃使用索引而进行全表扫描，如：

      select id from t where num=10 or num=20
      	可以这样查询：
      	select id from t where num=10
      	union all
      	select id from t where num=20
  5. 下面的查询也将导致全表扫描：(不能前置百分号)

      select id from t where name like ‘c%’
      	若要提高效率，可以考虑全文检索。
  6. in和not in也要慎用，否则会导致全表扫描，如：

      select id from t where num in(1,2,3)
      	对于连续的数值，能用between就不要用in了：
      	select id from t where num between 1 and 3
  7. 如果在where子句中使用参数，也会导致全表扫描。因为SQL只有在运行时才会解析局部变量，但优化程序不能将访问计划的选择推迟到运行时；它必须在编译时进行选择。然 而，如果在编译时建立访问计划，变量的值还是未知的，因而无法作为索引选择的输入项。如下面语句将进行全表扫描：

      select id from t where num=@num
      	可以改为强制查询使用索引：
      	select id from t with(index(索引名)) where num=@num
  8. 应尽量避免在where子句中对字段进行表达式操作，这将导致引擎放弃使用索引而进行全表扫描。如：
      	
      select id from t where num/2=100
      	应改为:
      	select id from t where num=100*2
  9. 应尽量避免在where子句中对字段进行函数操作，这将导致引擎放弃使用索引而进行全表扫描。如：
      	
      select id from t where substring(name,1,3)=’abc’–name以abc开头的id
      	select id from t where datediff(day,createdate,’2005-11-30′)=0–’2005-11-30′生成的id
      	应改为:
      	select id from t where name like ‘abc%’
      	select id from t where createdate>=’2005-11-30′ and createdate<’2005-12-1′
  10. 不要在where子句中的“=”左边进行函数、算术运算或其他表达式运算，否则系统将可能无法正确使用索引。
  11. 在使用索引字段作为条件时，如果该索引是复合索引，那么必须使用到该索引中的第一个字段作为条件时才能保证系统使用该索引，否则该索引将不会被使用，并且应尽可能的让字段顺序与索引顺序相一致。
  12. 不要写一些没有意义的查询，如需要生成一个空表结构：

       	select col1,col2 into #t from t where 1=0
       	这类代码不会返回任何结果集，但是会消耗系统资源的，应改成这样：
       	create table #t(…)
  13. 很多时候用exists代替in是一个好的选择：

       	select num from a where num in(select num from b)
       	用下面的语句替换：
       	select num from a where exists(select 1 from b where num=a.num)
  14. 并不是所有索引对查询都有效，SQL是根据表中数据来进行查询优化的，当索引列有大量数据重复时，SQL查询可能不会去利用索引，如一表中有字段sex，male、female几乎各一半，那么即使在sex上建了索引也对查询效率起不了作用。
  15. 索引并不是越多越好，索引固然可以提高相应的 select 的效率，但同时也降低了 insert及update的效率，因为insert或update时有可能会重建索引，所以怎样建索引需要慎重考虑，视具体情况而定。一个表的索引数最好不要超过6个，若太多则应考虑一些不常使用到的列上建的索引是否有 必要。
  16. 应尽可能的避免更新 clustered 索引数据列，因为clustered索引数据列的顺序就是表记录的物理存储顺序，一旦该列值改变将导致整个表记录的顺序的调整，会耗费相当大的资源。若应用系统需要频繁更新clustered索引数据列，那么需要考虑是否应将该索引建为clustered索引。
  17. 尽量使用数字型字段，若只含数值信息的字段尽量不要设计为字符型，这会降低查询和连接的性能，并会增加存储开销。这是因为引擎在处理查询和连接时会逐个比较字符串中每一个字符，而对于数字型而言只需要比较一次就够了。
  18. 尽可能的使用varchar/nvarchar代替char/nchar，因为首先变长字段存储空间小，可以节省存储空间，其次对于查询来说，在一个相对较小的字段内搜索效率显然要高些。
       19、任何地方都不要使用select * from t，用具体的字段列表代替“*”，不要返回用不到的任何字段。
  19. 尽量使用表变量来代替临时表。如果表变量包含大量数据，请注意索引非常有限（只有主键索引）。
  20. 避免频繁创建和删除临时表，以减少系统表资源的消耗。
  21. 临时表并不是不可使用，适当地使用它们可以使某些例程更有效，例如，当需要重复引用大型表或常用表中的某个数据集时。但是，对于一次性事件，最好使用导出表。
  22. 在新建临时表时，如果一次性插入数据量很大，那么可以使用select into代替create table，避免造成大量log，以提高速度；如果数据量不大，为了缓和系统表的资源，应先create table，然后insert。
  23. 如果使用到了临时表，在存储过程的最后务必将所有的临时表显式删除，先truncate table，然后drop table，这样可以避免系统表的较长时间锁定。
  24. 尽量避免使用游标，因为游标的效率较差，如果游标操作的数据超过1万行，那么就应该考虑改写。
  25. 尽量避免大事务操作，提高系统并发能力。


**总结：**

如何快速掌握MySQL?

1. 培养兴趣，夯实基础

  对于MySQL的学习来说， SQL语句是其中最为基础的部分，很多操作都是通过SQL语句来实现的。所以在学习的过程中，要多编写SQL语句，对于同一个功能，使用不同的实现语句来完成，从而深刻理解其不同之处。
2. 及时学习新知识，多实践操作

  数据库系统具有极强的操作性，需要多动手上机操作。在实际操作的过程中才能发现问题， 并思考解决问题的方法和思路，只有这样才能提高实战的操作能力。