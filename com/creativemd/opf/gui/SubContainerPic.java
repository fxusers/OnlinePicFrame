package com.creativemd.opf.gui;

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.opf.block.TileEntityPicFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class SubContainerPic extends SubContainer {
   public TileEntityPicFrame frame;

   public SubContainerPic(TileEntityPicFrame frame, EntityPlayer player) {
      super(player);
      this.frame = frame;
   }

   public void createControls() {
   }

   public void onGuiPacket(int controlID, NBTTagCompound nbt, EntityPlayer player) {
      if (controlID == 0) {
         this.frame.url = nbt.getString("url");
         this.frame.sizeX = Math.min(nbt.getFloat("x"), OPFrame.maxX);
         this.frame.sizeY = Math.min(nbt.getFloat("y"), OPFrame.maxY);
         this.frame.renderDistance = nbt.getInteger("render");
         this.frame.posX = nbt.getByte("posX");
         this.frame.posY = nbt.getByte("posY");
         this.frame.rotation = nbt.getByte("rotation");
         this.frame.visibleFrame = nbt.getBoolean("visibleFrame");
         this.frame.flippedX = nbt.getBoolean("flippedX");
         this.frame.flippedY = nbt.getBoolean("flippedY");
         this.frame.updateBlock();
      }

   }
}
