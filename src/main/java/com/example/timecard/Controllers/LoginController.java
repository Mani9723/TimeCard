package com.example.timecard.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


/**
 * Handles the login page
 */
public class LoginController
{
	@FXML
	private Label welcomeText;

	@FXML
	protected void onHelloButtonClick()
	{
		welcomeText.setText("Welcome to JavaFX Application!");
	}
}