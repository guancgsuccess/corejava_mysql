# JavaAPI中提供的日期处理类

JDK8.0之前提供了两种:

* java.util.Date 日期处理类
* java.util.Calendar 日历处理类
* java.text.SimpleDateFormat 日期格式化处理类

JDK8.0之前关于日期的API是**非常烂**的!

**JDK8.0中提供了新的日期的API - 等统一讲解JDK8.0新特性.*[LocalDate,LocalTime,LocalDateTime]



## java.util.Date

java.sql.Date是java.util.Date的子类.



### 构造方法

* ***Date() - 获取当前系统的日期***
* *Date(long date);//通过毫秒数来构建一个Date对象*
* Date(int year-1900,int month-1,int date);//通过指定的年月日来构建一个日期对象  **已经过时的**



### 常用方法

> 1. 获取日期的字段信息的
>
>    * int getYear();//获取年份 + 1900
>    * int month();//获取月份 - [0-11]
>    * int getDay();//获取周几
>    * int getDate();//获取的几号[一个月当中]
>    * int getHours();//获取的小时
>    * int getMinutes();//获取分数
>    * int getSeconds();//获取的是秒数
>    * long getTime();//获取当前系统的毫秒数 - 距离的是1970年1月1日的毫秒数.
>
> 2. 设置日期的字段信息的
>
>    * void setYear(int year - 1900);//设置年份
>
>    * void setMonth(int month - 1);//设置月份
>
>      .....
>
>    * void setTime(long time);//将时间毫秒数转换成Date类型.



#### 关于Java的时间起点

* 1969年 - unix操作系统诞生的日期,为了让毫秒数多用一点,往后延续了一年.
* 以前的操作系统是32位 - 最大的数(2^31-1) 约等于68年,如果计算机使用的仍然是32bit.顶多支持到时间到2038年.
* Java中处理的时间的一个范围应该是1900[包含]年以后 - 万历年



#### 注意点

Java中的日期对象是不能够进行运算的.



### 毫秒数和Date之间的转换

* **毫秒数转换成Date类型**
  * 构造Date(long date)
  * void setTime(long date);
* 日期转换毫秒数
* long getTime();



## java.util.Calendar日历处理类

它是抽象类 - 特点:是不能够被实例化.Calendar cal = new Calendar();//error



### 创建Calendar对象

* 面向父类编程 - 对象的编译时类型写成父类,对象的运行时类型写成子类.

  父类 对象 = new 子类();//并且对象只能调用父类中的提供的方法.

  对象的编译时类型决定了对象的权限.

  ~~~java
  Calendar cal = new GregorianCalendar();
  ~~~

* 利用简单工厂的设计模式来构建一个日历对象 - 推荐使用的方式

  ~~~java
  Calendar cal = Calendar.getInstance();
  ~~~

### 常见的方法

* 获取日历字段信息的

  int get(int field);

* 设置日历字段信息

  * void set(int field,int val);//给日历的某个字段进行设置值

  * void set(int year,int month,int date);//

### Date和Calendar之间的相互转换

* Calendar - > Date
  * Date getTime();

* Date - > Calendar
  * void setTime(Date);



### 总结

灵活掌握long<->Date<->Calendar之间的相互转换



# java.text.SimpleDateFormat 日期处理类

## 构造方法

* SimpleDateFormat(String parttern);

### 日期模板

* yy/yyyy - 年份  
* MM - 月份
* dd - 日
* HH - 时
* mm - 分
* ss - 秒
* EEE - 比如星期五

~~~java
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//如果传入的是一个非法模板,将会抛出java.lang.IllegalArgumentException - 非法参数异常
~~~

## 常用方法

* 格式化日期对象 - Date转换成String

  String format(Date date);//格式化Date为一个日期字符串.

* String - > Date

  Date parse(String sdate);//解析日期形式的字符串为日期对象.

​       调用该方法的时候,需要强制抓取异常.alt+enter生成

​      如果模板和日期的形式不一致的话,将会抛出一个解析失败异常java.text.ParseException

## 面向父类编程

类A是类B的父类 -> 父类中的非私有的成员是可以被子类共享的[访问的]

B b = new B();//此处的子类B的对象b是可以访问这个类B的父类A中的**非私有的成员的.**

java.text.DateFormat类是java.text.SimpleDate的父类.所以SimpleDateFormat的对象是可以访问DateFormat中的非私有的成员的[包括属性或者方法的].



## 作业

* 封装格式化日期和解析字符串俩个方法到DateUtil.java中.

* 实现日历

  启动程序:

  请您输入年份:2012

  请您输入月份:2

  ​		2012年2月

  日	一	二	三	四	五	六

  ​		         1*      2     3     4

  5

  ....

  . .

  ..                      29

  

  思路:

  1.  求出某年某月的最大天数  1 - max

     2. 求出今天是几号 - 数字加上*

     3. 某年某月的1号是周几 - 周三

        0 - 2 这个位置输出\t

        每次输出\t还是数字,计数器,每7个就换一行.

* 数组  - 长度是5个 - 存放的是值是1-6之间的随机整数,但是不允许重复.

  ~~~java
  int[] arr = new int[5];
  //伪代码
  for(int i=0;i<arr.length;i++){
      //随机生成1-6之间n
      //n放入到arr中
      
  }
  ~~~

  
