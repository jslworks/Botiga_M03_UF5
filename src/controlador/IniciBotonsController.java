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

public class IniciBotonsController extends Application {

	private ResourceBundle texts;
	
	//Injecció dels panells i controls de la UI definida al fitxer fxml
	@FXML private Button btnPersones;
	@FXML private Button btnSortir; 

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
		primaryStage.setTitle(texts.getString("title.agenda"));
		primaryStage.show();
       
	}

	@FXML
	private void onAction(ActionEvent e) throws Exception {
		if(e.getSource() == btnPersones){//verifica si el botón es igual al que llamo al evento	
			changeScene("/vista/PersonesView.fxml", "Persones");
		}else if(e.getSource() == btnSortir){
			Platform.exit();
		}
	}
	
	private void changeScene(String path, String title) throws IOException {
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
		stage.setTitle(title);
		stage.setScene(fm_scene);
		stage.show();
		
		/************** Modificar ************/
		//Crear un objecte de la clase PersonasController ja que necessitarem accedir al mètodes d'aquesta classe
		PersonesController personasControler = loader.getController();
		personasControler.setVentana(stage);
		
		//Programem l'event que s'executará quan es tanqui la finestra
		stage.setOnCloseRequest((WindowEvent we) -> {
			personasControler.sortir();
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
