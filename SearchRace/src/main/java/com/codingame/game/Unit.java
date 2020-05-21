package com.codingame.game;

public abstract class Unit extends Point {
    public double vx;
    public double vy;
    public double mass = 1.0;
    public double friction;

    Unit(double x, double y) {
        super(x, y);
        vx = 0.0;
        vy = 0.0;
    }

    double getSpeed(){
        return Math.sqrt(vx*vx+vy*vy);
    }

    double getSpeedAngle(){
        return Math.atan2(vy, vx);
    }

    void move(double t) {
        x += vx * t;
        y += vy * t;
    }

    public void adjust() {
        x = Utility.truncate(x);
        y = Utility.truncate(y);

        vx = Utility.truncate(vx * (1.0 - friction));
        vy = Utility.truncate(vy * (1.0 - friction));
    }

    Collision getCollision(Unit u, double checkedRadius) {
        // Check instant collision
        if (distance(u) <= checkedRadius) {
            return new Collision(0.0, this, u);
        }

        // Both units are motionless
        if (vx == 0.0 && vy == 0.0 && u.vx == 0.0 && u.vy == 0.0) {
            return Constants.NULL_COLLISION;
        }

        // Change referencial
        // Unit u is not at point (0, 0) with a speed vector of (0, 0)
        double x2 = x - u.x;
        double y2 = y - u.y;
        double r2 = checkedRadius;
        double vx2 = vx - u.vx;
        double vy2 = vy - u.vy;

        double a = vx2 * vx2 + vy2 * vy2;

        if (a <= 0.0) {
            return Constants.NULL_COLLISION;
        }

        double b = 2.0 * (x2 * vx2 + y2 * vy2);
        double c = x2 * x2 + y2 * y2 - r2 * r2;
        double delta = b * b - 4.0 * a * c;

        if (delta < 0.0) {
            return Constants.NULL_COLLISION;
        }

        double t = (-b - Math.sqrt(delta)) / (2.0 * a);

        if (t <= 0.0) {
            return Constants.NULL_COLLISION;
        }

        return new Collision(t, this, u);
    }
}