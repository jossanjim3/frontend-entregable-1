package us.master.entregable1.resttypes;

public class WeatherConditions {

    private float temp, feels_like, temp_max, temp_min;
    private int preasure, humidity;

    public WeatherConditions(float temp, float feels_like, float temp_max, float temp_min, int preasure, int humidity) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.preasure = preasure;
        this.humidity = humidity;
    }

    public WeatherConditions() {
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(float feels_like) {
        this.feels_like = feels_like;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public int getPreasure() {
        return preasure;
    }

    public void setPreasure(int preasure) {
        this.preasure = preasure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
