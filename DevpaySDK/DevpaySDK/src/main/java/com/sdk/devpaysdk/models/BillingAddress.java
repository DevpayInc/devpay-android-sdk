package com.sdk.devpaysdk.models;

import org.json.JSONObject;

public class BillingAddress {

    String country;
    String zip;
    String state;

    public String getCountry() {
        return country;
    }

    public String getZip() {
        return zip;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    String city;
    String street;

    public BillingAddress(String street,
                          String city,
                          String zip,
                          String state,
                          String country) {
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.state = state;
        this.country = country;
    }

    public JSONObject toJSON() {
        try {
            JSONObject billingAddrJsonObj = new JSONObject();
            billingAddrJsonObj.put("street", getStreet());
            billingAddrJsonObj.put("city", getCity());
            billingAddrJsonObj.put("country", getCountry());
            billingAddrJsonObj.put("zip", getZip());
            billingAddrJsonObj.put("state", getState());
            return billingAddrJsonObj;
        } catch (Exception e) {
        }
        return null;
    }

}
