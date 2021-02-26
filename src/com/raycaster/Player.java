package com.raycaster;

public class Player {
  private double x;
  private double y;
  private double angle;
  private Vector plane;

  public Player(double x, double y, double angle) {
    this.x = x;
    this.y = y;
    this.angle = angle;

    plane = VectorMath.hat(VectorMath.fromAngle(getAngle()));
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getAngle() {
    return angle;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }

  public void varyAngel(double amount) {
    angle += amount;
    setPlane(VectorMath.hat(VectorMath.fromAngle(getAngle())));
  }

  public Vector getPlane() {
    return plane;
  }

  public void setPlane(Vector plane) {
    this.plane = plane;
  }

  public void forward(int vel) {
    double vx = Math.cos(Math.toRadians(getAngle())) * vel;
    double vy = Math.sin(Math.toRadians(getAngle())) * vel;
    x += vx;
    y += vy;
  }

  public void backward(int vel) {
    double vx = Math.cos(Math.toRadians(getAngle())) * vel;
    double vy = Math.sin(Math.toRadians(getAngle())) * vel;
    x -= vx;
    y -= vy;
  }

  public void right(int vel) {
    double vx = Math.cos(Math.toRadians(getAngle())) * vel;
    double vy = Math.sin(Math.toRadians(getAngle())) * vel;
    x -= vy;
    y += vx;
  }

  public void left(int vel) {
    double vx = Math.cos(Math.toRadians(getAngle())) * vel;
    double vy = Math.sin(Math.toRadians(getAngle())) * vel;
    x += vy;
    y -= vx;
  }
}
