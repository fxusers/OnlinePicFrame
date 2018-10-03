package com.creativemd.opf.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.vecmath.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class DownloadThread extends Thread {
   public static HashMap loadedImages = new HashMap();
   public static HashMap loadedImagesSize = new HashMap();
   public static ArrayList loadingImages = new ArrayList();
   private String url;
   private float progress = 0.0F;
   private BufferedImage loadedImage = null;
   private static final int BYTES_PER_PIXEL = 4;

   public DownloadThread(String url) {
      this.url = url;
      this.start();
   }

   public boolean hasFinished() {
      return this.progress == 1.0F;
   }

   public boolean hasFailed() {
      return this.hasFinished() && this.loadedImage == null;
   }

   public BufferedImage getDownloadedImage() {
      return this.loadedImage;
   }

   public float getProgress() {
      return this.progress;
   }

   public void run() {
      try {
         this.loadedImage = ImageIO.read(new URL(this.url));
      } catch (Exception var2) {
         this.loadedImage = null;
         var2.printStackTrace();
      }

      this.progress = 1.0F;
   }

   public static int loadImage(DownloadThread thread) {
      if (!thread.hasFailed()) {
         BufferedImage image = thread.getDownloadedImage();
         int id = loadTexture(image);
         loadedImages.put(thread.url, id);
         loadedImagesSize.put(thread.url, new Vector2f((float)image.getWidth(), (float)image.getHeight()));
         return id;
      } else {
         return -1;
      }
   }

   public static int loadTexture(BufferedImage image) {
      int[] pixels = new int[image.getWidth() * image.getHeight()];
      image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
      ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

      int y;
      for(y = 0; y < image.getHeight(); ++y) {
         for(int x = 0; x < image.getWidth(); ++x) {
            int pixel = pixels[y * image.getWidth() + x];
            buffer.put((byte)(pixel >> 16 & 255));
            buffer.put((byte)(pixel >> 8 & 255));
            buffer.put((byte)(pixel & 255));
            buffer.put((byte)(pixel >> 24 & 255));
         }
      }

      buffer.flip();
      y = GL11.glGenTextures();
      GL11.glBindTexture(3553, y);
      GL11.glTexParameteri(3553, 10242, 33071);
      GL11.glTexParameteri(3553, 10243, 33071);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexImage2D(3553, 0, 32856, image.getWidth(), image.getHeight(), 0, 6408, 5121, buffer);
      return y;
   }
}
