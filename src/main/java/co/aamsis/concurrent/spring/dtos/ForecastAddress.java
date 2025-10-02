package co.aamsis.concurrent.spring.dtos;


public class ForecastAddress {

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
