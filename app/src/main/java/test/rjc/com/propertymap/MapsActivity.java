package test.rjc.com.propertymap;

import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity /*implements GoogleMap.OnMapClickListener,GoogleMap.OnMapLongClickListener,View.OnTouchListener*/ {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static boolean mMapIsTouched = false;
    private PolylineOptions polyOpt;
    private boolean Is_MAP_Moveable = false;


    //private PolygonOptions rectOptions;
    private Polygon polygon;
    public ArrayList<LatLng> val;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        //mMap.setOnMapClickListener(this);
        //mMap.setOnMapLongClickListener(this);
        val = new ArrayList<LatLng>();

        FrameLayout fram_map = (FrameLayout) findViewById(R.id.fram_map);

        fram_map.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                int x_co = Math.round(x);
                int y_co = Math.round(y);
                Point x_y_points = new Point(x_co, y_co);

                LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                double latitude = latLng.latitude;

                double longitude = latLng.longitude;

                //if(polygon !=null){
                //    polygon.remove();
                //}
                int eventaction = event.getAction();
                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        // finger touches the screen
                        Log.i("Test","ACTION_DOWN");
                        val.clear();
                        if(polygon!= null){
                            polygon.remove();
                            mMap.clear();
                        }

                        val.add(new LatLng(latitude, longitude));

                    case MotionEvent.ACTION_MOVE:
                        // finger moves on the screen
                        val.add(new LatLng(latitude, longitude));

                    case MotionEvent.ACTION_UP:
                        // finger leaves the screen
                        Draw_Map();
                        val.size();

                        break;
                }

                if (Is_MAP_Moveable == true) {
                    return true;

                } else {
                    return false;
                }
            }
         });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        //mMap.setOnMapClickListener(this);
        //mMap.setOnMapLongClickListener(this);

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    protected void moveTo(){
       CameraUpdate cu =   CameraUpdateFactory.newLatLng(
       new LatLng(0,0));
       mMap.moveCamera(cu);

        //CameraUpdate cu =
         //       CameraUpdateFactory.newLatLngZoom(
         //               new LatLng(0,0), 15);
        //mMap.moveCamera(cu);
    }

    public void moveToGeoJson(JSONObject geo){
        try{
            JSONArray arr = geo.getJSONObject("getmetry").getJSONArray("coordinates");

            CameraUpdate cu =
                    CameraUpdateFactory.newLatLngZoom(
                            new LatLng(Double.parseDouble(arr.get(0).toString()),Double.parseDouble(arr.get(1).toString())), 10);
            mMap.moveCamera(cu);

            mMap.addMarker(new MarkerOptions().position(new LatLng(arr.getDouble(0),arr.getDouble(1))).title( geo.getJSONObject("properties").getString("name")).icon(BitmapDescriptorFactory.fromResource(R.drawable.real_estate))).showInfoWindow();
            //locationMarker.showInfoWindow();

        }catch (JSONException e){

        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //get data from server

        this.moveTo();
        mMap.addMarker(new MarkerOptions().position(new LatLng(	35.6469712,139.71861360000003)).title("Ebisu"));

    }

    public void OnRequestData(View view){
        // httpリクエストを入れる変数
        Uri.Builder builder = new Uri.Builder();
        AsyncHttpRequest task = new AsyncHttpRequest(this);
        task.execute(builder);
    }

    public void OnDrawStateClick(View view){
        Log.i("TEST", "OnDrawStateClick");
        if (Is_MAP_Moveable != true) {
            Is_MAP_Moveable = true;
        } else {
            Is_MAP_Moveable = false;
        }
    }
/*
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.mMapIsTouched = true;

                Log.i("TEST", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                this.mMapIsTouched = false;
                Log.i("TEST", "ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private void addPoints(LatLng latLng) {
        if(polyOpt == null){
            polyOpt = new PolylineOptions();
        }
        polyOpt.add(latLng).width(5).color(Color.BLUE).geodesic(true);
        //mMap.addPolyline(polyOpt.add(latLng).width(5).color(Color.BLUE).geodesic(true));
        // move camera to zoom on map
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOWER_MANHATTAN,13));
    }

    @Override
    public void onMapClick(LatLng arg0) {
        //Make Your implementation HERE
        Log.i("TEST", "Clicked!");
        this.addPoints(arg0);
    }
    @Override
    public void onMapLongClick(LatLng arg0) {
        //Make Your implementation HERE
        Log.i("TEST", "Long clicked!");
        this.addPoints(arg0);
    }

   */

    public void Draw_Map() {
        //if(rectOptions==null){
        PolygonOptions rectOptions = new PolygonOptions();
        //}
        rectOptions.addAll(val);
        rectOptions.strokeColor(Color.BLUE);
        rectOptions.strokeWidth(7);
        rectOptions.fillColor(Color.CYAN);
        polygon = mMap.addPolygon(rectOptions);

    }
}