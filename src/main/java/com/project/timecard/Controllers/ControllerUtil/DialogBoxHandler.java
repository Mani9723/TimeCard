package com.project.timecard.Controllers.ControllerUtil;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Random;

public class DialogBoxHandler
{
	private StackPane stackPane;
	private JFXDialog dialog;
	private Parent parent;
	private GaussianBlur gaussianBlur;

	public DialogBoxHandler(StackPane stackPane, Parent root)
	{
		this.stackPane = stackPane;
		this.parent = root;
		gaussianBlur = new GaussianBlur();
		gaussianBlur.setRadius(7.5);
	}


	public void OkButton(String buttonMessage, JFXDialog dialog)
	{
		this.dialog = dialog;
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		dialogLayout.requestFocus();
		JFXButton button = new JFXButton("Return");
		button.setStyle("-fx-background-color: #c7daf8");
		button.setStyle("-fx-background-radius: 32");
//		button.setAlignment(Pos.CENTER);
//		button.setPrefSize(70,35);
		button.setTextFill(Paint.valueOf("black"));
		button.setButtonType(JFXButton.ButtonType.RAISED);
//		button.setBackground(new Background(new BackgroundFill(Paint.valueOf("#e9e9e9"),null,null)));
		this.dialog = new JFXDialog(stackPane,dialogLayout,dialogTransition());
		this.dialog.toFront();
		this.dialog.requestFocus();
		this.dialog.setTransitionType(JFXDialog.DialogTransition.TOP);
		button.setFocusTraversable(true);

		button.addEventHandler(MouseEvent.MOUSE_CLICKED,(MouseEvent mouseEvent )->{
			parent.setEffect(null);
			this.dialog.close();
		});

		button.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent keyEvent )->{
			parent.setEffect(null);
			this.dialog.close();
		});

		this.dialog.setOnDialogClosed((JFXDialogEvent event)-> parent.setEffect(null));

		Label label = new Label(buttonMessage);
		label.setTextFill(Color.valueOf("black"));
//		label.setContentDisplay(ContentDisplay.CENTER);
		dialogFinish(dialogLayout,button,label);
	}

	private JFXDialog.DialogTransition dialogTransition()
	{
//		int n = new Random().nextInt(5)+1;
		int n = 3;
		System.out.println(n);
		switch (n){
			case 1:
				return JFXDialog.DialogTransition.TOP;
			case 2:
				return JFXDialog.DialogTransition.BOTTOM;
			case 3:
				return JFXDialog.DialogTransition.CENTER;
			case 4:
				return JFXDialog.DialogTransition.RIGHT;
			case 5:
				return JFXDialog.DialogTransition.LEFT;
			default:
				return JFXDialog.DialogTransition.NONE;
		}
	}

	private void dialogFinish(JFXDialogLayout dialogLayout, JFXButton button, Label label)
	{
		dialogLayout.setBody(label);
//		dialogLayout.setAlignment(Pos.CENTER);
		dialogLayout.setActions(button);
//		dialogLayout.setBackground(new Background(
//				new BackgroundFill(Color.valueOf("bfe0ff"),null,null)));
		this.dialog.show();
		parent.setEffect(gaussianBlur);
	}

//	public void setNonStackPane(Parent root)
//	{
//		parent = root;
//	}

}
