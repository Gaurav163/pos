package com.increff.pos.util;

import com.increff.pos.model.ApiException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FileUploadUtil {

    public static <T> List<T> convert(MultipartFile file, Class<T> clazz) throws ApiException {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            Field[] fields = clazz.getDeclaredFields();
            List<T> forms = new ArrayList<>();
            System.out.println(reader.readLine());
            while (reader.ready()) {
                String[] values = reader.readLine().split("\t");
                T form = clazz.newInstance();
                int i = 0;
                for (Field field : fields) {
                    String name = field.getName();
                    String setter = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    Method set = clazz.getMethod(setter, field.getType());
                    set.invoke(form, getValue(values[i], field.getType()));
                    i++;
                }
                forms.add(form);
            }
            return forms;
        } catch (Exception e) {
            throw new ApiException("Something wrong with file, Error :");
        }
    }

    public static <T> T getValue(String input, Class<T> clazz) {
        // BEHOLD, MAGIC!

        if (clazz.isAssignableFrom(String.class)) {
            return (T) input;
        } else if (clazz.isAssignableFrom(Long.class)) {
            return (T) Long.valueOf(input);
        } else if (clazz.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(input);
        } else if (clazz.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(input);
        } else if (clazz.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(input);
        } else {
            return null;
        }

    }
}
