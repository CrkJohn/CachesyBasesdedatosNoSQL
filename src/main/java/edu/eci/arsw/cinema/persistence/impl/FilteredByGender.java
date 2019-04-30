/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence.impl;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import edu.eci.arsw.cinema.persistence.FilterException;
import edu.eci.arsw.cinema.persistence.TypeFiltro;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;


@Component("filteredByGender")
public class FilteredByGender  implements TypeFiltro{
    
    private  List<Movie> movies;
 

    @Override
    public List<Movie> filteredByGender(CinemaPersitence cinema, String cinemasName, String Date, String gender) {
        movies = new ArrayList<Movie>();
        try {                     
            for(CinemaFunction cinemaFunctions : cinema.getCinema(cinemasName).getFunctions()){
                if(cinemaFunctions.getMovie().getGenre().equals(gender)){
                    movies.add(cinemaFunctions.getMovie());
                }
            }
            
        } catch (CinemaPersistenceException ex) {
            Logger.getLogger(FilteredByGender.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movies;    
    }

    @Override
    public List<Movie> filteringByAvailability(CinemaPersitence cinema, String cinemasName, String date,int emptySeats) throws FilterException {
    	throw new FilterException("filtering by availability is an unsupported"); 
    }
    
}
