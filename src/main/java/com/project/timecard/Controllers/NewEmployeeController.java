package com.project.timecard.Controllers;

import com.jfoenix.controls.JFXDialog;
import com.project.timecard.Controllers.ControllerUtil.DialogBoxHandler;
import com.project.timecard.Controllers.ControllerUtil.SceneTransitioner;
import com.project.timecard.Models.Database.DatabaseHandler;
import com.project.timecard.Models.Objects.Email;
import com.project.timecard.Models.Objects.Employee;
import com.project.timecard.Utils.Clock;
import com.project.timecard.Utils.EmailSender;
import com.project.timecard.Utils.EmployeeIDGenerator;
import com.jfoenix.controls.JFXButton;
import javafx.concurrent.Task;
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
	public JFXButton loginReturnButton;

	@FXML
	private StackPane registerStackPane;

	@FXML
	private AnchorPane registerAnchorPane;

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

		dialogBoxHandler = new DialogBoxHandler(registerStackPane, registerAnchorPane);
		sceneTransitioner = new SceneTransitioner();
		Clock.startClock(timeLabel);
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
							finalizeRegistration(employee);
						} catch (SQLException e) {
							informUser("Error adding you to the system");
						}
						return;
					}else{
						informUser("Please Fill out all of the fields");
					}
				}
			}
		}

	}

	private void finalizeRegistration(Employee employee)
	{
		Task<Boolean> task = new Task<>()
		{
			@Override
			protected Boolean call() throws Exception
			{
				return sendEmail(employee);
			}
		};
		new Thread(task).start();
		informUser(employee.getFirstName() + " " +
				employee.getLastName() + " has been added.");

	}

	private boolean sendEmail(Employee employee)
	{
		System.out.println("In email method");
		Email email = new Email("mani.shah23@gmail.com",
				"Welcome to Modern Prison",
				employee.getFirstName() +
						", You have successfully registered. " +
						"Your Employee ID is " + employee.getEmpId() +
						". Your pay is $"+employee.getPay() +".");
		EmailSender emailSender = new EmailSender(email);
		return emailSender.send();
	}

	private void informUser(String message)
	{
		dialogBoxHandler.OkButton(message,new JFXDialog());
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
