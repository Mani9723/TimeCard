package com.project.timecard;

import com.project.timecard.Controllers.ClockInController;
import com.project.timecard.Models.Constants.FxmlFilePaths;
import com.project.timecard.Models.Database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ApplicationMain extends Application
{

	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(Stage stage) throws IOException
	{
		DatabaseHandler databaseHandler = new DatabaseHandler();
		databaseHandler.isDBConnected();
		FXMLLoader fxmlLoader = new FXMLLoader(ApplicationMain
				.class.getResource(FxmlFilePaths.clockInScreen.toString()));
		Scene scene = new Scene(fxmlLoader.load(), 600, 500);
		ClockInController clockInController = fxmlLoader.getController();
		clockInController.init(databaseHandler);
		stage.setTitle("Time Card");
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);

		//grab your root here
		scene.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});

		//move around here
		scene.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - xOffset);
			stage.setY(event.getScreenY() - yOffset);
		});


		stage.show();
	}

	public static void main(String[] args)
	{
		launch();
	}
}