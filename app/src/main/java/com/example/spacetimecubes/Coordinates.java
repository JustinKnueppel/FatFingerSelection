package com.example.spacetimecubes;


public class Coordinates<T> {
    private T x;
    private T y;
    public Coordinates(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }
}