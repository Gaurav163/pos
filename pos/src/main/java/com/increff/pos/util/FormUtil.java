package com.increff.pos.util;

import com.increff.pos.model.ApiException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class FormUtil {

    public static <T> void validateForm(T ob) throws ApiException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<T>> violations = validator.validate(ob);
        if (!violations.isEmpty()) {
            List<String> details = violations.stream().map(e -> e.getPropertyPath().toString())
                    .collect(Collectors.toList());
            throw new ApiException("Invalid input parameters : " + String.join(", ", details));
        }
    }

    public static <T> void normalizeList(List<T> list) {
        for (T ob : list) {
            normalizeForm(ob);
        }
    }

    public static <T> void normalizeForm(T ob) {
        try {
            Field[] fields = ob.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().equals(String.class)) {
                    String getter = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    String setter = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    Method get = ob.getClass().getMethod(getter);
                    Method set = ob.getClass().getMethod(setter, String.class);
                    set.invoke(ob, normalize((String) get.invoke(ob)));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurs while normalizing form");
        }
    }

    public static String normalize(String s) {
        return s == null ? null : s.toLowerCase().trim();
    }

}
