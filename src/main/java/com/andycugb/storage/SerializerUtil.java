package com.andycugb.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author andycugb
 *
 */
public final class SerializerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializerUtil.class);


    /**
     * 序列化
     * 
     * @param obj
     * @return
     * @throws java.io.IOException
     */
    public static byte[] write(Object obj) throws IOException {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }
    }

    /**
     * 反序列化
     * 
     * @param bytes
     * @return
     * @throws java.io.IOException
     */
    public static Object read(byte[] bytes) throws IOException {
        if (bytes == null) {
            return null;
        }
        ObjectInputStream oit = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object object = null;
        try {
            object = oit.readObject();
        } catch (ClassNotFoundException e) {
            LOGGER.error("Failed to deserialize byte array to object. ");
            return null;
        } finally {
            if (oit != null) {
                oit.close();
            }
        }
        return object;
    }

    /**
     * 序列化
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {

        if (null == object) {
            return null;
        }

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {

            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 反序列化
     * 
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {

        if (null == bytes || bytes.length == 0) {
            return null;
        }

        ByteArrayInputStream bais = null;
        try {

            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
