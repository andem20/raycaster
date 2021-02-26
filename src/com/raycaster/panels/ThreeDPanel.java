package com.raycaster.panels;

import com.raycaster.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;

public class ThreeDPanel extends JPanel {
  
  private final Raycaster raycaster;
  private final int WALL_HEIGHT = 100;
  private final double MAP_SCALE = .5;
  private final Player player;
  private final ArrayList<Wall> walls;
  // TODO fix y camera position
  private double y = 2.0;
  private final BufferedImage texture;
  private final BufferedImage sky;
  private final int WINDOW_SCALE = 4;

  public ThreeDPanel(int width, int height, Raycaster raycaster, Player player, ArrayList<Wall> walls) {
    this.raycaster = raycaster;
    this.player = player;
    this.walls = walls;

    texture = new ImageLoader("src/resources/bricks.jpg").getBufferedImage();
    sky = new ImageLoader("src/resources/sky.jpg").getBufferedImage();

    setPreferredSize(new Dimension(width * WINDOW_SCALE, height * WINDOW_SCALE));
    setBackground(Color.BLACK);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    Graphics2D g2d = (Graphics2D) g;

    setRenderingHints(g2d);

    double[] dists = raycaster.getCorrectedDists();

    int height = getPreferredSize().height;
    int width = getPreferredSize().width;
    int factor = (int) ((double) width / (double) dists.length);

    raycaster.calcIntersections(width);

    // Drawing sky
    Color c1 = new Color(140, 198, 224);
    Color c2 = new Color(14, 64, 87);
    GradientPaint gp = new GradientPaint(0, 0, c1, 0, (int) (height / y), c2);
    g2d.setPaint(gp);
    g2d.fillRect(0, 0, width, (int) (height / y));
//    g2d.drawImage(sky, 0, 0, width, (int) (height / y), null);

    // Drawing floor
    Color c3 = new Color(111, 82, 31);
    Color c4 = new Color(0, 0, 0);
    GradientPaint gp2 = new GradientPaint(0, (int) (height / y), c4, 0, height, c3);
    g2d.setPaint(gp2);
    g2d.fillRect(0, (int) (height / y), width, (int) (height * y));

    // Drawing walls
    for(int i = 0; i < dists.length; i++) {
      if(dists[i] < 0) continue;

      int color = (int) Math.max(255 - 255 * (dists[i] / height), 0);

      // drawing corner
      if(i != 0 && raycaster.getWallIntersecting()[i-1] != raycaster.getWallIntersecting()[i]) {
        color /= 1.5;
      }

      // Drawing wall
//      g2d.setColor(new Color((int) (40 * (color / 255.0)), (int) (132 * (color / 255.0)), (int) (49 * (color / 255.0))));
//      g2d.setColor(new Color((int) (i * (255.0 / dists.length)), (int) (i * (255.0 / dists.length)), (int) (i * (255.0 / dists.length))));
//      g2d.fillRect((int) (i * factor), (int) ((height - (WALL_HEIGHT * height / dists[i])) / y), (int) factor, (int) (WALL_HEIGHT * height / dists[i]));
      // TODO optimize
      Point2D pInter = raycaster.getIntersections()[i];
      int wallHeight = raycaster.getWallIntersecting()[i].getY2() - raycaster.getWallIntersecting()[i].getY1();
      int wallWidth = raycaster.getWallIntersecting()[i].getX2() - raycaster.getWallIntersecting()[i].getX1();
      int size = Math.max(wallHeight, wallWidth);

      double interX = Math.round(pInter.getX()) % size;
      double interY = Math.round(pInter.getY()) % size;

      double offset = (interX > interY ? pInter.getX() : pInter.getY()) % size;
      int textureWidth = texture.getWidth() / size;

      BufferedImage subImage = texture.getSubimage((int) (offset * textureWidth), 0, 1, texture.getHeight());

      float scaleFactor = 1.2f - (float) (dists[i] / height);
      if(walls.indexOf(raycaster.getWallIntersecting()[i]) % 4 == 0 || (walls.indexOf(raycaster.getWallIntersecting()[i]) + 1) % 4 == 0){
        scaleFactor *= 0.7;
      }

      RescaleOp op = new RescaleOp(scaleFactor, 0, null);

      subImage = op.filter(subImage, null);

      g2d.drawImage(subImage, i * factor, (int) ((height - (WALL_HEIGHT * height / dists[i])) / y), factor, (int) (WALL_HEIGHT * height / dists[i]), null);
    }

    // MAP RENDERING
    renderMap(g2d);
  }

  private void setRenderingHints(Graphics2D g2d) {
    RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHints(rh);
  }

  private void renderMap(Graphics2D g2d) {
    int width = getPreferredSize().width;
    double x1 = player.getX() * MAP_SCALE;
    double y1 = player.getY() * MAP_SCALE;

    g2d.setColor(Color.WHITE);
    Rectangle rect = new Rectangle(10, 10);
    rect.x = (int) (x1 - rect.width / 2);
    rect.y = (int) (y1 - rect.height / 2);

    g2d.fill(rect);

    // Drawing player plane
//    g2d.setColor(Color.RED);
//    g2d.drawLine(
//        (int) x1,
//        (int) y1,
//        (int) (x1 + player.getPlane().getX() * width),
//        (int) (y1 + player.getPlane().getY() * width)
//    );
//
//    g2d.drawLine(
//        (int) x1,
//        (int) y1,
//        (int) (x1 - player.getPlane().getX() * width),
//        (int) (y1 - player.getPlane().getY() * width)
//    );

    g2d.setColor(Color.WHITE);

    // Drawing walls
    for(Wall wall : walls) {
      g2d.drawLine((int) (wall.getX1() * MAP_SCALE), (int) (wall.getY1() * MAP_SCALE), (int) (wall.getX2() * MAP_SCALE), (int) (wall.getY2() * MAP_SCALE));
    }

    for(int i = 0; i < raycaster.getRays().length; i++) {
      Point2D p = new Point2D.Double(
          (raycaster.getRays()[i].getX() * width),
          (raycaster.getRays()[i].getY() * width)
      );

      boolean intersect = raycaster.getCorrectedDists()[i] >= 0;
      if (intersect) p = raycaster.getIntersections()[i];
//
//      double x2 = raycaster.getIntersections()[i].getX();
//      double y2 = raycaster.getIntersections()[i].getY();
//
//      double a = x2 - x1;
//      double b = y2 - y1;
//      double segLength = Math.sqrt(a * a + b * b);
//
//      Vector projection = VectorMath.projection(raycaster.getRays()[i], player.getPlane());
//
//      g2d.setColor(Color.RED);
//
////    Drawing projections to player plane
//      // TODO fix scaling
//      g2d.drawLine(
//          (int) (x1 + projection.getX() * segLength * MAP_SCALE),
//          (int) (y1 + projection.getY() * segLength * MAP_SCALE),
//          (int) (p.getX() * MAP_SCALE),
//          (int) (p.getY() * MAP_SCALE)
//      );

      g2d.setColor(Color.WHITE);

      // Drawing ray
      g2d.drawLine(
          (int) x1,
          (int) y1,
          (int) ((!intersect ? x1 : 0) + p.getX() * MAP_SCALE),
          (int) ((!intersect ? y1 : 0) + p.getY() * MAP_SCALE)
      );
    }
  }

  public void incY() {
    y += 0.1;
  }

  public void decY() {
    y -= 0.1;
  }
}