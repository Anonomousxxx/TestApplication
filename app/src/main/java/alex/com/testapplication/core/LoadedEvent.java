package alex.com.testapplication.core;

import java.util.List;

import alex.com.testapplication.model.Route;

/**
 * Created by Alex on 30.09.15.
 */
public class LoadedEvent {
    public List<Route> routes;

    public LoadedEvent(List<Route> routes) {
        this.routes = routes;
    }
}
