# Java中的格式化输出

PrintStream - printf(String format,Object... args);



## 格式化输出的模板

最精简的语法:%conversion

~~~java
%[argument_index$][flags][width][.precision]conversion
~~~



conversion - 占位符

* %s - 字符串[%s是可以作为整数,小数,字符串,布尔类型的占位符].
* %d - 整数[只能接受整数]
* %f - 小数[只能接受小数]
* %b - 接受的是布尔类型



参数的解释:

[.precision] - 精度 - 四舍五入.

[width] - 占位符所占的宽度.如果这个值比变量本身的宽度小,则无效.默认右对齐.

[flags] - 文本的对齐方式,默认的是右对齐,如果设置成-就变成了左对齐.

[argument_index$] - 占位符和变量的对应的位置,默认是一一对应的.



### 注意点

* 占位符和变量的类型以及位置.

  ~~~java
  System.out.printf("%d","hello");//java.util.IllegalFormatConversionException 非法格式化占位符异常
  ~~~

### 回忆异常

* java.util.InputMismatchException - 输入不匹配[nextInt - 接受了字符串]
* java.lang.StackOverflowError - 堆栈溢出[递归算法中没有指明递归的出口]
* java.lang.NullPointerException - 空指针异常[null.成员]
* java.lang.ArrayIndexOutOfBoundsException - 数组下标越界异常
* java.text.ParseException - 解析失败异常[SimpleDateFormat中的模板和字符串的形式不匹配的]
* java.lang.IllegalArgumentException - 非法参数异常[SimpleDateFormat中传入了一个非法的模板]



# 二维数组

二维数组的本质 - 二维数组中的每个元素其实都是一维数组.

## 语法

~~~java
元素类型[][] 变量名 = new 元素类型[rows][cols];
rows - 行
cols - 列 - 是可以省略不写的,就是一个不规则的二维数组.
    	  - 省略的时候,在使用之前,一定要先初始化.
~~~



## 赋值方式

* 定义二维数组的同时进行赋值

  ~~~java
  int[][] arr = {{1,2,3},{4,5,6},{7,8,9}};
  或者
  int[][] arr = new int[][]{{1,2,3},{4,5,6},{7,8,9}};
  ~~~

* 先初始化一个数组,然后进行赋值 - 下标

  ~~~java
  int[][] arr = new int[3][3];
  //一一进行赋值
  arr[0][0] = 10;
  arr[0][1] = 20;
  ~~~

* 先初始化一个数组,然后统一进行赋值[一行一行进行赋值]

  ~~~java
  int[][] arr = new int[3][3];
  arr[0] = new int[]{1,2,3};
  arr[1] = new int[]{4,5,6};
  //arr[2] = {7,8,9};//error
  ~~~

* 通过普通for循环进行赋值

  ~~~java
  int[][] arr = new int[3][3];
  for(int i=0;i<arr.length;i++){//外层循环表示行
      for(int j = 0;j<arr[i].length;j++){//内层循环表示列
          arr[i][j] = (int)(Math.random()*10+1);
      }
  }
  ~~~

  

## 遍历方式

* 通过普通for循环进行遍历

  ~~~java
  int[][] arr = new int[][]{{1,2,3},{4,5,6},{7,8,9}};
  for(int i=0;i<arr.length;i++){//外层循环表示行
      for(int j = 0;j<arr[i].length;j++){//内层循环表示列
        System.out.println(arr[i][j]);
      }
  }
  ~~~

* 增强for循环

  ~~~java
  for(元素类型 变量:对象名){
      //变量 - 代表的就是数组中的元素[元素的副本]
  }
  for(int[] n:arr){
      for(int t:n){
          System.out.print(t+"\t");
      }
      System.out.println();
  }
  
  ~~~

## 练习

* 杨辉三角

* 横排变竖排

  ~~~java
  白日依山尽，
  黄河入海流。
  欲穷千里目，
  更上一层楼。
  ~~~



























