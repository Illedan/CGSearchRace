package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    public ArrayList<Checkpoint> checkpoints;
    public Car car;
    private IPlayerManager manager;
    private int totalCheckpoints;
    public int currentCheckpoint = 0;
    public int timer;
    public double colTime = 0.0;
    public boolean isDone;
    public Game (String input, IPlayerManager manager){
        this.checkpoints = new ArrayList<>();
        for(String check : input.split(";")){
            String[] splitted = check.split(" ");
            checkpoints.add(new Checkpoint(Integer.parseInt(splitted[0]), Integer.parseInt( splitted[1])));
        }
        car = new Car(checkpoints.get(0).x, checkpoints.get(0).y, 0);
        car.angle = car.prevAngle = car.getAngle(checkpoints.get(1));
        car.adjust();

        this.manager = manager;
        totalCheckpoints = checkpoints.size()*Constants.Laps;
    }

    public void init(){
        manager.sendData(getInitialData());
    }

    public void onRound() throws Exception {
        if(isDone) {
            manager.endGame(true, Utility.roundToTwoDecimals(timer + colTime));
            return;
        }
        manager.sendData(getData());
        manager.execute();
        String input = manager.getOutputs();
        car.handleInput(input, manager);
        checkCollisions();
        car.adjust();

        if(!isDone) timer++;
        if(timer == 600 && !isDone){
            manager.endGame(false, 1000.0);
            isDone = true;
        }
    }

    private void checkCollisions(){
        boolean hasCollided = true;
        double t = 0.0;
        colTime = 2.0;
        while(!isDone && hasCollided){
            hasCollided = false;
            Collision col = car.getCollision(getNextCheckpoint(), Constants.CheckpointRadius);
            if(col.time >= 0.0 && col.time + t <= 1.0){
                hasCollided = true;
                currentCheckpoint++;
                t += col.time;
                colTime = t;
                car.move(col.time);
                manager.addGameSummary("Collision Time: " + t);
                if(currentCheckpoint >= totalCheckpoints){
                    isDone = true;
                }
            }
        }
        car.move(1.0-t);
    }

    private String[] getInitialData(){
        String[] data = new String[1+totalCheckpoints];
        data[0] = String.valueOf(totalCheckpoints);
        int current = 1;
        for(int i = 0; i < totalCheckpoints; i++){
            Checkpoint check = checkpoints.get((current++) % checkpoints.size());
            data[i+1] = (int)check.x + " " + (int)check.y;
        }
        return data;
    }

    private String[] getData(){
        String[] data = new String[1];
        int angle = (int)Math.round(Math.toDegrees(car.angle));
        int[] inputs = {currentCheckpoint, (int)car.x, (int)car.y, (int)car.vx, (int)car.vy, angle};
        String formatedInput = Arrays.toString(inputs).replaceAll(", ", " ").replace("[", "").replace("]", "");
        if(car.debug) {
            data = new String[2];
            data[1] = String.valueOf(colTime);
        }

        data[0] = formatedInput;
        return data;
    }

    private Checkpoint getNextCheckpoint(){
        return checkpoints.get(getNextCheckpointId());
    }

    public int getNextCheckpointId(){
        return (currentCheckpoint+1) % checkpoints.size();
    }
}
