package com.example.molecules_in_motion;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

public class GasModelController implements Initializable {

    private GraphicsContext gc;
    @FXML
    private Text collisionCount;
    @FXML
    private CheckBox idealGasCheck;

    public boolean idealGas = false;

    private Timeline collisionTimeline;
    public Queue<Integer> collisionCounts;
    public static int collisionsInCurrentSecond = 0;

    private int averageCollisions;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        collisionCounts = new LinkedList<>();
        collisionCount.setText("Collisions per second (Pressure): 0");
        collisionTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    collisionCounts.offer(collisionsInCurrentSecond);
                    if (collisionCounts.size() > 3) {
                        collisionCounts.poll();
                    }
                    int totalCollisions = collisionCounts.stream().mapToInt(Integer::intValue).sum();
                    int averageCollisions = totalCollisions / collisionCounts.size();
                    collisionCount.setText("Collisions per second (Pressure):" + averageCollisions);
                    collisionsInCurrentSecond = 0;
                })
        );
        collisionTimeline.setCycleCount(Timeline.INDEFINITE);
        collisionTimeline.play();
    }

    public void setCanvas(GraphicsContext gc){
        this.gc = gc;
    }

    public void onIdealClicked(){
        idealGas = idealGasCheck.isSelected();
        System.out.println("toggled to " + idealGas);
    }

    public void onAddClicked(){
        GasModel.makeParticle();
    }

    public void onAdd5(){
        for (int i = 0; i<5; i++) onAddClicked();
    }
    public void onAdd10(){
        for (int i = 0; i<10; i++) onAddClicked();
    }
    public boolean onRemoveClicked(){
        return GasModel.deleteParticle();
    }
    public void onRemove5(){
        for (int i = 0; i<5; i++) {
            if(!onRemoveClicked()) break;
        }
    }
    public void onRemove10(){
        for (int i = 0; i<10; i++) {
            if(!onRemoveClicked()) break;
        }
    }


    public void onTempUpClicked(){
        GasModel.tempMultiplier += 0.1;
    }

    public void onTempDownClicked(){
        if (GasModel.tempMultiplier >= 0) GasModel.tempMultiplier -= 0.1;
    }

}