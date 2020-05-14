package us.master.entregable1.resttypes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherRetrofitInterface {

    // api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={your api key}
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeatherLatLon(@Query("lat") float lat, @Query("lon") float lon, @Query("appid") String aapId) ;

    // api.openweathermap.org/data/2.5/weather?id={city id}&appid={your api key}
    @GET("data/2.5/weather")
    Call<WeatherResponse> getCurrentWeatherCityId(@Query("id") int id, @Query("appid") String aapId) ;

}
