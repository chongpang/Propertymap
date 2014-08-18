package test.rjc.com.propertymap;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
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

            mMap.addMarker(new MarkerOptions().position(new LatLng(arr.getDouble(0),arr.getDouble(1))).title( geo.getJSONObject("properties").getString("name"))).showInfoWindow();
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
}