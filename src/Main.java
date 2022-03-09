import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Locale.Category;

public class Main {
    public static void main(String[] args) {
        Locale localeFormat = Locale.getDefault(Category.FORMAT);
        Locale localeDisplay = Locale.getDefault(Category.DISPLAY);
        ResourceBundle texts = ResourceBundle.getBundle("botiga.texts", localeDisplay);

        Controller g = new Controller();
        g.run();
    }
}
