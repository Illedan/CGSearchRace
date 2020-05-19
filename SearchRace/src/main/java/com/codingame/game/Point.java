package com.codingame.game;


public class Point {
    public double x;
    public double y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double distance(Point p) {
        return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
    }

    public double getAngle(Point p2){
        double dx = p2.x-x;
        double dy = p2.y-y;
        return Math.atan2(dy, dx);
    }

    // Move the point to x and y
    void move(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Move the point to an other point for a given distance
    void moveTo(Point p, double distance) {
        double d = distance(p);

        if (d < Constants.EPSILON) {
            return;
        }

        double dx = p.x - x;
        double dy = p.y - y;
        double coef = distance / d;

        this.x += dx * coef;
        this.y += dy * coef;
    }

    Point getPoint(Point target, double distance){
        double d = distance(target);

        if (d < Constants.EPSILON) {
            return target.clonePoint();
        }

        double dx = target.x - x;
        double dy = target.y - y;
        double coef = distance / d;

        double x = this.x + dx * coef;
        double y = this.y + dy * coef;
        return new Point(x, y);
    }

    boolean isInRange(Point p, double range) {
        return p != this && distance(p) <= range;
    }

    public Point clonePoint(){
        return new Point(x, y);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Point other = (Point) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) return false;
        return true;
    }
}