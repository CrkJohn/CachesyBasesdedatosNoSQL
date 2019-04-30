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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import org.springframework.stereotype.Component;


@Component("inMemoryCinemaPersistence")
public class InMemoryCinemaPersistence implements CinemaPersitence {

	private final Map<String, Cinema> cinemas = new HashMap<>();

	public InMemoryCinemaPersistence() {
		// load stub data
		// Cinema 1
		String functionDate = "2018-12-18 15:30", functionDate2 = "2018-12-20 11:15";
		List<CinemaFunction> functions = new ArrayList<>();
		CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie", "Action"), functionDate);
		CinemaFunction funct2 = new CinemaFunction(new Movie("The Night", "Horror"), functionDate2);
		functions.add(funct1);
		functions.add(funct2);
		Cinema c = new Cinema("cinemaX", functions);

		// Cinema 2
		String functionDate3 = "2019-03-10 02:45";
		List<CinemaFunction> functions2 = new ArrayList<>();
		CinemaFunction funct2_1 = new CinemaFunction(new Movie("Inception", "Action"), functionDate3);
		CinemaFunction funct2_2 = new CinemaFunction(new Movie("Batman", "Action"), functionDate3);
		CinemaFunction funct2_3 = new CinemaFunction(new Movie("American pie 1", "Comedy"), functionDate3);
		functions2.add(funct2_1);
		functions2.add(funct2_2);
		functions2.add(funct2_3);
		Cinema c2 = new Cinema("cineColombia", functions2);

		cinemas.put("cinemaX", c);
		cinemas.put("cineColombia", c2);

	}

	@Override
	public void buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException {
		if (cinemas.containsKey(cinema)) {
			Cinema cine = cinemas.get(cinema);
			for (CinemaFunction cf : cine.getFunctions()) {
				if (cf.getMovie().getName().equals(movieName) && cf.getDate().equals(date)) {
					cf.buyTicket(row, col);
				}
			}
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
