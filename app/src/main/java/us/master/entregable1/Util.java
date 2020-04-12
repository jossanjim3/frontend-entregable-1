package us.master.entregable1;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import us.master.entregable1.entity.Trip;

public class Util {
    public static String formateaFecha(Calendar calendar) {
        int yy=calendar.get(Calendar.YEAR);
        int mm=calendar.get(Calendar.MONTH);
        int dd=calendar.get(Calendar.DAY_OF_MONTH);
        DateFormat formatoFecha=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        calendar.setTimeInMillis(0);
        calendar.set(yy, mm, dd, 0, 0, 0);
        Date chosenDate = calendar.getTime();
        return(formatoFecha.format(chosenDate));

    }

    public static String formateaFecha(long fecha) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(fecha*1000);
        DateFormat formatoFecha=DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        Date chosenDate = calendar.getTime();
        return(formatoFecha.format(chosenDate));
    }

    public static long Calendar2long(Calendar fecha) {
        return(fecha.getTimeInMillis()/1000);
    }

    public static String dateToString(Date date) {
        String pattern = "dd/MM/yyyy";
        DateFormat dfd = new SimpleDateFormat(pattern);
        return dfd.format(date);
    }

    public static  Date stringToDate(String sDate) {
        /*
        String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = format.parse(string);
        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
         */
        DateFormat format = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);
        Date startDate = null;
        try {
            if (sDate.length() > 0) {
                startDate = format.parse(sDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return startDate;
    }

    public static void doTripFavorite (Trip t, boolean isFavorite) {
        // buscar el viaje en la lista de trips de constantes y ponerlo a favorito o no con valor isFavorite
        Log.d("JD", t.toString());

//        int indexOfTrip = Constantes.trips.indexOf(t);
//        Log.d("JD", "indexOfTrip: " + indexOfTrip);
//        if (indexOfTrip != -1) {
//            Constantes.trips.get(indexOfTrip).setFavorite(isFavorite);
//        }

        int index = 0;
        for (Trip tt : Constantes.trips) {
            if (tt.getOrigen().equals(t.getOrigen())
                    && tt.getDestino().equals(t.getDestino())
                    && tt.getPrice() == t.getPrice()
                    && tt.getStartDate().equals(t.getStartDate())
                    && tt.getEndDate().equals(t.getEndDate()) ) {

                Constantes.trips.get(index).setFavorite(isFavorite);
            }
            index++;
        }
    }
}
