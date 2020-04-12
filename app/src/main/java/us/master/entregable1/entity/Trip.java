package us.master.entregable1.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import us.master.entregable1.Constantes;

public class Trip implements Serializable {
    private String origen, destino, description, imgUrl;
    private Date startDate, endDate;
    private float price;
    private boolean isFavorite = false;

    public Trip(String destino, String origen, String description, String imgUrl, Date startDate, Date endDate, float price, boolean isFavorite) {
        this.destino = destino;
        this.origen = origen;
        this.description = description;
        this.imgUrl = imgUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.isFavorite = isFavorite;
    }

    public Trip() {

    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // generar viajes aleatoriamente
    public static ArrayList<Trip> generateTrips(){
        ArrayList<Trip> trips = new ArrayList<Trip>();

        Random r = new Random();
        ThreadLocalRandom rd = ThreadLocalRandom.current();
        Calendar calendar = Calendar.getInstance();

        Date fechaInicio = new Date();
        calendar.setTime(fechaInicio);
        calendar.add(Calendar.MONTH, 3);
        Date fechaFin = calendar.getTime();

        for (int i = 0; i < 50; i++) {

            Date rDate = new Date(rd.nextLong(fechaInicio.getTime(), fechaFin.getTime()));
            calendar.setTime(rDate);
            calendar.add(Calendar.DAY_OF_YEAR,rd.nextInt(2,13));

            Trip tmp = new Trip();
            tmp.setDestino(Constantes.ciudades[r.nextInt(Constantes.ciudades.length)]);
            tmp.setOrigen(Constantes.lugarSalida[r.nextInt(6)]);
            tmp.setImgUrl(Constantes.urlImagenes[r.nextInt(7)]);
            tmp.setStartDate(rDate);
            tmp.setEndDate(calendar.getTime());
            tmp.setPrice(r.nextFloat()*1000);
            tmp.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            trips.add(tmp);
        }
        return trips;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
