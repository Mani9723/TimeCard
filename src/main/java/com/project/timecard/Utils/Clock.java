package com.project.timecard.Utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Clock
{
	public static Timeline startClock(Label timeLabel)
	{
		Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
			timeLabel.setText(LocalDateTime.now().format(formatter));
		}), new KeyFrame(Duration.seconds(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
		return clock;
	}
}
