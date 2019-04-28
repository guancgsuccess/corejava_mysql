# 集合框架

JCF - Java Collection FrameWork.

由SUN公司提供的一组接口和实现类,相当于容器.开发中用来替代"数组"的.



## JCF结构

JCF的顶级的父接口 - **java.util.Collection[I]** - 包含List接口和Set接口

List[I] - **代表的是有序可重复的数据**.

   - ArrayList[C] 

     - 底层是动态申请空间,动态改变长度的一个数组.
     - 底层的数据结构是基于线性结构的.**线性结构的特征**
       * 集合中必须要存在唯一的"第一个元素"
       * 集合中必须要存在唯一的"最后一个元素"
       * 除了最后一个元素,其余的元素都有唯一的"后继"
       * 除了第一个元素,其余的元素都有唯一的"前驱"
       * 比如(a0 a1... an-1 an)
       * **查询效率比较高,但是增删效率比较低.因为涉及到数组空间的申请和分配,并且还会造成数组中的元素的移动.**	

     * 线程不安全的.

   - LinkedList[C]

     * 底层是基于**双向链表**的,链表结构的特征
       * 每个节点都会保存它上一个节点的信息和下一个节点的信息,自己本身
       * 通过其中的一个节点,可以找到其余的任何一个节点
       * 双向链表,最后一个节点保存的next指向的是第一个节点.
       * 闭环的形式
       * 查询效率比较低,因为需要寻找其中的一个节点的时候,需要遍历整个链表结构.链表的物理地址是不连续的.增删效率比较高的.只需要涉及到一个元素的内存消耗.

     * 应用场景:适合解决队列,栈列问题的业务.

       * 队列 - 先进先出
       * 栈列 - 先进后出

       * **"贪吃蛇算法"**

   - Vector[C] - 原理,使用方式和ArrayList是保持一致的.但是它是线程安全的.



Set[I] - 代表的是**无序不可重复** - 拒绝添加.

   * HashSet[C] - 无序不可重复

     底层原理:基于HashMap.利用的是"散列表",或者叫做"哈希表".

     ​		当我们将对象放入到set集合容器中的时候,那么会调用对象的hashcode方法,得到一个"哈希值".

     ​		如果这个"哈希值"在这之前从来没有出现过,则直接将该对象放入到set容器中.当"哈希值"曾经

     ​		出现过,此时还不能判定是否为同一个对象.还必须再去调用对象的equals方法,如果equals返回true,

     ​		则拒绝添加,否则添加到容器中. - 仅仅是hash值一样的,还需要再去调用equals才能判断是否为同一个

     ​                对象.

     ~~~java
     public class Customer{
         
         //一个客户拥有多个订单
         //private Order[] orders;
     	
         //private List<Order> orders;
         
         //订单是不允许重复的对象
         private Set<Order> orders;
     }
     
     public class Order{
         
         //多个订单对应一个客户
         private Customer customer;
     }
     ~~~

     

   * TreeSet[C] - **不可重复,但是可以排序.**

     Set[I] - SortedSet[I] - TreeSet[C]

     底层是基于TreeMap.

Map[I] - 存储方式:是利用键值对来进行数据的存储**,针对"key"是无序不可重复的**,"value可以是重复的".

 * HashMap[C]

   JDK8.0之前底层是:数组+链表的结构

   JDK8.0底层是**:数组 + 链表 + 红黑树的结构** - 更加提高的map集合的查询的效率.

   **线程非安全的**.

 * Hashtable[C] - **线程安全的.**

   ​	- Properties[C] - 属性文件

 * TreeMap[C]

问法:

* HashSet和HashMap的区别?
  * HashSet的底层是HashMap
  * 前者的add方法调用的就是后者的put方法
  * 前者的原理是"哈希表",后者数组+链表+红黑树
  * 前者利用的是对象(hashcode和equals)来做到无序不可重复,拒绝添加;后者是根据唯一的key的hashcode值来做到key的无序不可重复的.后者的性能高一点.

* HashMap和Hashtable的区别?

​	 * HashMap是线程非安全的,Hashtable是线程安全的.

​	 * HashMap的key和value可以为null,但是Hashtable是不允许的.

​	  * HashMap的效率高,Hashtable的效率要低.  

* HashMap和TreeMap的区别?

