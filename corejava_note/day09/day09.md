# 多态的特征

* 对象的编译时类型决定了对象的访问能力 [编译时类型是声明该对象的类型]
* 对象的运行时类型决定了对象的行为能力 [运行时类型才是该对象真正的类型]



## 多态的应用

一个语言具备"多态"的特征,那么才认为叫做**面向对象**的语言!

javascript仅仅是**基于对象**的语言[不存在多态的特性]



典型的应用 - 面向父类编程



# 面向父类编程

* **对象的编译时类型写成父类,对象的运行时类型可以写成子类**

* 方法的返回类型写成父类 - [方法的执行结果的类型可以是子类]

  ~~~java
  public 父类 test(){
      //...
      //...
      return new 子类();
  }
  ~~~

* 方法的参数类型可以写成父类

  在传入值的时候,可以传入该父类的任何一个子类的对象

  ~~~java
  public void test(父类 变量){
      //...
  }
  
  test(new 子类());
  ~~~

# 工厂设计模式

Java中提供了23种设计模式.为了代码的拓展性和可维护性.

作用:根据传入的参数,返回某个类的具体的实例.

​         **将产品的消费者和产品的生产者解耦合**,让各自的模块"职责划分比较明确"

* 普通工厂模式 [不会使用到]
* **简单工厂模式[静态工厂模式]**
* **工厂方法模式**
* 抽象工厂模式 [暂时不了解]



## 简单工厂设计模式

 **将产品的消费者和产品的生产者解耦合**,让各自的模块"职责划分比较明确"

之前,在代码中是这样创建产品:

Product产品类是Car类的父类

~~~java
Product c = new Car();//此处想要使用car这个产品的时候,消费者需要自己生产这个产品,然后进行使用.
Product c2 = new Bike();//自己先生产产品,然后再进行使用
//弊端:使用者和生产者没有解耦合.
~~~

~~~java
Product c = new Car();//假设有100个地方都调用了.突然哪天Car类升级了...
//错误的想法是:对Car类中的方法进行改造
//直接对Car动代码是不符合软件开发中的"开闭原则" - 对外开放[扩展],对内最好不要进行修改!
//CarVersion2 extends Car

//凡是程序中出现new Car()的地方都要修改成new CarVersion2()
//不满足代码的可维护性和可拓展性
~~~



## 代码形式

将对象的创建的过程全部封装到工厂类中.

使用者只需要获取对象使用接口.降低了生产者和使用者之间的耦合性.

~~~java
//产品父类
public class Product{
    //...
}

//产品1号 - car
public class Car extends Product{
    //....
}

//产品2号 - plane
public class Plane extends Product{
    
}


//工厂类 - 专门用来实例化对象 - "生产产品".
//简单工厂的作用:根据传入的某个参数,来返回某个具体的实例的对象
public class ProductFactory{
    //定义若干个公开的静态的常量属性
    public static final int CAR = 1;
    public static final int PLANE = 2;
    
    //方法的返回类型写成父类 - [方法的执行结果的类型可以是子类]
    public static Product getInstance(int type){
        Product p = null;
        switch(type){
       		 case CAR:
        		p = new Car();
                break;
             case PALNE:
                p = new Plane();
                break;
    	}
        
        return p;
}

TestProduct.java

//谁使用,谁负责创建对象
//Product c = new Car();
//Product p = new Plane();

Product c = ProductFactory.getInstance(ProductFactory.CAR);//获取对象

~~~

观察以上的简单工厂的设计方式:

弊端:**工厂类本身仍然是不满足"开闭原则"的**

​         当出现一个新的产品的时候,仍然是需要修改工厂类的.



## 工厂方法设计模式

比较"繁" - 为每个不同的产品 - 对应单独的一个工厂.

虽然"繁" - 但是满足工厂类的"开闭原则"

~~~java
//产品父类
public class Product{
    //...
}

//产品1号 - car
public class Car extends Product{
    //....
}

//产品2号 - plane
public class Plane extends Product{
    
}

//工厂的顶级类
public class Factory{
    
}

//产品1号的工厂
public class CarFactory extends Factory{
    public static Product getInstance(){
        return new Car();
    }
}

//产品2号的工厂
public class PlaneFactory extends Factory{
    public static Product getInstance(){
        reutrn new Plane();
    }
}

Test.java
Product c = CarFactory.getInstance();
Product p = PlaneFactory.getInstence(); 
~~~

**弊端:虽然解决了"工厂类的"开闭原则 - 但是随着产品的增加,需要维护的工厂类也要跟着增加.**



# 反射创建对象

java.lang.Class<T> - 描述类的类,所有的类都是该类的对象而已.

class实例是由JVM创建的,我们只能去获取.

* static Class<?> forName(String className);//根据类的全限定名来获取类的class实例.

* T getInstance();//反射调用空参构造 - 创建对象



# static关键字

* static关键字来修饰属性
* static修饰代码块{} - 静态代码块
* static关键字来修饰方法
* 静态导入
* static修饰类 [4种内部类的实现,静态内部类]
* static关键字的应用 - **单例模式[懒汉,饿汉]**



## 修饰属性

使用static修饰的属性叫做静态属性 - 类变量 - 类拥有,对象共享的.

非静态的属性叫做普通属性 - 对象拥有的.



非静态属性和静态属性初始化的时机?

静态属性是JVM加载类进内存的时候,就会立即给静态属性分配空间以及初始化操作.并且初始化**只会初始化一次.**

非静态属性是创建对象的时候,才会给非静态属性分配空间以及初始化.



## 修饰代码块

代码块 - code block - {}

* 构造块 - 构造器 - 构造方法

