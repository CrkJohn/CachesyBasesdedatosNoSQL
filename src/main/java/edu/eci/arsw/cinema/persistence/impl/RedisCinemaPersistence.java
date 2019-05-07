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
		// load stub data
		// Cinema 1
		List<CinemaFunction> functionsX = new ArrayList<>();
		List<CinemaFunction> functionsY = new ArrayList<>();

		CinemaFunction funct1 = new CinemaFunction(new Movie("The Enigma", "Bibliography"), "2018-12-18 15:30");
		CinemaFunction funct2 = new CinemaFunction(new Movie("The Night", "Horror"), "2018-12-18 15:30");
		CinemaFunction funct3 = new CinemaFunction(new Movie("SuperHeroes Movie", "Action"), "2018-12-18 15:30");
		CinemaFunction funct4 = new CinemaFunction(new Movie("SuperHeroes Movie", "Action"), "2018-12-18 17:00");
		
		try {
			//LOAD DATA FROM REDIS
			funct1.setSeats(RedisMethods.getSeatsRedis("cinemaY",funct1));
			funct2.setSeats(RedisMethods.getSeatsRedis("cinemaX",funct2));
			funct3.setSeats(RedisMethods.getSeatsRedis("cinemaX",funct3));
			funct4.setSeats(RedisMethods.getSeatsRedis("cinemaY",funct4));
		} catch (CinemaException ex) {
			Logger.getLogger(RedisCinemaPersistence.class.getName()).log(Level.SEVERE, null, ex);
			//System.err.println("PAILAS");
		}
		functionsX.add(funct1);
		functionsX.add(funct2);
		functionsY.add(funct3);
		functionsY.add(funct4);
		//System.err.println("PERFECTO");
		
		Cinema cinemaX = new Cinema("cinemaX", functionsX);
		cinemas.put("cinemaX", cinemaX);
		Cinema cinemaY = new Cinema("cinemaY", functionsY);
		cinemas.put("cinemaY", cinemaY);
  }

	@Override
	public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException {
		String key = cinema+date+movieName;
		if (!redisMethods.getFromREDIS(key).equals("")) {
			redisMethods.buyTicketRedis(key,row,col);
		}
	}

	@Override
	public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) {
		List<CinemaFunction> functions = new ArrayList<CinemaFunction>();
		if (cinemas.containsKey(cinema)) {
			Cinema cine = cinemas.get(cinema);
			for (CinemaFunction cf : cine.getFunctions()) {
				if (cf.getDate().equals(date)) {
					functions.add(cf);
					// System.out.println(date);
				}
			}
		}
		return functions;
	}

	@Override
	public void saveCinema(Cinema c) throws CinemaPersistenceException {
		if (cinemas.containsKey(c.getName())) {
			throw new CinemaPersistenceException("The given cinema already exists: " + c.getName());
		} else {
			cinemas.put(c.getName(), c);
		}
	}

	@Override
	public Cinema getCinema(String name) throws CinemaPersistenceException {
		if (!cinemas.containsKey(name)) {
			throw new CinemaPersistenceException("The cinema doesn't exists :" + name);
		}
		return cinemas.get(name);
	}

	@Override
	public Set<Cinema> getAllCinemas() {
		Set<Cinema> cinemasAll = new HashSet<Cinema>();
		for (Map.Entry<String, Cinema> e : cinemas.entrySet()) {
			cinemasAll.add(e.getValue());
		}
		return cinemasAll;
	}

	@Override
	public CinemaFunction getCinemaFunctionbyCinemaDateAndMovie(String cinema, String date, String moviename)
			throws CinemaPersistenceException {

		if (cinemas.containsKey(cinema)) {
			Cinema cine = cinemas.get(cinema);
			for (CinemaFunction cf : cine.getFunctions()) {
				if (cf.getDate().equals(date) && cf.getMovie().getName().equals(moviename)) {
					return cf;
				}
			}
		}

		return null;

	}

	@Override
	public void setCinemaFuction(String name, CinemaFunction cinemaFunction) {
		for (Map.Entry<String, Cinema> e : cinemas.entrySet()) {
			if (e.getKey().equals(name)) {
				int ind = -1;
				for (CinemaFunction cinemaFunction1 : e.getValue().getFunctions()) {
					if (cinemaFunction.getMovie().equals(cinemaFunction.getMovie().getName())
							&& cinemaFunction.getDate().equals(cinemaFunction1.getDate())) {
						break;
					}
					ind++;
				}
				if (ind != -1) {
					cinemas.get(name).getFunctions().set(ind, cinemaFunction);
				} else {
					cinemas.get(name).getFunctions().add(cinemaFunction);
				}
			}
		}
	}

}
