package it.univaq.uffizigallery.services;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Riccardo on 30/03/2018.
 */

public class LocationService {

    private Context context;

    public LocationService(Context context){
        this.context = context;
    }

    //funzione appoggio
    public boolean isGPSEnabled(){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

}
