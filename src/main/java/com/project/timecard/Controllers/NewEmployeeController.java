package com.project.timecard.Controllers;

import com.jfoenix.controls.JFXDialog;
import com.project.timecard.Controllers.ControllerUtil.DialogBoxHandler;
import com.project.timecard.Controllers.ControllerUtil.SceneTransitioner;
import com.project.timecard.Models.Database.DatabaseHandler;
import com.project.timecard.Models.Objects.Employee;
import com.project.timecard.Utils.Clock;
import com.project.timecard.Utils.EmployeeIDGenerator;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewEmployeeController implements Initializable
{
	@FXML
	private StackPane registerStackPane;

	@FXML
	private AnchorPane registerAnchorPane;

	@FXML
	private StackPane root;

	@FXML
	private Label timeLabel;

	@FXML
	private TextField firstNameField;

	@FXML
	private TextField lastNameField;

	@FXML
	private Label idLabel;

	@FXML
	private PasswordField passwordField;

	@FXML
	private Label payLabel;

	@FXML
	private JFXButton confirmNewEmpButton;

	private static DialogBoxHandler dialogBoxHandler;
	private static DatabaseHandler databaseHandler;
	private static SceneTransitioner sceneTransitioner;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		dialogBoxHandler = new DialogBoxHandler(registerStackPane);
		dialogBoxHandler.setNonStackPane(registerAnchorPane);
		sceneTransitioner = new SceneTransitioner();
		Clock.startClock(timeLabel);
		root.requestFocus();
	}

	public void init(DatabaseHandler database)
	{
		databaseHandler = database;
	}

	@FXML
	void onConfirmButtonClicked(ActionEvent event)
	{
		if(event.getSource().equals(confirmNewEmpButton)) {
			Long[] ids = new Long[10];
			for (int i = 0; i < ids.length; i++) {
				ids[i] = EmployeeIDGenerator.generateID();
				if (!databaseHandler.usernameExists(Long.toString(ids[i]))) {
					idLabel.setText(idLabel.getText() + " " + ids[i]);
					payLabel.setText(payLabel.getText() + "12.00");
					if (fieldsFilled()) {
						Employee employee = new Employee(firstNameField.getText(),
								lastNameField.getText(), passwordField.getText(), 12.0);
						employee.setEmpId(ids[i]);
						try {
							databaseHandler.addNewEmployee(employee);
							System.out.println(employee.getFirstName() + " " +
									employee.getLastName() + " has been added.");
							dialogBoxHandler.OkButton(employee.getFirstName() + " " +
									employee.getLastName() + " has been added.",new JFXDialog());
						} catch (SQLException e) {
							dialogBoxHandler.OkButton("Error adding you to the system",new JFXDialog());
						}
						return;
					}else{
						dialogBoxHandler.OkButton("Please Fill out all of the fields", new JFXDialog());
					}
				}
			}
		}

	}

	private boolean fieldsFilled()
	{
		return firstNameField.getText().length() > 1 &&
				lastNameField.getText().length() > 1 &&
				passwordField.getText().length() > 1;
	}

	public void onReturnButtonClicked(ActionEvent event)
	{
		sceneTransitioner.loadLoginScene(event,databaseHandler);
	}
}
