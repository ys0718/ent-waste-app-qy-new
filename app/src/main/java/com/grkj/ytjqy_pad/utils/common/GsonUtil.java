package com.grkj.ytjqy_pad.utils.common;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenerte on 2017/8/2.
 */

public class GsonUtil {

    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    /**
     * 将object对象转成json字符串
     *
     * @param object
     * @return
     */
    public static String GsonToString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 将gsonString转成泛型bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }


    /**
     * 将gsonString转成泛型bean
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(Object gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(GsonUtil.GsonToString(gsonString), cls);
        }
        return t;
    }

    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> JsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> JsonToList(Object json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(GsonToString(json) ).getAsJsonArray();
        for (JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }



    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }


    public static <T> T objectToBean(Object ob, Class<T> cls) {
        T t = null;
        if (ob != null) {
            t = GsonToBean(GsonToString(ob), cls);
        }
        return t;
    }


    public static List<String> getListByKey(Object json, String key){
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonEle = jsonParser.parse(GsonToString(json));
        JsonObject jsonObject = jsonEle.getAsJsonObject();
        JsonElement jsonElementByKey = jsonObject.get(key);
        List<String> resultList = new ArrayList<>();
        if (jsonElementByKey!=null){
            JsonArray jsonArr = jsonElementByKey.getAsJsonArray();
            for (int i = 0; i < jsonArr.size(); i++) {
                String asString = jsonArr.get(i).getAsString();
                if (!TextUtils.isEmpty(asString)){
                    resultList.add(asString);
                }

            }
        }
        return resultList;
    }


    /**
     * 通过反射将childBean实体中相同的成员变量赋值到parentBean
     */
    public static Object fillSameFieldNameValue(Object childBean, Object parentBean) {
        Class<?> childBeanClass = childBean.getClass();
        Class<?> parentBeanClass = parentBean.getClass();
        Field[] declaredFields_child = childBeanClass.getDeclaredFields();
        Field[] declaredFields_parent = parentBeanClass.getDeclaredFields();

        //循环遍历两个实体的全部成员变量，并比较变量名是否相同
        for (int i = 0; i < declaredFields_child.length; i++) {
            for (int j = 0; j < declaredFields_parent.length; j++) {
                String childFieldName = declaredFields_child[i].getName();
                String parentFieldName = declaredFields_parent[j].getName();
                if (childFieldName.equals(parentFieldName)) {
                    try {
                        Object fieldValue = getFieldValue(childFieldName, childBean);
                        // 设置访问权限为 true，否则会抛出 IllegalAccessException 异常
                        declaredFields_parent[j].setAccessible(true);
                        declaredFields_parent[j].set(parentBean, fieldValue);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return parentBean;
    }

    /**
     * 通过反射将childBean实体中相同的成员变量赋值到parentBean
     */
    public static Object fillSameFieldNameNotEmpty(Object childBean, Object parentBean) {
        Class<?> childBeanClass = childBean.getClass();
        Class<?> parentBeanClass = parentBean.getClass();
        Field[] declaredFields_child = childBeanClass.getDeclaredFields();
        Field[] declaredFields_parent = parentBeanClass.getDeclaredFields();

        //循环遍历两个实体的全部成员变量，并比较变量名是否相同
        for (int i = 0; i < declaredFields_child.length; i++) {
            for (int j = 0; j < declaredFields_parent.length; j++) {
                String childFieldName = declaredFields_child[i].getName();
                String parentFieldName = declaredFields_parent[j].getName();
                if (childFieldName.equals(parentFieldName)) {
                    try {
                        Object fieldValue = getFieldValue(childFieldName, childBean);
                        // 设置访问权限为 true，否则会抛出 IllegalAccessException 异常

                        if (fieldValue!=null){
                            declaredFields_parent[j].setAccessible(true);
                            declaredFields_parent[j].set(parentBean, fieldValue);
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return parentBean;
    }

    public static String getKeyValue(Object targetObj, String keyName){
        Class<?> parentBeanClass = targetObj.getClass();
        Field[] declaredFields_parent = parentBeanClass.getDeclaredFields();

        for (int i = 0; i < declaredFields_parent.length; i++) {
            String parentFieldName = declaredFields_parent[i].getName();
            if (keyName.equals(parentFieldName)){
                try {
                    Object fieldValue = getFieldValue(parentFieldName, targetObj);
                    if (fieldValue!=null){
                        return fieldValue.toString();
                    }
                    // 设置访问权限为 true，否则会抛出 IllegalAccessException 异常
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    /**
     * 获取变量的值
     */
    private static Object getFieldValue(String fieldName, Object object) throws NoSuchFieldException, IllegalAccessException {
        // 获取字段对应的 Field 对象
        Field field = object.getClass().getDeclaredField(fieldName);

        // 设置访问权限为 true，否则会抛出 IllegalAccessException 异常
        field.setAccessible(true);

        // 返回字段的值
        return field.get(object);
    }


}
