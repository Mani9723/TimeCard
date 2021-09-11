package com.example.timecard.Controllers;

import com.example.timecard.Controllers.ControllerUtil.SceneTransitioner;
import com.example.timecard.Models.Database.DatabaseHandler;
import com.example.timecard.Utils.Clock;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

	private boolean timeButtonsClicked;

	private static DatabaseHandler databaseHandler;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		timeButtonsClicked = false;
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
		timeButtonsClicked = true;
		setEmployeeIdInfoVisibility(true);

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
