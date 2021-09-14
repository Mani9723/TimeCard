package com.project.timecard.Controllers;

import com.project.timecard.Controllers.ControllerUtil.SceneTransitioner;
import com.project.timecard.Models.Database.DatabaseHandler;
import com.project.timecard.Utils.Clock;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class ClockInController implements Initializable
{
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
	private Label clockSubmitLabel;

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


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		setEmployeeIdInfoVisibility(false);
		Clock.startClock(clockScreentimeLabel);
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
			if(result){
				id = employeeIdField.getText();
			}else{
				// Notify user
			}
			if(selectionEvent.getSource().equals(clockInButton)){

			}else if(selectionEvent.getSource().equals(clockOutButton)){

			}else if(selectionEvent.getSource().equals(mealInButton)){

			}else if(selectionEvent.getSource().equals(mealOutButton)){

			}
		}
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
		clockSubmitLabel.setVisible(visibility);
		submitRect.setVisible(visibility);
	}

}
