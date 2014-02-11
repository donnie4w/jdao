package com.jdao.dom;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2012-12-12
 * @verion 1.0
 */
public class Dom4jHandlerImpl implements Dom4jHandler {

	private Document document;
	private static OutputFormat outputFormat = OutputFormat.createPrettyPrint();

	static {
		outputFormat.setExpandEmptyElements(true);
	}

	@Override
	public Element getNode(String path) throws Dom4jHandlerException {
		Element visitElement = null;
		try {
			String[] ss = path.split("/");
			Element e = document.getRootElement();
			int i = 0;
			for (String s : ss) {
				if (i == 0) {
					i++;
					continue;
				}
				e = e.element(s);
			}
			visitElement = e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Dom4jHandlerException("element [" + path + "] not exist");
		}
		return visitElement;
	}

	@SuppressWarnings("unchecked")
	public List<Element> getChildNodes(String path) throws Dom4jHandlerException {
		Element element = this.getNode(path);
		if (element != null) {
			List<Element> lists = element.elements();
			return lists;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Element> getNodes(String path) throws Dom4jHandlerException {
		String elementName = path.substring(path.lastIndexOf("/") + 1, path.length());
		String newPath = path.substring(0, path.lastIndexOf("/"));
		Element superElement = this.getNode(newPath);
		return superElement.elements(elementName);
	}

	@Override
	public Document loadbyPath(String path) throws Dom4jHandlerException {
		SAXReader sr = new SAXReader();
		try {
			// File file = new File(path);
			this.document = sr.read(Dom4jHandlerImpl.class.getResourceAsStream(path));
			return this.document;
		} catch (Exception e) {
			throw new Dom4jHandlerException(e);
		}
	}

	@Override
	public String toString() {
		StringBuilder ss = new StringBuilder();
		Element element = this.document.getRootElement();
		getElements(element, ss);
		return ss.toString();
	}

	@SuppressWarnings("unchecked")
	private void getElements(Element element, StringBuilder ss) {
		String name = element.getName();
		String text = element.getText();
		List<Attribute> attributes = element.attributes();
		ss.append("<" + name);
		for (Attribute attr : attributes) {
			String attrName = attr.getName();
			String attrValue = attr.getValue();
			ss.append(" " + attrName + "=" + "\"" + attrValue + "\"");
		}
		ss.append(">");
		List<Element> elements = element.elements();
		for (Element e : elements) {
			getElements(e, ss);
		}
		ss.append(text.replaceAll("(\t)|(\n)", ""));
		ss.append("</" + name + ">");
	}

}