package com.google.demo.Service;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class KeywordList {
    private ArrayList<Keyword> kl;

    public KeywordList(String filter, String searchKeyword) {
        kl = new ArrayList<>();
        kl.add(new Keyword(searchKeyword, 1.2));
        add();
        add(filter);
    }

    // 讀取 initial.txt
    public void add() {
        try {
            ClassPathResource resource = new ClassPathResource("txt/initial.txt");
            InputStream inputStream = resource.getInputStream();
            Scanner scanner = new Scanner(inputStream);

            while (scanner.hasNextLine()) {
                String name = scanner.next();
                double weight = scanner.nextDouble();
                kl.add(new Keyword(name, weight));
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error reading initial file: " + e.getMessage());
        }
    }

    // 根據 filter 讀取對應的 TXT 檔案
    public void add(String filter) {
        try {
            if (filter != null && !filter.isEmpty()) {
                ClassPathResource resource = new ClassPathResource("txt/" + filter + ".txt");
                InputStream inputStream = resource.getInputStream();
                Scanner scanner = new Scanner(inputStream);

                while (scanner.hasNextLine()) {
                    String name = scanner.next();
                    double weight = scanner.nextDouble();
                    kl.add(new Keyword(name, weight));
                }
                scanner.close();
            }
        } catch (Exception e) {
            System.out.println("Error reading filter file: " + e.getMessage());
        }
    }

    public ArrayList<Keyword> getList() {
        return kl;
    }
}
