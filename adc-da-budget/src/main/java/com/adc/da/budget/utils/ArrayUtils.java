package com.adc.da.budget.utils;

import com.adc.da.util.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数组工具类
 *
 * @author qichunxu
 * @date 2019-04-10
 */
public class ArrayUtils {

    /**
     * 比较两个数组，返回不同的元素
     * {4,5} {5,8} -> {8}
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    public static <T> List<T> compare(T[] t1, T[] t2) {
        if (CollectionUtils.isEmpty(t2)){
            return Arrays.asList(t1);
        }
        if (CollectionUtils.isEmpty(t1)){
            return new ArrayList<>();
        }
        List<T> list1 = Arrays.asList(t1);
        List<T> list2 = new ArrayList<T>();
        for (T t : t2) {
            if (!list1.contains(t)) {
                list2.add(t);
            }
        }
        return list2;
    }

    /**
     * 比较两个数组，返回不同的元素
     * {4,5} {5,8} -> {8}
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    public static <T> List<T> compare(T[] t1, List<T>  t2) {
        if (CollectionUtils.isEmpty(t2)){
            return Arrays.asList(t1);
        }
        if (CollectionUtils.isEmpty(t1)){
            return new ArrayList<>();
        }
        List<T> list1 = Arrays.asList(t1);
        List<T> list2 = new ArrayList<T>();
        for (T t : t2) {
            if (!list1.contains(t)) {
                list2.add(t);
            }
        }
        return list2;
    }

    public static <T> List<T> compare(List<T> t1, List<T> t2) {
        if (CollectionUtils.isEmpty(t2)){
            return t1 ;
        }
        if (CollectionUtils.isEmpty(t1)){
            return new ArrayList<>() ;
        }
        List<T> retList = new ArrayList<T>();

        for (T t : t2) {
            if (!t1.contains(t)) {
                retList.add(t);
            }
        }
        return retList;
    }

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        list1.add("4");
        list1.add("5");

        List<String> list2 = new ArrayList<>();
        list2.add("5");
        list2.add("8");

        List<String> resultList = ArrayUtils.compare(list1,list2);
        System.out.println(resultList.toString());

    }



}
