package com.example.timecard;

import com.example.timecard.Models.Database.DatabaseHandler;
import com.example.timecard.Models.Objects.Employee;
import com.example.timecard.Utils.LoginValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ApplicationMain extends Application
{
	@Override
	public void start(Stage stage) throws IOException
	{
		FXMLLoader fxmlLoader = new FXMLLoader(ApplicationMain.class.getResource("login.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 500, 500);
		stage.setTitle("Time Card");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args)
	{
		launch();
	}
}