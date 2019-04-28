# 面向接口编程

* 对象的编译时类型写成接口,对象的运行时类型写成实现类
* 方法的返回类型写成接口,方法的返回的结果的类型写成实现类
* 方法的参数列表的参数类型写成接口,调用方法的时候可以传入接口的任何一个实现类



## 回调+模板设计模式

场景:当你去商店买东西的时候,断货了,让你留下一个联系方式[回调接口].

​	下次到货[触发回调函数发生的事件]就打电话[调用回调函数].

~~~java
public interface IUserSao{
    
    void delById(int id);
	
    User[] findAll();
}

public class UserDaoImpl implements IUserDao{
    @Override
    public void delById(int id){
        //1. 加载驱动 ...
        System.out.println("加载驱动...")
        //2. 获取连接
        System.out.println("获取连接...")
        
        //个性的东西 - 删除操作
        System.out.println("delete from tbl_user where id = ?");
        
        //释放资源
        System.out.println("释放资源...")
    }
    
    @Override
    public void findAll(){
         //1. 加载驱动 ...
        System.out.println("加载驱动...")
        //2. 获取连接
        System.out.println("获取连接...")
        
        //个性的东西 - 删除操作
        System.out.println("select * from tbl_user");
        
        //释放资源
        System.out.println("释放资源...")
    }
}


~~~

需要对公共的部分 - "和数据库建立连接以及断开连接" - 抽象出来.

* 制定一个接口规范

  ~~~java
  public interface IM{
      public void dml();
  }
  ~~~

* 模板

~~~java
public class JdbcTemplates{
    
    public static void execute(IM im){//可以传入该接口的任何一个实现类
         //1. 加载驱动 ...
        System.out.println("加载驱动...");
        //2. 获取连接
        System.out.println("获取连接...");
        
        im.dml();//真正调用的是实现类中重写之后的dml()方法
            
       //释放资源
        System.out.println("释放资源...");
    }
}

~~~

改造刚刚的UserDaoImpl.java

~~~java
public class UserDaoImpl implements IUserDao{
    @Override
    public void delById(int id){
        JdbcTemplates.execute(new IM(){
            @Override
            public void dml(){
                System.out.println("delete from tbl_uesr where id = ?");
            }
        });
    }
    
    @Override
    public void findAll(){
        JdbcTemplates.execute(new IM(){
            @Override
            public void dml(){
                System.out.println("select * from tbl_user");
            }
        });
    }
~~~

# 内部类

同一个.java文件中可以出现多个class类,但是只能出现一个public类 - 不属于内部类

* 成员内部类

  将内部类作为外部类的成员存在.

  * 成员内部类中不允许定义静态属性
  * 创建语法:外部类.内部类 对象 = new 外部类().new 内部类();

* 静态内部类

  将内部类作为外部类的静态成员存在.如果静态内部类是public,那么可以上升成为"顶级类"

  * 不能访问外部类的普通属性
  * 创建语法1:外部类.内部类 对象 = new 外部类.内部类();

* 局部内部类

  局部内部类是出现在外部类的某个方法的方法体中,并且局部内部类的生命只能存在于该方法中.

  * 局部内部类不允许使用public修饰符修饰

* 匿名内部类 - 使用比较多



# 枚举类型

是JDK5.0以后用来替代常量接口的.枚举类型都是属于类型安全的.

使用enum关键字来定义枚举类型,默认的自定义的枚举类型都会自动去继承java.lang.Enum



## 特点

* 枚举类型中存在的就是公开的静态的常量属性

  UP,DOWN;

* 枚举类型中的每个枚举常量都是代表该枚举类型的实例.

* 存在构造 - 私有的

* 存在普通属性

* 可以存在抽象方法 - 每个枚举常量都要去实现这个抽象方法.

  

## 应用场景

状态性的东西 - 保证数据的类型安全.

~~~java
public class Order{
    private int id;
    
    private String ordNo;
    
    private OrderStatus status;
    
}

//订单的枚举类型
public enum OrderStatus{
    FINISHIED,
    UNFINISHIED;
}

Order o = new Order(1,"1001",OrderStatus.FINISHIED);
~~~



## 枚举类型和String之间的相互转换

~~~java
//1. 将枚举类型转成字符串 - 未来数据库的列的类型是没有枚举类型的
//需要将枚举类型转换成字符串才能够插入到列的类型为varchar这个列中.
//java.lang.Enum提供了String toString()
String sea = s.toString();
System.out.println(sea);

//2.反之,从db取出来的F,M仍然也是字符串类型.
String sex = "F";
Student s1 = new Student();
//将字符串转换成枚举类型 - Gender
Gender gender = Enum.valueOf(Gender.class,"F");

s1.setGender(gender);

System.out.println(s1);
~~~



# java.lang.Object类

这个类是所有类的根类,当一个类没有指定明确的父类的时候,都会默认继承这个类.



## equals和hashcode方法

**这个俩个方法是成对出现的.**

比较:

* 基本类型采用 == 进行比较的. == 永远比较的是内存地址.

