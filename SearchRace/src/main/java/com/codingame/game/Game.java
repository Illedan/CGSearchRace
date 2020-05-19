package com.codingame.game;

import com.codingame.gameengine.core.AbstractPlayer;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    public ArrayList<Checkpoint> checkpoints;
    public Car car;
    private IPlayerManager manager;
    private int totalCheckpoints;
    private int currentCheckpoint = 1;
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
        car.angle = car.getAngle(checkpoints.get(1));
        car.adjust();

        this.manager = manager;
        totalCheckpoints = checkpoints.size()*Constants.Laps;
    }

    public void init(){
        manager.sendData(getInitialData());
    }

    public void onRound() throws Exception {
        if(isDone) {
            car.move(1.0);
            manager.endGame(true, Utility.roundToTwoDecimals(timer + colTime));
            return;

        }
        manager.sendData(getData());
        manager.execute();
        String input = manager.getOutputs();
        car.handleInput(input, manager);
        checkCollisions();
        car.move(1.0);
        car.adjust();

        if(!isDone) timer++;
        if(timer == 601){
            manager.endGame(false, 700.0);
        }
    }

    private void checkCollisions(){
        if(!isDone){
            colTime = 2.0;
            Collision col = car.getCollision(getNextCheckpoint(), Constants.CheckpointRadius);
            if(col.time >= 0.0 && col.time <= 1.0){
                currentCheckpoint++;
                colTime = col.time;
                if(currentCheckpoint > totalCheckpoints){
                    isDone = true;
                }
            }
        }
    }

    private String[] getInitialData(){
        String[] data = new String[1+totalCheckpoints+1];
        data[0] = String.valueOf(totalCheckpoints+1);
        int current = 0;
        for(int i = 0; i <= totalCheckpoints; i++){
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
        return checkpoints.get(currentCheckpoint % checkpoints.size());
    }

    public int getNextCheckpointId(){
        return currentCheckpoint % checkpoints.size();
    }
}
