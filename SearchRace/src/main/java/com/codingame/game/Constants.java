package com.codingame.game;

public class Constants {
    public static final double EPSILON = 0.00001;
    public static final int Laps = 3;
    public static final int Width = 16000;
    public static final int Height = 9000;
    public static final double CheckpointRadius = 600;
    public final static Collision NULL_COLLISION = new Collision(2.0);
    public static final double MAX_ROTATION_PER_TURN = Math.PI / 10;
    public static int CAR_MAX_THRUST = 200;
    public static final double CAR_FRICTION = 0.15;
}
