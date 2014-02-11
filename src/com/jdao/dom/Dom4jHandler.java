package com.jdao.dom;

import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2012-12-12
 * @verion 1.0
 */
public interface Dom4jHandler {

	public Document loadbyPath(String path) throws Dom4jHandlerException;

	public List<Element> getNodes(String path) throws Dom4jHandlerException;

	public Element getNode(String path) throws Dom4jHandlerException;

}
