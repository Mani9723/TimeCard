package com.example.timecard.Controllers;

import com.example.timecard.Controllers.ControllerUtil.SceneTransitioner;
import com.example.timecard.Models.Database.DatabaseHandler;
import com.example.timecard.Utils.Clock;
import com.example.timecard.Utils.LoginValidator;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


/**
 * Handles the login page
 */
public class LoginController implements Initializable
{
	@FXML
	private GridPane gridPane;

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
	private Label noticeLabel;

	@FXML
	private Label timeLabel;

	private static DatabaseHandler databaseHandler;
	private static SceneTransitioner sceneTransitioner;
	private String empId;
	private String empPass;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
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
		sceneTransitioner.loadRegisterScene(event,databaseHandler);
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
			boolean result = processEmployeeLogin();
			if(result){
				noticeLabel.setText("Success!");
			}else{
				noticeLabel.setText("Invalid Logon");
			}
		}else{
			noticeLabel.setText("Please Fill out both fields!");
		}
	}

	private boolean processEmployeeLogin()
	{
		LoginValidator loginValidator = new LoginValidator(databaseHandler);
		return loginValidator.validateLogin(Integer.parseInt(empId), empPass);

	}

}