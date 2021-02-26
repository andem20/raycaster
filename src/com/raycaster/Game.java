package com.raycaster;

import com.raycaster.panels.ThreeDPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Game implements Runnable {
  private boolean running = true;

  // Settings
  private final int FPS = 60;
  private final double UPDATE_CAP = 1E9 / FPS;
  private final int VELOCITY = 5;

  // Panels
  private int WIDTH;
  private int HEIGHT;
  private JFrame frame;
//  private TwoDPanel twoDPanel;
  private ThreeDPanel threeDPanel;

  private Controls controls;
  private Raycaster raycaster;
  private ArrayList<Wall> walls;
  private Player player;

  @Override
  public void run() {
    init();

    long startTime = System.nanoTime();
    long deltaTime;
    long unprocessedTime = 0;

    int frames = 0;
    long frameTime = 0;

    long renderTime = 0;

    while(running) {
      boolean render = false;

      // Calculate passed time and unprocessed time
      deltaTime = System.nanoTime() - startTime;
      unprocessedTime += deltaTime;

      // For counting fps
      frameTime += deltaTime;

      // Catch up with unproccessed time
      if(unprocessedTime >= UPDATE_CAP) {
        render = true;
        unprocessedTime -= UPDATE_CAP;
      }

      // Render
      if(render) {
        frames++;
        long st = System.nanoTime();
        // TODO based on time instead of fps
        if(controls.isKey(KeyEvent.VK_RIGHT)) player.varyAngel(4);
        if(controls.isKey(KeyEvent.VK_LEFT)) player.varyAngel(-4);
        if(controls.isKey(KeyEvent.VK_UP)) threeDPanel.decY();
        if(controls.isKey(KeyEvent.VK_DOWN)) threeDPanel.incY();

        if(controls.isKey(KeyEvent.VK_W)) player.forward(VELOCITY);
        if(controls.isKey(KeyEvent.VK_S)) player.backward(VELOCITY);
        if(controls.isKey(KeyEvent.VK_D)) player.right(VELOCITY);
        if(controls.isKey(KeyEvent.VK_A)) player.left(VELOCITY);

        raycaster.rotateRays();

//        twoDPanel.repaint();
        threeDPanel.repaint();

        long et = System.nanoTime();

        renderTime += et - st;
      }

      // FPS
      if(frameTime >= 1E9) {
//        System.out.println(frames);
//        System.out.println((renderTime / (double) FPS) / 1E6);
        renderTime = 0;
        frameTime = 0;
        frames = 0;
      }

      // Reset startime
      startTime = System.nanoTime();
    }
  }

  private void init() {
    frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(true);
    frame.setTitle("Raycaster");
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//    WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//    HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    WIDTH = 320;
    HEIGHT = 160;

    player = new Player(WIDTH / 2.0, HEIGHT / 2.0, 0.0);
    walls = new ArrayList<>();
    createWalls(WIDTH);

    raycaster = new Raycaster(WIDTH, 60, walls, player);

    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
//    twoDPanel = new TwoDPanel(WIDTH, HEIGHT, raycaster, walls, player);
    threeDPanel = new ThreeDPanel(WIDTH, HEIGHT, raycaster, player, walls);

//    container.add(twoDPanel);
    container.add(threeDPanel);

    frame.setVisible(true);
    frame.add(container);
    frame.pack();

    controls = new Controls();

    frame.addKeyListener(controls);
  }

  private void createWalls(int width) {
    int[][] map = new int[16][16];
    map[0] = new int[]{1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1};
    map[1] = new int[]{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
    map[2] = new int[]{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
    map[3] = new int[]{1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1};
    map[4] = new int[]{1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1};
    map[5] = new int[]{1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1};
    map[6] = new int[]{1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1};
    map[7] = new int[]{1,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1};
    map[8] = new int[]{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
    map[9] = new int[]{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
    map[10] = new int[]{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
    map[11] = new int[]{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
    map[12] = new int[]{1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1};
    map[13] = new int[]{1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1};
    map[14] = new int[]{1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1};
    map[15] = new int[]{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

    int cellWidth = width / map.length;

    for(int i = 0; i < map.length; i++) {
      for(int j = 0; j < map[i].length; j++) {
        if(map[i][j] == 0) continue;
        // Top
        int x1 = j * cellWidth;
        int y1 = (i * cellWidth);
        int x2 = (j * cellWidth + cellWidth);
        int y2 = y1;
        Wall wall = new Wall(x1, y1, x2, y2);
        walls.add(wall);

        // Right
        x1 = x2;
        y2 = y1 + cellWidth;
        wall = new Wall(x1, y1, x2, y2);
        walls.add(wall);

        // Bottom
        x1 = j * cellWidth;
        y1 = y2;
        wall = new Wall(x1, y1, x2, y2);
        walls.add(wall);

        // Left
        y1 = i * cellWidth;
        x2 = x1;
        wall = new Wall(x1, y1, x2, y2);
        walls.add(wall);
      }
    }
  }
}
