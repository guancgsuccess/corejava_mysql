# 1 提纲

* 方法的定义和使用
* Java中求随机数的俩种方式
* Java中提供的格式化输出printf
* 条件语句
* 循环语句

# 2 方法

作用:将一段程序封装到方法中,目的是为了提高整段代码的复用性和拓展性.

定义的语法:

~~~java
修饰符 返回类型 方法名([参数列表]) [throws 异常列表]{
    //方法体
}

~~~

注意点:

 	1. 方法体中不能再去定义方法.
 	2. 自定义的方法并不是程序的主入口,如果希望自定义的方法被调用,那么仍然需要在main方法中进行调用.



## 2.1修饰符

### 2.1.1 访问权限修饰符

修饰符既可以用来定义类,也可以用来定义方法.

| 修饰符    | 意义     | 所有地方 | 本类 | 同包 | 子类                                  |
| --------- | -------- | -------- | ---- | ---- | ------------------------------------- |
| public    | 公有的   | √        | √    | √    | √                                     |
| protected | 受保护的 |          | √    | √    | √                                     |
|           | 默认的   |          | √    | √    | 子类如果和父类不在同一个包中,不能访问 |
| private   | 私有的   |          | √    |      |                                       |



### 2.1.2 其他修饰符

**static - 静态的** - 使用static修饰的方法,叫做`静态方法`,否则叫做`非静态方法`

final - 最终的,使用final修饰的类,是不允许修改的.

abstract - 抽象的 - OO的一个高级的特性[抽象和接口]



注意点:修饰符使用的时候是没有顺序的.

~~~java
public static void main(Strifng[] args){....}
static public void main(String[] args){....}
~~~



## 2.2返回类型

### 2.2.1 有返回类型

返回类型可以是八大种基本数据类型byte short int long float double char boolean

也可以是引用类型[包含自定义对象类型].比如java.lang.String等.

引用类型:类类型,接口,数组,枚举,注解.

注意点:一旦某个方法带有返回类型,那么在**该方法体中的最后一定要出现return + 结果;**

并且当程序执行到return语句的时候,那么该方法调用结束!

#### 2.2.2.1 返回结果

* 返回的结果的类型要和方法定义时的返回类型保持一致
* 返回的结果的类型可以小于方法定义时的返回类型[满足类型的自动转换]
* 如果返回的结果的类型大于方法定义时的返回类型,需要进行类型的强制转换

#### 2.2.2.2 调用

* 方法的返回类型是什么,那么就用什么类型去接[存储].
* 方法的返回类型是什么,我们可以使用比方法的返回类型更大的类型去接[存储].



### 2.2.2 无返回类型

使用void

注意:无返回类型的方法体中也是可以出现return语句的.作用:终止方法的执行.



## 2.3 方法名

满足"小驼峰"命名法则.



## 2.4 参数列表

作用:是告诉调用方法的人,在调用方法的时候,需要传入几个什么类型的参数.满足类型的自动转换.

比如:

~~~java
public void test(double i){
    
}
~~~



* 如果方法含有参数,带参的方法
* 如果方法没有参数,无参的方法



## 2.5 方法的调用

只需要关心是否是静态方法还是非静态方法.是否使用static关键字来进行修饰的.

* 非静态方法 

  1. 创建对象 - 调用谁[类]里面的方法,就去创建谁[类]的对象!一定要有"对象"存在.

     ​                - 通过new关键字来创建对象 **类名 对象名 = new 类名();**

  2. 对象调用 - 通过对象.方法()进行调用.

* 静态方法

  直接通过**类名.方法名()**进行调用.

### 2.5.1 补充

**前提:在同一个类中[方法的定义者和方法的调用者].**

* 静态方法可以直接调用静态方法
* 非静态方法可以直接调用静态方法和非静态方法
* 静态方法中是不能够直接调用非静态方法的,如果非要调用,仍然是通过对象来调用.



## 2.6 static关键字

特点:使用static修饰的成员[方法,属性],属于类拥有的.没有使用static修饰的成员,属于对象拥有.

类拥有的成员,使用类进行调用.对象拥有的,使用对象来调用.

注意点:一般情况下,尽量不要使用static关键字来修饰成员.因为使用static关键字修饰的成员.在jvm

加载类的时候,就会优先分配内存空间.特别是一些工具类中的方法,就喜欢使用static关键字来定义.



## 3 参数列表

作用:把方法类比成"工厂",那么参数就相当于"原料".

