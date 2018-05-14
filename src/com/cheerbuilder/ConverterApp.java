package com.cheerbuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Created by 1 on 03.04.2018.
 */
public class ConverterApp  {
    public static void main(String[] args) throws ParserConfigurationException,
                                                  SAXException,
                                                  IOException {
        ConverterGUI.createGUI();
    }


}





