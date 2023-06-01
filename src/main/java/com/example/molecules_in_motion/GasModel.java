package com.example.molecules_in_motion;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class GasModel extends Application {
    private static final int height = 600;
    private static final int width = 900;
    public static int getHeight() {return height;}
    public static int getWidth() {return width;}
    public static double tempMultiplier = 1;

    public static int edgeCollisions = 0;

    protected static ArrayList<Particle> particles = new ArrayList<>();

    @FXML
    private final static GasModelController controller = new GasModelController();

    protected static boolean deleteParticle() {
        if (particles.size()!=0) {
            particles.remove((int)(Math.random()*particles.size()));
            return true;
        }
        else return false;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GasModel.class.getResource("gas-view.fxml"));
        fxmlLoader.setController(controller);
        GridPane root = fxmlLoader.load();

        Scene scene = new Scene(root);


        Canvas canvas = new Canvas(width, height);
        canvas.setId("gasCanvas");
        GraphicsContext gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        controller.setCanvas(gc);
        //controller.setCollisionCount();

        stage.setTitle("Gas Pressure");
        stage.setScene(scene);
        stage.show();


        for (int i = 0; i < 15; i++) {
            makeParticle();
        } //start with 15 particles

        // Animation loop to update and render the simulation
        AnimationTimer animationTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                // Clear the canvas
                //gc.clearRect(0, 0, width, height);
                gc.setFill(Color.GREY);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Update and draw each particle
                for (Particle particle : particles) {
                    particle.update();
                    particle.draw(gc);
                }
            }
        };
        animationTimer.start();
    }

    protected static void makeParticle() {
        //int radius = (int)(Math.random()*5+5);
        int radius = 7;
        double xVel = (Math.random()*7+1);
        if (Math.random() >= 0.5) xVel *= -1;
        double yVel = (Math.random()*7+1);
        if (Math.random() >= 0.5) yVel *= -1;

        particles.add(new Particle(
                (Math.random()*(width-radius*2)+radius),
                (Math.random()*(height-radius*2)+radius),
                xVel,
                yVel,
                radius,
                controller
        ));
    }

    public static void main(String[] args) {

        launch(args);


    }
}