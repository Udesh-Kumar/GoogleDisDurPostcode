package com.example.himanshu.directionapi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private PlaceAutocompleteFragment placeAutoComplete;
    private GoogleMap mMap;
    private PlaceAutocompleteFragment placeAutoComplete2;
    Button Click;
    double s_lat, S_lng, D_lat, D_lng;
    TextView Distance, Duration;
    PolylineOptions polylineOptions;
    Polyline line;
    List<LatLng> directionList = new ArrayList<LatLng>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Distance = findViewById(R.id.distance);
        Duration = findViewById(R.id.time);

        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                Log.d("Maps", "Place selected: " + place.getName());
                double lat = place.getLatLng().latitude;
                double lng = place.getLatLng().longitude;
                s_lat = lat;
                S_lng = lng;

                LatLng source = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(source).title("Here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 18f));
            }

            @Override
            public void onError(Status status) {
                Log.d("map", "An error occurred" + status);
            }
        });

//        placeAutoComplete2 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete2);
//        placeAutoComplete2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                Log.d("Maps", "Place selected: " + place.getName());
//                double lat = place.getLatLng().latitude;
//                double lng = place.getLatLng().longitude;
//                D_lat = lat;
//                D_lng = lng;
//                LatLng source = new LatLng(lat, lng);
//                mMap.addMarker(new MarkerOptions().position(source).title("Here"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(source, 18f));
//            }

//            @Override
//            public void onError(Status status) {
//                Log.d("map", "An error occurred" + status);
//            }
//        });




        Click = findViewById(R.id.Find);
        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> data = new HashMap<>();
                data.put("origin", s_lat + "," + S_lng);
                data.put("destination", D_lat + "," + D_lng);
                data.put("key", "AIzaSyDELpqMi27VwVMB44JliiQG3wSDAYEuG_c");

                Api api = ApiClient.apiclient().create(Api.class);
                Call<Map> call = api.placedata(data);


                call.enqueue(new Callback<Map>() {
                    @Override
                    public void onResponse(Call<Map> call, Response<Map> response) {
//                        Distance.setText(((LinkedTreeMap)((LinkedTreeMap)((ArrayList)((LinkedTreeMap)((ArrayList)response.body().get("routes")).get(0)).get("legs")).get(0)).get("distance")).get("text").toString());
//                        Duration.setText(((LinkedTreeMap)((LinkedTreeMap)((ArrayList)((LinkedTreeMap)((ArrayList)response.body().get("routes")).get(0)).get("legs")).get(0)).get("duration")).get("text").toString());
//    Log.d("onResponse","Hello"+((LinkedTreeMap)((LinkedTreeMap)((ArrayList)((LinkedTreeMap)response.body()).get("routes")).get(0)).get("overview_polyline")).get("points").toString());
                        //  Log.d("points","There are points"+((LinkedTreeMap)((LinkedTreeMap)((ArrayList)((LinkedTreeMap)((ArrayList)((LinkedTreeMap)((ArrayList)((LinkedTreeMap)response.body()).get("routes")).get(0)).get("legs")).get(0)).get("steps")).get(1)).get("polyline")).get("points").toString());



                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());  //Gson hame pue json formate ka data  ek String(json) me de diya
                        // String code[];
                        JSONArray legs;
                        try {
                            JSONObject jsonObject = new JSONObject(json); //because pura json formate ek object me he to json pass ker diya object bna ke

                            JSONArray route = jsonObject.getJSONArray("routes");//routes array me jana he Json array bna diya


                            int routeSize = route.length();// route array ka size nikal liya or loop me chala diya isme hi legs array ko get kerke legs ki lenght ko print kra diya


                            for (int i = 0; i < routeSize; i++) {  // i ek JSONObject ki trah kam kr rha he

                                legs = route.getJSONObject(i).getJSONArray("legs");  // getJSONObject and getJSONArray capital wale lene he JSONArray legs uper bna diya

                                System.out.println("Legs Size: " + legs.length());

                                int legsize = legs.length();

                                for (int j = 0; j < legsize; j++) {

                                    JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");
                                    System.out.println("Step Size: " + steps.length());
                                    for (int k = 0; k < steps.length(); k++) {   //steps ek Array he isse object ko get krenge

                                       // polylineOptions=new PolylineOptions();




                                        JSONObject polyline = steps.getJSONObject(k).getJSONObject("polyline");
                                        String lines = polyline.getString("points");         //Strig ki value he point usko get krenge
                                        System.out.println("PolyLine : " + lines);

                                        List<LatLng> singlePolyline = decodePoly(lines);

                                        for (int z=0;z<singlePolyline.size()-1;z++) {  //-1 lat and +1 lng ko bta rha he
                                            LatLng src = singlePolyline.get(z);
                                            LatLng dest = singlePolyline.get(z + 1);


                                            line = mMap.addPolyline(new PolylineOptions()
                                                    .add(new LatLng(src.latitude, src.longitude),
                                                            new LatLng(dest.latitude, dest.longitude)
                                                    ).width(5).color(Color.BLACK).geodesic(true));




                                        }



                                    }
                                }
                            }


                            //********  Kisi array me agar object ka naam diya hota he to to uska object bna ke value get kerte he
                            //*********Or  Array me object ka naam nhi hota to usko index no. se (loop) me measure kerte he
//                            JSONArray steps=jsonObject.getJSONArray("steps");
//                            int stepsSize=steps.length();
//                            for (int i=0;i<stepsSize;i++){
//                                JSONObject polyline=steps.getJSONObject(0);
//                                System.out.println("Polyline size"+polyline.length());
//
//
//
//                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }



                    @Override
                    public void onFailure(Call<Map> call, Throwable t) {

                    }
                });
            }
        });
    }

//    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions){
//        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
//        options.addAll(positions);
//        Polyline polyline = map.addPolyline(options);
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(new LatLng(positions.get(1).latitude, positions.get(1).longitude))
//                .zoom(17)
//                .build();
//        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        polylineOptions=new PolylineOptions();





//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//
//                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//                CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
//                mMap.clear();
//
//                MarkerOptions mp = new MarkerOptions().draggable(true);
//
//                mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
//
//                mp.title("my position");
//
//                mMap.moveCamera(center);
//
//                mMap.animateCamera(zoom);
//            }
//        });


        //Go to routes->legs->steps-> in json

        //For loop me lene he taki ek list me aa jaye

        //json ko formate me lane ke liye jsonformatter-online.com

        // for loop se value get kervani he points ki

        //json formate lane ke liye google developer se api lani he or api paste kerni he
    }
}
