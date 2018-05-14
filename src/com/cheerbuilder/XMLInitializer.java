package com.cheerbuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Получает xml  по заданному URI
 * и записывает в файл
 */
public class XMLInitializer {

    private URLConnection connect;
    private String uri;
    private Scanner scanner;
    private String xmlAsString;
    private File xmlFile;
    private String date;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    public static final String CURRENT_DATE = sdf.format(new Date());
    private static final String DATE_PARAMETR = "?date_req=";



    /*"Дефолтный" конструктор без указания даты*/
   //public XMLInitializer(String uri) throws IOException {
   //    this.uri = uri;
   //    connect = new URL(uri).openConnection();
   //    getDataFromXml();
   //}

    /*Конструктор с заданием конкретной даты*/
    public XMLInitializer(String uri,String date) throws IOException {
        this.uri = uri;
        this.date = date;
        String uriWithDate = uri + DATE_PARAMETR + date;
        connect = new URL(uriWithDate).openConnection();
        getDataFromXml();
    }


    /*Получает данные с xml страницы и записывает в файл*/
    private void getDataFromXml() throws IOException {
        /*Если файл с данными уже существует*/
        if (!(new File(date.replace('/','.') + ".xml")).exists()) {
            scanner = new Scanner(connect.getInputStream()); //Получаем данные из xml
            scanner.useDelimiter("\\Z");//Без него не работает, не понял почему
            xmlAsString = scanner.next(); //Записываем в строку
            fileCreate();
        } else {
            xmlFile = new File(date.replace('/','.') + ".xml");
        }

    }

    /*Создаёт xml-файл*/
    private void fileCreate() throws IOException {

        /*Задаём имя файла*/
        StringBuilder fileName = new StringBuilder("");
        //String uri = connect.getURL().toString();
        //int lastSlash = connect.getURL().toString().lastIndexOf('/');
        //for (int i = lastSlash + 1; i < uri.length(); i++) {
        //    fileName.append(uri.charAt(i));
        //}
        fileName.append(date.replace('/','.'));
       // System.out.println(date);

        /*Заполняем файл данными из xml*/
        xmlFile = new File(fileName.toString() + ".xml");

        try ( FileWriter fileWriter = new FileWriter(xmlFile)) {
            fileWriter.write(xmlAsString);
        }
    }


    public File getXmlFile() {
        return xmlFile;
    }



}
