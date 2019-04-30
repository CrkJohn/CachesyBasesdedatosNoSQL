package edu.eci.arsw.cinema.persistence.impl;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import edu.eci.arsw.cinema.persistence.FilterException;
import edu.eci.arsw.cinema.persistence.TypeFiltro;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;


@Component("filteringByAvailability")
public class FilteringByAvailability implements TypeFiltro{

    private  List<Movie> movies;

    @Override
    public List<Movie> filteredByGender(CinemaPersitence cinema, String cinemasName, String Date, String gender) throws FilterException {
    	throw new FilterException("filtering by gender is an unsupported"); 
    }

    @Override
    public List<Movie> filteringByAvailability(CinemaPersitence cinema, String cinemasName,String date, int emptySeats) {
        movies = new ArrayList<Movie>();        
        Set<Cinema> cinemas =  cinema.getAllCinemas();
        try{
            for(CinemaFunction cinemaFunctions : cinema.getCinema(cinemasName).getFunctions()){
            	if(cinemaFunctions.getDate().equals(date) && cinemaFunctions.getEmptySeats() == emptySeats){
                      movies.add(cinemaFunctions.getMovie());
                }
            } 
        }catch(Exception ex){
            
        }
        return movies;
    }

 
    
}
