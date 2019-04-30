/*package edu.eci.arsw.cinema.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import edu.eci.arsw.cinema.persistence.FilterException;

public class App {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        
		CinemaServices services = ac.getBean(CinemaServices.class);
		
        List<CinemaFunction> functions= new ArrayList<>();
        functions.add(new CinemaFunction(new Movie("SuperHeroes Movie 2","Action"),"2018-12-18 15:30"));
        functions.add(new CinemaFunction(new Movie("Captain marvel","Action"),"2012-12-18 02:10"));
        functions.add(new CinemaFunction(new Movie("The Night 2","Horror"),"2018-12-18 15:30"));
        functions.add(new CinemaFunction(new Movie("END GAME","Action"),"2018-12-18 15:30"));
        
        services.addNewCinema(new Cinema("Cinemark", functions));
        
        Set<Cinema> cinemas = services.getAllCinemas();
        System.out.println("CINEMAS FOUND: "+cinemas.size());
        for (Cinema cinema : cinemas) {
			System.out.println(cinema.getName());
		}
        
        try {
			services.getCinemaByName("Cinemark");
		} catch (CinemaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CinemaPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        List<CinemaFunction> cinemaFunctions = services.getFunctionsbyCinemaAndDate("Cinemark", "2018-12-18 15:30");
        System.out.println("CINEMA FUNCTIONS FOUND IN 2018-12-18: "+cinemaFunctions.size());
        for (CinemaFunction cf : cinemaFunctions) {
        	System.out.println(cf.getMovie().getName());
		}

		try {
			List<Movie> movies = services.filteredByGender(services.cps, "Cinemark", "2018-12-18 15:30", "Action");
			System.out.println("# Movies filtered by action gender: "+movies.size());
			for (Movie movie : movies) {
				System.out.println("Name: <"+movie.getName()+">, Gender: <"+movie.getGenre()+">");
			}
		} catch (FilterException e) {
			
		}
        
		try {
			List<Movie> movies = services.filteringByAvailability(services.cps, "Cinemark",  "2018-12-18 15:30", 84);
			System.out.println("# Movies filtered by availability 84: "+movies.size());
			for (Movie movie : movies) {
				System.out.println("Name: <"+movie.getName()+">");
			}
			
			try {
				services.buyTicket(0, 0, "Cinemark", "2018-12-18 15:30", "END GAME");
				services.buyTicket(0, 5, "Cinemark", "2018-12-18 15:30", "END GAME");
			} catch (CinemaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			movies = services.filteringByAvailability(services.cps, "Cinemark",  "2018-12-18 15:30", 82);
			System.out.println("# Movies filtered by availability 82: "+movies.size());
			for (Movie movie : movies) {
				System.out.println("Name: <"+movie.getName()+">");
			}
			
			
		} catch (FilterException e) {
			
		}
        
        
	}

}*/
