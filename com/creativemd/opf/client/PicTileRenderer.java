package com.creativemd.opf.client;

import com.creativemd.creativecore.client.rendering.RenderHelper3D;
import com.creativemd.opf.block.TileEntityPicFrame;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class PicTileRenderer extends TileEntitySpecialRenderer {
   public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
      if (te instanceof TileEntityPicFrame) {
         TileEntityPicFrame frame = (TileEntityPicFrame)te;
         if (!frame.url.equals("")) {
            if (frame.isTextureLoaded()) {
               float sizeX = frame.sizeX;
               float sizeY = frame.sizeY;
               GL11.glEnable(3042);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               GL11.glDisable(2896);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glEnable(3553);
               GL11.glBindTexture(3553, frame.textureID);
               GL11.glTexParameteri(3553, 10241, 9728);
               GL11.glTexParameteri(3553, 10240, 9728);
               GL11.glPushMatrix();
               GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
               ForgeDirection direction = ForgeDirection.getOrientation(frame.func_145832_p());
               RenderHelper3D.applyDirection(direction);
               if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) {
                  GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
               }

               double posX = -0.5D + (double)sizeX / 2.0D;
               if (frame.posX == 1) {
                  posX = 0.0D;
               } else if (frame.posX == 2) {
                  posX = -posX;
               }

               double posY = -0.5D + (double)sizeY / 2.0D;
               if (frame.posY == 1) {
                  posY = 0.0D;
               } else if (frame.posY == 2) {
                  posY = -posY;
               }

               if ((frame.rotation == 1 || frame.rotation == 3) && frame.posX == 2 ^ frame.posY == 2) {
                  GL11.glRotated(180.0D, 1.0D, 0.0D, 0.0D);
               }

               GL11.glRotated((double)(frame.rotation * 90), 1.0D, 0.0D, 0.0D);
               GL11.glTranslated(-0.945D, posY, posX);
               GL11.glEnable(32826);
               GL11.glScaled(1.0D, (double)frame.sizeY, (double)frame.sizeX);
               GL11.glBegin(9);
               GL11.glNormal3f(1.0F, 0.0F, 0.0F);
               GL11.glTexCoord3f(frame.flippedY ? 0.0F : 1.0F, frame.flippedX ? 0.0F : 1.0F, 0.0F);
               GL11.glVertex3f(0.5F, -0.5F, -0.5F);
               GL11.glTexCoord3f(frame.flippedY ? 0.0F : 1.0F, frame.flippedX ? 1.0F : 0.0F, 0.0F);
               GL11.glVertex3f(0.5F, 0.5F, -0.5F);
               GL11.glTexCoord3f(frame.flippedY ? 1.0F : 0.0F, frame.flippedX ? 1.0F : 0.0F, 0.0F);
               GL11.glVertex3f(0.5F, 0.5F, 0.5F);
               GL11.glTexCoord3f(frame.flippedY ? 1.0F : 0.0F, frame.flippedX ? 0.0F : 1.0F, 0.0F);
               GL11.glVertex3f(0.5F, -0.5F, 0.5F);
               GL11.glEnd();
               GL11.glPopMatrix();
               GL11.glDisable(3042);
               GL11.glEnable(2896);
            } else {
               frame.loadTexutre();
            }
         }
      }

   }
}
