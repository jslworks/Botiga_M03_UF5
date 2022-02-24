package bo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Person implements Identificable{

    private Integer idperson;
    private String dni;
    private String name;
    private String surnames;
    private LocalDate birthdate;
    private String email;
    private String phone;
    private Address address;
    private int count;

    //Set i get del id de la persona
    public void setIdperson(int i) {
        this.idperson = i;
    }

    @Override
    public int getId() {
        return this.idperson;
    }   

    //Set i get del DNI
    public void setDni(String d) {
        this.dni = d;
    }

    public String getDni() {
        return this.dni;
    }

    //Set i get del nombre
    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return this.name;
    }

    //Set i get dels cognoms
    public void setSurnames(String s) {
        this.surnames = s;
    }

    public String getSurnames() {
        return this.surnames;
    }

    //Set i get de la data de naixement
    public void setBirthdate(LocalDate b) {
        this.birthdate = b;
    }

    public LocalDate getBirthdate() {
        return this.birthdate;
    }

    //Set i get del correu electronic
    public void setEmail(String e) {
        this.email = e;
    }

    public String getEmail() {
        return this.email;
    }

    //Set i get del telefon
    public void setPhone(String e) {
        this.phone = e;
    }

    public String getPhone() {
        return this.phone;
    }

    //Set i get de la direcció
    public void setAddress(Address a) {
        this.address = a;
    }

    public Address getAddress() {
        return this.address;
    }

    public int getNumPeople() {
        return count;
    }

    protected Person(Integer idperson, String dni, String name, String surnames) {
        this.idperson = idperson;
        this.dni = dni;
        this.name = name;
        this.surnames = surnames;
    }
    
    protected Person(int idperson, String dni, String name, String surnames, Address address) {
        this.idperson = idperson;
        this.dni = dni;
        this.name = name;
        this.surnames = surnames;
        this.address = address;
    }

    protected Person(int i, String d, String n, String c, LocalDate b, String p, String e, Address a) {
        this.idperson = i;
        this.dni = d;
        this.name = n;
        this.surnames = c;
        this.birthdate = b;
        this.phone = p;
        this.email = e;
        this.address = a;
        this.count++;

    }

    public static int getYears(LocalDate a) {
        return (int) ChronoUnit.YEARS.between(a, LocalDate.now());
    }

    public static long yearsDif(LocalDate a, LocalDate b) {
        int dif = getYears(a) - getYears(b);
        return dif;
    }

    @Override
    public String toString() {
        return "Person{" + "idperson=" + idperson + ", dni=" + dni + ", name=" + name + ", surnames=" + surnames + ", birthdate=" + birthdate + ", email=" + email + ", phone=" + phone + ", address=" + address + '}';
    }
    
    public boolean isValid() {
        return (this.idperson == null || this.dni == null || this.name == null || this.surnames == null) ;
    }
}
