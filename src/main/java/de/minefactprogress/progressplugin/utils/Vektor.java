package de.minefactprogress.progressplugin.utils;

public class Vektor {

    private double x;
    private double y;

    public Vektor(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vektor normalize() {
        double length = getLength();

        if (x != 0 && y != 0) {
            x = x / length;
            y = y / length;
        }

        return this;
    }

    public Vektor multiply(double amount) {
        x *= amount;
        y *= amount;

        return this;
    }

    public Vektor rotate(double degrees) {
        double n = degrees * (Math.PI / 180);
        double rx = (x * Math.cos(n)) - (y * Math.sin(n));
        double ry = (x * Math.sin(n)) + (y * Math.cos(n));
        x = rx;
        y = ry;

        return this;
    }

    public Vektor clone() {
        return new Vektor(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }


    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