* HashSet和TreeSet的区别?

集合的工具类型java.util.Collections[C]中的API



# 同步问题

如何将一个ArrayList和HashMap做到线程安全?

~~~JAVA
List<String> list = new ArrayList<>();//线程非安全
List<String> lst = Collections.synchronizedList(list);//线程安全的ArrayList
//或者直接可以使用vector

Map<Integer,String> maps = new HashMap<>();
Map<Integer,String> map = Collections.synchronizedMap(maps);
//或者可以直接使用Hashtable

~~~



## java.util.Collection[父接口]

* boolean add(E e);//此处的E代表是泛型.

* void clear();//清空集合

* boolean remove(Object o);//移除集合中第一次出现的o.

* boolean isEmpty();//判断集合中是否有元素

* int size();//返回集合元素的个数

* boolean equals(Object obj);//判断集合是否相同.

* **default booealn removeIf(Predicate<? super E> filter);**//Predicate是JDK8.0中新提供的接口

* Object[] toArray();//将集合转换成数组

* **default Stream<E> stream();**//Stream是JDK8.0中提供的新的特性.

* Iterator<E> iterator();//返回集合的迭代器 - 集合特有的遍历方式.

* **void sort(Comparator<? super E> e);//JDK8.0中提供的排序的方法**

* **void forEach(Consumer<? super T> action) - Iterable中提供的JDK8.0中的新的方式**

  

# ArrayList

## 构建方式和常用方法

~~~java
{
        //1.构建ArrayList的方式
        //a. JDK5.0之前,没有引入泛型的概念 - 类型不安全的集合框架
        List list01 = new ArrayList();
        //测试可以添加的元素有哪些呢?
        list01.add(100);//ok
        list01.add("java");//ok
        list01.add(3.14);//ok
        list01.add(true);//ok
        list01.add(null);//ok

        //直接输出的
        System.out.println(list01);

        //b. JDK5.0以后引入了泛型的概念 - JCF - 类型安全的集合框架.
        //只能向集合中添加字符串
        //List<String> strList = new ArrayList<String>();

        //JDK7.0以后,运行时中的<>中的泛型可以省略不写
        List<String> strList = new ArrayList<>();
        strList.add("mysql");
        strList.add("xml");
        strList.add("redis");
        strList.add("xml");
        System.out.println(strList);

        //c. ArrayList<String> list = new ArrayList<>();

        //d. 使用java.util.Arrays类中的asList方法 - "坑"
        //List<String> list03 = Arrays.asList("a","b","b");//Ararys$ArrayList - 代理对象 - 调用remove - java.lang.UnsupportedOperationException
        //System.out.println(list03);

        List<String> list03 = new ArrayList<>(Arrays.asList("a","b","b"));

        System.out.println("==演示方法==");

        //移除集合中出现的第一个元素
        //strList.remove("xml");
        System.out.println(strList);

        //list03.remove("b");

        //根据下标进行查询
        System.out.println(strList.get(1));//集合的下标范围 0~集合大小-1
        //System.out.println(list03.get(1));

        //获取集合的大小
        System.out.println(strList.size());//4

        //判断集合中是否含有元素
        System.out.println(strList.isEmpty());//false

        //判断集合中是否包含某个元素
        System.out.println(strList.contains("redis"));//true

        //根据下标进行删除
        //strList.remove(1);
        //System.out.println(strList);

        //找出元素第一次出现的下标
        System.out.println(strList.indexOf("xml"));

    }
~~~



## 集合的迭代方式

* 直接输出集合对象 - 重写了toString方法

* 使用普通for循环进行遍历

* 使用增强for循环进行遍历 - **只读的**

* 使用集合特有的迭代器 - **迭代的过程中,集合的大小是不能够改变的**[直接进行集合的remove删除]

  需要使用remove操作,应该是用迭代器的remove操作.

  ~~~java
  List<String> strList = new ArrayList<>();
  strList.add("A");
  ...
  
  //1. 获取集合的迭代器对象
  Iterator<String> iter = strList.iterator();
  //仍然是属于串行的遍历方式 - 数据量比较大 - 性能就会降低
  while(iter.hasNext()){//如果迭代器中仍有数据可被迭代,则返回true,否则返回false
      String n = iter.next();//获取迭代的数据
      System.out.println(n);
  }
  ~~~

