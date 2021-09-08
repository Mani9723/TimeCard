module com.example.timecard {
	requires javafx.controls;
	requires javafx.fxml;
	requires com.jfoenix;
	requires java.sql;
	requires org.xerial.sqlitejdbc;


	opens com.example.timecard to javafx.fxml;
	exports com.example.timecard;
	exports com.example.timecard.Controllers;
	opens com.example.timecard.Controllers to javafx.fxml;
}