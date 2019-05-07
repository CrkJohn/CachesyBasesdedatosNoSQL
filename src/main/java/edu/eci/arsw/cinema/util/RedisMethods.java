package edu.eci.arsw.cinema.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import edu.eci.arsw.cinema.model.*;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.impl.RedisCinemaPersistence;

@Component
public class RedisMethods {

    public static void saveToREDIS(String key, String data) {
        Jedis jedis = JedisUtil.getPool().getResource();
        jedis.watch(key);
        Transaction t1 = jedis.multi();
        t1.set(key, data);
        t1.exec();
        jedis.close();
    }


    //Method persistence
    public static List<List<AtomicBoolean>> buyTicketRedis(String key, int row, int col) {
        String value = getFromREDIS(key);
        ObjectMapper mapper = new ObjectMapper();
        if (!value.equals("")) {
            try {
                // cinemaY2018-12-18 15:30The Enigma"
                int startIndex = -1, endIndex = 0;
                for (int i = 0; i < key.length(); ++i) {
                    if (Character.isDigit(key.charAt(i))) {
                        if (startIndex == -1) {
                            startIndex = i;
                        } else {
                            endIndex = i;
                        }
                    }
                }
                String functionDate = key.substring(startIndex, endIndex + 1);
                String json = "{\"date\": \"" + functionDate + "\", \"seats\":\"" + value + "\"}";
                CinemaFunction cinemaFunction = mapper.readValue(json, CinemaFunction.class);
                try {
                    cinemaFunction.buyTicket(row, col);
                } catch (CinemaException e) {
                    e.printStackTrace();
                }
                String  seats = cinemaFunction.getSeats().toString();
                saveToREDIS(key, seats);
                return cinemaFunction.getSeats();
            }catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
         
        }
        return null;    
    }

    public static List<List<AtomicBoolean>> buyTicketRedis(String key) throws CinemaException {
        String value = getFromREDIS(key);
        ObjectMapper mapper = new ObjectMapper();
        if(!value.equals("")){
            try{
                // cinemaY2018-12-18 15:30The Enigma"
                int startIndex = -1 ,  endIndex = 0;
                for(int i  = 0 ; i < key.length() ; ++i){
                    if(Character.isDigit(key.charAt(i))){
                        if(startIndex == -1 ) {
                            startIndex = i;
                        }else{
                            endIndex = i;
                        }
                    }
                }
                String nameCinema  = key.substring(0,startIndex), functionDate = key.substring(startIndex,endIndex+1);
                String functionMovieName =  key.substring(endIndex, key.length());  
                String json = "{\"date\": \""+functionDate+"\", \"seats\":\""+value+"\"}";
                CinemaFunction cinemaFunction = mapper.readValue(json, CinemaFunction.class);
                return cinemaFunction.getSeats();
            }catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
         
        }else{
            throw new CinemaException("The cinema does not exist in the database");
        }
		return null;
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
            if (result.size() > 0) {
                intentar = false;
                content = data.get();
                jedis.close();// Cerrar recurso jedis
            }
        }
        return content;
    }

    public static List<List<AtomicBoolean>> getSeatsRedis(String nameCinema , CinemaFunction cinemaFunction) throws CinemaException{
        String key = nameCinema+cinemaFunction.getDate()+cinemaFunction.getMovie().getName();
        return buyTicketRedis(key);
    }

    public static void main(String[] args) throws CinemaException {
        //saveToREDIS("this is test","this is values of the test");    
    	//System.out.println(buyTicketRedis("cinemaY2018-12-18 15:30The Enigma"));
    	/*CinemaFunction cinemaFunction = new CinemaFunction(new Movie("SuperHeroes Movie", "Action"), "2018-12-18 17:00");
    	List<List<AtomicBoolean>> seats = getSeatsRedis("cinemaY", cinemaFunction);
    	//List<List<AtomicBoolean>> seats = buyTicketRedis("cinemaX2018-12-18 15:30SuperHeroes Movie");
        for(int i=0; i<seats.size(); i++){
            for(int j=0; j<seats.get(i).size(); j++){
                System.out.print(" "+seats.get(i).get(j));
            }
            System.out.println("");
        }*/
    	RedisCinemaPersistence rediscp = new RedisCinemaPersistence();
    }
}