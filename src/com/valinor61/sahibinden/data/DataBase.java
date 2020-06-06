package com.valinor61.sahibinden.data;


import com.valinor61.sahibinden.toolkit.Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class DataBase {
    //Tüm değişkenleri ve özelliklerini içeren class yapısı
    private final String type;
    private final String name;
    private final String linkCode;

    public DataBase(String type, String name, String value, String id) {
        this.type = type;
        this.name = name;
        this.linkCode = id + "=" + value;
    }

    public static void generateDatabase(String html, String fileName) {
        try {
            Path path = Paths.get(fileName);
            BufferedWriter writer = Files.newBufferedWriter(path);
            LinkedList<String> list = Tools.findBetweenList(html, ">\n" +
                    "    <dt id=\"", "/dd>\n" +
                    "    </dl>\n" +
                    "</div>\n" +
                    "                        <div ");
            for (String x : list) {
                String type = Tools.findBetween(x, "collapseTitle",
                        "/d", 0);
                type = Tools.findBetween(type, ">", "<", 0);
                LinkedList<String> list2 = Tools.findBetweenList(x, "<div", "</div>");
                for (String y : list2) {
                    String name = Tools.formatText(Tools.findBetween(y, "</i>", "</a>", 0));
                    String value = Tools.findBetween(y, "data-value=\"", "\"", 0);
                    String id = Tools.findBetween(y, "data-id=\"", "\"", 0);
                    if (!value.equals("") && !name.equals("") && !id.equals("")) {
                        writer.write(String.format("%s\t%s\t%s\t%s",
                                type,
                                name,
                                value, id));
                        writer.newLine();
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Can't save database");
        }
    }

    public static LinkedList<DataBase> readDataBase(String fileName) {
        LinkedList<DataBase> dataBases = new LinkedList<>();
        try {
            Path path = Paths.get(fileName);
            BufferedReader reader = Files.newBufferedReader(path);
            String input;
            while ((input = reader.readLine()) != null) {
                String[] itemPieces = input.split("\t");
                String type = itemPieces[0];
                String name = itemPieces[1];
                String value = itemPieces[2];
                String id = itemPieces[3];
                dataBases.add(new DataBase(type, name, value, id));
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Can't read database");
        }
        return dataBases;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getLinkCode() {
        return linkCode;
    }

    @Override
    public String toString() {
        return type + ": " + name + ", " + linkCode;
    }
}
