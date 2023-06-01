package com.example.molecules_in_motion;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particle {
    final int restitutionCoefficient = 1;
    private double xPos;
    private double yPos;

    private double xVel;
    private double yVel;

    private final int RADIUS;

    private final GasModelController CONTROLLER;

    public Particle(double xPos, double yPos, double xVel, double yVel, int radius, GasModelController controller){
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVel = xVel;
        this.yVel = yVel;
        this.RADIUS = radius;
        this.CONTROLLER = controller;
    }

    public void checkCollisions(){
        //do this
        if (CONTROLLER.idealGas) return;
        for (Particle other: GasModel.particles){
            if (other != this) {
                double dx = other.xPos - xPos;
                double dy = other.yPos - yPos;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < RADIUS + other.RADIUS) { // Collided with another particle
                    // Calculate collision response
                    double overlap = RADIUS + other.RADIUS - distance;
                    double collisionAngle = Math.atan2(dy, dx);
                    double shiftX = overlap * Math.cos(collisionAngle)*0.5;
                    double shiftY = overlap * Math.sin(collisionAngle)*0.5;

                    // Adjust positions to resolve overlap
                    xPos -= shiftX;
                    yPos -= shiftY;
                    other.xPos += shiftX;
                    other.yPos += shiftY;

                    // Calculate velocities after collision using conservation of momentum
                    double relativeVelocityX = -xVel + other.xVel;
                    double relativeVelocityY = -yVel + other.yVel;
                    double normalX = dx/distance;
                    double normalY = dy/distance;

                    double dotProduct = relativeVelocityX * normalX + relativeVelocityY * normalY;

                    double impulseScalar = -(restitutionCoefficient) * dotProduct;
                    double impulseX = impulseScalar*normalX;
                    double impulseY = impulseScalar*normalY;

                    xVel -= impulseX;
                    yVel -= impulseY;

                    other.xVel += impulseX;
                    other.yVel += impulseY;
                }
            }
        }
    }
    public void update(){
        xPos += xVel*GasModel.tempMultiplier;
        yPos += yVel*GasModel.tempMultiplier;
        boolean temp = false;

        if (yPos + RADIUS > GasModel.getHeight()) {
            yPos = GasModel.getHeight()-RADIUS;
            yVel *= -1;
            GasModel.edgeCollisions++;
            GasModelController.collisionsInCurrentSecond++;
            temp = true;
        } else if (yPos - RADIUS < 0) {
            yPos = RADIUS;
                    yVel *= -1;
            GasModel.edgeCollisions++;
            GasModelController.collisionsInCurrentSecond++;
            temp = true;
        }
        if (xPos + RADIUS > GasModel.getWidth()) {
            xPos = GasModel.getWidth()-RADIUS;
            xVel *= -1;
            if (!temp) {
                GasModel.edgeCollisions++;
                GasModelController.collisionsInCurrentSecond++;
            }
        } else if (xPos - RADIUS < 0) {
            xPos = RADIUS;
            xVel *= -1;
            if (!temp) {
                GasModel.edgeCollisions++;
                GasModelController.collisionsInCurrentSecond++;
            }
        }

        if (!CONTROLLER.idealGas) checkCollisions();
    }

    public void draw(GraphicsContext gc) {
        // Draw the particle as a filled circle on the provided GraphicsContext
        gc.setFill(Color.BLUE);
        gc.fillOval(xPos - RADIUS, yPos - RADIUS, 2 * RADIUS, 2 * RADIUS);
    }
}
