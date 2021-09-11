package com.example.timecard;

import com.example.timecard.Models.Database.DatabaseHandler;
import com.example.timecard.Models.Objects.Employee;
import com.example.timecard.Utils.LoginValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class ApplicationMain extends Application
{

	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(Stage stage) throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(ApplicationMain.class.getResource("clockInScreen.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 600, 500);
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