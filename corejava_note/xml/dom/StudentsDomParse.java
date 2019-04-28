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
 * ����������ʾ DOM����
 *
 * @author success
 *
 * 2010-8-11����2:21:58
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
	 * ����
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static List<Student> getStudentsFromXML()
			throws ParserConfigurationException, SAXException, IOException {
		//1.��ȡDOM��������
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		//2.ͨ��������ȡ��������
		DocumentBuilder db = factory.newDocumentBuilder();

		//3.׼������
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(PATH);

		Document doc = db.parse(in);

		return handle(doc);
	}

	/**
	 * �����ĵ���
	 * @param doc
	 * @return
	 */
	private static List<Student> handle(Document doc) {
		List<Student> students = new ArrayList<>();

		//��ȡ���ڵ�
		Element rootElement = doc.getDocumentElement();

		//��ȡ�����ֽڵ�
		NodeList studentList = rootElement.getElementsByTagName("student");

		//����
		for (int i = 0; i < studentList.getLength(); i++) {
			//��ȡ��ÿ��ѧ��Ԫ��
			Element studentElement = (Element) studentList.item(i);
			//��ÿ��ѧ��Ԫ��ת����ѧ������
			Student s = handleElement(studentElement);
			//��ÿ��ѧ����ӵ����ϵ���
			students.add(s);
		}

		return students;
	}

	/**
	 * ��ÿ��ѧ��Ԫ��ת����ѧ������
	 * @param studentElement
	 * @return
	 */
	private static Student handleElement(Element studentElement) {
		Student s = new Student();
		//��������
		s.setId(Integer.parseInt(studentElement.getAttribute("id")));

		//����Ԫ��
		//String name = studentElement.getElementsByTagName("name").item(0).getTextContent();
		//s.setName(name);
		s.setName(getValue("name", studentElement));
		s.setNo(getValue("no", studentElement));
		s.setBirthday(DateUtil.parse(getValue("birthday", studentElement),
				"yyyy-MM-dd"));

		//��ȡ��ַԪ��
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
	 * ͨ��Ԫ�صı�ǩ��������м���ı�����
	 */
	private static String getValue(String tag, Element studentElement) {
		return studentElement.getElementsByTagName(tag).item(0)
				.getTextContent();
	}
}
