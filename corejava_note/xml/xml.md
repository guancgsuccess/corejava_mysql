# XML介绍

~~~java
XML是Extensible Markup Language的简写，一种扩展性标识语言。 
XML里允许你自己创建这样的标签，所以叫做可扩展性。
~~~



## xml和html区别

* html中的标签是固定的,而xml中的标签是可以自定义的.并且可以是中文,但是不推荐
* html是负责展示数据的.而xml是负责保存数据的.[xml是可以当做数据库来使用的].

## XML用途

- 把数据从 HTML 分离：数据能够存储在独立的 XML 文件中；
- 简化数据共享：不兼容的系统之间轻松地交换数据；如中英文切换
- 简化数据传输：XML 数据以文本格式存储。



## json格式

用来进行数据共享和传输的数据格式推荐使用json格式.

比xml要比较"干净".解析json和解析xml,前者性能高.



## XML应用场景

- 数据交换 
- Web服务 
- 内容管理 
- Web集成 
- 配置 - xml配置,properties配置,yml配置

# XML语法

首先,要保证你所写的XML是**格式良好的**(Well Formed)
使用浏览器来检测即可.

1. 第一条语句

   ~~~xml
   <?xml version="1.0" encoding="UTF-8"?>
   ~~~

2. 标记用<>括起来

3. 标记有开始就有结束,一定要成对.如:

   ~~~xml
   <a></a>
   ~~~

4. 标记中可以包含属性,同一个标记中属性是唯一的,不能同名
   属性必须是:name=value,值使用双引号或者**单引号**括起来.如:

   ~~~xml
   <cd id="001" type='摇滚'></cd>
   ~~~

5. 标记可以嵌套,但是不能互相包含
   ~~~xml 
   <a><b></b></a>//OK
   <a><b></a></b>//error
   ~~~

6. 标记名称可以是任意的字母组成,含中文或者数字.
     注意:标记尽量取有意义的名字.
7. 任何XML文件,有且仅有一个根标记.

## 示例

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<mycd>
	<cd id="001">
		<artist>张学友</artist>
		<year>1998</year>
		<company>滚石唱片</company>
		<music>17</music>
	</cd>
	<cd id="002">
		<artist>李代沫</artist>
		<year>2012</year>
		<company>中国好声音</company>
		<music>10</music>
	</cd>
</mycd>
~~~

### 练习

练习:[写一个格式良好的XML文档]
描述一下自己所喜欢的或玩过的游戏产品
至少写3样

~~~xml
<game id="" isfree="">
~~~

* 名称
* 公司
* 发行时间
* 游戏类型
* 运行模式

# xml约定

目的是在应用系统中制定好XML文档的结构，包含：标记名称、标记的出现次序。

W3C制定的两种技术来约定 XML 文档：
1. **DTD** 
2. **SCHEMA**



## DTD是什么?

Document Type Definition 文档类型定义
它是用来在一个应用系统中限定XML文档的标记以及它的层次结构.

* 满足DTD要求的XML,叫做合法的文档
* 合法的文档一定是格式良好的
* 格式良好的文档不一定是合法的



### DTD语法

