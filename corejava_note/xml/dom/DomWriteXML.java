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
 * ����������ʾ��JAVA����ת����XML�ļ�
 *
 * @����:�ܳɹ�
 * @ʱ��:2010-8-11 ����6:50:26
 */
public class DomWriteXML {
	/**
	 * �־û�����XML�ļ���
	 */
	public boolean writeListIntoXML(List<Book> books, String filepath) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			//����DOM��
			Document doc = builder.newDocument();
			//�������ڵ�,����Ϊbooks
			Element root = doc.createElement("books");
			//��ӵ�����
			doc.appendChild(root);
			//��������,���λ�ȡbook����,ת����Element
			Iterator<Book> iter = books.iterator();
			while (iter.hasNext()) {
				Book b = iter.next();
				//��ͼ�����ת����Element����
				Element bookElement = createBookElement(doc, b);
				//�Ѵ����õ�ͼ��Ԫ����ӵ���Ԫ����
				root.appendChild(bookElement);
			}
			return writeDocumentIntoXML(doc, filepath);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��Book����ת����Element����
	 */
	public Element createBookElement(Document doc, Book b) {
		//����һ��Ԫ��
		Element bookElement = doc.createElement("book");
		//����Ԫ������
		bookElement.setAttribute("id", String.valueOf(b.getId()));
		//�����Ԫ��
		bookElement.appendChild(createElement(doc, "name", b.getName()));
		bookElement.appendChild(createElement(doc, "author", b.getAuthor()));
		bookElement.appendChild(createElement(doc, "price",
				String.valueOf(b.getPrice())));
		//�������BookInfo��Ԫ�صĻ�
		if (b.getBookinfo() != null) {
			BookInfo info = b.getBookinfo();
			//����bookInfo��Ԫ��
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
	 * ������Ԫ�ؽڵ�
	 */
	public Element createElement(Document doc, String tagName, String value) {
		Element e = doc.createElement(tagName);
		e.setTextContent(value);
		return e;
	}

	public boolean writeDocumentIntoXML(Document doc, String filepath) {
		boolean r = false;
		//1.��ȡת������
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			//2.ͨ��������ȡת����
			Transformer former = factory.newTransformer();
			//3.׼������
			//3.1.����DOM������XML��Դ
			Source source = new DOMSource(doc);
			//3.2.׼�������������ַ������
			Result result = new StreamResult(new FileWriter(filepath));

			//���ø�ʽ
			//1.���ñ���
			former.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			//2.���û���
			former.setOutputProperty(OutputKeys.INDENT, "yes");
			//3.��������
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
		Book b1 = new Book(1, "��������", "����", 50, null);
		BookInfo info = new BookInfo("���������", new Date(), 1000, "����һ������");
		b1.setBookinfo(info);
		Book b2 = new Book(2, "��������", "JK����", 120, info);
		Book b3 = new Book(3, "��������", "�Ϸ�", 80, info);
		Book b4 = new Book(4, "�����", "Ī��", 70, info);
		List<Book> books = new ArrayList<Book>();
		books.add(b1);
		books.add(b2);
		books.add(b3);
		books.add(b4);
		boolean bool = dwx.writeListIntoXML(books, "F:\\XML\\books.xml");
		if (bool) {
			System.out.println("XML�ļ����ɳɹ�!");
		} else {
			System.out.println("����ʧ��!");
		}
	}
}
