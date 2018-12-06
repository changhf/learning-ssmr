package com.smmr;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author <a href="mailto:changhuafeng@zafh.com.cn">常华锋</a>
 * @version V1.0.0
 * @since 2018/12/5
 */
public class LuaDemo {

    public static void testLuaFile() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
        File file = new File("D:\\test.lua");
        byte[] bytes = getFileToByte(file);
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        byte[] sha1 = jedis.scriptLoad(bytes);
        Object obj = jedis.evalsha(sha1, 2, "key1".getBytes(), "key2".getBytes(), "2".getBytes(), "4".getBytes());
        System.out.println(obj);
    }

    public static byte[] getFileToByte(File file) {
        byte[] by = new byte[(int) file.length()];
        try {
            FileInputStream is = new FileInputStream(file);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] bb = new byte[2048];
            int ch;
            ch = is.read(bb);
            while (ch != -1) {
                byteStream.write(bb, 0, ch);
                ch = is.read(bb);
            }
            by = byteStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return by;
    }

    public static void main(String[] args) {
        testLuaFile();
    }

}
