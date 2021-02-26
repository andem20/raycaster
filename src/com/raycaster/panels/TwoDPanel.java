package com.raycaster.panels;

import com.raycaster.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TwoDPanel extends JPanel {

  private final Raycaster raycaster;
  private ArrayList<Wall> walls;
  private final Player player;
  private final double SCALE = 1;

  public TwoDPanel(int width, int height, Raycaster raycaster, ArrayList<Wall> walls, Player player) {
    setPreferredSize(new Dimension(width, height));

    setBackground(Color.DARK_GRAY);

    this.raycaster = raycaster;
    this.walls = walls;
    this.player = player;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    setRenderingHints(g2d);

    g2d.setColor(Color.WHITE);

    double x1 = player.getX() * SCALE;
    double y1 = player.getY() * SCALE;

    double width = getPreferredSize().getWidth();

    Rectangle rect = new Rectangle((int) (10 * SCALE), (int) (10 * SCALE));
    rect.x = (int) (x1 - rect.width / 2);
    rect.y = (int) (y1 - rect.height / 2);

    g2d.fill(rect);

    // Drawing player plane
    g2d.setColor(Color.RED);
    g2d.drawLine(
        (int) x1,
        (int) y1,
        (int) (x1 + player.getPlane().getX() * width),
        (int) (y1 + player.getPlane().getY() * width)
    );

    g2d.drawLine(
        (int) x1,
        (int) y1,
        (int) (x1 - player.getPlane().getX() * width),
        (int) (y1 - player.getPlane().getY() * width)
    );

    g2d.setColor(Color.WHITE);

    // Drawing walls
    for(Wall wall : walls) {
      g2d.drawLine((int) (wall.getX1() * SCALE), (int) (wall.getY1() * SCALE), (int) (wall.getX2() * SCALE), (int) (wall.getY2() * SCALE));
    }

    raycaster.calcIntersections(width);

    for(int i = 0; i < raycaster.getRays().length; i++) {
      Point2D p = new Point2D.Double(
          x1 + raycaster.getRays()[i].getX() * width,
          y1 + raycaster.getRays()[i].getY() * width
      );

      if(raycaster.getIntersections()[i] != null) p = raycaster.getIntersections()[i];

      double x2 = raycaster.getIntersections()[i].getX();
      double y2 = raycaster.getIntersections()[i].getY();

      double a = x2 - x1;
      double b = y2 - y1;
      double segLength = Math.sqrt(a * a + b * b);

      Vector projection = VectorMath.projection(raycaster.getRays()[i], player.getPlane());

      g2d.setColor(Color.RED);

      // Drawing projections to player plane
      g2d.drawLine(
          (int) (x1 + projection.getX() * segLength * SCALE),
          (int) (y1 + projection.getY() * segLength * SCALE),
          (int) (p.getX() * SCALE),
          (int) (p.getY() * SCALE)
      );

      g2d.setColor(Color.WHITE);

      // Drawing ray
      g2d.drawLine(
          (int) x1,
          (int) y1,
          (int) (p.getX() * SCALE),
          (int) (p.getY() * SCALE)
      );
    }
  }



  private void setRenderingHints(Graphics2D g2d) {
    RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHints(rh);
  }
}
