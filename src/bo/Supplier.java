package bo;

import java.time.LocalDate;

public class Supplier extends Person {

    public Supplier(Integer idperson, String dni, String name, String surnames, Address address) {
        super(idperson, dni, name, surnames, address);

    }

    public Supplier(Integer i, String d, String n, String c, LocalDate b, String p, String e, Address a) {
        super(i, d, n, c, b, p, e, a);

    }
}
