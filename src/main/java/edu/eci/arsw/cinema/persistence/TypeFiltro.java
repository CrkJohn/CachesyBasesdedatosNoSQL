/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import java.util.List;
import java.util.Map;

public interface TypeFiltro{
    
    /**
     *
     * @param cinema
     * @param cinemasName
     * @param Date
     * @param gender
     * @return 
     * @throws FilterException 
     */
    public List<Movie> filteredByGender(CinemaPersitence cinema, String cinemasName , String Date ,String gender) throws FilterException;        

    public List<Movie> filteringByAvailability(CinemaPersitence cinema, String cinemasName, String date, int emptySeats ) throws FilterException;

}
