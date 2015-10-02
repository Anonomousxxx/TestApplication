package alex.com.testapplication.model;

import android.util.Log;

import com.google.gson.annotations.Expose;

/**
 * Created by Alex on 30.09.15.
 */
public class Address {

    @Expose
    private String country;

    @Expose
    private String zipCode;

    @Expose
    private String city;

    @Expose
    private String countryCode;

    @Expose
    private String street;

    @Expose
    private String houseNumber;

    private String geoString;

    public Address() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    //had problems with generating name of the place for geocoder search.
    //zip code is not relevant at all and country name consists only of one letter
    public String generateGeoString() {
        geoString = "";
        add(street + " " + houseNumber);
        add(city);

        Log.d("GeoString:", geoString);

        return geoString;
    }

    public void add(String stringToAdd) {
        if (stringToAdd != null && stringToAdd.length() > 0)
            geoString = geoString + stringToAdd + ",";
    }

}
