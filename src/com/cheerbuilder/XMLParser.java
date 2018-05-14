package com.cheerbuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 03.04.2018.
 *
 * Парсит xml документ и получает хеш-таблицу,
 * состоящую из сокращённого названия валюты и
 * её номинала в эквиваленте рубля
 */
public class XMLParser {

    //private String uri;
    private XMLInitializer xmlInitializer;
    //private File xmlFile;
    private Document xmlDoc;
    private Map<String,Float> moneyMap;
    //private String date;


    //public XMLParser(String uri) throws IOException, ParserConfigurationException, SAXException {
    //    //this.uri = uri;
    //    xmlInitializer = new XMLInitializer(uri);
    //    xmlFile = xmlInitializer.getXmlFile();
    //    xmlDoc = docCreate(xmlFile);
    //    moneyMap = new HashMap<>();
    //    xmlParse();
    //}

    public XMLParser(String uri,String date) throws IOException, ParserConfigurationException, SAXException {
        //this.uri = uri;
        xmlInitializer = new XMLInitializer(uri,date);
        //xmlFile = xmlInitializer.getXmlFile();
        xmlDoc = docCreate(xmlInitializer.getXmlFile());
        moneyMap = new HashMap<>();
        xmlParse();
    }

    /*Создаёт объект для работы с xml данными*/
    private Document docCreate(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();//Создаём фабрику строителей
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder(); //Конкретный строитель документа
        return documentBuilder.parse(xmlFile);
    }

    /*Парсит xml документ и заполняет карту moneyMap (название валюты - значение)*/
    private void xmlParse() {
        //NodeList dataList = xmlDoc.getElementsByTagName("ValCurs");
        //Node node0 = dataList.item(0);
        //String data = ((Element)node0).getAttribute("Date");


        NodeList nodeList = xmlDoc.getElementsByTagName("Valute"); //Получаем список всех узлов с таким названием


        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i); //Получаем каждый узел по отдельгности
            Element element = (Element)node; //Получаем элемент

            StringBuilder key = new StringBuilder(element.getElementsByTagName("CharCode")
                                                             .item(0).getTextContent()
                                                  + " " +  '-' + " "
                                                  + element.getElementsByTagName("Name")
                                                           .item(0).getTextContent());

            int nominal = Integer.parseInt(element.getElementsByTagName("Nominal")
                                                  .item(0).getTextContent());

            StringBuilder val = new StringBuilder(element.getElementsByTagName("Value")
                                                         .item(0).getTextContent());
            /*Костыль для замены запятой на точку*/
            for (int temp = 0; temp < val.length(); temp++) {
                if (val.charAt(temp) == ',')
                    val.setCharAt(temp,'.');
            }

            Float value = Float.valueOf(val.toString()) / nominal;


            moneyMap.put(key.toString(),value);

        }

        /*Добавим рубль*/
        moneyMap.put("RUB - Russian",1F);
    }

    public Map<String,Float> getMoneyMap() {
        return moneyMap;
    }
}
