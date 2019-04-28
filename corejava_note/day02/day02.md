# 提纲

* 变量的定义和使用
* 基本数据类型
* 二进制,八进制,十六进制三者的转换
* 运算符[算术运算符,逻辑运算符,位运算符]
* 条件语句[if..else..   switch..case...]



## 变量

编程的核心:就是对Data数据进行CRUD操作.

C - Create - 创建 - 增

R - Retreive - 检索 - 查 [重点]

U - Update - 更新 

D - Delete - 删除

### 数据存储的介质

* 内存 - [比和磁盘交互的速度要快,生命周期]
* 磁盘[xml,json,数据库]

在Java的内存中是如何存储一个数据的?

**是通过数据类型和变量来定义和存储数据.**



## 数据类型

Java中的数据类型分成两大类.一类是基本数据类型,一类是引用类型[对象类型].

基本数据类型通常归纳为八大种,**但是注意Java编程思想中将void归纳为基本数据类型.**



## JVM的内存模型

* 栈  - 基本数据类型存储在栈里面

  ​        特点:只要空间开辟了,那么**大小是固定的.**读取效率比较高

  ​	         "以空间换时间".

* 堆

* 方法区



## 基本数据类型存储

1. Java中出现的整数,默认的就是int类型
2. Java中出现的小数,默认的就是double类型

语法:数据类型 变量名 [= 初始值];

### 整数型

| 名称      |        |              | 范围         | 默认值 |
| --------- | ------ | ------------ | ------------ | ------ |
| byte      | 字节   | 1个字节8bit  | -128~127     | 0      |
| short     | 短整型 | 2个字节16bit | -2^15~2^15-1 | 0      |
| int     √ | 整型   | 4个字节32bit | -2^31~2^31-1 | 0      |
| long  √   | 长整型 | 8个字节64bit | -2^63~2^63-1 | 0      |



### 小数型

| float     | 单精度浮点数 | 4个字节32bit | -3.4*10^38~3.4*10^38   | 0.0  |
| --------- | ------------ | ------------ | ---------------------- | ---- |
| double  √ | 双精度浮点数 | 8个字节64bit | -1.7*10^308~1.7*10^308 | 0.0  |



### 其余

| char    | 字符类型 | 2个字节16bit | '\u0000'~'\uFFFF' | '\u0000' 空格 |
| ------- | -------- | ------------ | ----------------- | ------------- |
| boolean | 布尔类型 | 1个字节8bit  | true/false        | false         |



## 引用类型

总体分成内置对象类型和自定义对象类型[无数种].

* 类类型   - 比如String,System,PrintStream - 都是属于内置的对象类型
* 接口类型
* 数组类型
* 枚举
* 注解
* 自定义对象 - 比如HelloWorld



# 变量的定义

语法:

~~~java
数据类型 变量名 [= 初始值];

//定义一个int类型的变量,并且赋值为10
int a = 20;
int b = 20;
System.out.println(a == b);//true


//编译器优先处理的是int a = 20;就会在栈中创建一个空间,保存的是变量a.
//在栈查找有没有字面量为20的数字.由于是第一次,没有,则开辟区域,保存20这个字面量
//然后将变量a指向20这个字面量.
//程序到达int b = 20这行代码的时候,仍然查询字面量20是否存在.发现是存在的,
//直接将变量b也指向20这个字面量.

~~~



###变量名 - 满足"标识符"的命名规则

和类名命名的规则区别是:变量的命名使用的是"小驼峰"命名,而类名使用的"大驼峰".

唯一的差别,一个是首字母小写,一个是首字母大写.studentPrj



## 变量的赋值方式

* 声明或者定义变量的同时进行赋值

  ~~~java
  int i = 100;
  ~~~

* 先声明一个变量,然后进行赋值.

  **变量在使用之前,一定要先赋值[初始化]**

  ~~~java
  int i;//仅仅是定义了一个变量
  i = 100;//最终由JVM中的GC进行回收和释放.
  i = 300;//变量是可以进行多次赋值
  System.out.println(i);
  ~~~

* 可以进行链式赋值

  ~~~java
  int x,y,z=100;//仅仅是对z进行了赋值
  int a =10,b=20,c=30;
  ~~~

* 使用表达式进行赋值

  由变量或者字面量以及运算符组成合理的语句 - 表达式

  ~~~java
  int a = 100;
  int b = 200;
  int result = a + b;
  ~~~

* 通过方法进行赋值

  ~~~java
  public class Test{
      public static void main(String[] args){
          int a = getResult();
          System.out.println(a);
      }
      
      //自定义一个方法,返回一个整数,100
      public static int getResult(){
          return 100;
      }
  }
  
  
  ~~~

