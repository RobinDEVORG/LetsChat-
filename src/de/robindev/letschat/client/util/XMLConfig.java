package de.robindev.letschat.client.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author RobinDEV (10.09.2016, 11:29:28)
 */
public class XMLConfig {

	private DocumentBuilderFactory builderFactory;
	private DocumentBuilder docBuilder;
	private Document doc;

	private TransformerFactory transformerFactory;
	private Transformer transformer;

	private DOMSource source;
	private StreamResult result;

	public XMLConfig(File file) {
		builderFactory = DocumentBuilderFactory.newInstance();
		transformerFactory = TransformerFactory.newInstance();

		try {
			docBuilder = builderFactory.newDocumentBuilder();
			transformer = transformerFactory.newTransformer();
		} catch (ParserConfigurationException | TransformerConfigurationException ex) {
			ex.printStackTrace();
		}

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			doc = docBuilder.parse(file);
		} catch (Exception e) {
		}

		if (doc == null) {
			doc = docBuilder.newDocument();
		}

		System.out.println(doc == null);

		source = new DOMSource(doc);
		result = new StreamResult(file);
	}

	public Element addElement(String name) {
		Element root = doc.createElement(name);
		doc.appendChild(root);
		return root;
	}

	public Element addElement(Element element, String name) {
		Element root = doc.createElement(name);
		element.appendChild(root);
		return root;
	}

	public Attr setAttribute(Element element, String name, String value) {
		Attr result = doc.createAttribute(name);
		result.setValue(value);
		element.setAttributeNode(result);
		return result;
	}

	public boolean elementExists(String elementName) {
		return doc.getElementsByTagName(elementName).getLength() > 0;
	}

	public NodeList getNodeList(String tag) {
		return doc.getElementsByTagName(tag);
	}

	public boolean save() {
		try {
			doc.normalize();
			transformer.transform(source, result);
			return true;
		} catch (TransformerException e) {
			e.printStackTrace();
			return false;
		}
	}
}
