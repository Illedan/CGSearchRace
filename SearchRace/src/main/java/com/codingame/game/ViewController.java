package com.codingame.game;

import com.codingame.gameengine.module.entities.*;
import com.codingame.gameengine.module.tooltip.TooltipModule;

import java.util.ArrayList;
import java.util.Random;

public class ViewController {
    private GraphicEntityModule module;
    private Game game;
    private TooltipModule tooltipModule;
    private DebugModule debugModule;
    private Text Timer;
    private Text CheckpointTarget;
    private ArrayList<Circle> checkpoints = new ArrayList<>();
    private int currentCheck = 0;
    private Group car;
    private Group exhaust;
    private Group carPositionGroup;
    private Circle previousLocation;
    private Sprite arrow;
    private Text message;
    private Line targetLine;


    public final int Width = 1920;
    public final int Height = 1080;
    public double Scale;
    private Random rnd = new Random();
    public ViewController(GraphicEntityModule module, Game game, TooltipModule tooltipModule, DebugModule debugModule){
        this.module = module;
        this.game = game;
        this.tooltipModule = tooltipModule;
        this.debugModule = debugModule;
        Scale = Width/(double)Constants.Width;
    }

    public int getPos(double pos) {
        return (int)Math.round(pos*Scale);
    }

    public void init(){

        module.createSprite().setImage("back.jpg")
                .setBaseWidth(Width)
                .setBaseHeight(Height*2)
        .setZIndex(-10);


        previousLocation = module.createCircle()
                .setRadius(getPos(300))
                .setFillAlpha(0.0)
                .setX(getPos(game.car.x))
                .setY(getPos(game.car.y))
                .setLineColor(0x000077)
                .setLineAlpha(0.1)
                .setAlpha(0.5)
                .setLineWidth(5).setVisible(false);
        debugModule.addItem(previousLocation.getId());
        carPositionGroup = module.createGroup().setZIndex(5)
                .setX(getPos(game.car.x))
                .setY(getPos(game.car.y));

        carPositionGroup.add(arrow = module.createSprite()
                .setImage("Arrow.png")
                .setAnchorY(0.5)
                .setAnchorX(0)
                .setZIndex(100)
                //.setBaseWidth(getScaled(model.radius*2))
                //.setBaseHeight(getScaled(model.radius*1.3))
                .setAlpha(1.0)
                .setScale(0.5)
                .setTint(0xf5ee00).setVisible(false)
                .setRotation(Math.PI*0.5));
        debugModule.addItem(arrow.getId());


        carPositionGroup.add(car = module.createGroup());
        car.add(module.createSprite().setImage("car.png")
                .setBaseHeight(120)
                .setBaseWidth(70)
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
                    .setFillAlpha(0.3).setZIndex(1);
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

        message = module.createText("")
                .setX(Width/2)
                .setY(10)
                .setAnchorX(0.5)
                .setFontSize(50)
                .setFillColor(0xffffff);

        CheckpointTarget = module.createText("1")
                .setX(Width-10)
                .setY(10)
                .setAnchorX(1)
                .setAnchorY(0)
                .setFontSize(50)
                .setFillColor(0xffffff);

        targetLine =  module.createLine().setZIndex(100)
                .setLineColor(0xff0000)
                .setLineWidth(2).setAlpha(0.0, Curve.IMMEDIATE);
        debugModule.addItem(targetLine.getId());
    }

    private boolean lineHack;
    public void onRound(){
        if(game.car.target != null){
            if(lineHack)
            targetLine.setX(carPositionGroup.getX(), Curve.IMMEDIATE)
                    .setY(carPositionGroup.getY(), Curve.IMMEDIATE)
                    .setX2(getPos(game.car.target.x), Curve.IMMEDIATE)
                    .setY2(getPos(game.car.target.y), Curve.IMMEDIATE)
                    .setAlpha(1.0, Curve.IMMEDIATE);
            else
                targetLine.setX2(carPositionGroup.getX(), Curve.IMMEDIATE)
                        .setY2(carPositionGroup.getY(), Curve.IMMEDIATE)
                        .setX(getPos(game.car.target.x), Curve.IMMEDIATE)
                        .setY(getPos(game.car.target.y), Curve.IMMEDIATE)
                        .setAlpha(1.0, Curve.IMMEDIATE);
            lineHack = !lineHack;
        }
        else{
            targetLine.setAlpha(0, Curve.IMMEDIATE);
        }
        drawSkidMark(carPositionGroup.getX(), carPositionGroup.getY(), getPos(game.car.x), getPos(game.car.y), game.car.angle, game.car.prevAngle);
        previousLocation.setX(carPositionGroup.getX(), Curve.IMMEDIATE).setY(carPositionGroup.getY(), Curve.IMMEDIATE);
        message.setText(game.car.message);
        exhaust.setScale((double)(game.car.thrust+50)/200.0);
        module.commitEntityState(0, exhaust);
        carPositionGroup.setX(getPos(game.car.x))
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

        arrow.setRotation(game.car.getSpeedAngle(), Curve.NONE);
        arrow.setScale(0.2+0.25*game.car.getSpeed()/300);
    }

    private double skidStrength;
    private double wheelAngleDist = Math.PI / 10*2;
    public static double angleDist(double alpha, double beta) {
        double phi = Math.abs(beta - alpha) % (Math.PI*2);
        double distance = phi > Math.PI ? (Math.PI*2) - phi : phi;
        return distance;
    }

    private void drawSkidMark(int x0, int y0, int x1, int y1, double angle, double prevAngle){
        // height 50 and 10 degrees of angle on all 4 sides
        double speedAngle = game.car.getSpeedAngle();
        double diff = angleDist(angle, speedAngle);


        double wheelDist = 50;
        //skidStrength = 1;
        //drawSkid(x1, y1, x1+Math.cos(angle)*wheelDist, y1+Math.sin(angle)*wheelDist);
        skidStrength = (diff)/Math.PI*Math.min(0.5, game.car.getSpeed()/600.0);
        prevAngle += wheelAngleDist;
        angle += wheelAngleDist;
        drawSkid(x0+Math.cos(prevAngle)*wheelDist, y0+Math.sin(prevAngle)*wheelDist, x1+Math.cos(angle)*wheelDist, y1+Math.sin(angle)*wheelDist);
        prevAngle -= wheelAngleDist*2;
        angle -= wheelAngleDist*2;
        drawSkid(x0+Math.cos(prevAngle)*wheelDist, y0+Math.sin(prevAngle)*wheelDist, x1+Math.cos(angle)*wheelDist, y1+Math.sin(angle)*wheelDist);
        prevAngle += Math.PI;
        angle += Math.PI;
        drawSkid(x0+Math.cos(prevAngle)*wheelDist, y0+Math.sin(prevAngle)*wheelDist, x1+Math.cos(angle)*wheelDist, y1+Math.sin(angle)*wheelDist);
        prevAngle += wheelAngleDist*2;
        angle += wheelAngleDist*2;
        drawSkid(x0+Math.cos(prevAngle)*wheelDist, y0+Math.sin(prevAngle)*wheelDist, x1+Math.cos(angle)*wheelDist, y1+Math.sin(angle)*wheelDist);
    }

    private void drawSkid(double x, double y, double x2, double y2){
        module.createLine()
                .setLineWidth(8)
                .setLineAlpha(skidStrength, Curve.IMMEDIATE)
                .setZIndex(3)
                .setLineColor(0x000000)
                .setX((int)Math.round(x))
                .setY((int)Math.round(y))
                .setX2((int)Math.round(x2))
                .setY2((int)Math.round(y2));
    }
}
