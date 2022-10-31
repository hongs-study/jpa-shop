package com.example.jpashop;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CsvTest {
    private String filename = "/Users/aisha/Documents/testtest.txt";

    @Test
    void test() {

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(filename));
            String[] entries = "EW#City#State".split("#");  // 1
            writer.writeNext(entries);  // 2

            String[] entries1 = {"W", "Youngstown", "OH"};  // 3
            writer.writeNext(entries1);

            String[] entries2 = {"W", "Williamson", "WV"};
            writer.writeNext(entries2);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void test2() {
        List<String> objects = new ArrayList<>();
        objects.add("1");
        objects.add("2");
        String[] array = objects.toArray(new String[0]);
        Arrays.stream(array).forEach(System.out::println);
    }
}
