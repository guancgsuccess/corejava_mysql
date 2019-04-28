# 复习

## 封装性

保证安全,屏蔽底层.

1. 将属性私有化 - private - 不能让外界直接进行访问
2. 提供getter/setter方法  - setter可以进行参数有效性判断  getter可以访问的时候进行过滤.

## setter和带参构造赋值的区别

* 带参构造是在**创建对象**的同时进行赋值,**赋值**的机会只有一次.
* setter是可以对同一个对象进行**多次赋值**

如果构造方法中也想对参数进行有效性判断的话.那么可以在构造中调用setter方法.

## this关键字

* 代表的是当前对象 - 调用该方法的对象.
* 可以出现在构造方法中,调用构造方法.this([参数列表]) - 前提:必须出现在构造的首行



# 实体类业务类

* 实体类:主要体现在保存数据,数据的载体.
* 业务类:主要体现在操作数据.数据的处理.



## 两者合二为一

将业务方法也放入到实体类中了.

~~~java
//实体类
public class Acccount{//银行账户实体类
    private int id;
    
    private String accno;
    
    private double balance;//余额
    
    //constructor getter/setter toString
    
    //业务方法 - 存钱
    public void addMoney(double money){
        if(money<=0){
            //.....
            return;
        }
        this.balance+=money;
    }
    
    //业务方法 - 取钱
    public void minuMoney(double money){
        if(money<=0 || money>this.balance){
            //...
            return;
        }
        this.balance-=money;
    }
}
//测试类
public class TestAccount{
    public static void main(String[] args){
        Account acc = new Account(1,"1001",100.00);
        acc.addMoney(100.0);
        accc.minuMoney(100.0);
        System.out.println(acc.getBalance());
    }
}
~~~



## 两者分开 - 推荐使用的

将业务方法专门封装到一个类中,这个类就叫做业务类.

~~~java
//实体类 - JavaBean
public class Acccount{//银行账户实体类
    private int id;
    
    private String accno;
    
    private double balance;//余额
    
    //constructor getter/setter toString
  
}
//业务类 - 业务方法如何进行设计
public class AccountBiz{
    
    //存钱
    public void addMoney(Account acc,double money){
        //判断
        if(money<=0){
            //...
            return;
        }
        double d = acc.getBalance();
        d+=money;
        acc.setBalance(d);
    } 
}
//测试类
public class TestAccount{
    public static void main(String[] args){
        //创建业务类
        AccountBiz biz = new AccountBiz();
        
        //创建的是账户类
        Account acc1 = new Account(1,"1001",100.00);
        
        Account acc2 = new Account(2,"1002",200.00);
        
        //思考,addMoney(参数)->参数->
        biz.addMoney(acc1,50.0);
        System.out.println(acc1.getBalance());
        
    }
}
~~~



### 练习

EmpBiz业务类

~~~java
//实体类
package tech.aistar.day07.homework;

import java.util.Arrays;

/**
 * @author success
 * @version 1.0
 * @description:本类用来演示:实体类和业务类合二为一
 * @date 2019/4/3 0003
 */
public class Emp {//自关联
    private int id;

    private String empName;

    private double salary;

    //一个上司拥有多个下属
    private Emp[] emps;

    //空参构造
    public Emp(){

    }

