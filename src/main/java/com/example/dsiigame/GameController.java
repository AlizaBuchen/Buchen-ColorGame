package com.example.dsiigame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Random;

public class GameController {
    private final Color[] colors = {Color.RED, Color.GREEN, Color.YELLOW, Color.PINK, Color.BLUE};

    @FXML
    private Rectangle colorStrip;

    @FXML
    private Text score;

    @FXML
    private Label label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, bonus;

    private final Random random = new Random();

    private Label[] labels;

    private int currentScore = 0;

    @FXML
    private Button startButton;

    /**
     * Initializes the game timers and sets up the action for the start button.
     * Timelines start once the start button is clicked
     *
     * @variable colorTimeline controls how often the color to choose will be changes
     * @variable labelTimeline controls how often more boxes will appear with different colors and point values
     * @variable bonusTimeline controls hos often the bonus button appears
     */
    public void initialize() {
        Timeline colorTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> changeColor()));
        colorTimeline.setCycleCount(Timeline.INDEFINITE);

        Timeline labelTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> displayLabel()));
        labelTimeline.setCycleCount(Timeline.INDEFINITE);

        Timeline bonusTimeline = new Timeline(new KeyFrame(Duration.seconds(7), event -> bonusDisplay()));
        bonusTimeline.setCycleCount(Timeline.INDEFINITE);


        startButton.setOnAction(event -> {
            changeColor();
            colorTimeline.play();
            labelTimeline.play();
            bonusTimeline.play();
            startButton.setVisible(false);
            colorStrip.setVisible(true);
            score.setVisible(true);
            updateScoreText();
        });
    }

    /**
     * Controls size, text and duration that the bonus button appears on the screen for
     */
    private void bonusDisplay() {
        bonus.setVisible(true);

        Timeline hideBonusTimeline = new Timeline(new KeyFrame(Duration.seconds(1.5), event -> {
            bonus.setVisible(false);
            bonus.setText("Bonus!");
            bonus.setScaleX(1.0);
            bonus.setScaleY(1.0);
        }));
        hideBonusTimeline.setCycleCount(1);
        hideBonusTimeline.play();

        handleClick(bonus, Color.GOLD, 5);
    }

    /**
     * Randomly selects and changes the color of the color strip to either red, pink, yellow, green or blue
     */
    private void changeColor() {
        int randomIndex = random.nextInt(colors.length);
        colorStrip.setFill(colors[randomIndex]);
    }

    /**
     * Enables labels 1-10 to randomly appear on screen with randomly selected color
     * (red, green, pink, yellow or blue) and random point values 1-3
     * @variable randomDuration determines the amount of time the label will appear on screen (depending on point value)
     */
    private void displayLabel() {
        labels = new Label[] {label1, label2, label3, label4, label5, label6, label7, label8, label9, label10};
        for (Label label : labels) {
            if (!label.isVisible() && random.nextBoolean()) {
                int points = random.nextInt(3) + 1;
                Color boxColor = colors[random.nextInt(colors.length)];
                label.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: BLACK; -fx-padding: 5;",
                        boxColor.toString().replace("0x", "#")));

                label.setScaleX(1.0);
                label.setScaleY(1.0);

                label.setVisible(true);
                label.setText(String.valueOf(points));
                label.setUserData(points);

                handleClick(label, boxColor, points);

                double randomDuration;
                if (points == 1) {
                    randomDuration = 3;
                } else if (points == 2) {
                    randomDuration = 2;
                } else {
                    randomDuration = 1.5;
                }

                Timeline hideTimeline = new Timeline(new KeyFrame(Duration.seconds(randomDuration), event -> {
                    label.setVisible(false);
                }));
                hideTimeline.setCycleCount(1);
                hideTimeline.play();
            }
        }
    }

    /**
     * Handles when the user clicks a label
     * @param label the label/box the user clicked
     * @param boxColor the color of the box that was clicked
     * @param points the amount of points that box is worth
     */
    private void handleClick(Label label, Color boxColor, int points) {
        label.setOnMouseClicked(event -> {
            if (boxColor.equals(colorStrip.getFill())) {
                currentScore += points;
                label.setStyle("-fx-background-color: LIGHTGREEN; -fx-text-fill: BLACK; -fx-padding: 5;");
                label.setText("✓"); // unicode: \u2713
            }
            else if (boxColor.equals(Color.GOLD)){
                currentScore += 5;
                label.setText("+5");
            }
            else {
                currentScore -= points;
                label.setStyle("-fx-background-color: SALMON; -fx-text-fill: BLACK; -fx-padding: 5;");
                label.setText("✗"); // unicode: \u2717
            }
            enlarge(label);
            updateScoreText();

        });
    }

    /**
     * Handles the reaction to the user clicking on a label
     * (changes color and text depending on if the action was correct, incorrect or a bonus)
     * @param label the label that was clicked
     */
    private void enlarge(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), label);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);

        scaleTransition.setOnFinished(e -> label.setVisible(false));
        scaleTransition.play();
    }

    /**
     * keeps track of the current score
     */
    private void updateScoreText() {
        score.setText("Score: " + currentScore);
    }

    /**
     * Handles what happens when the user clicks on the start button (game begins)
     */
    @FXML
    protected void onStartButtonClick() {
        startButton.setVisible(false);
        colorStrip.setVisible(true);
        score.setVisible(true);
        updateScoreText();
    }
}