package alex.com.testapplication.core;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import alex.com.testapplication.model.Order;
import alex.com.testapplication.model.Route;
import de.greenrobot.event.EventBus;

/**
 * Created by Alex on 30.09.15.
 */
public class LoaderService extends IntentService {

    private Geocoder mGeocoder;
    private OkHttpClient mHttpClient;
    private Gson mGson;

    private final String BASIC_URL = "http://mobapply.com/tests/orders/";


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LoaderService() {
        super("LoaderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Loading started", "");

        mHttpClient = new OkHttpClient();
        String response = "";
        try {
            response = load(BASIC_URL, mHttpClient).string();

            Log.d("Response: ", response);

            if (response.length() > 0)
                parseResponse(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseResponse(String response) throws IOException {
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        mGeocoder = new Geocoder(this, Locale.getDefault());

        //parse orders to json using the gson library
        Type listType = new TypeToken<List<Order>>() {
        }.getType();
        List<Order> orders = mGson.fromJson(response, listType);

        //parsing the result list of order objects and add them to route objects
        List<Route> routes = new ArrayList<>();
        for (Order order : orders) {
            //get address using geocoder
            //try catch in case if some addresses don't get parsed
            try {
                Address startAddress = mGeocoder.getFromLocationName(order.getDepartureAddress().generateGeoString(), 1).get(0);
                Address endAddress = mGeocoder.getFromLocationName(order.getDestinationAddress().generateGeoString(), 1).get(0);

                routes.add(new Route().setStartLocation(new LatLng(startAddress.getLatitude(), startAddress.getLongitude()))
                        .setFinishLocation(new LatLng(endAddress.getLatitude(), endAddress.getLongitude())).
                                setStartName(startAddress.getFeatureName()).setFinishName(endAddress.getFeatureName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //post the created routes to UI thread
        EventBus.getDefault().post(new LoadedEvent(routes));
    }

    //load data from url using the http client
    private ResponseBody load(String url, OkHttpClient client) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body();
    }
}