* 使用java.util.List接口中的forEeach语法

  ~~~java
  list.forEeach(s - > System.out.println(s));
  list.forEeach(System.out::println);
  ~~~

* 使用java.util.Stream接口中的forEeach

  ~~~java
  list.stream().forEeach(s - > System.out.println(s));
  list.stream().forEeach(System.out::println);
  ~~~

  

# Stream接口

**jdk8.0以后利用Stream中提供的API结合lambda表达式能够高效去处理集合.** - 并行操作.

~~~java
package tech.aistar.day14;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author success
 * @version 1.0
 * @description:本类用来演示:并行的
 * @date 2019/4/12 0012
 */
public class StreamDemo {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(2);
        list.add(8);
        list.add(11);

        //anyMatch - 只要存在任意一个元素满足-true
        //allMatch - 必须所有的元素
        System.out.println(list.stream().anyMatch(integer -> integer == 2));

        System.out.println(list.stream().allMatch(integer -> integer<10));

        //集合排重的方式...
        List<Integer> strList = list.stream().distinct().collect(Collectors.toList());
        System.out.println(strList);

        //找出集合前4个元素
        List<Integer> integers = list.stream().limit(4).collect(Collectors.toList());
        System.out.println(integers);

        //返回集合的大小
        System.out.println(list.stream().count());

        //filter - 条件的过滤
//        List<Integer> filters = list.stream().filter(new Predicate<Integer>() {
//            @Override
//            public boolean test(Integer integer) {
//                return integer == 3;
//            }
//        }).collect(Collectors.toList());

        List<Integer> filters = list.stream().filter(integer -> integer==3).collect(Collectors.toList());
        System.out.println(filters);

        //找出集合中最大的
//        System.out.println(list.stream().max(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o1 - o2;
//            }
//        }).get());

        Integer max = list.stream().max(((o1, o2) -> o1 - o2)).get();
        System.out.println(max);

        List<Integer> sorts = list.stream().sorted((o1, o2) -> o1-o2).collect(Collectors.toList());
        System.out.println(sorts);

        System.out.println("===========");
        //将集合转换Stream类型
        //Stream<Integer> stream = list.stream();

        //调用排重方法
        //stream has already been operated upon or closed
        //一个stream对象只能使用1次
        //List<Integer> strList = stream.distinct().collect(Collectors.toList());
        //System.out.println(strList);

        //将Stream->集合
        //List<Integer> newList = stream.collect(Collectors.toList());
        //System.out.println(newList);
    }
}
~~~

## 关于集合的排序

两种方式:

* **使用java.util.Stream接口中提供的sorted方法**
* **使用java.util.Collection接口中提供的sort方法**

~~~java
package tech.aistar.day14;

import tech.aistar.day11.homework.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author success
 * @version 1.0
 * @description:本类用来演示:
 * @date 2019/4/12 0012
 */
public class SortDemo {
    public static void main(String[] args) {
        Book b = new Book(1,"丰乳肥臀","莫言",100.0,new Date());
        Book b1 = new Book(2,"等一个人的咖啡","九把刀",200.0,new Date());
        Book b2 = new Book(3,"西游记","吴承恩",300.0,new Date());
        Book b3 = new Book(4,"红楼梦","曹雪芹",500.0,new Date());
        Book b4 = new Book(5,"狂人日志","鲁迅",600.0,new Date());
        Book b5 = new Book(6,"茶馆","老舍",300.0,new Date());

        List<Book> books = new ArrayList<>();

        books.add(b);
        books.add(b1);
        books.add(b2);
        books.add(b3);
        books.add(b4);
        books.add(b5);

        //根据价格降序排.

        List<Book> sorts = books.stream().sorted((o1, o2) -> (int)(o2.getPrice() - o1.getPrice())).collect(Collectors.toList());

        //sorts.forEach(s -> System.out.println(s));

        sorts.forEach(System.out::println);

        System.out.println("===");

        books.sort((o1, o2) -> (int)(o2.getPrice() - o1.getPrice()));

        sorts.forEach(System.out::println);
    }
}
~~~

# LinkedList

推荐使用其本身作为编译时类型 - 特有的方法.

## 常用方法

~~~java
package tech.aistar.day14;

import java.util.LinkedList;

