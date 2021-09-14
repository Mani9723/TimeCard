package com.project.timecard.Controllers.ControllerUtil;

import com.project.timecard.ApplicationMain;
import com.project.timecard.Controllers.ClockInController;
import com.project.timecard.Controllers.LoginController;
import com.project.timecard.Controllers.NewEmployeeController;
import com.project.timecard.Models.Database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneTransitioner
{

	private double xOffset = 0;
	private double yOffset = 0;


	public void loadLoginScene(ActionEvent actionEvent, DatabaseHandler databaseHandler)
	{
		FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource("login.fxml"));
		Parent loginParent = null;
		try {
			loginParent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert loginParent != null;
		Scene currScene = new Scene(loginParent);
		LoginController controller = loader.getController();
		controller.init(databaseHandler);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource())
				.getScene()
				.getWindow();
		allowDragableScene(loginParent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();

	}

	public void loadClockInScene(ActionEvent actionEvent, DatabaseHandler databaseHandler)
	{
		FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource("clockInScreen.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert parent != null;
		Scene currScene = new Scene(parent);
		ClockInController controller = loader.getController();
		controller.init(databaseHandler);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource())
				.getScene()
				.getWindow();
		allowDragableScene(parent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();

	}

	public void loadRegisterScene(ActionEvent actionEvent, DatabaseHandler databaseHandler)
	{
		FXMLLoader loader = new FXMLLoader(ApplicationMain.class.getResource("registerScene.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert parent != null;
		Scene currScene = new Scene(parent);
		NewEmployeeController controller = loader.getController();
		controller.init(databaseHandler);
		Stage homeWindow = (Stage)((Node)actionEvent.getSource())
				.getScene()
				.getWindow();
		allowDragableScene(parent,homeWindow);
		homeWindow.setScene(currScene);
		homeWindow.show();
	}

	private void allowDragableScene(Parent parent, Stage stage)
	{
		parent.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		parent.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - xOffset);
			stage.setY(event.getScreenY() - yOffset);
		});
	}

}
