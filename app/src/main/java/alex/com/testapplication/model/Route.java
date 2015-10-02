package alex.com.testapplication.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Alex on 30.09.15.
 */
public class Route {

    private LatLng startLocation;
    private String startName;
    private LatLng finishLocation;
    private String finishName;

    public Route() {
    }

    public LatLng getFinishLocation() {
        return finishLocation;
    }

    public Route setFinishLocation(LatLng finishLocation) {
        this.finishLocation = finishLocation;
        return this;
    }

    public String getFinishName() {
        return finishName;
    }

    public Route setFinishName(String finishName) {
        this.finishName = finishName;
        return this;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public Route setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
        return this;
    }

    public String getStartName() {
        return startName;
    }

    public Route setStartName(String startName) {
        this.startName = startName;
        return this;
    }
}
