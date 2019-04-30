package edu.eci.arsw.cinema.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import java.util.List;

public class RedisMethods {

    public static void saveToREDIS(String key , String data){
        Jedis jedis = JedisUtil.getPool().getResource();
        jedis.watch(key);
        Transaction t1 = jedis.multi();
        t1.set(key, data);
        t1.exec();                
        jedis.close();
    }

    public static String getFromREDIS(String key){
        boolean intentar = true;
        String content = "";
        while (intentar) {
            Jedis jedis = JedisUtil.getPool().getResource(); // Inicializar jedis y obtener recursos
            jedis.watch(key);      // Hacer watch de la llave
            Transaction t = jedis.multi();// Crear la transacción t
            Response<String> data = t.get(key);
            List<Object> result = t.exec();
            System.out.println(result.toString());
            if (result.size() > 0) {
                intentar = false;
                content = data.get();
                jedis.close();// Cerrar recurso jedis
            }
        }
        return content;
    }
    public static void main(String[] args) {
        //saveToREDIS("this is test","this is values of the test");     
        System.out.println(getFromREDIS("this is test")); 
        System.out.println("aca");  
    }

}
