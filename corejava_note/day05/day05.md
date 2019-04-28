# 递归算法

意思:方法体中调用自己.

步骤:1. 方法中**调用自己**

​	 2. 递归语句中一定要指明递归的出口,否则会抛出java.lang.StackOverflowError 堆栈溢出

注意点:尽量不要使用递归,**每次计算的时候都会在内存中保存中间的结果**,性能比较低下.

应用场景:大量的重复的运算的业务,可以使用递归.

能用循环语句解决的,尽量就不要使用递归算法.

比如:十进制->二进制    兔子问题    阶乘   汉诺塔...



## 练习

* 兔子问题 - 斐波那契数 - 1 1 2 3 5 ....

* 阶乘

* 最大公约数

* **经典的题:求杨辉三角**

  ~~~java
  1
  1 1
  1 2 1
  1 3 3 1
  1 4 6 4 1
  ~~~

# 数组

本质上就是一个容器,用来存储一组**具有相同类型**的元素.

它在地址中的地址是连续的[有序的] - 可以通过下标进行访问.线性的数据结构.

*数组本身是属于对象类型*.一旦在内存中确定了,那么这个数组将不能改变.也就是说数组只能进行遍历[查询]操作.

可以对数组中的元素进行修改操作

我们经常提到对数据进行增,删都不是对原来的数组进行操作,因为数组[长度]一旦确定将不能再改变了.



## 如何定义数组

语法:**元素类型[] 数组对象 = new 元素类型[数组长度];**

特点:

* 元素类型 - 决定了 存储在这个数组中的数据的类型

* *元素类型可以是基本数据类型,也可以是对象类型[内置,自定义的]*

* 数组的长度 - 决定了数组中可以存放的数据的数量.并且定义数组的时候,长度一旦确定了,将不能改变.

  这也决定了我们不能对已经确定长度的数组进行直接的增和删操作.

* 数组的类型就是 - 元素类型[]
* 通过数组对象.length属性来获取数组的长度.

## 初始化数组

向数组中存放数据. - 定义一个数组的时候,必须要给定一个长度.

### 赋值方式

* 先定义一个数组,然后通过下标进行赋值.数组的下标的范围是[0,数组长度-1].

  ~~~java
  //定义一个长度为3的数组,里面可以存放的数据是int类型
  int[] arr;
  arr = new int[3];//此处的arr的数据类型就是int[]
  //内存中的存储情况:
  //通过new关键在在jvm的堆区,确定一个长度为3的数组对象.
  //并且将这个数组对象在堆区的内存地址赋值给了变量arr,保存在栈区.
  
  //通过下标一一进行赋值
  arr[0] = 1;//给数组的第一个位置赋值值是1
  arr[1] = 2;
  arr[2] = 3;
  //arr[3] = 4; - > 将会抛出一个java.lang.ArrayIndexOutOfBoundsException数组下标越界异常.
  ~~~

* 在定义数组的同时进行赋值操作

  长度可以省略不写,但是在内存中,该数组的长度是确定的.

  ~~~java
  String[] arr = new String[]{"A","B","C"};//数组arr类型就是String[]
  ~~~

  简写成:

  ~~~jaa
  String[] arr = {"A","B","C"}//平常推荐使用的.
  ~~~

* 可以通过普通for循环进行赋值

  ~~~java
  int[] arr = new int[3];
  for(int i=0;i<arr.length;i++){
      //进行赋值
      arr[i] = (int)(Math.random()*100+1);
  }
  ~~~

* 通过*java.util.Arrays*

  它是一个数组工具类,里面提供了一个fill方法.

  static void fill(boolean[] arr,boolean val);

  ~~~java
  boolean[] flags = new boolean[10];
  Arrays.fill(flags,true);//给数组中的每个下标对应的元素全部赋值为true.
  ~~~

#### 默认值

如果没有对数组中的元素进行赋值 - 那么都会分配一个默认值[根据数组的元素类型]

| byte[]                     | 0        |
| -------------------------- | -------- |
| short[]                    | 0        |
| int[]                      | 0        |
| long[]                     | 0        |
| float[]                    | 0.0      |
| double[]                   | 0.0      |
| boolean[]                  | false    |
| char[]                     | '\u0000' |
| 对象类型[]  - 比如String[] | null     |

## 数组的遍历方式

假设存在:int[] arr = {1,2,3};

* 通过下标一一获取

  ~~~java
  System.out.println(arr[0]);//获取数组的第一个下标
  System.out.println(arr[100]);//抛出java.lang.ArrayIndexOutOfBoundsException
  ~~~

* 通过普通for循环进行遍历

  ~~~java
  for(int i = 0;i<arr.length;i++){
      System.out.println(arr[i]);
      //不是只读的
      //arr[i] = arr[i] +1;//ok,允许修改数组中的具体的元素.
  }
  ~~~

* jdk5.0以后支持的foreach语法 - 增强for循环.

  **特点:只读的.在遍历的时候,是不能对数组中的元素进行修改操作的.**

  ~~~java
  for(元素类型 变量:数组对象){
     System.out.println(变量);//此处的变量不是代表下标,就是代表数组中的具体的某个值.
  }
  for(int n:arr){
      //n = 10;//error;
      System.out.println(n);
  }
  ~~~

* 通过java.util.Arrays

  1. static String toString(int[] arr); -> static String toString(基本类型[] arr);

  2. static String toString(Object[] arr) ;

     所有的对象的根类都会默认继承java.lang.Object.

  ~~~java
  String str = Arrays.toString(arr);//出来的结果是[1,2,3];
  ~~~



## 关于数组的拷贝方式

java.util.Arrays

* static int[] copyOf(int[] arr,int length);//从原数组中拷贝length个长度的元素.

  底层并没有直接操作原来的数组.

  1. 可以完全拷贝数组 - length == arr.length
  2. 截取 - length < arr.length
  3. 扩容 - length > arr.length - 只能在数组的末尾进行扩容.

  如果内置的某个方法含有返回类型 - > 底层基本上都是新建了一个对象

  如果内置的某个方法是void - > 就是对原来的对象进行了直接的操作.



java.lang.System

* static voidarraycopy(Object src,  int srcPos, Object dest, int destPos,  int length)   

  参数意义:

  src -  原数组

  srcPos - 原数组拷贝的起始位置的下标

  dest - 目标数组

  destPos - 目标数组放入元素的下标

  int length -   拷贝多少个.





































