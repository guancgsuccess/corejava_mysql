package com.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xml.entity.Address;
import com.xml.entity.Student;
import com.xml.util.DateUtil;

/**
 *  
 * 本类用来演示 DOM解析
 *
 * @author success
 *
 * 2010-8-11下午2:21:58
 */
public class StudentsDomParse {
	private static String PATH = "com/xml/dtd/students.xml";

	public static void main(String[] args) {
		try {
			for (Student s : getStudentsFromXML()) {
				System.out.println(s);
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 解析
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static List<Student> getStudentsFromXML()
			throws ParserConfigurationException, SAXException, IOException {
		//1.获取DOM解析工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		//2.通过工厂获取及解析器
		DocumentBuilder db = factory.newDocumentBuilder();

		//3.准备参数
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(PATH);

		Document doc = db.parse(in);

		return handle(doc);
	}

	/**
	 * 处理文档树
	 * @param doc
	 * @return
	 */
	private static List<Student> handle(Document doc) {
		List<Student> students = new ArrayList<>();

		//获取根节点
		Element rootElement = doc.getDocumentElement();

		//获取所有字节点
		NodeList studentList = rootElement.getElementsByTagName("student");

		//遍历
		for (int i = 0; i < studentList.getLength(); i++) {
			//获取到每个学生元素
			Element studentElement = (Element) studentList.item(i);
			//将每个学生元素转换成学生对象
			Student s = handleElement(studentElement);
			//将每个学生添加到集合当中
			students.add(s);
		}

		return students;
	}

	/**
	 * 将每个学生元素转换成学生对象
	 * @param studentElement
	 * @return
	 */
	private static Student handleElement(Element studentElement) {
		Student s = new Student();
		//设置属性
		s.setId(Integer.parseInt(studentElement.getAttribute("id")));

		//设置元素
		//String name = studentElement.getElementsByTagName("name").item(0).getTextContent();
		//s.setName(name);
		s.setName(getValue("name", studentElement));
		s.setNo(getValue("no", studentElement));
		s.setBirthday(DateUtil.parse(getValue("birthday", studentElement),
				"yyyy-MM-dd"));

		//获取地址元素
		Element addrElement = (Element) studentElement.getElementsByTagName(
				"address").item(0);
		if (null != addrElement) {
			Address addr = new Address();
			addr.setCity(getValue("city", addrElement));
			addr.setProvince(getValue("province", addrElement));
			s.setAddr(addr);
		}
		return s;
	}

	/**
	 * 通过元素的标签名来获得中间的文本内容
	 */
	private static String getValue(String tag, Element studentElement) {
		return studentElement.getElementsByTagName(tag).item(0)
				.getTextContent();
	}
}
