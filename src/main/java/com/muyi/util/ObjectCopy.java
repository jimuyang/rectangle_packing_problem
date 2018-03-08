package com.muyi.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Author: muyi-corp
 * @Date: Created in 23:14 2018/1/14
 * @Description: 利用序列化进行对象的深度copy
 */
public class ObjectCopy {

    public static Object deepCopy(Object object) throws Exception {
        // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，
        // 而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(object);

        // 将流序列化成对象
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }
}
