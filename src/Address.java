

public class Address {

    private String locality;
    private String province;
    private String zipCode;
    private String street;

    //Set i get de la localitat
    public void setLocality(String l) {
        this.locality = l;
    }

    public String getLocality() {
        return this.locality;
    }

    //Set i get de la provincia
    public void setProvince(String p) {
        this.province = p;
    }

    public String getProvince() {
        return this.province;
    }

    //Set i get del codi postal
    public void setZipCode(String c) {
        this.zipCode = c;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    //Set i get del adreça
    public void setStreet(String a) {
        this.street = a;
    }

    public String getStreet() {
        return this.street;
    }

    //El constructor de adreça
    public Address(String l, String p, String c, String a) {
        this.locality = l;
        this.province = p;
        this.zipCode = c;
        this.street = a;

    }

    @Override
    public String toString() {
        return "Address{" + "locality=" + locality + ", province=" + province + ", zipCode=" + zipCode + ", address=" + street + '}';
    }
    
}
