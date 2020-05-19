package com.codingame.game;

import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.util.ArrayList;
import java.util.Random;

public class ViewController {
    private GraphicEntityModule module;
    private Game game;
    private TooltipModule tooltipModule;
    private Sprite car;
    private Text Timer;
    private ArrayList<Sprite> checkpoints = new ArrayList<>();
    private int currentCheck = 0;

    public final int Width = 1920;
    public final int Height = 1080;
    public double Scale;
    private Random rnd = new Random();
    public ViewController(GraphicEntityModule module, Game game, TooltipModule tooltipModule){
        this.module = module;
        this.game = game;
        this.tooltipModule = tooltipModule;
        Scale = Width/(double)Constants.Width;
    }

    public int getPos(double pos) {
        return (int)Math.round(pos*Scale);
    }

    public void init(){
        module.createRectangle().setFillColor(0x343434)
                .setWidth(1920)
                .setHeight(1080);
        module.createSprite().setImage("back.jpg")
                .setBaseWidth(Width)
                .setBaseHeight(Height*2);

        car = module.createSprite().setImage("car.png")
                .setBaseHeight(120)
                .setBaseWidth(80)
                .setZIndex(5)
                .setAnchor(0.5)
                .setX(getPos(game.car.x))
                .setY(getPos(game.car.y));
        car.setRotation(game.car.angle + Math.PI/2);
        module.commitEntityState(0.0, car);
        int id = 0;
        for(Checkpoint point : game.checkpoints){
            Circle check = module.createCircle().setRadius(getPos(Constants.CheckpointRadius))
                    .setX(getPos(point.x))
                    .setY(getPos(point.y))
                    .setFillColor(0x343434)
                    .setLineAlpha(0.8)
                    .setLineColor(0x770000)
                    .setFillAlpha(0.8);
            module.createText(String.valueOf(id++))
                    .setX(getPos(point.x))
                    .setY(getPos(point.y))
                    .setFontSize(50)
                    .setAnchor(0.5)
                    .setZIndex(2)
                    .setFillColor(0xffffff);
            module.commitEntityState(0.0, check);
            checkpoints.add(module.createSprite().setImage("checkpoint.png")
                    .setBaseHeight(getPos(Constants.CheckpointRadius*2))
                    .setBaseWidth(getPos(Constants.CheckpointRadius*2))
                    .setAnchor(0.5)
                    .setX(check.getX())
                    .setY(check.getY()));
            tooltipModule.setTooltipText(checkpoints.get(checkpoints.size()-1), "Checkpoint\nx = " + (int) point.x + "\ny = " + (int) point.y);
        }

        Timer = module.createText("0")
                .setX(10)
                .setY(10)
                .setAnchor(0)
                .setFontSize(50)
                .setFillColor(0xffffff);
    }

    public void onRound(){
        car.setX(getPos(game.car.x))
            .setY(getPos(game.car.y));
        tooltipModule.setTooltipText(car, "Car\nx = " + (int)game.car.x + "\ny = " + (int)game.car.y);
        car.setRotation(game.car.angle + Math.PI/2);
        Timer.setText(game.timer+"");
        int nextCheck = game.getNextCheckpointId();
        if(currentCheck != nextCheck){
            Sprite prev = checkpoints.get(currentCheck);
            Sprite next = checkpoints.get(nextCheck);
            next.setTint(0x999999);
            prev.setTint(0xffffff);
            currentCheck = nextCheck;
            module.commitEntityState(Math.min(1.0, game.colTime), next, prev);
        }
    }
}
