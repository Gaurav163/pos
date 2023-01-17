package com.increff.pos.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FileUploadUtil {

    public static <T> List<T> convert(MultipartFile file, Class<T> clazz) throws IOException {
        try {
            Field[] fields = clazz.getDeclaredFields();
            int columnCount = fields.length;
            String[] names = new String[columnCount];
            String[] types = new String[columnCount];
            int[] index = new int[columnCount];
            for (int i = 0; i < columnCount; i++) {
                index[i] = -1;
                names[i] = fields[i].getName();
                types[i] = fields[i].getType().getName();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String[] columns = reader.readLine().split("\t");

            for (int j = 0; j < columns.length; j++) {
                String column = columns[j].trim();
                for (int i = 0; i < columnCount; i++) {
                    if (column.equals(names[i])) {
                        if (index[i] != -1) {
                            throw new IllegalStateException("Multiple Column with heading : '" + column + "' exist in file");
                        }
                        index[i] = j;
                        break;
                    }
                }
            }
            int maxIndex = 0;
            for (int i = 0; i < columnCount; i++) {
                maxIndex = Math.max(maxIndex, index[i]);
                if (index[i] == -1) {
                    throw new IllegalStateException("No Column with heading : '" + names[i] + "' exist in file");
                }
            }


            List<T> forms = new ArrayList<>();

            while (reader.ready()) {
                String[] values = reader.readLine().split("\t");
                List<String> obj = new ArrayList<>();
                if (values.length < maxIndex) {
                    forms.add(null);
                    continue;
                }
                // check merge
                T form = clazz.newInstance();

                for (int i = 0; i < columnCount; i++) {
                    String methodName = "set" + names[i].substring(0, 1).toUpperCase() + names[i].substring(1);
                    Method sett = clazz.getMethod(methodName, String.class);
                    sett.invoke(form, values[i]);
                }
                forms.add(form);
            }
            return forms;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Somthing weent wrong with file");
        }
    }


}