  ~~~java
  int i = 3;
  int j = 3;
  i == j;//true
  ~~~

* java.lang.Object类中的提供的equals方法底层就是使用 == 比较的.

  如果希望比较的是对象中的属性值的话,那么我们需要重写equals方法.

  所以,对象类型的比较一般建议使用equals方法.



### 重写equals方法

* **非空性**
* **自反性**
* **一致性**
* 传递性
* 对称性

~~~java
private int id;

private String sname;

User user = new User();

s1.equals(user);

@Override
public boolean equals(Object obj){
    //非空性
    if(obj == null)
        return false;
    //自反性
    if(this == obj)
        return true;
    //一致性
    //需要进行类型的判断,如果不进行类型的判断
    //有可能出现java.lang.ClassCastException类型转换失败异常
    if(obj instanceof Student){
        Student s = (Student)obj;

        //正常开放中,只要提供比较id即可
        if(this.id == s.getId() && this.name.equals(s.getSanme()))
            return true;
    }
    
    return false;
}
~~~

练习:希望id和sname同时一样的时候,才人会是同一个对象.

## hashcode方法

哈希值 - 地址 - 对象是存储在堆空间中的.

尽可能保证equals返回false的时候,hashcode能够不一样.

但是如果equlas返回true的话,hashcode肯定是不一样的.



## java.lang.Class<T>

Class<?> getClass();

**这个类是用来描述类的类**.**所有的类都可以看成是这个类的实例.**

它是由JVM创建的.并且**一个类无论被实例化多少次,它在JVM中的class实例永远只有1个**

也是后面要学习的**Java反射技术的基础类.**



思考:某个对象的class实例由创建?

答:由JVM创建,我们只能获取class实例.



### 获取class实例的方式

* 对象.getClass();

* 类名.class

* 基本类型.class - int.class

* 包装类型.class - Integer.class

* Class.forName("类的全限定名"); - 推荐使用的

  需要强制抓取异常,并且如果类的全限定名不存在,则会出现java.lang.ClassNotFoundException



## finalize方法

不鼓励使用finalize方法.

**当GC回收垃圾对象的时候,在进行回收之前会去调用该对象的finalize方法.并且无论程序中是否出现异常**

**那么这个方法都会执行**.

作用:

* 使用**protected**修饰,子类可以覆盖该方法以实现资源清理工作,GC在回收对象之前调用该方法
* System.gc()增加了finalize方法执行的机会,但是不可盲目依赖它们
* Java语言规范并不保证finalize方法会被及时地执行,而且根本不会保证它们会被执行
* 可能会带来性能问题.JVM通常在单独的低优先级线程中完成finalize的执行
* 对象再生问题:finalize方法中,可将回收对象赋值给GC Roots可达的对象引用,从而达到对象再生的目的.
* finalize方法至多由GC执行一次.



**面试题:final finally finalize的区别**



## clone方法

如果希望对象可以使用clone方法,那么我们应该让类去实现可clone的接口,否则抛出java.lang.CloneNotSupportedException

返回对象的一个副本.

* 浅层clone - 浅拷贝 - java.lang.Object对象中clone方法默认就是浅拷贝的方式

  **基本类型拷贝一份给副本**,对象类型共享.String,Date类型除外.

  

* 深层clone - 深拷贝

  将所有的类型全部考本一份给副本.



# 作业

~~~java
1.定义图书操作业务接口 IBookBiz

有如下方法:
//添加图书
 void add(Book b);
//根据图书name 来删除指定图书
 void deleteByName(String name);
//打印出所有的图书信息
 void outputAllBooks();

2. 定义 数组图书管理业务实现类(ArrayBookBiz) 
实现 IBookBiz接口，
实现所有方法,
提示，给出如下属性
属性:
private static final int CAPACITY = 5;
private int count; //代表有效图书数量
private Book[] books = new Book[CAPACITY];
方法：
  就是实现接口中的所有方法

最后，写测试类，测试这三个方法.
~~~



~~~java
id  name  price  author
1   西游记  100   success
2   水浒传	 100   success
加入购物车>1
	===购物车界面===
id  name  price  author    数量
1   西游记  100   success    1
输入p,返回首页:>p
id  name  price  author
1   西游记  100   success
2   水浒传	 100   success
加入购物车>1
	===购物车界面===
id  name  price  author    数量
1   西游记  100   success    2

~~~

