package com.ltc.redis.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * 2015-09-17
 *
 * @author wanghaipeng
 * Bean复制，支持属性过滤，默认支持List Set复制，可以在copy中自行扩展
 */
public class BeanCopyUtils {

    private static Logger log = LoggerFactory.getLogger(BeanCopyUtils.class);

    private static Set basicClass = new HashSet() {
        {
            add(Integer.class);
            add(Float.class);
            add(Double.class);
            add(Boolean.class);
            add(Short.class);
            add(Byte.class);
            add(Long.class);
            add(Character.class);
            add(int.class);
            add(float.class);
            add(double.class);
            add(boolean.class);
            add(short.class);
            add(byte.class);
            add(long.class);
            add(char.class);
            add(String.class);
        }
    };

    static <E, T> BeanCopier initCopier(Class<E> sourceClass, Class<T> targetClass) {
        return BeanCopier.create(sourceClass, targetClass, false);
    }

    public static <E, T> T copyBean(BeanCopier beanCopier, E e, T t) {
        if (beanCopier != null) {
            beanCopier.copy(e, t, null);
        }
        return t;
    }

    public static <E, T> T copyBean(BeanCopier beanCopier, E e, Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            return copyBean(beanCopier, e, t);
        } catch (Exception ex) {
        }
        return null;
    }

    public static <E, T> List<T> copyBean(BeanCopier beanCopier, List<E> eList, Class<T> clazz) {
        List<T> tList = Lists.newArrayList();
        for (E e : eList) {
            tList.add(copyBean(beanCopier, e, clazz));
        }
        return tList;
    }

    public static <E, T> T copyProperties(E e, T t) {
        BeanUtils.copyProperties(e, t);
        return t;
    }

    public static <E, T> T copyProperties(E e, Class<T> clx) {
        try {
            return copyProperties(e, clx.newInstance());
        } catch (Exception ex) {
            return null;
        }
    }

    public static <E, T> List<T> copyProperties(List<E> eList, Class<T> clx) {
        List<T> tList = Lists.newArrayList();
        for (E e : eList) {
            tList.add(copyProperties(e, clx));
        }
        return tList;
    }

    public static Object getValue(Object source, String fieldName) {
        try {
            Class clazz = source.getClass();
            Method method = clazz.getMethod(convertGetterName(fieldName), null);
            return method.invoke(source, null);
        } catch (Exception e) {
            throw new RuntimeException("not found the field " + fieldName);
        }
    }

    public static String convertGetterName(String field) {
        char firstChar = field.charAt(0);
        return "get" + Character.toTitleCase(firstChar) + field.substring(1);
    }

    /******
     *
     * @author wanghaipeng
     * @date 2015-9-15
     * @param sour
     * @param dest 目标对象的Class对象
     */
    public static <T> T copy(Object sour, Class<T> destClass) {
        return copy(sour, destClass, null, null);
    }

    /******
     *
     * @author wanghaipeng
     * @date 2015-9-15
     * @param sour
     * @param dest 目标对象的Class对象
     * @param needToFilterProperties 复制过程中需要过滤掉的属性名称
     * @param rootProperty 对象视图根结点名称，默认就为空的，根据复制的深度来扩展开
     * @desc 具体类型可以自己扩展
     */
    public static <T> T copy(Object source, Class<T> destClass, Class destcollectionClass) {
        return copy(source, destClass, null, null, destcollectionClass);
    }

    /******
     *
     * @author wanghaipeng
     * @date 2015-9-15
     * @param sour
     * @param dest 目标对象的Class对象
     * @param needToFilterProperties 复制过程中需要过滤掉的属性名称
     */
    public static <T> T copy(Object source, Class<T> destClass, Set<String> needToFilterProperties) {
        return copy(source, destClass, needToFilterProperties, null);
    }

    /******
     *
     * @author wanghaipeng
     * @date 2015-9-15
     * @param sour
     * @param dest 目标对象的Class对象
     * @param needToFilterProperties 复制过程中需要过滤掉的属性名称
     * @param rootProperty 对象视图根结点名称，默认就为空的，根据复制的深度来扩展开
     * @desc 具体类型可以自己扩展
     */
    private static <T> T copy(Object source, Class<T> destClass, Set<String> needToFilterProperties, String rootProperty, Class... destcollectionClass) {
        T dest = null;
        try {
			/*if(source instanceof List){
				List destList=(List) source.getClass().newInstance();
				List sourList=(List) source;
				for(int i=0;i<sourList.size();i++){
					destList.add(clone(sourList.get(i),(destClass==null?sourList.get(i).getClass():destClass),needToFilterProperties,rootProperty));
				}
				dest=destList;
			}else */
            if (source instanceof Collection) {
                Collection destList = new ArrayList();
                ;
                if (destcollectionClass != null && destcollectionClass.length == 1 && destcollectionClass[0].equals(Set.class)) {
                    destList = new HashSet();
                }
                Collection sourList = (Collection) source;
                Iterator ite = sourList.iterator();
                while (ite.hasNext()) {
                    Object obj = ite.next();
                    destList.add(clone(obj, (destClass), needToFilterProperties, rootProperty));
                }
                dest = (T) destList;
            } else {
                dest = clone(source, (destClass), needToFilterProperties, rootProperty);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return dest;
    }

    /**
     * Map转换层Bean，使用泛型免去了类型转换的麻烦。
     *
     * @param <T>
     * @param map
     * @param class1
     * @return
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> class1) {
        T bean = null;
        try {
            bean = class1.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(bean, map);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /******
     *
     * @author wanghaipeng
     * @date 2015-9-15
     * @param sour
     * @param dest 目标对象的Class对象
     * @param needToFilterProperties 复制过程中需要过滤掉的属性名称
     * @param rootProperty 对象视图根结点名称，默认就为空的，根据复制的深度来扩展开
     */
    private static <T> T clone(Object sour, Class<T> destClass, Set<String> needToFilterProperties, String rootPropertys) {
        T dest = null;
        try {
            Class<?> declaringClass = destClass.getDeclaringClass();
            //判断是否是内部类
            if (declaringClass != null) {
                Class[] classes = destClass.getConstructors()[0].getParameterTypes();
                //如果是实例内部类
                if (classes.length > 0) {
                    dest = (T) destClass.getConstructors()[0].newInstance(declaringClass);
                } else {//否则就是静态内部类
                    dest = (T) destClass.getConstructors()[0].newInstance();
                }
            } else {
                try {
                    dest = destClass.newInstance();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Class<?> sourClass = sour.getClass();
        Field[] sourModelFields = BeanCopyUtils.getClassAllFileds(sourClass);
        Field[] destModelFields = BeanCopyUtils.getClassAllFileds(destClass);
        for (Field sourModelField : sourModelFields) {
            try {
                String fieldName = sourModelField.getName();
                if (fieldName.equals("payProducts")) {
                    new Date();
                }
                String rootProperty = "";
                if (StringUtils.isBlank(rootPropertys)) {
                    rootProperty = fieldName;
                } else {
                    rootProperty = rootPropertys + "." + fieldName;
                }
                //如果复制的属性恰好是需要去过滤的就直接返回
                if ((needToFilterProperties != null && needToFilterProperties.contains(rootProperty)) || fieldName.equals("serialVersionUID") || fieldName.startsWith("this$")) {
                    continue;
                }
                PropertyDescriptor pdModel = null;
                try {
                    pdModel = new PropertyDescriptor(fieldName, sourClass);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    continue;
                }
                Object sourceValue = null;
                try {
                    sourceValue = pdModel.getReadMethod().invoke(sour);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                //如果要将要复制的属性值为null就跳过复制
                if (sourceValue == null) {
                    continue;
                }
                boolean existsField = false;
                for (Field destModelField : destModelFields) {
                    if (destModelField.getName().equals(fieldName)) {
                        existsField = true;
                        break;
                    }
                }
                //如果将要复制的属性在目标对象上没有对象的属性名称就直接滤过
                if (!existsField) {
                    continue;
                }
                boolean isDeepClone = false;
                if (needToFilterProperties != null && needToFilterProperties.size() > 0) {
                    Iterator<String> ite = needToFilterProperties.iterator();
                    while (ite.hasNext()) {
                        String needToFilterProperty = ite.next();
                        //如果要过滤的属性比当前属性更深层次的话就必须要深度克隆
                        if (needToFilterProperty.startsWith(rootProperty)) {
                            isDeepClone = true;
                            break;
                        }
                    }
                }
                PropertyDescriptor pdDto = new PropertyDescriptor(fieldName, destClass);
                if (sourceValue != null && sourceValue.getClass() != pdDto.getReadMethod().getReturnType() && !basicClass.contains(sourceValue.getClass())) {
                    if (sourceValue instanceof Collection) {
                        ParameterizedType parameterizedType = (ParameterizedType) pdDto.getReadMethod().getGenericReturnType();
                        if (parameterizedType != null) {
                            Class returnClass = (Class) parameterizedType.getActualTypeArguments()[0];
                            Object fieldValue = copy(sourceValue, returnClass, needToFilterProperties, rootProperty, pdDto.getReadMethod().getReturnType());
                            pdDto.getWriteMethod().invoke(dest, fieldValue);
                        }
                    } else {
                        Object fieldValue = copy(sourceValue, pdDto.getReadMethod().getReturnType(), needToFilterProperties, rootProperty);
                        pdDto.getWriteMethod().invoke(dest, fieldValue);
                    }
                } else if (!isDeepClone) {//如果不要深度克隆的话就直接复制就行了
                    try {
                        pdDto.getWriteMethod().invoke(dest, sourceValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Object fieldValue = copy(sourceValue, null, needToFilterProperties, rootProperty);
                    pdDto.getWriteMethod().invoke(dest, fieldValue);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return dest;
    }

    /**
     * 2015-09-19
     * wanghaipeng
     *
     * @param cl
     * @return 获得类及所有父类的属性对象
     */
    private static Field[] getClassAllFileds(Class cl) {
        List<Class> list = new ArrayList<Class>();
        getClassAllClass(cl, list);
        list.add(cl);
        Class[] ct = new Class[list.size()];
        for (int n = 0; n < list.size(); n++) {
            ct[n] = list.get(n);
        }
        int count[] = new int[ct.length];
        int num = 0;
        for (int i = 0; i < ct.length; i++) {
            Class te = ct[i];
            if (te == null) {
                continue;
            }
            count[i] = te.getDeclaredFields().length;
            num += count[i];
        }
        Field modelFields[] = new Field[num];
        int k = 0;
        for (int j = 0; j < ct.length; j++) {
            Class te = ct[j];
            if (j != 0) {
                k = k + count[j - 1];
            }
            System.arraycopy(te.getDeclaredFields(), 0, modelFields, k, count[j]);
        }
        return modelFields;
    }

    /**
     * 获得所有的超类集合
     *
     * @param ct
     * @param list
     */
    private static void getClassAllClass(Class ct, List<Class> list) {
        Class cl = ct.getSuperclass();
        if (cl != null) {
            list.add(cl);
            getClassAllClass(cl, list);
        }
    }

    public static void main(String[] args) {
        BeanCopyUtils.User user1 = new BeanCopyUtils.User();
        BeanCopyUtils.User user2 = new BeanCopyUtils.User();
        BeanCopyUtils.Score score1 = new BeanCopyUtils.Score();
        score1.setName("语文");
        score1.setScore(90);
        BeanCopyUtils.Score score2 = new BeanCopyUtils.Score();
        score2.setName("数学");
        score2.setScore(100);
        List<Score> scores = new ArrayList<Score>();
        scores.add(score1);
        scores.add(score2);
        user1.setUsername("rose");
        user1.setPassword("rose");
        user1.setScores(scores);
        user2 = (User) copy(user1, User.class, new HashSet<String>() {
            {
                add("scores.name");
            }
        }, null);
    }


    public static class Score {
        private String name;
        private float score;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "Score [name=" + name + ", score=" + score + "]";
        }
    }

    public static class User {
        private String username;

        private String password;

        private List<Score> scores;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<Score> getScores() {
            return scores;
        }

        public void setScores(List<Score> scores) {
            this.scores = scores;
        }

        @Override
        public String toString() {
            return "User [username=" + username + ", password=" + password
                    + ", scores=" + scores + "]";
        }


    }

}
