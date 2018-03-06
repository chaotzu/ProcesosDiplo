package com.example.alumno11.procesosdiplo;

import java.util.List;
import java.util.Map;

/**
 * Created by alumno11 on 24/02/18.
 */

//Para manejar servicio web es màs facil manejarlo con un objeto para facilitar el parseo

public class Video {

    private String id=null;
    private String title=null;
    private String year=null;
    private String type=null;
    private String date=null;
    private String director=null;
    private String escritor = null;
    private String actores =null;
    private String plot=null;
    private String poster=null;
    private List<Map<String,String>> ratings=null;

    public Video() {
    }

    public Video(String id, String title, String year, String type, String poster) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.type = type;
        this.poster = poster;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getEscritor() {
        return escritor;
    }

    public void setEscritor(String escritor) {
        this.escritor = escritor;
    }

    public List<Map<String, String>> getRatings() {
        return ratings;
    }

    public void setRatings(List<Map<String, String>> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", director='" + director + '\'' +
                ", escritor='" + escritor + '\'' +
                ", actores='" + actores + '\'' +
                ", plot='" + plot + '\'' +
                ", poster='" + poster + '\'' +
                ", ratings=" + ratings +
                '}';
    }
}