目的:是为了告诉方法的调用者,在调用方法的时候**必须**传入几个参数以及确定参数的类型.



## 3.1 参数的分类

* 形参 - 定义在方法的参数列表中的参数,并没有实际的值.

  ~~~java
  public void test(int age,String name){//此处的age和name就表示形参...
      //
  }
  ~~~

* 实参 - 在调用方法的时候,传入的具体的参数,它是有实际的值.

  ~~~java
  m.test(23,"java");//此处的23和"java"就代表实参
  ~~~

## 3.2 不定长参数列表

~~~java
public static void test(int... args){....}
~~~



# 导包

参见方法的调用.

场景:当方法的定义者和使用者不在同一个包下的时候.

在没有使用导包语句之前,需要

~~~java
tech.aistar.day03.aa.AA aa = new tech.aistar.day03.aa.AA();
~~~

通过import语句来导包.[一定是出现在package语句的下方的]

语法:

~~~java
import 包名.类名1;//精确导包
import 包名.类名2;//精确导包
或者
import 包名.*;//性能比较低下,每次都会进行查找
~~~

结论:如果调用内置的对象的时候,如果这个内置的对象存在于java.lang包下

那么不需要手动导入,它会自动到java.lang下去查找.如果不是在这个包下的内置的类在使用的时候

都是需要进行手动通过import语句进行导包的.



# Java中方法参数传递的方式

**结论:Java中只有值传递,并没有地址传递.**



## 争议的理论

表述:Java中的基本类型是按照值传递的方式,对象类型是按照地址[引用]传递的方式!

值传递 - 直接将变量中的值赋值给了方法的参数的变量.将值复制了一份.[文件的拷贝]

引用传递 - 将地址传递一份给了副本变量.[磁盘窗口],String类型除外.

java.lang.String类型虽然是一个对象类型,但是具有值传递的特性,



### 引用地址

~~~java
int[] arr = new int[]{1,2,3};//数组本身也是属于对象类型.引用类型
//习惯将arr叫做对象.实际上此处的arr并不是对象.
//引用和对象之间的关系???
//基本类型是保存在jvm的栈区,对象是保存在jvm的堆区.
//通过new关键字在jvm的堆区开辟一块区域来保存数组对象{1,2,3},
//然后将该对象在堆空间中的内存地址,赋值给了引用arr,保存在栈区
//总结:对象是存储在堆,引用是存储在栈.
//总结:只要看到new关键字,就一定会在堆区开辟新的空间[地址肯定就是不一样的]
~~~



# 重载方法

场景:一定是出现在**同一个类**中的同名方法.一个类中拥有的多个同名的方法[重载方法].

好处:简化API

特点:

* 出现在同一个类中,方法名要一样
* 如果参数的个数不一样的话,就不用考虑参数类型.
* 参数的个数一样的话,那么类型一定不一样[顺序].
* 重载的方法的修饰符和返回类型是可以不一样的.



# Java中求随机数的俩种方式

计算机语言中是没有绝对随机的.



## 通过java.util.Random类



学会使用API手册.步骤 d

* 索引 - 输入查询的类 - 精确找到[包]
* 快速定位到方法摘要
  * 查看该方法是否是静态的方法 - 决定调用的方式[对象还是类]
  * 查看该方法的返回类型以及参数列表 - 决定用什么类型存储以及调用的时候传入什么类型的数据以及参数的个数

* 如果确定了调用的方法是非静态的,那么我们需要快速定位到构造方法摘要



### 使用

//1. 导包 - 非java.lang下的类都是需要手动进行导入的

//2.使用的是int nextInt(int n)求随机数的方法 - 非静态的 - 创建Random对象

//3.创建对象 - 类名 对象 = new 类名();Random r = new Random();

//4.对象进行调用 -> int result = r.nextInt(10);//返回的是一个[0-10)之间的随机整数



## 通过java.lang.Math数学类

static double random();//返回的是[0.0,1.0)之间的随机小数.

笔试中经常混淆的

* static long round(double n);// floor((a + 1/2) * 2)

* static double floor(double n);

   返回最大的（最接近正无穷大）`double` 值，该值小于等于参数，并等于某个整数。



# 补充final关键字

使用final关键字修饰的变量叫做常量.一旦使用final关键字修饰变量赋值之后,那么这个变量将不允许改变.

并且常量的命名一般是全部采用大写字母组成,如果存在多个单词,则使用_隔开.
