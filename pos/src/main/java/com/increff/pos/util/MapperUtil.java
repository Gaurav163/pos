package com.increff.pos.util;

import com.increff.pos.model.ApiException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapperUtil {
    private MapperUtil() {
    }

    public static <T, S> T mapper(S ob, Class<T> clazz) throws ApiException {
        try {
            if (ob == null) return null;
            List<Field> oldFields = getFields(ob.getClass());
            List<Field> newFields = getFields(clazz);
            T newObj = clazz.newInstance();
            for (Field from : oldFields) {
                for (Field to : newFields) {
                    if (to.getName().equals(from.getName()) && from.getType().getName().equals(to.getType().getName())) {
                        String getter = "get" + from.getName().substring(0, 1).toUpperCase() + from.getName().substring(1);
                        String setter = "set" + to.getName().substring(0, 1).toUpperCase() + to.getName().substring(1);
                        Method get = ob.getClass().getMethod(getter);
                        Method set = clazz.getMethod(setter, to.getType());
                        set.invoke(newObj, get.invoke(ob));
                    }
                }
            }
            return newObj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(e.getMessage());
        }
    }

    private static <T> List<Field> getFields(Class<T> clazz) {
        Class current = clazz;
        List<Field> fields = new ArrayList<>();
        while (current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    public static <T, S> List<T> mapper(List<S> list, Class<T> clazz) throws ApiException {
        List<T> newList = new ArrayList<>();
        for (S obj : list) {
            newList.add(mapper(obj, clazz));
        }
        return newList;
    }
}
