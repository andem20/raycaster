package com.raycaster;

public class Vector {
  private double x;
  private double y;
  private double z;

  public Vector(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector(double x, double y) {
    this(x, y, 0);
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public void setX(double n) {
    x = n;
  }

  public void setY(double n) {
    y = n;
  }

  public void setZ(double n) {
    z = n;
  }

  public void scale(double scale) {
    double x = getX() * scale;
    double y = getY() * scale;
    double z = getZ() * scale;
    setX(x);
    setY(y);
    setZ(z);
  }

  public String print() {
    return "x: " + getX() + "\ny: " + getY() + "\nz: " + getZ();
  }
}
