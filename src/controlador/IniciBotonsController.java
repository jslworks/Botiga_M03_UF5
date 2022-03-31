package controlador;

import java.io.IOException;
import java.util.Locale;
import java.util.Locale.Category;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.bo.Boton;

public class IniciBotonsController extends Application {

	private ResourceBundle texts;
	
	//Injecció dels panells i controls de la UI definida al fitxer fxml
	@FXML private Button personButton;
	@FXML private Button productButton;
	@FXML private Button exitButton; 

	@Override
	public void start(Stage primaryStage) throws IOException {

		//Carrega el fitxer amb la interficie d'usuari inicial (Scene)
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/IniciBotonsView.fxml"));
		
		//Carregar fitxer de textos multiidioma de la localització actual
		Locale localitzacioDisplay = Locale.getDefault(Category.DISPLAY);
		texts = ResourceBundle.getBundle("vista.Texts", localitzacioDisplay);
		//fins aquí tot igual, només falta assignar el fitxer de recursos al formulari
		loader.setResources(texts);

		Scene fm_inici = new Scene(loader.load());
		
		//Li assigna la escena a la finestra inicial (primaryStage) i la mostra
		primaryStage.setScene(fm_inici);
		primaryStage.setTitle(texts.getString("title.shop"));
		primaryStage.show();
       
	}

	@FXML
	private void onAction(ActionEvent e) throws Exception {
		Button btn = (Button) e.getSource();
		
		switch (btn.getId()) {
			case "personButton":	
				changeScene("/vista/PersonesView.fxml", Boton.Persona);
				break;
			case "productButton":
				changeScene("/vista/ProductsView.fxml", Boton.Producto);
				break;
			case "exitButton":
				Platform.exit();
				break;
			default:
				break;
		}
	}
	
	private void changeScene(String path, Boton title) throws IOException {
		//Carrega el fitxer amb la interficie d'usuari
		FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
		
		//Carregar fitxer de textos multiidioma de la localització actual
		Locale localitzacioDisplay = Locale.getDefault(Category.DISPLAY);
		texts = ResourceBundle.getBundle("vista.Texts", localitzacioDisplay);
		//fins aquí tot igual, només falta assignar el fitxer de recursos al formulari
		loader.setResources(texts);
		
		//Crea una nova finestra i l'obre 
		Stage stage = new Stage();
		Scene fm_scene = new Scene(loader.load());
		stage.setTitle(title.toString());
		stage.setScene(fm_scene);
		stage.show();
		
		switch(title.toString()) {
			case "Persona":
				PersonesController personasControler = loader.getController();
				personasControler.setVentana(stage);
				
				//Programem l'event que s'executará quan es tanqui la finestra
				stage.setOnCloseRequest((WindowEvent we) -> {
					personasControler.sortir();
				});
				break;
			case "Producto":
				
				break;
		}
		
		/************** Modificar ************/
		//Crear un objecte de la clase PersonasController ja que necessitarem accedir al mètodes d'aquesta classe
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
