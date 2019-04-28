# 正则表达式(Rgex)

作用:

* 给定字符串是否匹配正则表达式的规则["过滤器"]
* 从一段字符串中,找出符合正则表达式规则的字符串.



## 规则

1. 预定义字符

   ~~~java
   . 任何字符（与行结束符可能匹配也可能不匹配） 
   \d 数字：[0-9] 
   \D 非数字： [^0-9] 
   \s 空白字符：[ \t\n\x0B\f\r] 
   \S 非空白字符：[^\s] 
   \w 单词字符：[a-zA-Z_0-9] 
   \W 非单词字符：[^\w] 
   ~~~

2. 量词

   ~~~java
   X? X，一次或一次也没有 
   X* X，零次或多次 
   X+ X，一次或多次 
   X{n} X，恰好 n 次 
   X{n,} X，至少 n 次 
   X{n,m} X，至少 n 次，但是不超过 m 次 
   ~~~

   练习:

   * 用户名必须是小写字母开头,其余单词是由字母或者数字或者下划线组成,总长度在6-8位.

     ~~~java
     [a-z]\\w{5,7}
     或者
     [a-z][a-zA-Z_0-9]{5,7}
     ~~~

   * 匹配手机号

     分析:以13  14  15   17  18 开头,其他的肯定都是数字.总长度是11位

     ~~~java
     1[3|4|5|7|8]\\d{9}
     或者
     1[3|4|5|7|8][0-9]{9}
     ~~~

   * 中文

     ~~~java
     [\u4e00-\u9fa5]
     ~~~

   * 邮箱

     1. `@之前必须有内容且只能是字母（大小写）、数字、下划线(_)、减号（-）、点（.）`
     2. `@和最后一个点（.）之间必须有内容且只能是字母（大小写）、数字、点（.）、减号（-），且两个点不能挨着`
     3. `最后一个点（.）之后必须有内容且内容只能是字母（大小写）、数字且长度为大于等于2个字节，小于等于6个字节`

     ~~~java
     [a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}
     ~~~

# Pattern

java.util.regex.Pattern类



## 常用的方法

* static boolean matches(String regex,CharSequece charSequence); 

  用来判断传入的字符序列是否匹配正则regex.

  

* static Pattern compile(String regex);//将给定的字符串编译到模式中.

  ~~~java
  Pattern pattern = Pattern.compile("[a-z]\\w{5,7}");
  ~~~

* Matcher matcher(CharSequece charSequence);//返回获得一个匹配器

  此处的参数是匹配的源头.

  ~~~java
  Matcher m = pattern.matcher("文章段落");
  ~~~



# Matcher

java.util.regex.Matcher类



## 常用方法

* boolean find();//不断从匹配器中寻找成功的序列,直到匹配器末尾,找不到则返回false

  ~~~java
  while(m.find()){
      
  }
  ~~~

* int start()

  返回匹配成功的字符序列的起始位置

  ~~~java
  String = "hello234uyr";
  ~~~

  

* int end()

  返回匹配成功的字符序列的结束位置,不包含.



* String group()

  返回的就是匹配成功的序列.



# 异常[Throwable]

程序在运行的过程中出现了不正常的情况 - 异常.

根据异常的严重程度,分成两大类,一类叫做错误[error],一类叫做异常[Exception]



## 错误

基本是都是虚拟机级别的,人是处理不了的.因为错误一旦发生,那么程序就会退出.程序失去了运行的物理环境.

1. java.lang.StackOverflowError - 堆栈溢出 - 因递归太深,没有指明递归的出口.
2. java.lang.OutOfMemoryError - 内存泄露



## 异常

异常又可以分成两大类

* 非运行时异常
* 运行时异常

所有的运行时异常都是Exception类的子类.

## 运行时异常

又叫做未检测异常,在编译期间,不会要求我们强制地去处理这个异常,而是在程序在运行的过程中如果发生了不正常的情况,才会抛出这个异常.通常是不需要程序员进行手动处理的.只需要编码阶段谨慎操作.

java.lang.RuntimeException

* java.lang.NullPointerException - 空指针异常

* java.lang.ArithmeticException - 除数为0
* java.lang.ArrayIndexOutOfBoundException - 数组下标越界异常
* java.lang.ClassCastException - 类型转换失败异常[之前需要用instanceof]
* java.util. InputMismatchException - 输入不匹配异常
* java.lang.IllegalArgumentException - 非法参数异常
* java.lang.StringIndexOutOfBoundsException - 字符串下标越界异常

