package com.codingame.game;

public class Collision {
    public double time;
    public Unit a;
    public Unit b;

    Collision(double t) {
        this(t, null, null);
    }

    Collision(double t, Unit a) {
        this(t, a, null);
    }

    Collision(double t, Unit a, Unit b) {
        this.time = t;
        this.a = a;
        this.b = b;
    }
}