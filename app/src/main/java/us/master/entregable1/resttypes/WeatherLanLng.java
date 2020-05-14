package us.master.entregable1.resttypes;

public class WeatherLanLng {

    private float lat;
    private float lon;

    public WeatherLanLng(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public WeatherLanLng() {
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "WeatherLanLng{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
