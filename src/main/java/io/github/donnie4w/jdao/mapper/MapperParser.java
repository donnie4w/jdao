/*
 * Copyright (c) 2024, donnie <donnie4w@gmail.com> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * github.com/donnie4w/jdao
 */

package io.github.donnie4w.jdao.mapper;

import io.github.donnie4w.jdao.handle.JdaoException;
import io.github.donnie4w.jdao.handle.JdaoRuntimeException;
import io.github.donnie4w.jdao.util.Logger;
import io.github.donnie4w.jdao.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapperParser {
    final static Map<String, ParamBean> mapper = new ConcurrentHashMap<>();
    final static Map<String, List<String>> namespaceMapper = new ConcurrentHashMap<>();

    public static ParamBean getParamBean(String mapperId) {
        return mapper.get(mapperId);
    }

    public static ParamBean getParamBean(String namespace, String id) {
        if (Utils.stringValid(namespace) && Utils.stringValid(id)) {
            return mapper.get(namespace.concat(".").concat(id));
        }
        return null;
    }

    static boolean containsMapperId(String mapperId) {
        return mapper.containsKey(mapperId);
    }

    public static List<String> getMapperIds(String namespace) {
        return namespaceMapper.get(namespace);
    }

    static boolean hasMapper() {
        return mapper.size() > 0 || namespaceMapper.size() > 0;
    }

    static void Mapper(String xmlpath) throws JdaoException {
        try {
            File inputFile = new File(xmlpath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList mapperList = doc.getElementsByTagName("mappers");
            if (mapperList.getLength() > 0) {
                Node mapperNode = mapperList.item(0);
                NodeList nodeList = mapperNode.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node mappernode = nodeList.item(i);
                    if (mappernode.getNodeType() == Node.ELEMENT_NODE) {
                        if (mappernode.getNodeName().equals("mapper")) {
                            Element element = (Element) mappernode;
                            String resource = element.getAttribute("resource");
                            Logger.log("[parser mapper resource: ]", resource);
                            parseMapper(resource);
                        }
                    }
                }
            } else {
                parseMapper(xmlpath);
            }
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

    private static void namespaceMapperAdd(String namespace, String id) {
        if (!namespaceMapper.containsKey(namespace)) {
            namespaceMapper.put(namespace, new ArrayList<>());
        }
        if (id != null) namespaceMapper.get(namespace).add(id);
    }

    private static void parseMapper(String xmlpath) throws JdaoException {
        try {
            File inputFile = new File(xmlpath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList mapperList = doc.getElementsByTagName("mapper");
            for (int i = 0; i < mapperList.getLength(); i++) {
                Node mapperNode = mapperList.item(i);
                if (mapperNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element mapperElement = (Element) mapperNode;
                    String namespace = mapperElement.getAttribute("namespace");
                    NodeList nodeList = mapperElement.getChildNodes();
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        Node selectNode = nodeList.item(j);
                        if (selectNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) selectNode;
                            String id = element.getAttribute("id");
                            String inputType = element.getAttribute("parameterType");
                            String resultType = element.getAttribute("resultType");
                            String sql = getTextContent(element);
                            String sqlType = element.getTagName().toLowerCase();
                            if (sqlType == "select") {
                                namespaceMapperAdd(namespace, id);
                            }
                            ParamBean paramBean = new ParamBean(namespace, id, sqlType, sql, inputType, resultType);
                            SqlNode node = sqlNode(element);
                            if (node != null) {
                                paramBean.sqlnode = node;
                            }
                            String mapkey = namespace == null ? id : namespace + "." + id;
                            if (mapper.containsKey(mapkey)) {
                                String s = String.format("namespace and id are defined repeatedly: [namespace:%s][id:%s]", mapkey, id);
                                throw new JdaoException(s);
                            }
                            mapper.put(mapkey, paramBean);
                        }
                    }
                }
            }
            Logger.log("jdao Mapper parsing completed");
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

    static String getTextContent(Element selectElement) {
        StringBuilder content = new StringBuilder();
        NodeList childNodes = selectElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                content.append(node.getTextContent().trim()).append(" ");
            }
        }
        return content.toString().trim();
    }


    static SqlNode sqlNode(Element element) {
        StringBuilder content = new StringBuilder();
        NodeList childNodes = element.getChildNodes();

        List<Element> elementList = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                content.append(node.getTextContent().trim()).append(" ");
            }
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elementList.add((Element) node);
            }
        }

        if (elementList.size() == 0) {
            return null;
        }

        content.toString().trim();
        SqlNode sqlNode;
        switch (element.getTagName().toLowerCase()) {
            case "select":
                sqlNode = new Select(content.toString().trim());
                break;
            case "update":
                sqlNode = new Update(content.toString().trim());
                break;
            case "insert":
                sqlNode = new Insert(content.toString().trim());
                break;
            case "delete":
                sqlNode = new Delete(content.toString().trim());
                break;
            default:
                if (Logger.isVaild()) {
                    Logger.severe("Unsupport tag: " + element.getTagName());
                }
                throw new JdaoRuntimeException("Unsupport tag: " + element.getTagName());
        }
        for (Element elem : elementList) {
            SqlNode node = getSqlNode(elem);
            if (node != null) {
                sqlNode.addSqlNode(node);
            }
        }
        return sqlNode;
    }

    static SqlNode getSqlNode(Element element) {
        StringBuilder content = new StringBuilder();
        NodeList childNodes = element.getChildNodes();
        List<Element> elementList = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                content.append(node.getTextContent().trim()).append(" ");
            }
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elementList.add((Element) node);
            }
        }
        SqlNode node = null;
        switch (element.getTagName().toLowerCase()) {
            case "where":
                node = new WhereNode(content.toString().trim());
                break;
            case "trim":
                node = new TrimNode(element.getAttribute("prefix"), element.getAttribute("suffix"), element.getAttribute("prefixOverrides"), element.getAttribute("suffixOverrides"));
                break;
            case "set":
                node = new SetNode();
                break;
            case "foreach":
                node = new Foreach(content.toString().trim(), element.getAttribute("collection"), element.getAttribute("item"), element.getAttribute("index"), element.getAttribute("open"), element.getAttribute("close"), element.getAttribute("separator"));
                break;
            case "choose":
                node = new ChooseNode(content.toString().trim(), element.getAttribute("test"));
                break;
            case "when":
                node = new WhenNode(content.toString().trim(), element.getAttribute("test"));
                break;
            case "otherwise":
                node = new OtherWise(content.toString().trim(), element.getAttribute("test"));
                break;
            case "if":
                node = new IfNode(content.toString().trim(), element.getAttribute("test"));
                break;
            default:
                if (Logger.isVaild()) {
                    Logger.severe("Unsupport tag: " + element.getTagName());
                }
                throw new JdaoRuntimeException("Unsupport tag: " + element.getTagName());
        }
        for (Element elem : elementList) {
            node.addSqlNode(getSqlNode(elem));
        }
        return node;
    }


}