  当创建对象的时候,就会执行构造块.时机要落后于普通代码.

* 普通代码块 - {}

  当创建对象的时候,会优先[**优先于构造块**]执行普通代码块中的程序.只要创建对象,就会执行.

  因此,普通代码块很少在项目中出现,如果有,一般是将普通代码中的代码迁移到了构造块中.

* 静态代码块 - static {}

  当jvm加载类的时候,优先会执行静态代码块中的程序,只执行一次.

  可以用来加载一些比较费时费力的配置文件.



## JVM加载类进内存的活动进程

* jvm加载需要的类进内存
* 给所有的静态成员分配空间
* 给所有的静态成员初始化
* 调用静态代码块
  - 如果创建了对象
    1. 给所有的非静态成员分配空间
    2. 给所有的非静态成员初始化
    3. 调用普通代码块
    4. 调用构造块



## 修饰方法

~~~java
//普通方法中是不能够去定义静态属性的
public void test(){
    static int id = 1;
    System.out.println(id);
}
~~~



## 静态导入

import static 包名.静态属性;

~~~java
//静态导入
import static java.util.Calendar.YEAR;
~~~



# 单例模式

作用:保证在一个应用系统中,某个类的实例在内存中拥有只有1个.

应用场景:在构建这个类的实例的时候,内部做了很多占系统资源的动作.比较耗内存的.

​		比如:连接池,数据源工厂.



## 饿汉模式

多线程的环境下仍然是线程安全的

~~~java
public class SingleTon{
    //2.自己提供该类的唯一实例.
    private static SingleTon instance = new SingleTon();
    
    //1. 提供一个私有的构造
    private SingleTon(){
        System.out.println("==只会出现一次==")
    }
    
    //3.提供一个公开的方法
    public static SingleTon getInstance(){
        return instance;
    }
}
TestSingleTon.java
SingleTon t1 = SingleTon.getInstance();
~~~



## 懒汉模式

这种写法在多线程的环境下就是错误的.

线程不安全 - 多线程环境下,不能保证SinleTonLazy的实例在内存中只有1个.

延迟单例的初始化的时机.

~~~java
public class SingleTonLazy{
    //2.自己提供该类的唯一实例.
    private static SingleTon instance = null;
    
    //1. 提供一个私有的构造
    private SingleTon(){
        System.out.println("==只会出现一次==")
    }
    
    //3.提供一个公开的方法
    public static SingleTon getInstance(){
        return instance == null ? instance = new SingleTon() : instance;
    }
}
~~~



## 解决方法1 - 性能比较低下的

~~~java
public class SingleTonLazy{
    //2.自己提供该类的唯一实例.
    private static SingleTon instance = null;
    
    //1. 提供一个私有的构造
    private SingleTon(){
        System.out.println("==只会出现一次==")
    }
    
    //3.提供一个公开的方法
    //性能比较低下的
    public static synchronized SingleTon getInstance(){
        return instance == null ? instance = new SingleTon() : instance;
    }
}
~~~



## 解决方案2 - 双重检测

~~~java
public class SingleTonLazy{
    //2.自己提供该类的唯一实例.
    private static SingleTon instance = null;
    
    //1. 提供一个私有的构造
    private SingleTon(){
        System.out.println("==只会出现一次==")
    }
    
    //3.提供一个公开的方法
    //性能比较低下的
    public static SingleTon getInstance(){
        //..
        if(null == instance){
            sychronized(SingleTonLazy.class){
                if(instance == null){
                    instance = new SingleTonLazy();
                }
            }
        }
        return instance;
    }
}
~~~

# final关键字

final - 最终的.不能改变的.

* 修饰局部变量

  ~~~java
  final int MAX = 1;//不能再次修改
  ~~~

* 修饰成员变量[属性] - 常量属性

  ~~~java
  public class TT{
      public final int DAY_OF_MONTH = 1;//不能改变
  }
  ~~~

* 和static关键字一起修饰属性

  静态常量属性

  赋值方式1:

  ~~~java
  public static final int CAR = 1;//不能改变
  ~~~

  赋值方式2:

  ~~~java
  public static final int CAR;
  //可以通过静态代码块进行赋值
  static{
      CAR = 1;
  }
  ~~~

* 修饰类

  final修饰的类是不能够被继承的.

* 修饰符方法

  final修饰的方法是不允许重写的.



# 预习

* 抽象和接口
* equals和hashcode方法的实现方式
* 包装类Interger的使用
* java.lang.String和java.lang.StringBuilder和java.lang.StringBuffer
* 正则表达式

















