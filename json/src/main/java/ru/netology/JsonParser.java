package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public static void main(String[] args) {
        String fileName = "new_data.json";
        String jsonString = readString(fileName);
        List<Employee> employeeList = jsonToList(jsonString);
        writeList(employeeList);
    }

    public static String readString(String fileName) {
        StringBuilder jsonString = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                jsonString.append(strLine);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return jsonString.toString();
    }

    public static List<Employee> jsonToList(String jsonString) {
        List<Employee> employeeList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            for (Object jsonObject : jsonArray) {
                Employee employee = gson.fromJson(String.valueOf(jsonObject), Employee.class);
                employeeList.add(employee);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employeeList;

        //===========================================================
        // Второй вариант (из строки json сразу в массив):
        //===========================================================
        /*
        List<Employee> employeeList = new ArrayList<>();
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Employee[] employee = gson.fromJson(jsonString, Employee[].class);
            for (int ii = 0; ii < employee.length; ii++) {
                employeeList.add(employee[ii]);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employeeList;
        */
        //===========================================================
    }

    public static void writeList(List<Employee> list) {
        for (Employee employee : list) {
            System.out.println(employee.toString());
        }
    }

}
