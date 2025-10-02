package co.aamsis.concurrent.spring.dtos;

import java.util.List;

public class ForecastBody {
    List<ForecastAddress> addresses;
}

class ForecastAddress {

    public ForecastAddress(String address, String postalCode) {
        this.address = address;
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    private String address;
    private String postalCode;
}