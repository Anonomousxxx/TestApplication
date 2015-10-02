package alex.com.testapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import alex.com.testapplication.core.LoadedEvent;
import alex.com.testapplication.core.LoaderService;
import alex.com.testapplication.model.Route;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Marker> markers = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading content");
        progressDialog.setCancelable(true);

        createMap();
        loadData();
    }

    private void loadData() {
        if (hasInternet()) {
            Intent intent = new Intent(MainActivity.this, LoaderService.class);
            startService(intent);
            progressDialog.show();
        } else
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    private boolean hasInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void createMap() {
        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.show_on_map))
                    .getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //zoom on clicked marker
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                List<Marker> markers = new ArrayList<>();
//                markers.add(marker);
//                moveMapCamera(markers, 300);
//
//                if (!marker.isInfoWindowShown())
//                    marker.showInfoWindow();
//
//                return true;
//            }
//        });
    }

    //recieves the loaded routes from the background service
    @SuppressWarnings("unused")
    public void onEventMainThread(LoadedEvent event) {
        Log.d("Routes:", String.valueOf(event.routes.size()));
        if (mMap != null) {
            List<Route> routes = event.routes;
            for (Route route : routes) {
                addRoute(route);
            }
        }
        moveMapCamera(markers, 100);
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    //adds markers and a line between them
    public void addRoute(Route route) {
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(route.getStartLocation())
                .title(route.getStartName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(route.getFinishLocation())
                .title(route.getFinishName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))));
        mMap.addPolyline(new PolylineOptions().add(route.getStartLocation(), route.getFinishLocation()).color(Color.BLUE).width(3));
    }

    //moves map camera to just see all the markers
    public void moveMapCamera(List<Marker> markers, int padding) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker i : markers)
            builder.include(i.getPosition());
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
