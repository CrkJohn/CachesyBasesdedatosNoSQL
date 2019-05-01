
package edu.eci.arsw.cinema.deserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;


//https://www.baeldung.com/jackson-deserialization
public class CinemaFunctionDeserializer extends StdDeserializer<CinemaFunction> {

    public CinemaFunctionDeserializer() {
        this(null);
    }

    public CinemaFunctionDeserializer(Class<?> vc) {
        super(vc);
    }

    
    @Override
    public CinemaFunction deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Movie movie = null;
        String seats = node.get("seats").asText();
        seats = (String) seats.subSequence(0,seats.length()-1); // Delete first [ and end ]
        String date = node.get("date").asText();
        CinemaFunction cinemaFunctionMapper = new CinemaFunction(movie, date);
        return cinemaFunctionMapper;
    }
}