package com.codingame.game;

import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.util.ArrayList;
import java.util.Random;

public class ViewController {
    private GraphicEntityModule module;
    private Game game;
    private TooltipModule tooltipModule;
    private Text Timer;
    private Text CheckpointTarget;
    private ArrayList<Circle> checkpoints = new ArrayList<>();
    private int currentCheck = 0;
    private Group car;
    private Group exhaust;

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
        car = module.createGroup()
                .setZIndex(5)
                .setX(getPos(game.car.x))
                .setY(getPos(game.car.y));
        car.add(module.createSprite().setImage("car.png")
                .setBaseHeight(120)
                .setBaseWidth(80)
                .setAnchor(0.5).setZIndex(10));
        car.setRotation(game.car.angle + Math.PI/2);
        exhaust = module.createGroup().setZIndex(-1).setScale(0);
        car.add(exhaust);
        SpriteAnimation anim = module.createSpriteAnimation()
                .setImages("fire1.png", "fire2.png")
                .setDuration(200)
                .setLoop(true)
                .setY(50)
                .setX(15)
                .setTint(0x000000)
                .setAlpha(0.5)
                .setAnchor(0.5)
                //.setRotation(Math.PI)
                .setPlaying(true);
        exhaust.add(anim);
        module.commitEntityState(0.0, car);
        int id = 0;
        for(Checkpoint point : game.checkpoints){
            Circle check = module.createCircle().setRadius(getPos(Constants.CheckpointRadius))
                    .setX(getPos(point.x))
                    .setY(getPos(point.y))
                    .setFillColor(0x343434)
                    .setLineAlpha(0.2)
                    .setLineWidth(2)
                    .setLineColor(0x343434)
                    .setFillAlpha(0.3);
            module.createText(String.valueOf(id++))
                    .setX(getPos(point.x))
                    .setY(getPos(point.y))
                    .setFontSize(50)
                    .setAnchor(0.5)
                    .setAlpha(0.7)
                    .setZIndex(2)
                    .setFillColor(0xffffff);
            module.commitEntityState(0.0, check);
            checkpoints.add(check);
            tooltipModule.setTooltipText(checkpoints.get(checkpoints.size()-1), "Checkpoint\nx = " + (int) point.x + "\ny = " + (int) point.y);
        }

        Timer = module.createText("0")
                .setX(10)
                .setY(10)
                .setAnchor(0)
                .setFontSize(50)
                .setFillColor(0xffffff);

        CheckpointTarget = module.createText("1")
                .setX(Width-10)
                .setY(10)
                .setAnchorX(1)
                .setAnchorY(0)
                .setFontSize(50)
                .setFillColor(0xffffff);
    }

    public void onRound(){
        exhaust.setScale((double)(game.car.thrust+100)/200.0);
        module.commitEntityState(0, exhaust);
        car.setX(getPos(game.car.x))
            .setY(getPos(game.car.y));
        tooltipModule.setTooltipText(car, "Car\nx = " + (int)game.car.x + "\ny = " + (int)game.car.y);
        car.setRotation(game.car.angle + Math.PI/2);
        Timer.setText(game.timer+"");
        int nextCheck = game.getNextCheckpointId();
        if(currentCheck != nextCheck){
            Circle prev = checkpoints.get(currentCheck);
            Circle next = checkpoints.get(nextCheck);
            next.setLineColor(0x7ab0db).setLineAlpha(1.0).setLineWidth(10);
            prev.setLineColor(0x343434).setLineAlpha(0.2).setLineWidth(2);
            currentCheck = nextCheck;
            module.commitEntityState(Math.min(1.0, game.colTime), next, prev);
            CheckpointTarget.setText(currentCheck+" ("+game.currentCheckpoint+")");
        }
    }
}
