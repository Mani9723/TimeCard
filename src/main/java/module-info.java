module com.example.timecard {
	requires javafx.controls;
	requires javafx.fxml;
	requires com.jfoenix;
	requires java.sql;
	requires org.xerial.sqlitejdbc;
	requires java.mail;
	requires activation;


	opens com.example.timecard to javafx.fxml;
	exports com.example.timecard;
	exports com.example.timecard.Controllers;
	exports com.example.timecard.Models.Database;
	opens com.example.timecard.Controllers to javafx.fxml;
}