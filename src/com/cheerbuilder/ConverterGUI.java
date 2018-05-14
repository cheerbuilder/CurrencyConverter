package com.cheerbuilder;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 04.04.2018.
 *
 * GUI для конвертера валют
 */
public class ConverterGUI extends JPanel {

    /*Графические элементы*/
    private JPanel mainPanel;
    private JPanel[] valutePanel;
    private JComboBox[] valuteList;
    private JFormattedTextField[] valuteText;
    private JFormattedTextField dateField;


    /*Элементы конвертера*/
    private  String uri;
    private  XMLParser xmlParser;
    private  Map<String,Float> moneyMap;
    private  Map<String,Float> defaultMoneyMap;
    private  Object[] valutes;
    private  Converter converter;

    /*Сохраняемые значения ввода*/
    private StringBuilder[] textFieldCache;


    public ConverterGUI() throws ParserConfigurationException, SAXException, IOException {

        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));

        /*Получаем данные о валютах*/
        setConverterObjects(XMLInitializer.CURRENT_DATE);
        guiComponentsCreating();

        /*Создаём слушателей для текстовых полей и выпадающих списков*/
        comBoxesController();
        textFieldsController();
        dataFieldController();

    }

    /*Создание панели графических элементов*/
    private void guiComponentsCreating() {

        /*Создаём панель графических элементоа*/
        valutePanel = new JPanel[2];
        valuteList = new JComboBox[2];
        valuteText = new JFormattedTextField[2];


        for(int i = 0; i <= 1; i++) {
            valuteList[i] = createValuteList();
            valuteText[i] = createTextField(new Dimension(100,30));
            valutePanel[i] = createValutePanel(valuteList[i],valuteText[i]);
        }

        textFieldCache = new StringBuilder[2];

        dateField = createDateField(new Dimension(100,30));
        dateField.setValue(new Date());


       /*Создаём главную панель*/
        mainPanel = createPanelForComponents(new JComponent[]{valutePanel[0],
                                                              valutePanel[1],
                                                              dateField},
                                             BoxLayout.Y_AXIS);



        add(mainPanel);
    }

    /*Создаёт "валютную" панель (комбокс + текстфилд)*/
    private JPanel createValutePanel(JComboBox valList, JFormattedTextField valText) {
        JPanel valutePanel = new JPanel();
        JComponent[] components = {valList,valText};
        valutePanel = createPanelForComponents(components,BoxLayout.X_AXIS);
        return  valutePanel;
    }

    /*Создаёт панель для компонентов*/
    public JPanel createPanelForComponents(JComponent[] comp,int boxType) {
        JPanel panel = new JPanel();
        for (JComponent component : comp) {
            panel.setLayout(new BoxLayout(panel,boxType));
            panel.add(component);
        }
        return panel;
    }

    /*Создаёт выпадающий список с валютами*/
    private JComboBox createValuteList() {
        final DefaultComboBoxModel listModel = new DefaultComboBoxModel();
        for (Object valute : valutes) {
            listModel.addElement(valute);
        }
        JComboBox valuteList = new JComboBox(listModel);
        return valuteList;
    }

    /*Обновляет комбоксы*/
    //private void valuteListUpdate() {
    //    /*НУЖНО СДЕЛАТЬ, ЧТОБЫ ОБНОВЛЯЛ СПИСОК ВАЛЮТ В СЛУЧАЕ !!!*/
    //    //DefaultComboBoxModel model1 = (DefaultComboBoxModel)valuteList[0].getModel();
    //    //DefaultComboBoxModel model2 = (DefaultComboBoxModel)valuteList[1].getModel();
    //    //model1.removeAllElements();
    //    //model2.removeAllElements();
    //    //for (Object valute : valutes) {
    //    //    model1.addElement(valute);
    //    //    model2.addElement(valute);
    //    //    //System.out.println(valute);
    //    //}
    //    //System.out.println(valuteList[0].getItemAt(0));

   // //}

    /*Создаёт текстовое поле для ввода*/
    private JFormattedTextField createTextField(Dimension dim) {
        NumberFormat onlyNumber = new DecimalFormat("##0.###");
        NumberFormatter formatter = new NumberFormatter(onlyNumber);
        //formatter.setAllowsInvalid(false);
        //formatter.setOverwriteMode(true);
        JFormattedTextField textField = new JFormattedTextField(formatter);

        textField.setPreferredSize(dim);
        return  textField;
    }
    /*Текстовое поле для ввода даты*/
    private JFormattedTextField createDateField(Dimension dim) {
        DateFormat onlyDate = new SimpleDateFormat("dd.MM.yyyy");
        DateFormatter dateFormatter = new DateFormatter(onlyDate);
        dateFormatter.setAllowsInvalid(false);
        //dateFormatter.setOverwriteMode(true);

        JFormattedTextField textField = new JFormattedTextField(dateFormatter);
        textField.setPreferredSize(dim);
        return  textField;
    }

    /*Обработчик ввода текстовых полей*/
    private void textFieldsController() {
        valuteText[0].addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                valuteText[1].setText("");
                textFieldCache[1] = new StringBuilder("");
                textFieldCache[0] = new StringBuilder(valuteText[0].getText());
                if (!valuteText[0].getText().equals("")) {
                    createConverter(valuteList,valuteText);
                    setResults();
                }


            }

            @Override
            public void keyTyped(KeyEvent e) {
                String acceptChar = "0123456789.";
                int num = 0;
                for (char c : acceptChar.toCharArray()) {
                    if (e.getKeyChar() == c) {
                        num++;
                    }
                }
                if (num == 0) {
                    e.consume();
                }
            }
        });


        valuteText[1].addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                valuteText[0].setText("");
                textFieldCache[0] = new StringBuilder("");
                textFieldCache[1] = new StringBuilder(valuteText[1].getText());
                if (!valuteText[1].getText().equals("")) {
                    createConverter(valuteList,valuteText);
                    setResults();
                }
                //System.out.println("1:" + textFieldCache[0]);
                //System.out.println("1:" + textFieldCache[1]);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                String acceptChar = "0123456789.";
                int num = 0;//счётчик не разрешённых символов
                for (char c : acceptChar.toCharArray()) {
                    if (e.getKeyChar() == c) {
                        num++;
                    }
                }
                if (num == 0) {
                    e.consume();
                }
            }
        });

    }


    /*Обработчик нажатия выпадающих списков*/
    public void comBoxesController() {
        valuteList[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!valuteText[0].getText().equals("")) {
                    valuteText[0].setText(textFieldCache[0].toString());
                    valuteText[1].setText(textFieldCache[1].toString());
                } else {
                    valuteText[0].setText("");
                    textFieldCache[0] = new StringBuilder("");
                    valuteText[1].setText("1");
                    textFieldCache[1] = new StringBuilder("1");
                }
                    createConverter(valuteList,valuteText);
                    setResults();
            }
        });

        valuteList[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!valuteText[1].getText().equals("")) {
                    valuteText[0].setText(textFieldCache[0].toString());
                    valuteText[1].setText(textFieldCache[1].toString());
                } else {
                    valuteText[0].setText("1");
                    textFieldCache[0] = new StringBuilder("1");
                    valuteText[1].setText("");
                    textFieldCache[1] = new StringBuilder("");
                }

                createConverter(valuteList,valuteText);
                setResults();
            }
        });
    }

    /*Обработчик ввода в поле даты*/
    public void dataFieldController() {
        dateField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if ((textFieldCache[0] != null)
                    || (textFieldCache[0] != null))  {
                    String  dateFromTextField = dateField.getText();
                    if (dateFromTextField != XMLInitializer.CURRENT_DATE) {
                        try {
                            setConverterObjects(dateFromTextField);
                        } catch (ParserConfigurationException e1) {
                            e1.printStackTrace();
                        } catch (SAXException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        /*Обновляем валютный список в случае если валюты прошлых годов имеют другие названия*/
                        if (!defaultMoneyMap.keySet().equals(moneyMap.keySet())) {
                            //valuteListUpdate();
                        }

                    }
                    createConverter(valuteList,valuteText);
                    setResults();
                }


            }
        });
    }


    /*Инициализирует данные о валютах (без учёта даты)*/
    //private  void setConverterObjects() throws ParserConfigurationException,
    //    SAXException, IOException {
    //    //uri = "https://www.cbr-xml-daily.ru/daily_utf8.xml";
    //    uri = "http://www.cbr.ru/scripts/XML_daily_eng.asp";
    //    //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //    //System.out.println("?date_req=" + dateFormat.format(new Date()));
    //    xmlParser = new XMLParser(uri);
    //    moneyMap = xmlParser.getMoneyMap();
    //    defaultMoneyMap = moneyMap;
    //    valutes = moneyMap.keySet().toArray();
    //    //System.out.println(XMLInitializer.CURRENT_DATE);
    //}

    /*Инициализирует данные о валютах по дате*/
    private void setConverterObjects(String date) throws ParserConfigurationException,
        SAXException, IOException {
        //uri = "https://www.cbr-xml-daily.ru/daily_utf8.xml";
        uri = "http://www.cbr.ru/scripts/XML_daily_eng.asp";
        xmlParser = new XMLParser(uri,date.replace('.','/'));
        moneyMap = xmlParser.getMoneyMap();
        defaultMoneyMap = moneyMap;
        valutes = moneyMap.keySet().toArray();

    }

    /*Конвретирует введённые значения для валют*/
    private void createConverter(JComboBox[] valList, JFormattedTextField[] valText) {

        Object valute[][] = new Object[2][2]; //Массив [валюта1] - [значение1]
                                              //       [валюта2] - [значение2]

        for (int i = 0; i <= 1; i++) {
            int selecIndex = valList[i].getSelectedIndex();
            valute[i][0] = valList[i].getItemAt(selecIndex).toString();
            //if (valText[i].getText().equals("") ) {
            if (textFieldCache[i].toString().equals("") ) {
                valute[i][1] = null;
            } else {
                //valute[i][1] = Float.valueOf(valText[i].getText());
                valute[i][1] = Float.valueOf(textFieldCache[i].toString());
            }
        }

        String valute1 =  (String)valute[0][0];
        Float val1 = (Float)valute[0][1];
        String valute2 = (String)valute[1][0];
        Float val2 = (Float)valute[1][1];

        converter = new Converter(valute1,valute2,val1,val2,
                                  moneyMap);
        converter.convert();

    }



    /*Вывод рузультатов*/
    private void setResults() {

        for (int i = 0; i <= 1; i++) {
            if (textFieldCache[i].toString().equals(""))
                valuteText[i].setText(converter.getConvertedMoney()
                                               .get(i)
                                               .toString());
        }
        //System.out.println("Field1: " + textFieldCache[0]);
        //System.out.println("Field2: " + textFieldCache[1]);
        //System.out.println();

    }


    ////public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
    //    createGUI();
    //}


    public static void createGUI() throws IOException, SAXException, ParserConfigurationException {

       JFrame mainFrame = new JFrame("MyConverter");
       mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       JComponent newContent = new ConverterGUI();
       mainFrame.setContentPane(newContent);
       mainFrame.pack();
       mainFrame.setResizable(false);
       mainFrame.setVisible(true);

       ////ConverterGUI converterGUI = new ConverterGUI();
       //converterGUI.setConverterObjects();

       //Converter converter = new Converter("USD","EUR",null,1f,moneyMap);
       //converter.convert();

       //System.out.println(converter.getConvertedMoney().get(0));
       //System.out.println(converter.getConvertedMoney().get(1));


    }

}
