# 面向对象

* OO - Object Oritened - 面向对象
* OOAD - Object Oritened Analysis Design - 面向分析与设计
* OOP - Object Oritened Programming - 面向对象编程



## 核心

面向对象思想是符合人类思维的思想.

面向过程[典型的就是C语言] - 程序 = 算法 + 数据结构[拿到一个业务,优先考虑的就是怎么实现,具体的怎么去操作]

面向对象[java,python,php,c#] - 程序 = 对象 [万物皆对象].



拿到一个业务,优先利用OO的思想,找出业务中的"**关键的"对象**,然后分析出该对象的**属性(该对象拥有什么)**,

接着分析该对象的**功能(操作对象的方法)**,接着我们还需要考虑**对象与对象之间的关系**.最后才是考虑怎么去

实现[算法+数据结构].



## 案例

小明去餐馆吃饭::

* 面向过程的思想:材料?怎么做出来的?火候?
* 面向对象的思想:只关心吃什么,不关心怎么做出来的? - 服务员给一个菜单即可.[每道菜其实就是一个对象].



## 面向对象特性

* 封装
* 继承
* 多态
* 抽象
* 接口



## 类和对象

把多个对象之间的共性的特征,抽象出一个"类"别出来.

类是一个抽象的概念,对象是一个具体的.

类是构建对象的"蓝图",对象是类的具体的实例化.

水果 - > 苹果,香蕉   动物->..   交通工具->...



## 实体类

* 属性
* 构造
* getter/setter
* toString

### 属性

**成员(Member)包含属性和方法的.**

如果属性不给定一个初始值的话,那么系统会自动分配一个默认值.



语法:

~~~java
修饰符 数据类型 属性名 [= 初始值];
~~~

#### 修饰符

1. 访问修饰符

   |                         | 所有的地方 | 子类 | 同一个包 | 本类中 |
   | ----------------------- | ---------- | ---- | -------- | ------ |
   | public  公共的,公有的   | √          | √    | √        | √      |
   | protected 受保护的      |            | √    | √        | √      |
   | 缺省的                  |            |      | √        | √      |
   | private 私有的 - 推荐的 |            |      |          | √      |

2. 其他修饰符

   final static abstract

#### 数据类型

可以是基本数据类型和对象类型.

#### 属性名

满足"小驼峰"的命名法则.

#### 如何进行封装

* 属性私有化 - 将属性的修饰符设置成private
* 提供getter/setter方法 - 获取属性/设置属性

### 构造

特点:

1. 构造方法是不需要指明返回类型的
2. 构造方法是允许重载的.

~~~java
修饰符 类名([参数列表]){
    
}
~~~



* 无参构造

  如果实体类中没有手动指定构造的话,那么**系统默认会分配一个空参构造.**

  如果手动指定一个带参构造,那么将不再分配一个空参构造.

* 带参构造

  **作用:就是可以在创建对象的同时,给对象的属性进行赋值操作.**

  注意:**只能给同一个对象赋值一次**.

  ~~~java
  //通过new关键字在堆中创建了一个学生对象,并且将对象的内存地址赋值给了引用s1
  Student s1 = new Student(1,"tom",23);
  //又在堆空间中开辟了区域,创建新的对象,并且能将新的对象的内存地址赋值给了应用s1
  s1 = new Student(1,"success",18);//算不算给第一次对象重新赋值???
  //实际上内存中存在了俩个对象,当执行到第二句代码的时候,那么第一次创建的对象会被GC回收
  //成为了一个垃圾对象,此处并不是修改了第一个对象的属性值.
  ~~~

  

#### this关键字

代表的就是当前对象

作用:

* 用来区分方法参数和属性
* 调用本类中的构造 - this语句必须出现在首行.

### getter/setter

getter方法 - 可以进行筛选

setter方法 - 进行参数有效性判断

​		   - 可以对属性进行多次赋值.

命名的规范推荐使用:set+属性名[首字母变成大写]

​				    get+属性名[首字母变成大写]



### toString

功能:返回对象的字符串表现形式

~~~java
Student s1 = new Student(1,"tom",23);
//打印对象的时候,默认会去调用该对象的toString方法
//当一个类没有指定父类的时候,那么这个类默认会去继承java.lang.Object
//如果某个对象中没有提供toString方法,Student的父类就是Object类
//那么就会去调用父类中的toString方法[this.getClass().getName() + "@" + Integer.toHexString(this.hashCode());]
System.out.println(s1);//tech.aistar.day07.Student@28d93b30
System.out.println(s1.toString());
//如果本类中提供了toString - 重写了Object中的toString方法的时候,那么调用自己的 - "就近原则".

System.out.println("myAddress:"+s1.getClass().getName()+"@"+Integer.toHexString(s1.hashCode()));

Student s2 = new Student(1,"tom",23);
System.out.println(s2);//tech.aistar.day07.Student@1b6d3586
// == 永远比较的是内存地址
System.out.println(s1 == s2);//false
~~~



# 对象与对象之间的关系

关系的横向

* 1:1
* 1:N
* 自关联
* N:N -> 暂时先不考虑

关系的方向性:

* 单向关联
* 双向关联



## 一对一

优先选用单向关联.

### 双向关联 - 彼此维护

~~~java
public class Husband{
    private int id;
    
    private String name;
    
    //一个丈夫拥有一个妻子
    private Wife wife;
}

public class Wife{
    private int id;
    
    private String name;
    
    //一个妻子只能被一个丈夫享用
    private Husband husband;
}
~~~



### 单向关联

关系仅仅由一方进行维护

~~~java
public class Husband{
    private int id;
    
    private String name;
    
    //一个丈夫拥有一个妻子
    private Wife wife;
}

public class Wife{
    private int id;
    
    private String name;
    
    //一个妻子只能被一个丈夫享用
    //private Husband husband;
}
~~~



## 一对多

一个客户拥有多个订单,

一个订单只能属于一个客户

### 双向关联

~~~java
public class Customer{
    private int id;
    
    private String phone;
    
    //一个客户拥有多个订单
    private Order[] orders;
    
    public void setOrders(Order[] orders){
        this.orders = orders;
    }
}

TestCustomer.java
Customer c1 = new Customer(1,"15906128572");
//创建一个订单数组 
Order[] orders = new Orders[1];
//将订单对象放入到订单数组中
Order o1 = new Order(1,100,"101",new Date());
orders[0] = o1;

c1.setOrders(orders);


public class Order{
    private int id;
    
    private double price;
    
    private String ordNo;
    
    private Date createDate;
    
    //多个订单对应一个客户
    private Customer customer;
    
}
~~~



### 单向关联

~~~java
public class Customer{
    private int id;
    
    private String phone;
    
    //一个客户拥有多个订单
    private Order[] orders;
    
    public void setOrders(Order[] orders){
        this.orders = orders;
    }
}

public class Order{
    private int id;
    
    private double price;
    
    private String ordNo;
    
    private Date createDate;
    
    //多个订单对应一个客户
   // private Customer customer;
    
}

~~~

## 自关联

自己关联自己

~~~java
public class Emp{
    private int id;
    
    private String empName;
    
    //一个上司拥有多个下属
    //一个下属只能拥有一个直接的上司
    private Emp[] emps;
}

//创建一个上司对象
Emp boss = new Emp(1,"success",new Emp[0]);

//创建一个下属对象
Emp e1 = new Emp(2,"王硕",null);

boss.addEmp(e1);

public void addEmp(Emp emp){
    
}
~~~



## 作业

~~~java
作业:

一.写一个描述计算机的类
含如下属性:
	品牌 (如 联想\DELL\IBM 等)
	价格 (如 4679.8)
	描述 (对计算机的描述)
	
	显示器 (显示器信息)

含如下方法:
	构造方法
	所有属性的 getter/setter方法
	提供 toString() 方法, 此方法的作用是
		输出当前计算机对象的品牌\价格\描述,以及显示器的信息.
	

注：
针对 价格，不能低于1000, 也不能超过20000;
对于超出这个值的，给一个默认值：2000

二.写一个描述 显示器 的类
含如下属性:
	显示器类型 (如CRT ,液晶的,LED的)
	显示器尺寸 (如 17寸\23\19\25)
	显示器品牌 (如:三星\ViewSonic\SONY 等)
	
含如下方法:
	构造方法
	所有属性的 getter/setter方法
	
注：对于显示器尺寸，必需是 17\23\19\25	，其它尺寸都不合格，
对于输入不合格的尺寸，统一规定是 17。


二.完成员工类 Employee,需要包含员工的基本属性
(编号,姓名,工资)和上司以及下属,提供如下方法:
属性的存/取方法
构造方法
业务方法包含:
   添加下属的方法
   根据id进行删除
   输出自己所属下属的方法
   获取自己下属的人数个数.
~~~



## 作业2

百度面试题:

随机生成1-5之间的整数,随机15个.

写一个程序,统计每个随机数出现的次数.

然后根据出现的次数进行降序排.



提示:

1. 确定二维数组的行数,列就是2列

2. 第一列显示的是随机数,第二列显示的是次数

   [1,2,3,2,1,2,3,2,1,1,1] -> 直接调用排重的方法 -> arr.length

3. 排序





















