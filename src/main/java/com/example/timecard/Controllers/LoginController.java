package com.example.timecard.Controllers;

import com.example.timecard.Models.Database.DatabaseHandler;
import com.example.timecard.Utils.LoginValidator;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Handles the login page
 */
public class LoginController implements Initializable
{

	@FXML
	private Label welcomeText;

	@FXML
	private TextField empIdField;

	@FXML
	private PasswordField empPassField;

	@FXML
	private JFXButton loginButton;

	@FXML
	private Label noticeLabel;

	private static DatabaseHandler databaseHandler;
	private String empId;
	private String empPass;

	@FXML
	void onLoginClicked(ActionEvent event)
	{
		if(event.getSource().equals(loginButton)){
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
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		databaseHandler = new DatabaseHandler();
	}



	private boolean processEmployeeLogin()
	{
		LoginValidator loginValidator = new LoginValidator(databaseHandler);
		return loginValidator.validateLogin(Integer.parseInt(empId), empPass);

	}

}