    public Emp(int id, String empName, double salary, Emp[] emps) {
        this.id = id;
        this.empName = empName;
        this.salary = salary;
        //构造方法中允许调用setter
        setEmps(emps);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Emp[] getEmps() {
        return emps;
    }

    public void setEmps(Emp[] emps) {
        this.emps = emps;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Emp{");
        sb.append("id=").append(id);
        sb.append(", empName='").append(empName).append('\'');
        sb.append(", salary=").append(salary);
        sb.append('}');
        return sb.toString();
    }
}

//业务类
public class EmpBiz{
    /**
    * 添加员工
    *@param boss 上司
    *@param e 普通下属
    */
    public void addEmp(Emp boss,Emp e){
        //判断是否添加自己...
        if(boss == e){
            //...
            return;
        }
        //只有上司才拥有添加下属的权限
        if(null == boss.getEmps()){
            //...
            return;
        }
        //对上司的数组进行扩容操作...
        Emp[] emps = Arrays.copyOf(boss.getEmps(),boss.getEmps().length+1);
        //放入下属
        emps[emps.length-1] = e;
        //重新设置
        boss.setEmps(emps);
    }
    
   /**
    * 根据员工的id进行删除操作
    */
    public void delById(Emp boss,int id){
        
    }
    /**
    * 1. 查[删,增,改]什么
    * 2. 去哪里[查,删,改,增]
    *
	* 根据员工的名字模糊出查询上司的下属.
	* 字符串的精确比较使用的是 java.lang.equals方法
	* 模糊查询 contains方法
	*/
    public Emp[] findByEmpName(Emp boss,String empName){
        
    }
    
    /**
	* 输出下属的信息
	*/
    public void outEmpInfo(Emp boss){
        //只有上司才拥有添加下属的权限
        if(null == boss.getEmps()){
            //...本身就是下属
            return;
        }
        
        if(boss.getEmps().length == 0){
            //...没有下属,请先添加
            return;
        }
        for(Emp e:boss.getEmps()){
            //...
        }
    }
}

//测试类
public class TestEmpBiz{
    public static void main(String[] args){
    	//业务类对象
        EmpBiz biz = new EmpBiz();
        
          //1.定义一个上司和下属
        //上司是需要设置emp[]
        Emp boss = new Emp(1,"boss",2500.00,new Emp[0]);

        //2.定义一个下属 - 没有添加员工的权限
        Emp e1 = new Emp(2,"tom",1000.00,null);
        Emp e2 = new Emp(3,"jack",2000.00,null);
        
        //调用添加下属 - 第一个参数代表的是上司,第二个参数代表的是下属
        biz.addEmp(boss,e1);
        
        //输出下属的信息
        biz.outEmpInfo(boss);
    }
}
~~~



# 继承

OO - Encapsulation Inheritance Polymorphism 

应用场景:

教务管理系统 - Teacher[id,tno,name,age,gender] Student[id,sno,name,gender]

考虑到代码的复用性 - 需要将共性的成员提取出来 - 单独放入到一个"类"中.

**父类中存放的是子类中共有的属性.**

满足子类继承父类的规则,一定要满足**子类 is 父类**的关系.

父类 - 超类 - 超级类 - 根类 - 基类

## 继承的好处

* 子类可以共享父类中的非私有的成员.提高了代码的复用性

* 提高了代码的可维护性[修改一些共用的属性]

  

## 继承的缺陷

* 父类变,子类就必须变
* 继承破坏了封装,对于父类而言,它的实现细节对子类来说都是透明的
* 继承是一种**强耦**合关系.

软件开发的原则:高内聚,低耦合



## 继承的使用

* 使用extends关键字来指明父类.
* Java中只支持单重继承[C++是支持多重继承].一个类只能拥有一个**直接的**父类.

* 单继承的方式是保证了类的层次性,避免出现网状结构.

  ~~~java
  A extends B
  ~~~

* 如果一个类没有显式地指明它的父类的话,那么这个类默认会去继承java.lang.Object

* Java中的继承具有传递性

  ~~~java
  A extends B extends C
  A可以共享B,也可以共享C
  ~~~

* Java中如何实现多重继承[接口的方式,接口是允许的.]



## 总结继承四句话

当子类继承了某个父类之后:

* 子类拥有父类中的所有的成员[包含私有的],但是子类只能使用非私有的成员.
* 父类中的构造器是不会被子类继承的.
* 子类中可以拥有"个性"的成员 - 对父类的拓展.
* 子类中可以重写"父类"的方法.



## 子类被实例化的过程

当创建子类对象的时候,会优先先创建父类的对象.

* 分配空间
* 初始化[成员]
* 调用构造块

## super关键字

### 作用 - 构造

父类的构造器是不能够被继承的.

如果类的构造器中没有显式地写super语句的话,那么系统会默认分配一个super()

**如果是super() - 调用父类的构造方法 - 也是必须出现在构造块的首行的位置**

**还可以使用super(参数列表) - 来调用父类中已经存在的对应的构造方法.**

### 作用2 - 显式去调用父类的成员

场景:当子类中存在和父类中同名的成员的时候,如果不使用super关键字,那么默认调用的就是子类自己的.

如果想要调用父类的,必须使用super.成员进行调用.



# 方法的重写

面试题:方法的重载和方法的重写的区别

应用场景:一定是出现在父子类关系中.

* 方法名肯定是一样的.
* 方法的参数列表应该是一样的.
* 子类方法的返回类型应该是父类方法返回类型的子类或者是相同的.
* 子类的访问修饰符应该小于或者等于父类的访问修饰符[public<protect<缺省<private]
* 子类的方法的抛出的异常列表应该是父类方法抛出的异常列表的子类或者是相同的.

当方法名,参数列表,抛出的异常列表,返回类型,访问修饰符,父子类之间高度保持一致的情况 - **重构**



思考:为什么要有方法的重写?

原因是:希望能够使用父类作为对象的编译时类型的时候,仍然是可以访问到该方法的.具体调用的时候

是调用子类重写之后的.



# 多态

发生的条件:

* 存在父子类关系
* 一定要有方法的重写

特征:

* 对象的编译时类型 - 决定了对象的访问能力
* 对象的运行时类型 - 才是对象真正的类型 - 决定了对象的行为能力.

~~~java
编译时类型 对象 = new 运行时类型()
~~~

**面向父类编程特征之一:对象的编译时类型可以写成父类,对象的运行时类型可以写成子类.**

~~~java
Animal cat = new Cat();
1. cat的编译时类型就是Animal,cat对象只能访问到Animal中的非私有的成员,也包括Animal继承过来的成员.
   Cat子类中提供的特有的方法catchMouse()方法.cat对象是访问不到的.
2. cat的运行时类型就是Cat - 所谓的行为能力 - 如果父类中提供了某个方法,子类中重写了该方法的话.
   那么在真正运行该方法的时候,调用的是子类中重写之后的.
~~~



如果在业务中,使用父类作为编译时类型之后,某个阶段,非要调用子类中特有的方法,该怎么办?

需要进行类型的强制转换.

~~~java
父类 对象1 = new 子类();
子类 对象2 = (子类)对象1;
~~~

在**向上转型**的时候,如果不进行类型的判断,容易出现java.lang.ClassCastException类型转换失败异常.



## instanceof关键字

用来判断类型  A is B



# 作业

1. 设计一个形状类Shape,方法:求周长和求面积
      形状类的子类:Rect(矩形),Circle(圆形)
      Rect类的子类:Square(正方形)
      不同的子类会有不同的计算周长和面积的方法
      创建三个不同的形状对象,放在Shape类型的数组里,
      分别打印出每个对象的周长和面积

2. 某公司的雇员分为以下若干类：
   Employee：这是所有员工总的父类，
   属性：员工的姓名,员工的生日。
   方法：getSalary(int month) 
   根据参数月份来确定工资，
   **如果该月员工过生日，则公司会额外奖励100元。**

   2-1. SalariedEmployee：
   Employee的子类，拿固定工资的员工。属性：月薪

   2-2. HourlyEmployee：
   Employee的子类，按小时拿工资的员工，
   属性：每小时的工资、每月工作的小时数
   每月工作超出160小时的部分按照1.5倍工资发放。

   2-3. SalesEmployee：
   Employee的子类，销售人员，工资由月销售额和提成率决定。
   属性：月销售额、提成率

   2-4. BasedPlusSalesEmployee：
   SalesEmployee的子类，有固定底薪的销售人员，
   工资由底薪加上销售提成部分。属性：底薪。

写一个程序，把若干各种类型的员工放在一个Employee数组里，
写一个函数，打印出某月每个员工的工资数额。
注意：要求把每个类都做成完全封装，不允许非私有化属性。