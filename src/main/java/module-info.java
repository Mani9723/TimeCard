module com.example.timecard {
	requires javafx.controls;
	requires javafx.fxml;
	requires com.jfoenix;
	requires java.sql;
	requires org.xerial.sqlitejdbc;
	requires java.mail;
	requires activation;


	opens com.project.timecard to javafx.fxml;
	exports com.project.timecard;
	exports com.project.timecard.Controllers;
	exports com.project.timecard.Models.Database;
	opens com.project.timecard.Controllers to javafx.fxml;
}