/**
 * @author success
 * @version 1.0
 * @description:本类用来演示:LinkedList
 * @date 2019/4/12 0012
 */
public class LinkedListDemo {
    public static void main(String[] args) {
        //其余构建方式参考ArrayList
        LinkedList<String> list = new LinkedList<>();
        //队列 - 先进先出
        //栈列 - 先进后出

        //add添加方式 - 队列的数据结构
        list.add("python");
        list.add("java");
        list.add("c");

        //将元素添加到第一个位置
        list.addFirst("go");

        //boolean offer(E e);//尾部添加
        //boolean offerFirst(E e);//头部添加
        //boolean offerLast(E e)//尾部添加

        //获取第一个元素,但是不移除
        //String firstElement = list.element();
        //System.out.println(firstElement);

        //String firstElement = list.getFirst();
        //System.out.println(firstElement);

        //获取最后一个元素
        //String lastElement = list.getLast();
        //System.out.println(lastElement);

        //System.out.println(list.peek());//头
        //System.out.println(list.peekFirst());//头
        //System.out.println(list.peekLast());//尾部

        //弹出栈顶元素 - 获取第一个元素,但是并且移除了
        //System.out.println(list.pop());

        //压入栈顶
        //list.push("ruby");

        //push和pop -> 栈列的实现方式
        //add和getFirst - > 队列的实现方式

        //System.out.println(list.poll());//pollFirst pollLast

        //移除第一个元素,并且返回
        //System.out.println(list.removeFirst());

        //移除最后一个元素,并且返回
        System.out.println(list.removeLast());

        System.out.println("=========");
        //遍历方式和之前的一模一样
        list.forEach(System.out::println);
    }
}
~~~

## 应用问题

括号匹配 :括号要对称匹配.

满足情况:() - {}  - []    ()[]{}   [({})]   (){[]}

不满足情况:(]   [(]){}

~~~java
思路:
1. char[] arr = 输入的字符串.toCharArray();//[{()}]

   准备LinkedList - list

