package com.itshareplus.googlemapdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener{

    private GoogleMap mMap;
    private Button btnFindPath;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    String origin, destination;
    TextView duracion, distancia, resultado;
    public double latA = -33.024825;
    public double lngA = -71.562775;
    LatLng auto;
    int dura1, dist1;
    int dura2, dist2;
    int dura3, dist3;
    int dur, dis;
    int r = 0;
    int a = 0, b = 0, c = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        duracion = (TextView) findViewById(R.id.tvDuration);
        distancia = (TextView) findViewById(R.id.tvDistance);
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        resultado = (TextView) findViewById(R.id.tvResultado);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
    @Override
    public void onMarkerDragStart(Marker marker) {
        latA = -33.024825;
        lngA = -71.562775;
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Geocoder gc = new Geocoder(MapsActivity.this);
        LatLng ll = marker.getPosition();
        latA=ll.latitude;
        lngA=ll.longitude;
        Toast.makeText(getApplication()," LAT "+latA+" LNG "+lngA,Toast.LENGTH_SHORT).show();
    }
});



        if(mMap!=null){
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LatLng ll = marker.getPosition();
                latA= ll.latitude;
                lngA=ll.longitude;
                Toast.makeText(getApplication()," LAT "+latA+" LNG "+lngA,Toast.LENGTH_SHORT).show();

                return null;
            }
        });

        }

        goToLocationZoom(latA, lngA, 13);
        persoGoogleMap();
        markerAuto();

    }

    public void markerAuto() {
        auto = new LatLng(latA, lngA);
        mMap.addMarker(new MarkerOptions()
                .position(auto)
                .title("Auto chocado")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.auto_ver))
                .anchor(0.5f, 0.5f)
                .draggable(true)
                .flat(true)
                .rotation(180));

    }

    public void buscarRuta1(View view) {
        r = 1;
        resultado.setText("");
        LatLng cam1 = new LatLng(-33.037421, -71.598078);
        mMap.addMarker(new MarkerOptions()
                .position(cam1)
                .title("Camion1")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.grua))
                .draggable(true)
                .flat(true));

        destination = new Double(latA).toString() + "," + new Double(lngA).toString();
        origin = "-33.037421, -71.598078";
        sendRequest(origin, destination);

    }


    public void buscarRuta2(View view) {
        r = 2;
        resultado.setText("");
        LatLng cam2 = new LatLng(-33.038946, -71.543694);
        mMap.addMarker(new MarkerOptions()
                .position(cam2)
                .title("Camion2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.grua))
                .draggable(true)
                .flat(true));

        destination = new Double(latA).toString() + "," + new Double(lngA).toString();
        origin = "-33.038946, -71.543694";
        sendRequest(origin, destination);

    }

    public void buscarRuta3(View view) {
        r = 3;
        resultado.setText("");
        LatLng cam3 = new LatLng(-33.010953, -71.552441);
        mMap.addMarker(new MarkerOptions()
                .position(cam3)
                .title("Camion3")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.grua))
                .draggable(true)
                .flat(true));

        destination = new Double(latA).toString() + "," + new Double(lngA).toString();
        origin = "-33.010953, -71.552441";
        sendRequest(origin, destination);

    }

    private void goToLocationZoom(double lati, double lngi, float zoom) {
        LatLng ll = new LatLng(lati, lngi);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);

    }

    private void persoGoogleMap() {
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
    }

    private void sendRequest(String origin, String destination) {

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Calentando motores.",
                "Buscando destino!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {

            duracion.setText(route.duration.text);
            distancia.setText(route.distance.text);
            dur = route.duration.value;
            dis = route.distance.value;

            switch (r) {
                case 1:
                    a = 1;
                    dura1 = dur;
                    dist1 = dis;
                    Toast.makeText(getApplicationContext(), "Distancia =" + dist1 + " Duracion " + dura1, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    b = 1;
                    dura2 = dur;
                    dist2 = dis;
                    Toast.makeText(getApplicationContext(), "Distancia =" + dist2 + " Duracion " + dura2, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    c = 1;
                    dura3 = dur;
                    dist3 = dis;
                    Toast.makeText(getApplicationContext(), "Distancia =" + dist3 + " Duracion " + dura3, Toast.LENGTH_SHORT).show();
                    break;
            }


            // Toast.makeText(getApplicationContext(),"Distancia ="+dis+" Duracion "+dur,Toast.LENGTH_SHORT).show();
            /*

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.auto))
                    .title(route.endAddress)
                    .position(route.endLocation)));
*/

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }
    }

    public void bestRoute(View view) {
        resultado.setText("");
       if((dura1!=0) && (dura2!=0) && (dura3!=0)){

           Toast.makeText(getApplicationContext(), "ruta1 ="+dura1 , Toast.LENGTH_SHORT).show();
           Toast.makeText(getApplicationContext(), "ruta2 ="+ dura2, Toast.LENGTH_SHORT).show();
           Toast.makeText(getApplicationContext(), "ruta3 ="+dura3 , Toast.LENGTH_SHORT).show();

        if (dura1 < dura2) {
            if (dura1 < dura3) {
                resultado.setText("Mejor ruta = RUTA 1");

            } else {
                resultado.setText("Mejor ruta = RUTA 3");

            }
        } else if (dura2 < dura3) {
            resultado.setText("Mejor ruta = RUTA 2");

        } else {
            resultado.setText("Mejor ruta = RUTA 3");
        }
    }

        /*
        if((dura1!=0) && (dura2!=0)){

                if (dura1 < dura2) {
                    resultado.setText("Mejor ruta = RUTA 1");

                } else {
                    resultado.setText("Mejor ruta = RUTA 2");
                }
        }

        if((dura1!=0) && (dura3!=0)){

            if (dura1 < dura3) {
                resultado.setText("Mejor ruta = RUTA 1");

            } else {
                resultado.setText("Mejor ruta = RUTA 3");
            }
        }
        if((dura2!=0) && (dura3!=0)){

            if (dura2 < dura3) {
                resultado.setText("Mejor ruta = RUTA 2");

            } else {
                resultado.setText("Mejor ruta = RUTA 3");
            }
        }
*/



            r = 0;

            dura1 = 0;
            dura2 = 0;
            dura3 = 0;
            dist1 = 0;
            dist2 = 0;
            dist3 = 0;




    }

    public void onDirectionFinderSuccess1(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {

            duracion.setText(route.duration.text);
            distancia.setText(route.distance.text);
            dur = route.duration.value;
            dis = route.distance.value;

                    PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }
    }

    public void onDirectionFinderSuccess2(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {

            duracion.setText(route.duration.text);
            distancia.setText(route.distance.text);
            dur = route.duration.value;
            dis = route.distance.value;

                   PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }
    }

    public void onDirectionFinderSuccess3(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {

            duracion.setText(route.duration.text);
            distancia.setText(route.distance.text);
            dur = route.duration.value;
            dis = route.distance.value;

                    PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }
    }
}
