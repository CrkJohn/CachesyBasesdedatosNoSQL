/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence.impl;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import edu.eci.arsw.cinema.util.RedisMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("RedisCinemaPersistence")
public class RedisCinemaPersistence implements CinemaPersitence {

	private final Map<String, Cinema> cinemas = new HashMap<>();

	@Autowired
	RedisMethods  redisMethods;

    public RedisCinemaPersistence(){
        /*CinemaFunction funct4 = new CinemaFunction(superheroes,functionDate2);
        try {
        //LOAD DATA FROM REDIS
            funct1.setSeats(RedisMethods.getSeatsRedis("cinemaX",funct1));
            funct2.setSeats(RedisMethods.getSeatsRedis("cinemaX",funct2));
            funct3.setSeats(RedisMethods.getSeatsRedis("cinemaY",funct3));
            funct4.setSeats(RedisMethods.getSeatsRedis("cinemaY",funct4));
        } catch (CinemaException ex) {
            Logger.getLogger(RedisCinemaPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        functionsX.add(funct1);
        */
    }


	@Override
	public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException {
		String key = cinema+date+movieName;
		if (!redisMethods.getFromREDIS(key).equals("")) {
			redisMethods.buyTicketRedis(key);
		}
	}

	@Override
	public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) {
		return null;
	}

	@Override
	public void saveCinema(Cinema cinema) throws CinemaPersistenceException {
		
	}

	@Override
	public Cinema getCinema(String name) throws CinemaPersistenceException {
		return null;
	}

	@Override
	public Set<Cinema> getAllCinemas() {
		return null;
	}

	@Override
	public CinemaFunction getCinemaFunctionbyCinemaDateAndMovie(String cinema, String date, String movie)
			throws CinemaPersistenceException {
		return null;
	}

	@Override
	public void setCinemaFuction(String name, CinemaFunction cinemaFunction) {
		
	}

}