~~~dtd
<!ELEMENT games (game)+>
<!ELEMENT game (name, company, year, type)>
<!ATTLIST game id CDATA #REQUIRED>
<!ATTLIST game isfree CDATA #IMPLIED>
<!ELEMENT name (#PCDATA)>
<!ELEMENT company (#PCDATA)>
<!ELEMENT year (#PCDATA)>
<!ELEMENT type (#PCDATA)>
~~~

## 注意点

类型:

* CDTA - 修饰属性的类型
* PCDATA - 修饰元素,标签,标记的类型

#REQUIRED - 必须要写的属性

#IMPLIED - 可有可无



**注:属性一定依附于元素存在**

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE games SYSTEM "games.dtd">
<games>
	<game id='1001' isfree='true'>
     	<name></name>   
        <company></company>
        <year></year>
        <type></type>
    </game>
    <game id="1002">
    	<name></name>   
        <company></company>
        <year></year>
        <type></type>
    </game>
</games>
~~~



### DTD缺点

* DTD不能自定义类型 
* DTD的内置类型太少，只有 #PCDATA / CDATA
* DTD中对元素的次数限定不够精确[+ ? *]
* DTD的语法是全新的，与XML没有关系。

## Schema是什么?

基于DTD的缺点,W3C制定了新的用来限定XML规范的标准,
这个标准就是Schema.

Schema的出现是为了替代DTD,它修复了DTD的所有缺点,并且可以满足DTD的所有功能.

更重要的一点是,**Schema采用的也是XML语法,**当然,Schema本身的标记是固定的.



### Schema常用标记

元素 						解释
schema 					定义 schema 的根元素。
attribute 				定义一个属性。
sequence 				要求子元素必须按顺序出现。每个子元素可出现 0 到任意次数。
group 					定义在复杂类型定义中使用的元素组。
complexType 			定义复杂类型。
simpleType 				定义一个简单类型，规定约束以及关于属性或仅含文本的元素的值的信息。attributeGroup 			定义在复杂类型定义中使用的属性组。
complexContent 			定义对复杂类型（包含混合内容或仅包含元素）的扩展或限制。
documentation 			定义 schema 中的文本注释。
element 				定义元素。
extension 				扩展已有的 simpleType 或 complexType 元素。
field 					规定 XPath 表达式，该表达式规定用于定义标识约束的值。
import 					向一个文档添加带有不同目标命名空间的多个 schema。
include					向一个文档添加带有相同目标命名空间的多个 schema。
key 						指定属性或元素值（或一组值）必须是指定范围内的键。
keyref 					规定属性或元素值（或一组值）对应指定的 key 或 unique 元素的值。
list 						把简单类型定义为指定数据类型的值的一个列表。
notation 				描述 XML 文档中非 XML 数据的格式。
redefine 				重新定义从外部架构文件中获取的简单和复杂类型、组和属性组。
restriction 				定义对 simpleType、simpleContent 或 complexContent 的约束。
all 						规定子元素能够以任意顺序出现，每个子元素可出现零次或一次。
selector 					指定 XPath 表达式，该表达式为标识约束选择一组元素。
simpleContent			 包含对 complexType 元素的扩展或限制且不包含任何元素。
union 					定义多个 simpleType 定义的集合。
unique 					指定属性或元素值（或者属性或元素值的组合）在指定范围内必须是唯一的。



### Schema示例

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://www.w3.org/2001/XMLSchema"
	targetNamespace="http://www.example.org/student_1_0"
	xmlns:tns="http://www.example.org/student_1_0"
	elementFormDefault="qualified">
	<element name="students">
		<complexType>
			<sequence minOccurs="1" maxOccurs="unbounded">
				<element name="student" type="tns:StuType"/>
			</sequence>
		</complexType>
	</element>
	<complexType name="StuType">
		<sequence>
			<element name="name" type="string"/>
			<element name="no" type="string"/>
			<element name="birthday" type="date"/>
			<element name="address" type="tns:AddrType"/>
		</sequence>
		<attribute name="id" type="int" use="required"></attribute>
	</complexType>
	<complexType name="AddrType">
		<sequence>
			<element name="province" type="string"/>
			<element name="city" type="string"/>
		</sequence>
	</complexType>
</schema>
~~~

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<tns:students xmlns:tns="http://www.example.org/student_1_0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.example.org/student_1_0 students_1_0.xsd ">
  <tns:student id="001">
    <tns:name>Jack</tns:name>
    <tns:no>1001</tns:no>
    <tns:birthday>2001-01-01</tns:birthday>
    <tns:address>
      <tns:province>江苏</tns:province>
      <tns:city>常州</tns:city>
    </tns:address>
  </tns:student>
  <tns:student id="002">
    <tns:name>Tom</tns:name>
    <tns:no>1002</tns:no>
    <tns:birthday>2001-01-01</tns:birthday>
    <tns:address>
      <tns:province>江苏</tns:province>
      <tns:city>常州</tns:city>
    </tns:address>
  </tns:student>
  <tns:student id="003">
    <tns:name>Jack</tns:name>
    <tns:no>1003</tns:no>
    <tns:birthday>2001-01-01</tns:birthday>
    <tns:address>
      <tns:province>江苏</tns:province>
      <tns:city>常州</tns:city>
    </tns:address>
  </tns:student>
</tns:students>
~~~



### XML命名空间

作用就是防止命名冲突

~~~xml
<element xmlns:命名空间简称="公司URL">
~~~

# XML解析方式

在得到一个XML文件之后，应该利用程序按照里面元素的定义名称取出相应的内容，这就是XML解析。

* SAX解析 - 原生的 - jdk提供的API - 只能读,不能写 - 基于事件驱动的方式.
* DOM方式 - 原生的DOM操作 - jdk中提供的API - 写法相当繁琐
* **DOM4J方式 - 重点掌握** - 使用的是第三方jar包 - 进行了高级的封装
* JDOM方式 - 使用的是第三方jar
* **JAXB - JDK7.0中提供的新特性 - 基于注解的方式.**



## 几种解析方式的优缺点

1. SAX是基于事件流的解析

   > a.它是顺序的读取XML文件，不需要一次把整个文件都读入内存。当遇到文件开头、文档结束、或者开始标签和结束标签时，它会触发一个事件，用户通过在其回调事件中写入处理代码来处理XML文件，适用与XML的顺序访问。
   >
   > 
   >
   > b.顺序的过程，解析速度快，由于其不需要将整个 XML 文档读入内存当中，它对系统资源的节省是十分显而易见的.
   >
   > 
   >
   > c.适用范围：大型 XML 文件解析、只需要部分解析或者只想取得部分 XML 树内容、
   > 有 XPath 查询需求、有自己生成特定 XML 树对象模型的需求

2. DOM 可逆的过程

   > a.将整个XML文件装入，组装成一颗DOM树，然后通过节点以及节点之间的关系来解析xml文件，适合对对XML的随机访问。
   >
   > b.不过当XML过大时，其性能会严重下降；这个问题是DOM树形结构造成的，这种结构内存占用较大。
   >
   > c.适用范围：小型 XML 文件解析、需要全解析或者大部分解析 XML、需要修改 XML 树内容以生成自己的对象模型.

3. DOM4J解析

   >DOM4J有更复杂的api,所以dom4j比jdom有更大的灵活性.DOM4J性能最好，连Sun的JAXM也在用DOM4J.目前许多开源项目中大量采用DOM4J，例如大名鼎鼎的Hibernate也用DOM4J来读取XML配置文件。如果不考虑可移植性，那就采用DOM4J.
   >
   >优点：1. 灵活性最高	
   >
   >	    2. 易用性和功能强大、性能优异
   >
   >缺点：1. 复杂的api	
   >
   >	    2. 移植性差
   >
   >适用：自行选择

