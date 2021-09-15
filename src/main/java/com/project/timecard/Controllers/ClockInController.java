package com.project.timecard.Controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.project.timecard.Controllers.ControllerUtil.DialogBoxHandler;
import com.project.timecard.Controllers.ControllerUtil.SceneTransitioner;
import com.project.timecard.Models.Database.DatabaseHandler;
import com.project.timecard.Utils.Clock;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClockInController implements Initializable
{
	@FXML
	private StackPane clockInStackPane;

	@FXML
	private AnchorPane clockInAnchorPane;

	@FXML
	private JFXButton clockInExitButton;

	@FXML
	private Label clockScreentimeLabel;

	@FXML
	private JFXButton clockInButton;

	@FXML
	private JFXButton clockOutButton;

	@FXML
	private JFXButton mealInButton;

	@FXML
	private JFXButton mealOutButton;

	@FXML
	private TextField employeeIdField;

	@FXML
	private JFXButton clockInSubmitButton;

	@FXML
	private JFXButton employeeLoginButton;

	@FXML
	private Rectangle clockInRect;

	@FXML
	private Rectangle submitRect;

	@FXML
	private Rectangle mealOutRect;

	@FXML
	private Rectangle mealInRect;

	@FXML
	private Rectangle clockOutRect;

	private ActionEvent selectionEvent;

	private static DatabaseHandler databaseHandler;

	private static DialogBoxHandler dialogBoxHandler;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		setEmployeeIdInfoVisibility(false);
		Clock.startClock(clockScreentimeLabel);
		dialogBoxHandler = new DialogBoxHandler(clockInStackPane);
		dialogBoxHandler.setNonStackPane(clockInAnchorPane);
	}

	public void init(DatabaseHandler database)
	{
		databaseHandler = database;
		databaseHandler.isDBConnected();

	}

	@FXML
	public void onTimeButtonsClicked(ActionEvent event)
	{
		setEmployeeIdInfoVisibility(true);
		selectionEvent = event;

		if(event.getSource().equals(clockInButton)){
			setSelectionHighlight(clockInRect);
			removeSelectionHighlight(clockOutRect,mealInRect,mealOutRect);
		}
		else if(event.getSource().equals(clockOutButton)){
			setSelectionHighlight(clockOutRect);
			removeSelectionHighlight(clockInRect,mealInRect,mealOutRect);
		}
		else if(event.getSource().equals(mealInButton)){
			setSelectionHighlight(mealInRect);
			removeSelectionHighlight(clockOutRect,clockInRect,mealOutRect);
		}
		else if(event.getSource().equals(mealOutButton)){
			setSelectionHighlight(mealOutRect);
			removeSelectionHighlight(clockOutRect,mealInRect,clockInRect);
		}
	}

	private void setSelectionHighlight(Rectangle rectangle)
	{
		rectangle.setStrokeWidth(3);
		rectangle.setStroke(Paint.valueOf("#000000"));
	}

	private void removeSelectionHighlight(Rectangle ... rectangles)
	{
		for(Rectangle rectangle : rectangles){
			rectangle.setStrokeWidth(0);
		}
	}

	@FXML
	void onClockInSubmitClicked(ActionEvent event)
	{
		String id;
		if(event.getSource().equals(clockInSubmitButton)){
			boolean result = validateIdInput();
			if(result && databaseHandler.usernameExists(employeeIdField.getText())) {
				id = employeeIdField.getText();

				if (selectionEvent.getSource().equals(clockInButton)) {
					informUser(id + ": Clocked In!");
				} else if (selectionEvent.getSource().equals(clockOutButton)) {
					informUser(id + ": Clocked Out!");
				} else if (selectionEvent.getSource().equals(mealInButton)) {
					informUser(id + ": Meal Clocked In!");
				} else if (selectionEvent.getSource().equals(mealOutButton)) {
					informUser(id + ": Meal Clocked out!");
				}
				employeeIdField.clear();
				setEmployeeIdInfoVisibility(false);
				removeSelectionHighlight(clockInRect,clockOutRect,mealInRect,mealOutRect);
			}else{
				dialogBoxHandler.OkButton("Invalid ID",new JFXDialog());
				employeeIdField.clear();

			}
		}
	}

	private void informUser(String message)
	{
		dialogBoxHandler.OkButton(message,new JFXDialog());
	}

	private boolean validateIdInput()
	{
		try{
			Long.parseLong(employeeIdField.getText());
			return true;
		}catch (NumberFormatException e){
			return false;
		}
	}

	@FXML
	void onEmployeeLoginClicked(ActionEvent event)
	{
		SceneTransitioner sceneTransitioner = new SceneTransitioner();
		sceneTransitioner.loadLoginScene(event, databaseHandler);
	}

	@FXML
	public void onExitClicked(ActionEvent event)
	{
		if(event.getSource().equals(clockInExitButton)){
			System.exit(0);
		}
	}

	private void setEmployeeIdInfoVisibility(boolean visibility)
	{
		employeeIdField.setVisible(visibility);
		clockInSubmitButton.setVisible(visibility);
		submitRect.setVisible(visibility);
	}

}
