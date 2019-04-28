# OO的抽象数据类型

* 抽象类(abstract class)
* 接口(interface)

# 抽象类

在业务中,父类只知道需要完成的功能有哪些,但是不知道功能的具体该如何去实现?



使用abstract修饰的类,同样使用abstract修饰方法,叫做抽象方法[只有方法的签名,没有方法体的方法].

~~~java
public abstract class Shape{
    //1. 可以有普通属性
    //2. 可以有抽象方法
    //3. 可以有普通方法
    //4. 可以存在构造器的,不能是私有的
}
~~~

## 特征

* **抽象类是不能够被实例化的**.不能够被new.

* 抽象类天生就是用来被子类继承的.

* 只有抽象类或者接口才有资格去定义抽象方法.

* 如果某个类继承了抽象类,那么这个子类必须要实现[重写]的**抽象方法.**除非这个子类本身也是一个抽象类.

* **抽象类也是不能够支持多重继承的**,java中实现多重继承的方式,只能通过接口的方式.

  

## 不能一起使用的修饰符

* final修饰符和abstract修饰符

  abstract修饰的类天生就是用来被继承,final修饰的类是绝对不允许被继承.

  abstract修饰的方法天生就是用来被子类重写,final修饰的方法是不允许被重写.

* abstract修饰符和private修饰符

* abstract修饰符和static修饰符

  静态方法是不存在多态的,是不能被重写的.

  ~~~java
  public class Sup{
      
      public static void test(){
          System.out.println("Sup");
      }
      
  }
  
  public class Sub extends Sup{
      public static void test(){
          System.out.println("Sub...");
      }
  }
  
  Sup s = new Sub();
  s.test();//Sup
  
  ~~~

  

## 抽象类的应用

抽象类的应用 - 模板设计模式

应用场景:如何在系统中制定一个"业务流程",但是不需要关注流程的具体的实现,让这些具体的实现延迟到子类中.

解决方案:

制定一个"顶级的业务流程"的抽象类,包含的要素如下:

* 定义若干个执行流程的业务方法[抽象方]
* 提供一个公开的final修饰的方法,组装这些抽象方法.

~~~java
public abstract class 小品{
    //定义若干个抽象的的方法
    //1. 开幕()...
    //2. 登台()...
    //3. 表演()...
    //4. 谢幕()...
    
    //控制业务方法的执行的顺序.并且子类是不允许修改这个顺序的.
    public final void test(){
        开幕();
        登台();
        表演();
        谢幕();
    }
    
}

public class 岳云鹏 extends 小品{
    //重写抽象父类中的所有的抽象方法
    //具体的业务流程的细节是在各个子类中完成的.
}

public class 张云雷 extends 小品{
    
}

小品 yue = new 岳云鹏();
yue.test();

小品 zyl = new 张云雷();
zyl.test();
~~~

# 接口

JDK8.0之前的解释:接口是一系列抽象方法以及公共的常量[enum枚举类型]的集合.

JDK8.0之后:接口中允许存在静态方法和使用default关键字来定义普通方法.

接口倾向于一种"协议",一种"契约",是一种"规范".屏蔽了"底层".

开发中,接口的作用:

* 制定的标准
* 团队协作中,分离,并行开发的一种模式.
* 告诉开发人员,需要完成的业务操作有哪些.
* 可以支持多重继承的



## 语法

使用insterface关键字来定义一个接口,但是经过编译之后,接口也是一个.class字节码文件.

接口的命名规范:通常要么是以I开头,要么是以able结尾.

~~~java
public interface 接口{
    //1. 接口中无构造器 - 不能被创建对象
    //2. 接口中的常量都是公开的静态的常量属性
    //3. 接口中只能出现抽象的方法[jdk8.0之前]
    //4. jdk8.0以后 - 通过default关键字来构建普通方法
    //5. jdk8.0以后 - 可以定义静态方法 - 接口名.方法名
}
~~~

## 接口的分类

1. 业务接口 - 仅仅包含一些抽象的方法.

2. 常量接口 - 仅仅包含常量属性

3. 标记接口 - 什么都没有,是用来进行类型判断的.

   一旦实现了某个标记接口,那么可以用instanceof来进行判断

   ```
   java.io.Serializable
   ```

## 常量接口

如果接口中只存在常量,那么这个接口有个特殊的名字 - 常量接口.

**用来管理应用程序中所有的常量** - 但是在JDK5.0以后被enum枚举类型替代了.

~~~java
//接口中的常量只能是公开的静态的常量属性
//public static final int CAR = 100;

//简写成:
//public int CAR = 100;

//最精简的方式
int CAR = 100;
~~~



## 方法

* JDK8.0之前

  ~~~java
   //3. JDK8.0之前 - 接口中只能是公开的抽象的方法
  //接口的作用 - 告知需要完成的功能有哪些?
  //public abstract void save();
  
  //精简:
  //public void save();
  
  void save();
  ~~~

* jdk8.0以后

  ~~~java
  //3. JDK8.0以后 - 支持静态方法的定义 - 接口名.方法名
  public static void crud(){
      System.out.println("CURD STATIC...");
  }
  
  //4. JDK8.0以后 - 可以通过default关键字来定义一个普通方法
  //可以通过接口的实现类进行调用
  public default void close(){
      System.out.println("close()...");
  }
  ~~~

## 类接口关系

* 类与类 - 继承 [单继承]
* 接口与接口 - 继承[可以是多重继承,多个接口之间用逗号隔开]
* 类和接口 - 实现[implements]
  * 如果某个类实现了接口,那么这个类必须要实现该接口中的所有的抽象方法,除非这个类本身也是一个抽象类.



## 面向接口编程

* 对象的编译时类型可以写成接口,对象的运行时类型可以写成接口的实现类.

* 方法的返回类型写成接口,方法的返回结果可以是接口的任何一个实现类的实例

* 方法的参数类型写成接口,可以传递该接口的任何一个实现类的实例 - **回调设计模式**

  

## 对比抽象类和接口

### 相同点

* 都不能够被实例化
* 都是属于抽象的类型.

### 不同点

* 抽象类只支持单继承,接口是支持多继承
* 抽象类中是可以存在构造器的,但是接口中是不允许的
* 抽象类中是可以存在普通属性的,接口中只能存在公开的静态的常量属性.

**优先使用接口.**



## 案例

1. 抽象父类 - Vehicle交通工具 -

   * 属性 id,name,speed

      - 抽象方法

   2. 子类 

      * Car - 汽车
        * 特有的属性: stWheel
        * 特有的方法 - run
      * Plane - 飞机 
        * 特有的属性: wing
        * 特有的方法 - fly

3. 接口 

   * 业务接口

     ~~~java
     public String getSpeend(Vehicle v);//判断是否超速
     ~~~

     

   * 常量接口 - 用来管理系统中所有的常量

   * 标记接口 - ICheckSpeed

     

4. 接口的实现类

5. 简单工厂类
6. 单元测试类




























