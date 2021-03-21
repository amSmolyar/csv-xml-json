package ru.netology;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Csv {
    public static void main(String[] args) {
        String fileName = "data.csv";

        List<String[]> employeeList = new ArrayList<>();
        employeeList.add("1,John,Smith,USA,25".split(","));
        employeeList.add("2,Ivan,Petrov,RU,23".split(","));

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        // создаем csv файл:
        makeCSV(employeeList, fileName);
        // парсинг csv файла:
        List<Employee> list = parseCSV(columnMapping, fileName);
        // формирование строчки в формате json:
        String json = listToJson(list);
        // запись в json файл:
        writeString(json,"data.json");
    }

    public static void makeCSV(List<String[]> list, String fileName) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeAll(list);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> listEmployee = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToParse = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            listEmployee = csvToParse.parse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return listEmployee;
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
}

