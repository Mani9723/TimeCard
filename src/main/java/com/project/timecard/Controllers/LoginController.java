package com.project.timecard.Controllers;

import com.jfoenix.controls.JFXDialog;
import com.project.timecard.Controllers.ControllerUtil.DialogBoxHandler;
import com.project.timecard.Controllers.ControllerUtil.SceneTransitioner;
import com.project.timecard.Models.Database.DatabaseHandler;
import com.project.timecard.Utils.Clock;
import com.project.timecard.Utils.LoginValidator;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Handles the login page
 */
public class LoginController implements Initializable
{

	@FXML
	private StackPane loginStackPane;

	@FXML
	private AnchorPane loginAnchorPane;

	@FXML
	private TextField empIdField;

	@FXML
	private PasswordField empPassField;

	@FXML
	private JFXButton loginButton;

	@FXML
	private JFXButton registerButton;

	@FXML
	private JFXButton exitButton;

	@FXML
	private JFXButton loginReturnButton;

	@FXML
	private Label timeLabel;

	private static DatabaseHandler databaseHandler;
	private static SceneTransitioner sceneTransitioner;
	private static DialogBoxHandler dialogBoxHandler;
	private String empId;
	private String empPass;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		dialogBoxHandler = new DialogBoxHandler(loginStackPane, loginAnchorPane);
		exitButton.requestFocus();
		Clock.startClock(timeLabel);
		sceneTransitioner = new SceneTransitioner();

	}

	public void init(DatabaseHandler database)
	{
		databaseHandler = database;
		databaseHandler.isDBConnected();
	}


	@FXML
	void onLoginClicked(ActionEvent event)
	{
		if(event.getSource().equals(loginButton)){
			validateUserInput();
		}
	}

	@FXML
	public void onEnterKeyReleased(KeyEvent keyEvent)
	{
		if(keyEvent.getCode().equals(KeyCode.ENTER)){
			validateUserInput();
		}
	}

	@FXML
	public void onRegisterClicked(ActionEvent event)
	{
		if(event.getSource().equals(registerButton)) {
			sceneTransitioner.loadRegisterScene(event, databaseHandler);
		}
	}

	@FXML
	void onExitButtonClicked(ActionEvent event)
	{
		if(event.getSource().equals(exitButton)){
			System.exit(0);
		}
	}

	@FXML
	public void onReturnButtonClicked(ActionEvent event)
	{
		if(event.getSource().equals(loginReturnButton)){
			sceneTransitioner.loadClockInScene(event,databaseHandler);
		}
	}

	private void validateUserInput()
	{
		if(empIdField.getText().length()>1
				&& empPassField.getText().length()>1){
			empId = empIdField.getText();
			empPass = empPassField.getText();
			boolean result = false;
			try {
				result = processEmployeeLogin();
			}catch (NumberFormatException e){
				e.printStackTrace();
			}
			if(result){
				informUser("Success!");
			}else{
				informUser("Invalid Logon");
			}
		}else{
			informUser("Please Fill out both fields!");
		}
	}

	private void informUser(String message)
	{
		dialogBoxHandler.OkButton(message, new JFXDialog());
	}

	private boolean processEmployeeLogin() throws NumberFormatException
	{
		LoginValidator loginValidator = new LoginValidator(databaseHandler);
		return loginValidator.validateLogin(Integer.parseInt(empId), empPass);

	}

}