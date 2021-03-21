package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Xml {
    public static void main(String[] args) {
        String fileName = "data.xml";

        List<String[]> employeeList = new ArrayList<>();
        employeeList.add("1,John,Smith,USA,25".split(","));
        employeeList.add("2,Ivan,Petrov,RU,23".split(","));

        // создание файла xml:
        xmlCreateExample(employeeList, fileName);
        // парсинг xml файла:
        List<Employee> readList = parseXML(fileName);
        // формирование строчки в формате json:
        String json = listToJson(readList);
        // запись в json файл:
        writeString(json,"data2.json");
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employeeList = new ArrayList<>();
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        String[] tagValue = new String[columnMapping.length];

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));

            // корневой узел:
            Node root = document.getDocumentElement();
            // список дочерних узлов:
            NodeList nodeList = document.getElementsByTagName("employee");

            for (int ii = 0; ii < nodeList.getLength(); ii++) {
                Node innerNode = nodeList.item(ii);
                if (Node.ELEMENT_NODE == innerNode.getNodeType()) {
                    if (innerNode.getNodeName().equals("employee")) {
                        Element element = (Element) innerNode;

                        for (int mapCnt = 0; mapCnt < columnMapping.length; mapCnt++) {
                            String tagName = columnMapping[mapCnt];
                            tagValue[mapCnt] = element.getElementsByTagName(tagName).item(0).getTextContent();
                        }

                        try {
                            int id = Integer.parseInt(tagValue[0]);
                            String firstName = tagValue[1];
                            String lastName = tagValue[2];
                            String country = tagValue[3];
                            int age = Integer.parseInt(tagValue[4]);
                            employeeList.add(new Employee(id,firstName,lastName,country,age));
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employeeList;
    }

    public static <T> String listToJson(List<T> list) {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<T>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String data, String fileName) {
        File newFile = new File(fileName);
        try {
            if (newFile.createNewFile()) {
                System.out.println("Создан файл " + fileName);
                try (FileWriter writer = new FileWriter(fileName, false)) {
                    writer.write(data);
                    writer.append('\n');
                    writer.flush();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void xmlCreateExample(List<String[]> employeeList, String fileName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element staff = document.createElement("staff");
            document.appendChild(staff);

            for (String[] employeeItem : employeeList) {
                Element employee = document.createElement("employee");
                staff.appendChild(employee);
                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(employeeItem[0]));
                employee.appendChild(id);
                Element firstMame = document.createElement("firstName");
                firstMame.appendChild(document.createTextNode(employeeItem[1]));
                employee.appendChild(firstMame);
                Element lastName = document.createElement("lastName");
                lastName.appendChild(document.createTextNode(employeeItem[2]));
                employee.appendChild(lastName);
                Element country = document.createElement("country");
                country.appendChild(document.createTextNode(employeeItem[3]));
                employee.appendChild(country);
                Element age = document.createElement("age");
                age.appendChild(document.createTextNode(employeeItem[4]));
                employee.appendChild(age);
                System.out.println("add employee");
            }

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileName));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            try {
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.transform(domSource, streamResult);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        }
    }
}

