package com.raycaster;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

  private BufferedImage bufferedImage;

  public ImageLoader(String path) {
    File imageFile = new File(path);

    try {
      bufferedImage = ImageIO.read(imageFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void print() {
    for(int y = 0; y < bufferedImage.getHeight(); y++) {
      for(int x = 0; x < bufferedImage.getWidth(); x++) {
        Color color = new Color(bufferedImage.getRGB(x, y));
        System.out.println(color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
      }
    }
  }

  public BufferedImage getBufferedImage() {
    return bufferedImage;
  }
}