2. 将数组中的第一个元素压入栈顶 - list.push(arr[0]);
   list的栈顶应该存有一个元素[
       
3. 从arr的第二个位置开始向后遍历,模拟遍历匹配的过程
   arr[1] = '{'
   
   栈顶元素:list.getFirst();//[
   每次遍历获取当前下标的元素和栈顶的元素进行比较 [{
   
   栈顶元素如果和当前的不匹配:继续压入栈顶.list.push(arr[1]);
       								 list.push(arr[2]);
          
   如果匹配:则弹出栈顶元素 list.pop()
       
   如果集合中没有数据,则代表匹配
~~~

# HashSet

~~~java
package tech.aistar.day14;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author success
 * @version 1.0
 * @description:本类用来演示:
 * @date 2019/4/15 0015
 */
public class HashSetDemo {
    public static void main(String[] args) {
        //模拟数据
        Book b1 = new Book(1,"aaa","小红",100.0,new Date());


        Book b3 = new Book(3,"ccc","小名",300.0,new Date());
        Book b4 = new Book(4,"ddd","小黄",400.0,new Date());

        Book b2 = new Book(1,"aaa","小红拒绝添加",100.0,new Date());

        //创建一个Set集合
        Set<Book> books = new HashSet<>();//无序不可重复
        books.add(b1);
        books.add(b3);
        books.add(b4);

        books.add(b2);

        //获取集合的大小
        System.out.println(books.size());

        //不能通过下标进行遍历的,它是无序的.

        //集合特有的迭代器.
        Iterator<Book> iter = books.iterator();

        while(iter.hasNext()){
            Book b = iter.next();
            System.out.println(b);
        }

    }
}
~~~

# TreeSet

## 构造

* TreeSet() - 按照自然排序

  如何做到不可重复的:通过不断调用compareTo方法,如果返回结果为0,则拒绝添加.

* TreeSet(Comparator<? supper E> c) - 传入一个比较器对象,可以定制排序的规则.

## 注意点

排序的两种方式:

* 第一种是实现了java.lang.Comparable接口,重写compareTo方法

* 如果利用的是自然排序的方式,那么添加到容器中的每个元素应该去实现java.lang.Comparable接口

  并且重写里面的compareTo方法.如果没有实现该接口的话,那么在添加元素到容器的时候将会抛出

  java.lang.ClassCastException.

* 第二种利用java.util.Comparator比较器接口来进行排序.



# HashMap

~~~java
{
        //1.创建一个HashMap对象
        //key只能是Integer类型,value只能是String类型,key是无序不可重复的

        //key的泛型我们都是采取包装类型或者String
        Map<Integer,String> maps = new HashMap<>();

        //2. 存放数据 - 将key和value进行映射

        maps.put(1,"奔驰");
        maps.put(2,"奥迪");
        maps.put(3,"宝马");

        //如果有重复的,则覆盖.
        maps.put(3,"大众");

        //通过key来获取value - key是唯一的
        System.out.println(maps.get(2));//奥迪

        //判断map集合中是否包含某个key
        System.out.println(maps.containsKey(4));//false

        //获取map集合中的所有的value.
        Collection<String> collections = maps.values();
        System.out.println(collections);

        //清空集合
//        maps.clear();

        //根据key来做移除操作
       // maps.remove(2);

//        maps.forEach(new BiConsumer<Integer, String>() {
//            @Override
//            public void accept(Integer integer, String s) {
//                System.out.println(integer+":"+s);
//            }
//        });

        //JDK8.0的迭代的方式
        maps.forEach((k,v) -> System.out.println(k+"->"+v) );

        //3. 直接输出
        System.out.println(maps);

        System.out.println("=====JDK8.0 脱手写出map集合特有的迭代方式=====");

        //第一种方式 - 将所有的key全部获取封装到set集合中
        Set<Integer> keySets = maps.keySet();
        //使用set集合的迭代器 - 迭代出所有的key
        Iterator<Integer> iterSet = keySets.iterator();
        while(iterSet.hasNext()){
            Integer key = iterSet.next();
            //根据key来获取value
            String value = maps.get(key);
            System.out.println(key+"="+value);
        }

        System.out.println("===第二种方式===");

        //第二种方式 - 返回key,value,封装到一个Entry对象中
        Set<Map.Entry<Integer,String>> entrySets = maps.entrySet();

        //迭代set集合,每次拿出来的都是Entry对象
        Iterator<Map.Entry<Integer,String>> entryIterator = entrySets.iterator();
        while(entryIterator.hasNext()){
            Map.Entry<Integer,String> entry = entryIterator.next();
            //获取key
            Integer key = entry.getKey();
            //获取value
            String value = entry.getValue();
            System.out.println(key+"-->"+value);
        }
    }
~~~



# Properties属性类

属于Hashtable[C]下面的子类.

作用:就是JVM加载.propeties属性文件到内存中会绑定到Properties对象中.

基于文件的保存形式在磁盘中的.properties文件.存储的形式:

~~~java
# key - value注释
key1 = value1
key2 = value2
~~~

需要设置propeties文件的编码和.java文件的编码应该要统一 - utf-8



框架的配置文件传统方式有两种,并且默认的就是读取.properties文件.还可以是.xml文件.**还可以是.yml文件.**



为什么要将一些配置信息保存到配置文件中去呢?

就是希望改动配置的时候,不需要对原来的代码重新编译和打包上传.



# TreeMap

* 如果用默认的排序规则[key在字典中的顺序],那么添加到里面的元素,都要去实现java.lang.Comparable接口.

  ~~~java
  TreeMap<Integer,Book> books = new TreeMap<>();
  books.put(1,b1);
  books.put(2,b2);
  此处的Book实体类如果不去实现java.lang.Comparable接口的话,那么会抛出java.lang.ClassCastException
  ~~~

* 可以在构建TreeMap对象的时候,传入一个比较器接口

  ~~~java
  TreeMap<Integer,Book> books = new TreeMap<>(c);
  Book实体类没有必要去实现Comparable接口了.
  可以定制排序的规则,不再是使用key.  o1.getPrice()>o2.getPrice()
  ~~~



# Collections

面试题:Collection和Collections的区别?

java.util.Collections提供了一些操作集合的API,比如排序,反转,随机打乱...



# 集合的细节操作

~~~java
package tech.aistar.day14;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author success
 * @version 1.0
 * @description:本类用来演示:集合的细节操作
 * @date 2019/4/15 0015
 */
public class CollectionDetailsDemo {
    public static void main(String[] args) {
        List<Book> books01 = new ArrayList<>();

        Book b1 = new Book(1,"aaa","小红",100.0,new Date());
        Book b2 = new Book(3,"ccc","小名",300.0,new Date());
        Book b3 = new Book(4,"ddd","小黄",400.0,new Date());

        books01.add(b1);
        books01.add(b2);
        books01.add(b3);

        List<Book> books03 = new ArrayList<>();
        books03.addAll(books01);//非结构性修改仍然会对books03进行了影响.

        //可以利用一个集合来构建出另外一个集合
        //不是同一个对象
        List<Book> bookList = new ArrayList<>(books01);

        System.out.println(books01 == bookList);//false

        //结构性修改..修改books01,不会对基于books01生成的booksList集合造成任何影响.
        //包含随机打乱,移除,排序
        Collections.shuffle(books01);
        books01.remove(b2);

        //非结构性修改 - 修改元素的属性的值
        //会对bookList造成影响
        books01.get(0).setBookName("Java");

        books01.forEach(System.out::println);

        System.out.println("====");

        //观察bookList
        bookList.forEach(System.out::println);

        System.out.println("===");

        books03.forEach(System.out::println);
    }
}

~~~



# 泛型

JDK5.0以后才引入了泛型的概念

本质:类型的参数化 - 自从引入了泛型以后,JCF框架才被叫做类型安全的框架

~~~java
//jdk5.0之前
List list = new ArrayList();//类型不安全的,"ok",23,null,true

//jdk5.0之后 - 类型安全的JCF框架
List<String> list = new ArrayList<>();
~~~



## 泛型不存在多态

**泛型只有编译期间的概念,在运行期间是无效的.**

~~~java
Number n = new Integer(10);//ok
Number m = new Long(20L);//ok

List<Number> n1 = new ArrayList<Integer>();//error
List<Number> m1 = new ArrayList<Long>();//error

System.out.println(n1.getClass().getName());//class java.util.ArrayList
System.out.println(m1.getClass().getName());//class java.util.ArrayList

正因为泛型在运行期间是无效的,所以JDK7.0才去除了运行时类型<>中的类型.
~~~



## 上限和下限

* ? extends T - 指明上限 - 传入T本身或者T的子类
* ? super T - 指明下限 - 传入的是T本身或者T的父类

### 应用

~~~java
List<Integer> n1 = new ArrayList<Integer>();//ok
n1.add(10);
n1.add(20);
n1.add(30);

List<Long> m1 = new ArrayList<Long>();//ok
m1.add(100L);
m1.add(200L);
m1.add(300L);

/**
     * ? extends T - T对象或者是T的子类
     * @param m1
     */
private static void printList(List<? extends Number> m1) {
    Iterator<? extends  Number> iter = m1.iterator();
    while(iter.hasNext()){
        Number n = iter.next();
        System.out.println(n);
    }
}
~~~

## 自定义泛型

~~~java
package tech.aistar.day14.maps;

/**
 * @author success
 * @version 1.0
 * @description:本类用来演示:自定义泛型
 * @date 2019/4/15 0015
 */
public class MyEntry<K,V> {
    private K key;

    private V value;

    public MyEntry() {
    }

    public MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MyEntry{");
        sb.append("key=").append(key);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
~~~



### 应用场景

分页 - 当前页/总页 每页显示多少条 显示的数据.



# 作业

~~~java
public class Purcase{ //购买类
	private String brand; //品牌
	private String name; //产品名
	private double cost; // 费用
}

List<Purcase> list = new ArrayList<>();

Purcase p1 = new Purcase("宝洁","洗手粉",18.5);
Purcase p2 = new Purcase("联合利华","肥皂",4.5);
Purcase p3 = new Purcase("宝洁","牙膏",32.5);
Purcase p4 = new Purcase("宝洁","毛巾",14.5);
Purcase p5 = new Purcase("洁利","洗面奶",26.0);
Purcase p6 = new Purcase("好迪","洗发水",27.5);
Purcase p7 = new Purcase("多芬","沐浴露",38.5);
Purcase p8 = new Purcase("宝洁","洗洁精",3.4);

list.add(p1);
list.add(p2);
....

要求:写一个程序,打印出各品牌所花费的总费用.
[可选,排好序后再打印输出,按花费总费用的降序排序]
Map<String,List<Purcase>> maps = new HashMap<>();
~~~

