package com.raycaster;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls implements KeyListener {

  private final int NUM_KEYS = 256;
  private final boolean[] keys = new boolean[NUM_KEYS];
  private final boolean[] lastKeys = new boolean[NUM_KEYS];

  @Override
  public void keyTyped(KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed(KeyEvent keyEvent) {
    keys[keyEvent.getKeyCode()] = true;
    lastKeys[keyEvent.getKeyCode()] = false;
  }

  @Override
  public void keyReleased(KeyEvent keyEvent) {
    keys[keyEvent.getKeyCode()] = false;
    lastKeys[keyEvent.getKeyCode()] = true;
  }

  public boolean isKey(int keyCode) { return keys[keyCode]; }

  public boolean lastPressed(int keyCode){
    return lastKeys[keyCode];
  }
}
