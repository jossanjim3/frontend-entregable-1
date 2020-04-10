package us.master.entregable1.entity;

import java.util.ArrayList;

import us.master.entregable1.ListTripsActivity;
import us.master.entregable1.R;

public class Enlace {
    private String descripcion;
    private int recursoImageView;
    private Class clase;

    public Enlace(String descripcion, int recursoImageView, Class clase) {
        this.descripcion = descripcion;
        this.recursoImageView = recursoImageView;
        this.clase = clase;
    }

    public String getDescripcion() {

        return descripcion;
    }

    public void setDescripcion(String descripcion) {

        this.descripcion = descripcion;
    }

    public int getRecursoImageView() {

        return recursoImageView;
    }

    public void setRecursoImageView(int recursoImageView) {
        this.recursoImageView = recursoImageView;
    }

    public Class getClase() {
        return clase;
    }

    public void setClase(Class clase) {
        this.clase = clase;
    }

    public static ArrayList<Enlace> getMenu(){
        ArrayList<Enlace> menu = new ArrayList<Enlace>();

        menu.add(new Enlace("Viajes", R.drawable.ic_place_24dp, ListTripsActivity.class));
        menu.add(new Enlace("Favoritos", R.drawable.favorite_start_24dp, ListTripsActivity.class));

        return menu;
    }
}
