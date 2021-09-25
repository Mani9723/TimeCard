package com.project.timecard.Controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.project.timecard.Controllers.ControllerUtil.DialogBoxHandler;
import com.project.timecard.Controllers.ControllerUtil.SceneTransitioner;
import com.project.timecard.Models.Database.DatabaseHandler;
import com.project.timecard.Models.Objects.Employee;
import com.project.timecard.Models.Objects.Shift;
import com.project.timecard.Models.Objects.TimeCard;
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
import java.time.LocalDate;
import java.time.LocalTime;
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
		dialogBoxHandler = new DialogBoxHandler(clockInStackPane, clockInAnchorPane);
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

	// TODO: Make sure to handle null values from the shift class when adding to the database.
	@FXML
	void onClockInSubmitClicked(ActionEvent event)
	{
		TimeCard timeCard = new TimeCard();
		Employee employee;
		Shift shift;
		String id;
		if(event.getSource().equals(clockInSubmitButton)){
			boolean result = validateIdInput();
			if(result && databaseHandler.usernameExists(employeeIdField.getText())) {
				id = employeeIdField.getText();
				employee = databaseHandler.getEmployee(id);
				shift = databaseHandler.getShift(id);

				if(shift == null) {
					if (!selectionEvent.getSource().equals(clockInButton)) {
						informUser("Please Clock In First");
					}else if(selectionEvent.getSource().equals(clockInButton)){
						updateEmpClockIn(employee, timeCard);
					}
				} else if(!selectionEvent.getSource().equals(clockInButton)){
					shift.setEmployee(employee);
					if (selectionEvent.getSource().equals(clockOutButton)) {
						handleClockOut(id,shift);
					} else if (selectionEvent.getSource().equals(mealInButton)) {
						handleMealBegin(id,shift);
					} else if (selectionEvent.getSource().equals(mealOutButton)) {
						handleMealEnd(id,shift);
					}
				}else{
					informUser("Already Clocked In");
				}
				employeeIdField.clear();
				setEmployeeIdInfoVisibility(false);
				removeSelectionHighlight(clockInRect,clockOutRect,mealInRect,mealOutRect);
			}else{
				informUser("Invalid ID");
				employeeIdField.clear();

			}
		}
	}

	private void handleClockOut(String id, Shift shift)
	{
		if(shift.getTimeCard().isMealBreakStarted() && !shift.getTimeCard().isMealBreakFinished()){
			informUser("Please end meal break first");
		}else if(!shift.getTimeCard().isMealBreakStarted()
				|| (shift.getTimeCard().isMealBreakStarted() && shift.getTimeCard().isMealBreakFinished())){
			shift.getTimeCard().clockOut(LocalTime.now());
			if (databaseHandler.updateShift(id, shift)) {
				informUser(id + ": Clocked Out!");
			} else {
				informUser(id + ": Error Clocking Out");
			}
		}
	}

	private void handleMealBegin(String id, Shift shift)
	{
		if(shift.getTimeCard().getShiftEnd() != null) {
			informUser("Employee already clocked out");
		}else if(shift.getTimeCard().isMealBreakStarted() || shift.getTimeCard().isMealBreakFinished()){
			informUser("Meal Break already accounted for");
		}
		else {
			shift.getTimeCard().clockMealBreakBegin(LocalTime.now());
			if (databaseHandler.updateShift(id, shift)) {
				informUser(id + ": Meal Clocked In!");
			} else {
				informUser(id + ": Error Clocking In");
			}
		}
	}

	private void handleMealEnd(String id, Shift shift)
	{
		if(shift.getTimeCard().isMealBreakStarted() && !shift.getTimeCard().isMealBreakFinished()
				&& shift.getTimeCard().getShiftEnd() == null) {
			shift.getTimeCard().clockMealBreakEnd(LocalTime.now());
			if (databaseHandler.updateShift(id, shift)) {
				informUser(id + ": Meal Clocked Out!");
			} else {
				informUser(id + ": Error Clocking Out");
			}
		}else{
			informUser(id + ": Error");
		}
	}

	private void updateEmpClockIn(Employee employee, TimeCard timeCard)
	{
		Shift shift = new Shift();
		timeCard.clockIn(LocalTime.now());
		shift.setDate(LocalDate.now());
		shift.setEmployee(employee);
		shift.setTimeCard(timeCard);
		if(databaseHandler.addNewShift(shift)) {
			informUser(employee.getFirstName() + ": Clocked In!");
		}else{
			informUser(employee.getFirstName() +": Error Clocking in");
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
