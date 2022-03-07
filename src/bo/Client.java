package bo;

import java.time.LocalDate;

public class Client extends Person {

    public Client(int idperson, String dni, String name, String surnames, Address address) {
        super(idperson, dni, name, surnames, address);

    }

    public Client(int i, String d, String n, String c, LocalDate b, String p, String e, Address a) {
        super(i, d, n, c, b, p, e, a);

    }
}