## 变量的分类

* 类变量
* 成员变量
* 局部变量[今天讲] - 出现在方法体中.



### 局部变量

它的生命周期是只在它存在的块block{}中是有效的.
并且同一生命周期中定义的变量是不能够同名的.

* 方法执行,变量开始,方法执行完毕,变量销毁



## 数据类型之间的转换

* 低字节 -> 高字节 - 自动转换 `

  byte->short->int->long

  int - > float    ×   俩者都是占4个字节

  int - > double √

  long - > double ×

  char -> int √

  ~~~java
  byte b = 100;
  short s = b;
  ~~~

  

* 高字节 -> 低字节 - 强制类型转换

  语法:

  ~~~java
  at代表高字节的数据类型,bt代表的是低字节的数据类型
  at i = 100;
  bt b = (bt)at;
  小数据类型  变量 = (小数据类型)大数据类型变量;
  ~~~

* 隐式转换

  1. 定义long长整型 - 在整数后面加上L/l

  1. 定义float类型 - 在小数后面加上F/f - 强制的.
  2. 定义double类型 - 在小数后面加上D/d

# char类型

char类型的数据都会有对应的ascii码的值[0-65535]

范围:0x0000~0xFFFF

~~~java
int t = 10;//默认的是十进制
int o = 032;//八进制 - > 十进制,java中的八进制的数以0开头.
int h = 0xffff;//十六进制 - > 十进制 65535
~~~



java底层对char类型的处理,都是处理的char类型对应的ascii码值.

~~~java
package tech.aistar.day02;

/**
 * @author success
 * @version 1.0
 * @description:本类用来演示:char类型
 * @date 2019/3/26 0026
 */
public class CharDemo {
    public static void main(String[] args) {
        int o = 032;//八进制[以0开头]->十进制  3*8^1+2*8^0 = 26
        System.out.println("o:"+o);//26

        int h = 0xffff;
        System.out.println("h:"+h);

        //char c1 = 65535;
        //char c1 = 65;
        char c1 = 060;//八进制
        System.out.println("c1:"+c1);

        //char类型的数据计算的时候,底层都是转换成ascii对应的数值
        char cc = '中'+'国';//计算...
        System.out.println(cc);

        char c2 = '0';

        char c3 = 'a'+1;
        //'a' -> 97
        //97 + 1 = 98
        //char c3 = (char)98;
        //底层char c3 = (char)
        System.out.println("c3:"+c3);//b

        int result = 'a'+1;
        System.out.println("result:"+result);//97

        int m = 1;
        char n = (char) ('a' + m);
        System.out.println("n:"+n);//b

        char name = '\u7ba1';
        System.out.println(name);
        //通过cmd - native2ascii - 查看中文的unicode编码

        //可以存放转义字符
        char sign = '\n';
        System.out.println("好"+sign+"人");
    }
}
~~~



# 特殊符号

知道\代表转义字符

~~~java
1. \n - 换行,输出完毕之后,光标停在下一行的起始位置
2. \r - 回车,输出完毕之后,光标停在当前行的起始位置
3. \b - 退格
4. \t - 制表符,相当于tab键[默认为四格].
5. \" - 双引号引号
6. \' - 单引号
7. \\ - 单个\
~~~

练习:

1. 输出内容    james:"java天下无敌"
2. 输出内容    D:\news\temp



# 进制

## 二进制

由0和1组成,逢2进1.java中的数字都是带有符号的,有正数和负数.

用二进制表示数字的时候,那么最高位是"符号位".0代表正数,1代表负数

记住:**存储1这个数字的时候,需要消耗-5v的电压**.存储0的时候,不需要消耗电压.



### 二进制和十进制转换

原理:让十进制的数不断除以2,直到商是0的时候,然后倒过来取余数.

10 - > 01010 -> 简写:1010 -> 全写:00000000 00000000 00000000 00001010

### 二进制转十进制

~~~java
101011 -> 1*2^5 + 0 + 1*2^3+0 + 1*2^1 + 1*2^0 = ?
~~~



## 八进制

使用频率最低.

由0-7组成,逢8进1

java中八进制的数使用以0开头的数字表示.比如:int i = 032 -> i = 26

### 八进制转二进制

原理:将八进制中的每个数字,拆分成由3位组成的二进制.

032 -> 011 010



### 二进制转八进制

从右->左,每3个分一组,计算出对应的十进制,如果最高位不足3位,那么左边补0

001 001 110 101 -> 01165



### 十进制转八进制

原理:让十进制的数不断除以8,直到商是0的时候,然后倒过来取余数.最高位加上0

26 -> 032



### 八进制转十进制

~~~java
032 = 3*8^1 + 2*8^0 = 26
~~~



## 十六进制

由数字0-9或者a-f[或者A-F].逢16进1

java中的表现形式:0xaf5d

### 十六进制和二进制

原理:将十六进制中的每个数字,拆分成由4位组成的二进制.

0xaf5d -> 1010 1111 0101 1101



### 二进制转十六进制

从右->左,每4个分一组,计算出对应的十进制,如果最高位不足4位,那么左边补0

0101 0111 1011 1011 1011 -> 0x57bbb



尝试:十六进制和十进制 

176 -> 0x00b0



# 运算符

## 算术运算符

~~~java
+ - * / %
前++和后++   共同点:变量都会自增1
前--和后--
+=  -= /=  %=  *=
~~~

* 加号 - 在java中唯一重载的运算符.重载的意思就是代表它具有多个作用.

  ​          数值之间表示计算加法,一旦出现字符串,表示拼接.

* 取整 /   比如:10/3 = 3

* 取模(余) %  比如:10%3 = 1

  ~~~java
  int year = 2019;
  //输出形式如下:
  //千位:2
  //百位:0
  //十位:1
  //个位:9;
  Sysem.out.println("千":+year/1000);
  Sysem.out.println("百":+year/100%10);
  Sysem.out.println("十":+year/10%10);
  Sysem.out.println("个":+year%10);
  ~~~

  

* 前++   ++出现在变量的左边

  ~~~java
  int a = 1;
  //将自增之后的值赋值给了c
  int c = ++a;//相当于a = a + 1
  System.out.println(a);//2
  System.out.println(c);//2
  ~~~

  

* 后++   ++出现在变量的右边

  ~~~java
  int n = 1;
  //先自增之前的值赋值给c,然后自己再自增
  int c = n++;//相当于 n = n + 1
  System.out.println(n);//2
  System.out.println(c);//1
  ~~~

  练习:

  ~~~java
  int x = 1;
  
  //x的值  2      3    2
  int y = x++ + ++x + x--;
  //expr:	1	+  3  + 3 = y = 7
  
  //y:    6
  int c = y-- + --y + x++;
  //expr:7  + 5 + 2
  计算c = 14
  ~~~

  练习:

  ~~~java
  byte b = 127;
  //b = (byte)(b + 1);//存在编译错误
  // ++ -- 底层会进行窄化操作的.
  b++;//无编译错误 ok   b = (byte)(b + 1);
  System.out.println(b);//-128
  ~~~

* += -= /= %= ....

  ~~~java
  short s = 23;
  //小的类型和大的类型进行计算的时候,结果的类型是偏向类型大的一方.
  //s = s + 2;//编译错误.存在编译错误
  //s = (short)(s+2);
  s += 2;//编译是ok,在内部也会进行强制类型转换
  s最终的值就是25.
  
  ~~~

## 比较运算符

~~~java
> < >= <= == != 
System.out.println(3>2);//true
~~~

返回的结果是一个boolean类型



## 逻辑运算符

* 逻辑运算符是不能够进行计算的
* 作用是用来连接条件的

### 逻辑与

&& - 也叫做短路与.

语法:表达式1 && 表达式2 && 表达式3 && ....

当且仅当所有的表达式都是true的时候,那么结果才是true.

**解释什么是短路?**

如果表达式1返回的是false.那么表达式1后面的所有的表达式将不会执行.

**推荐将表达式最有可能是false的放在左边.为了提高程序的性能!**



### 逻辑或

|| - 也叫做短路或

语法:表达式1 || 表达式2 || 表达式3 || ....

只要表达式中存在一个true,那么结果就是true.

如果表达式1返回的是true.那么表达式1后面的所有的表达式将不会执行.

**推荐将表达式最有可能是true的放在左边.为了提高程序的性能!**



### 逻辑非

! - true的结果变成false,false的结果变成true.

~~~java
boolean flag = true;
System.out.println(!flag);//false
~~~



## 位运算符

位运算符是计算性能最高的.



## 按位与

& - 非短路与

* 用来计算的,&运算

  只有全是1的时候,结果才是1,只要出现一个0,结果就是0

  ~~~java
  10 & 8 = 8
  //转换成对应的二进制进行计算
    1 0 1 0 
  & 1 0 0 0
  ------------
    1 0 0 0   -> 转成十进制 8
  ~~~

  

* 也是可以用来连接条件的,但是不推荐使用.

  所有的表达式都会执行.只有所有的表达式都是true,结果才是true.



## 按位或

|  - 非短路或

* 计算法则:只要出现一个1,结果就是1

  ~~~java
  10 | 8 = 10
    1 0 1 0 
  | 1 0 0 0
  ------------
    1 0 1 0   -> 转成十进制 10
  
  ~~~

  

* 可以用来连接条件,只要出现一个true,结果就是true



## 异或

^ - 相同为0,不同为1.

总结:一个数连续异或同一个数俩次,结果是它本身.

​	进行简单的加密和解密操作.

~~~java
10 ^ 8 = 2
1 0 1 0 
1 0 0 0
--------
0 0 1 0
    
10 ^ 8 ^ 8 = 10
0 0 1 0 
1 0 0 0
-------
1 0 1 0 ->10
~~~

练习:

定义俩个变量,交换俩个变量的数值

~~~java
int a = 10;
int b = 20;
//写一个程序

System.out.println("a:"+a);//20
System.out.println("b:"+b);//10
~~~



## 概念

* 原码
* 反码
* 补码

正数的原码,反码,补码都是它本身.比如整数10.

负数的二进制的表现形式是采用补码的形式.**补码 = 反码(对原码进行符号位不变,其余位按位取反) + 1;**



取反~和反码并不是一样的.

~9 = -10

原码:00000000 00000000 00000000 00001001

取反:11111111 11111111 11111111 11110110



计算-10对应的二进制,其实就是求出-10的补码.

1. 求出-10的原码:1000 0000 000000000 00001010

2. 求出-10的反码:1111 1111 11111111 11110101

3. 反码                                                                       +1

   ​                         1111 1111  11111111 11110110

   ------------------------------------------------------------------------

公式:~x = -(x+1)



## 位移

笔试题:请你用最高性能的方式求出2的3次方.

<< - 左移

~~~java
System.out.println(2 << 2);//8
~~~



~~~java
>>  带符号的右移动,符号一起移动,最高位补符号位.
>>> 不带符号的右移动,最高位补0.
~~~

~~~java
-10 >> 2

-10 >>>2

11111111  11111111 11110110 >> 2 = -3

11111111  11111111 11110110 >> >2

1111111111  11111111 111101 -> 明显是一个负数的二进制表现形式->~2 = -3.

取反  000000000   00000000 000010 = 2

0011111111  11111111 111101 -> 正数->二进制->十进制 = 1073741821
~~~



**计算:-15>>2  -15>>>2**

-15的原码:10000000 00000000 00000000 00001111

-15的反码:11111111 11111111 11111111 11110000

​                                                                                        +1

-15的补码 : 11111111 11111111 11111111 11110001



换种方式:-15 = ~14

11111111 11111111 11111111 11110001>>2 = ~3 = -4

1111111111 11111111 11111111 111100 - 取反 = 000000000000000000000011 = ~3

11111111 11111111 11111111 11110001>>>2

0011111111 11111111 11111111 111100 = 1073741820



# 条件语句

* if和else语句
* switch..case语句
* 三目运算符

## IF..ELSE..

作用:用来进行条件判断,控制代码块中的程序是否执行.

### 使用方式

* if语句可以单独使用

  ~~~java
  boolean flag = true;
  if(flag){
      //当if括号中的表达式返回true的时候
      //那么if的块{}中的代码才会执行
  }
  ~~~

* if和else一起使用

  ~~~java
  boolean flag = true;
  if(flag){
      //flag为true的时候,执行此处的代码
  }else{
      //flag为false的时候,执行else块中的代码
  }
  ~~~

* if..else可以嵌套使用

  ~~~java
  int a = 10;
  if(a % 2 == 0){
      if(a>6){
          //..
      }else{
          //...
      }
  }else{
      //....
  }
  ~~~

* if..else if..else if..[else...]

  主要判断的条件一定要互斥.

  ~~~java
  if(表达式){
      //...
  }else if(表达式){
      //...
  }else if(表达式){
      //..
  }else{
      //..
  }
  ~~~



### 作业

1. 买奶茶,每杯10元.每第二杯半价.买了5杯,求出总价

   ~~~java
   //编程从变量开始
   int cup = 5;//定义杯数
   double price = 10.0d;//价格
   //逻辑 - 10 5 10 5 10
   ~~~

2. 判断某年某月最大天数.

   ~~~java
   int year = 2012;//定义一个年份;
   int month = 2;//定义一个月份;
   //逻辑 - 大月  小月  判断闰年 -> 29天
   //逻辑 - 判断某年是否为闰年
   ~~~

3. 成绩90~100 -> 显示优秀.  小于0或者大于100的 -> 显示成绩无效

   [80~90] - 显示良好,[70-80]-显示中等,[60-70]-显示及格,[0-60]显示不及格














