/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.model;

import edu.eci.arsw.cinema.deserializer.CinemaFunctionDeserializer;
import edu.eci.arsw.cinema.persistence.CinemaException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(using = CinemaFunctionDeserializer.class)
public class CinemaFunction {
    
    private Movie movie;
    private List<List<AtomicBoolean>> seats=new ArrayList<>();
    private String date;

    
    public CinemaFunction(){}
    
    public CinemaFunction(Movie movie, String date){
        this.movie=movie;
        this.date=date;
        //7x12
        for (int i=0;i<7;i++){
            List<AtomicBoolean> row= new ArrayList<>(Arrays.asList(new AtomicBoolean[12]));
            for(int j=0; j<12; j++) {
            	Random r = new Random();
            	if(r.nextInt()%2==0) {
            		row.set(j, new AtomicBoolean(true));
            	}else {
            		row.set(j, new AtomicBoolean(false));
            	}
            }
            //Collections.fill(row, new AtomicBoolean(true));
            this.seats.add(row);
        }

    }
    
    public void buyTicket(int row,int col) throws CinemaException{
        if (seats.get(row).get(col).equals(true)){
            seats.get(row).set(col, new AtomicBoolean(false));
        }else{
            throw new CinemaException("Seat booked");
        }
    }
    
    public int getEmptySeats(){
        int countEmptySeat = 0;
        for(List<AtomicBoolean> listaBoolean  : seats){
            for(AtomicBoolean emptySeat  : listaBoolean){
            	boolean f = emptySeat.get();
                if(f){
                    countEmptySeat++;
                }
            }            
        }
        return countEmptySeat;
    }
    
    public List<List<AtomicBoolean>> getSeats() {
        return this.seats;
    }

    public void setSeats(List<List<AtomicBoolean>> newSeats){
        this.seats = newSeats;
    }
    
    public Movie getMovie() {
        return movie;
    }

    synchronized public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getDate() {
        return date;
    }

    synchronized public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    
}