## 非运行时异常

又叫做已检测异常,在编译期间,如果存在异常,那么会在编译期间强制我们对异常进行处理.

程序员是需要对其进行处理的.并且处理的方式分成积极处理和消极处理.

* java.lang.CloneNotSupportedException 需要克隆的对象没有去实现java.lang.Cloneable接口
* java.text.ParseException - 解析失败异常
* java.lang.InterruptedException - 程序中断异常
* java.lang.ClassNotFoundException - 找不到类异常
* java.io.FileNotFoundException - 文件找不到异常
* java.io.IOException - IO异常

* java.sql.SqlExcetpion - Sql语句异常



### 积极处理方式

try...catch..的方式来进行异常的积极处理 - **自己的问题自己处理掉了.**

通常是和finally一起配合使用的

* try后面跟多个catch块 - 推荐使用的方式 - 可以针对不同的异常进行日志的记录.

  ~~~java
  try{
      
  }catch(异常类1 e1){
      //...
  }catch(异常类2 e2){
      //..
  }catch(异常类3 e3){
      
  }finally{
      //...
  }
  ~~~

  ~~~java
  try{
      //出现了异常类 - 子类2 - ParseException
      //注意catch()中的异常一定要考虑异常的父子关系
      //子类会出现在父类的上方
  }catch(异常类-子类2 e1){
      //...
  }catch(异常类 - 父类1 e2){
      //..
  }finally{
      //...
  }
  ~~~

  

* 使用一个catch块来捕捉多个异常

  JDK7.0以后才支持的.

  ~~~java
  try{
      
  }catch(异常类1 | 异常类2 | 异常类3 e){
      //...
  }finally{
      //...
  }
  
  ~~~

* 使用同一的异常父类

  ~~~java
  try{
      //...
  }catch(Exception e){
      //..
  }finally{
      //
  }
  ~~~

  补充:try..finally是可以单独一起使用的.

  ~~~java
  try{
     //...
  }finally{
      //...
  }
  ~~~

  补充:final finally finalize三者区别.

### 消极处理方式

**理念:方法中出现了异常,应该是由方法的最终的调用者进行处理.**

1. throw + 异常的对象 - 出现在方法体中

2. throws + 异常类 - 出现在方法的声明中.

   ~~~java
   修饰符 返回类型 方法名([参数列表]) [throws 异常列表]{
       //方法体.
       //throw + 异常的对象.
   }
   ~~~

   注意:异常不要抛给main,如果发生了异常,JVM直接退出.

   

# 自定义异常

程序中出现的异常可能是内置的,也有可能是定制的.后面学的框不单只是统一处理内置的异常,也要能够处理业务中

可能出现的自定义的情况.

* 自定义一个非运行时异常

  写一个类去继承java.lang.Exception类.

  ~~~java
  public class MyBalanceNoEnoughException extends Exception{
  
      public MyBalanceNoEnoughException(){
  
      }
  
      public MyBalanceNoEnoughException(String message) {
          super(message);
      }
  }
  ~~~

  

* 自定义一个运行时异常

  写一个类去继承java.lang.RuntimeException

# 作业

~~~java
文件内容:

The Associated Press won an award for its series on 
the profiling of Muslims by the New York Police 
Department.
But for the first time since 1977 there was no prize 
for fiction.The Pulitzer panel praised the Patriot-News.


作业要求:

设计一程序,提供方法如下方法:
1.找出以上文本中所有的数字,要求是完整的数据,
比如,上面有 1977, 这是一个数字,不是4个(1,9,7,7)
2.打印出所有的单词,如 The,...等, 
并统计文章中共有多少个单词.

提示:
采用 Matcher中的 find(), group(), start(), end() 方法.
~~~



# 预习

java.lang.Integer - int

* Integer包装类中常用的方法
* 基本类型int和包装类型Integer以及字符串类型String三者之间的相互转换.

java.lang.Double - double

java.lang.Character - char

java.lang.Byte - byte

java.lang.Boolean - boolean



Java中的字符串处理类

* java.lang.String

* java.lang.StringBuilder

* java.lang.StringBuffer





