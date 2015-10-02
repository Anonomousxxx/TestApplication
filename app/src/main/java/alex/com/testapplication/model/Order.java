package alex.com.testapplication.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Alex on 30.09.15.
 */

public class Order {

    @Expose
    public String uuid;

    @Expose
    public Address departureAddress;

    @Expose
    public Address destinationAddress;

    public Order() {

    }

    public Address getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(Address departureAddress) {
        this.departureAddress = departureAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
