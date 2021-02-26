package com.raycaster;

public class VectorMath {
  public static double dotProduct(Vector v1, Vector v2) {
    return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() + v2.getZ();
  }

  public static double magnitude(Vector v) {
    return Math.sqrt(dotProduct(v, v));
  }

  public static double determinant(Vector v1, Vector v2) {
    return v1.getX() * v2.getY() - v2.getX() * v1.getY();
  }

  public static Vector getUnitVector(Vector v) {
    double mag = magnitude(v);
    double x = v.getX() / mag;
    double y = v.getY() / mag;
    double z = v.getZ() / mag;

    return new Vector(x, y, z);
  }

  public static Vector fromAngle(double angle) {
    angle = Math.toRadians(angle);
    double x = Math.cos(angle);
    double y = Math.sin(angle);

    return new Vector(x, y);
  }

  public static double toAngleRadians(Vector v1, Vector v2) {
    double dot = dotProduct(v1, v2);
    double det = determinant(v1, v2);
    return Math.atan2(det, dot);
  }

  public static double toAngleRadians(Vector v) {
    return toAngleRadians(new Vector(1, 0), v);
  }

  public static double toAngleDegrees(Vector v1, Vector v2) {
    return Math.toDegrees(toAngleRadians(v1, v2));
  }

  public static double toAngleDegrees(Vector v) {
    return Math.toDegrees(toAngleRadians(new Vector(1, 0), v));
  }

  public static Vector projection(Vector from, Vector to) {
    return scale(to, dotProduct(from, to) / (magnitude(to) * magnitude(to)));
  }

  public static Vector scale(Vector v, double scale) {
    double x = v.getX() * scale;
    double y = v.getY() * scale;
    double z = v.getZ() * scale;
    return new Vector(x, y, z);
  }

  public static Vector add(Vector v1, Vector v2) {
    double x = v1.getX() + v2.getX();
    double y = v1.getY() + v2.getY();
    double z = v1.getZ() + v2.getZ();
    return new Vector(x, y, z);
  }

  public static Vector subtract(Vector v1, Vector v2) {
    double x = v1.getX() - v2.getX();
    double y = v1.getY() - v2.getY();
    double z = v1.getZ() - v2.getZ();
    return new Vector(x, y, z);
  }

  public static Vector hat(Vector v) {
    double x = -v.getY();
    double y = v.getX();
    return new Vector(x, y);
  }

  public static Vector crossProduct(Vector v1, Vector v2) {
    double x = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
    double y = v1.getZ() * v2.getX() - v1.getX() * v2.getZ();
    double z = v1.getX() * v2.getY() - v1.getY() * v2.getX();
    return new Vector(x, y, z);
  }

  public static Vector fromPoints(double x1, double y1, double z1, double x2, double y2, double z2) {
    double x = x2 - x1;
    double y = y2 - y1;
    double z = z2 - z1;
    return new Vector(x, y, z);
  }

  public static Vector fromPoints(double x1, double y1, double x2, double y2) {
    return fromPoints(x1, y1, 0, x2, y2, 0);
  }
}