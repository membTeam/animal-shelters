package ru.animals.utils;

import ru.animals.utilsDEVL.ValueFromMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DistributeBackQuery {

    public static ValueFromMethod distributeBackQuery(String backQuery) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var index = backQuery.indexOf(".");
        var strClass = backQuery.substring(0, index);

        Class<?> clazz = Class.forName(strClass);
        Method method = clazz.getMethod(backQuery.substring(++index), null);

        var result = method.invoke(null, null);

        return new ValueFromMethod(result);
    }

}
