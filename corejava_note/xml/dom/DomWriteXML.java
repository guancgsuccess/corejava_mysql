package com.xml.dom;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xml.util.DateUtil;
import com.xml.entity.Book;
import com.xml.entity.BookInfo;

/**
 * 本类用来演示把JAVA对象转换成XML文件
 *
 * @作者:管成功
 * @时间:2010-8-11 下午6:50:26
 */
public class DomWriteXML {
	/**
	 * 持久化对象到XML文件中
	 */
	public boolean writeListIntoXML(List<Book> books, String filepath) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//创建DOM树
			Document doc = builder.newDocument();
			//创建根节点,命名为books
			Element root = doc.createElement("books");
			//添加到树上
			doc.appendChild(root);
			//遍历集合,依次获取book对象,转换成Element
			Iterator<Book> iter = books.iterator();
			while (iter.hasNext()) {
				Book b = iter.next();
				//把图书对象转换成Element对象
				Element bookElement = createBookElement(doc, b);
				//把创建好的图书元素添加到根元素上
				root.appendChild(bookElement);
			}
			return writeDocumentIntoXML(doc, filepath);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 把Book对象转换成Element对象
	 */
	public Element createBookElement(Document doc, Book b) {
		//创建一个元素
		Element bookElement = doc.createElement("book");
		//设置元素属性
		bookElement.setAttribute("id", String.valueOf(b.getId()));
		//添加子元素
		bookElement.appendChild(createElement(doc, "name", b.getName()));
		bookElement.appendChild(createElement(doc, "author", b.getAuthor()));
		bookElement.appendChild(createElement(doc, "price",
				String.valueOf(b.getPrice())));
		//如果存在BookInfo子元素的话
		if (b.getBookinfo() != null) {
			BookInfo info = b.getBookinfo();
			//创建bookInfo子元素
			Element bookInfo = doc.createElement("bookinfo");
			bookInfo.appendChild(createElement(doc, "publish_house",
					info.getPublish_house()));
			bookInfo.appendChild(createElement(doc, "publish_date",
					DateUtil.format(info.getPublish_date(), "yyyy-MM-dd")));
			bookInfo.appendChild(createElement(doc, "pages",
					String.valueOf(info.getPages())));
			bookInfo.appendChild(createElement(doc, "description",
					info.getDescription()));
			bookElement.appendChild(bookInfo);
		}
		return bookElement;
	}

	/**
	 * 创建子元素节点
	 */
	public Element createElement(Document doc, String tagName, String value) {
		Element e = doc.createElement(tagName);
		e.setTextContent(value);
		return e;
	}

	public boolean writeDocumentIntoXML(Document doc, String filepath) {
		boolean r = false;
		//1.获取转换工厂
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			//2.通过工厂获取转换器
			Transformer former = factory.newTransformer();
			//3.准备参数
			//3.1.根据DOM树构建XML资源
			Source source = new DOMSource(doc);
			//3.2.准备输出结果集的字符输出流
			Result result = new StreamResult(new FileWriter(filepath));

			//设置格式
			//1.设置编码
			former.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//2.设置换行
			former.setOutputProperty(OutputKeys.INDENT, "yes");
			//3.设置缩进
			former.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			
			former.transform(source, result);
			r = true;
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return r;
	}

	public static void main(String[] args) {
		DomWriteXML dwx = new DomWriteXML();
		Book b1 = new Book(1, "骆驼祥子", "老舍", 50, null);
		BookInfo info = new BookInfo("人民出版社", new Date(), 1000, "这是一本好书");
		b1.setBookinfo(info);
		Book b2 = new Book(2, "哈利波特", "JK罗玲", 120, info);
		Book b3 = new Book(3, "随遇而安", "孟非", 80, info);
		Book b4 = new Book(4, "红高粱", "莫言", 70, info);
		List<Book> books = new ArrayList<Book>();
		books.add(b1);
		books.add(b2);
		books.add(b3);
		books.add(b4);
		boolean bool = dwx.writeListIntoXML(books, "F:\\XML\\books.xml");
		if (bool) {
			System.out.println("XML文件生成成功!");
		} else {
			System.out.println("操作失败!");
		}
	}
}
