package controlador;

import java.io.IOException;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.bo.Product;
import model.dao.ProductDAO;

public class ProductController{

	// Para gestionar la persistencia de los productos
	private ProductDAO productes;

	// Para gestionar el producto actual:
	private Product producto;

	// Inidicar de nuevo registro:
	private boolean nouRegistre = false;

	private Stage ventana;

	// Elementos TableView y contenidos para su gestion
	@FXML private TextField idTF;
	@FXML private TextField nombreTF;
	@FXML private TextField precioTF;
	@FXML private TextField stockTF;
	@FXML private DatePicker fechaInicioDP;
	@FXML private DatePicker fechaFinalDP;

	// Con esto conseguiremos validar los datos introducidos
	private ValidationSupport vs;

	@FXML private void initialize() {
		productes = new ProductDAO();
		productes.openAll();

		//Validació dades
		//https://github.com/controlsfx/controlsfx/issues/1148
		//produeix error si no posem a les VM arguments això: --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED
		vs = new ValidationSupport();
		vs.registerValidator(idTF, true, Validator.createEmptyValidator("ID obligatori"));
		vs.registerValidator(nombreTF, true, Validator.createEmptyValidator("Nom obligatori"));
		vs.registerValidator(precioTF, true, Validator.createRegexValidator("Preu ha de ser un número", "\\d*", Severity.ERROR));
		vs.registerValidator(stockTF, true, Validator.createRegexValidator("Stock ha de ser un número", "\\d*", Severity.ERROR));
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	@FXML private void onKeyPressedId(KeyEvent e) throws IOException {

		if ((e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB)) {
			// Comprovar si existeix la persona indicada en el control idTextField
			producto = productes.find(Integer.parseInt(idTF.getText()));
			if (producto != null) {
				// posar els valors per modificarlos
				nombreTF.setText(producto.getNombre());
				precioTF.setText(String.valueOf(producto.getPrecio()));
				stockTF.setText(String.valueOf(producto.getStock()));
				fechaInicioDP.setValue(producto.getFechaInicial());
				fechaFinalDP.setValue(producto.getFechaFinal());
			} else {
				// nou registre
				limpiarFormNoId();
			}
		}
	}
	 
	@FXML private void onSaveAction(ActionEvent e) throws IOException {
		//verificar si les dades són vàlides				
		if(isDatosValidos()){
			producto = new Product(
					Integer.parseInt(idTF.getText()),
					nombreTF.getText(),
					Double.parseDouble(precioTF.getText()),
					Integer.parseInt(stockTF.getText()),
					fechaInicioDP.getValue(),
					fechaFinalDP.getValue() );

			// Desactivar stock
			stockTF.setEditable(false);
			
			productes.save(producto);
			limpiarFormulario();
			productes.showAll();
		}
	}

	@FXML private void onDeleteAction(ActionEvent e) throws IOException {

		if(isDatosValidos()){
			if(productes.delete(Integer.parseInt(idTF.getText()))){
				limpiarFormulario();
				productes.showAll();
			}
		}
	}

	@FXML private void onExitAction(ActionEvent e) throws IOException {

		sortir();

		ventana.close();
	}

	public void sortir(){
		productes.saveAll();
		productes.showAll();
	}

	private boolean isDatosValidos() {

		//Comprovar si totes les dades són vàlides
		if (vs.isInvalid()) {
			String errors = vs.getValidationResult().getMessages().toString();
			// Mostrar finestra amb els errors
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(ventana);
			alert.setTitle("Camps incorrectes");
			alert.setHeaderText("Corregeix els camps incorrectes");
			alert.setContentText(errors);
			alert.showAndWait();
		
			return false;
		}
		return true;
	}

	private void limpiarFormulario(){
		idTF.setText("");
		limpiarFormNoId();
	}

	private void limpiarFormNoId(){
		nombreTF.setText("");
		precioTF.setText("");
		stockTF.setText("");
		fechaInicioDP.getEditor().clear();
		fechaFinalDP.getEditor().clear();
	}
	
}
