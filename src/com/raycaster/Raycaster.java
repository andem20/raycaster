package com.raycaster;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Raycaster {
  private final Vector[] rays;
  private final ArrayList<Wall> walls;
  private int fieldOfView;
  private Point2D[] intersections;
  private double[] correctedDists;
  private final Player player;
  private Wall[] wallIntersecting;

  public Raycaster(int numRays, int fieldOfView, ArrayList<Wall> walls, Player player) {
    rays = new Vector[numRays];
    correctedDists = new double[numRays];
    this.walls = walls;
    this.fieldOfView = fieldOfView;
    this.player = player;
    intersections = new Point2D[numRays];

    wallIntersecting = new Wall[numRays];

    for(int i = 0; i < rays.length; i++) {
      double angle = i * fieldOfView / ((double) numRays - 1) - fieldOfView / 2.0;
      rays[i] = VectorMath.fromAngle(angle);
    }
  }

  public void rotateRays() {
    for(int i = 0; i < rays.length; i++) {
      double angle = i * fieldOfView / ((double) rays.length - 1) - fieldOfView / 2.0 + player.getAngle();
      rays[i] = VectorMath.fromAngle(angle);
    }
  }

  public void calcIntersections(double rayLength) {
    for(int i = 0; i < rays.length; i++) {
      double closest = Double.MAX_VALUE;
      Wall currentWall = null;
      double x1 = player.getX();
      double y1 = player.getY();
      double px = -1;
      double py = -1;
      for(Wall wall : walls) {
        double x2 = x1 + rays[i].getX() * rayLength;
        double y2 = y1 + rays[i].getY() * rayLength;
        double x3 = wall.getX1();
        double y3 = wall.getY1();
        double x4 = wall.getX2();
        double y4 = wall.getY2();

        double divisor = ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4));
        double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / divisor;
        double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / divisor;

        if(t >= 0 && t <= 1 && u >= 0 && u <= 1) {
          double a = x1 - (x1 + t * (x2 - x1));
          double b = y1 - (y1 + t * (y2 - y1));
          double c = Math.sqrt(a * a + b * b);
          if(c < closest) {
            currentWall = wall;
            closest = c;
            px = x1 + t * (x2 - x1);
            py = y1 + t * (y2 - y1);
          }
        }
      }

      intersections[i] = new Point2D.Double(px, py);

      double a = px - x1;
      double b = py - y1;
      double segLength = Math.sqrt(a * a + b * b);

      if(closest != Double.MAX_VALUE) {
        Vector projection = VectorMath.projection(rays[i], player.getPlane());

        double a2 = px - (x1 + projection.getX() * segLength);
        double b2 = py - (y1 + projection.getY() * segLength);

        correctedDists[i] = Math.sqrt(a2 * a2 + b2 * b2);
        wallIntersecting[i] = currentWall;
      } else {
        correctedDists[i] = -1;
      }
    }
  }

  public Vector[] getRays() {
    return rays;
  }

  public Point2D[] getIntersections() {
    return intersections;
  }

  public double[] getCorrectedDists() {
    return correctedDists;
  }

  public Wall[] getWallIntersecting() {
    return wallIntersecting;
  }
}
