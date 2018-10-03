package com.creativemd.opf.block;

import com.creativemd.creativecore.common.tileentity.TileEntityCreative;
import com.creativemd.creativecore.common.utils.CubeObject;
import com.creativemd.opf.client.DownloadThread;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityPicFrame extends TileEntityCreative 
{
   @SideOnly(Side.CLIENT)
   public DownloadThread downloader;
   @SideOnly(Side.CLIENT)
   public int textureID;
   @SideOnly(Side.CLIENT)
   public boolean failed;
   public int renderDistance = 512;
   public String url = "";
   public float sizeX = 1.0F;
   public float sizeY = 1.0F;
   public boolean flippedX;
   public boolean flippedY;
   public byte rotation = 0;
   public byte posX = 0;
   public byte posY = 0;
   public boolean visibleFrame = true;

   public TileEntityPicFrame() 
   {
      if (FMLCommonHandler.instance().getEffectiveSide().isClient()) 
      {
         this.initClient();
      }
   }

   @SideOnly(Side.CLIENT)
   public void initClient() 
   {
      this.textureID = -1;
      this.failed = false;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldLoadTexture() 
   {
      return !this.isTextureLoaded() && !this.failed;
   }

   @SideOnly(Side.CLIENT)
   public void loadTexutre() 
   {
      if (this.shouldLoadTexture()) 
      {
         if (this.downloader == null) 
         {
            Integer id = (Integer)DownloadThread.loadedImages.get(this.url);
            if (id == null) 
            {
               if (!DownloadThread.loadingImages.contains(this.url)) 
               {
                  DownloadThread.loadingImages.add(this.url);
                  this.downloader = new DownloadThread(this.url);
               }
            } 
            else 
            {
               this.textureID = id;
            }
         }

         if (this.downloader != null && this.downloader.hasFinished()) 
         {
            if (this.downloader.hasFailed()) 
            {
               this.failed = true;
            } 
            else 
            {
               this.textureID = DownloadThread.loadImage(this.downloader);
            }

            DownloadThread.loadingImages.remove(this.url);
            this.downloader = null;
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public boolean isTextureLoaded() 
   {
      return this.textureID != -1;
   }

   @SideOnly(Side.CLIENT)
   public double func_145833_n() 
   {
      return Math.pow((double)this.renderDistance, 2.0D);
   }

   public AxisAlignedBB getBoundingBox() 
   {
      CubeObject cube = new CubeObject(0.0D, 0.0D, 0.0D, 0.05D, 1.0D, 1.0D);
      float sizeX = this.sizeX;
      float sizeY = this.sizeY;
      double offsetX = 0.0D;
      double offsetY = 0.0D;
      switch(this.rotation) {
      case 1:
         sizeX = this.sizeY;
         sizeY = -this.sizeX;
         if (this.posY == 0) {
            ++offsetY;
         } else if (this.posY == 2) {
            --offsetY;
         }
         break;
      case 2:
         sizeX = -this.sizeX;
         sizeY = -this.sizeY;
         if (this.posX == 0) {
            ++offsetX;
         } else if (this.posX == 2) {
            --offsetX;
         }

         if (this.posY == 0) {
            ++offsetY;
         } else if (this.posY == 2) {
            --offsetY;
         }
         break;
      case 3:
         sizeX = -this.sizeY;
         sizeY = this.sizeX;
         if (this.posX == 0) {
            ++offsetX;
         } else if (this.posX == 2) {
            --offsetX;
         }
      }

      if (this.posX == 1) {
         offsetX += (double)(-sizeX + 1.0F) / 2.0D;
      } else if (this.posX == 2) {
         offsetX += (double)(-sizeX + 1.0F);
      }

      if (this.posY == 1) {
         offsetY += (double)(-sizeY + 1.0F) / 2.0D;
      } else if (this.posY == 2) {
         offsetY += (double)(-sizeY + 1.0F);
      }

      ForgeDirection direction = ForgeDirection.getOrientation(this.func_145832_p());
      if (direction == ForgeDirection.UP) {
         cube.minZ -= (double)(sizeX - 1.0F);
         cube.minY -= (double)(sizeY - 1.0F);
         cube.minZ -= offsetX;
         cube.maxZ -= offsetX;
         cube.minY -= offsetY;
         cube.maxY -= offsetY;
      } else {
         cube.maxZ += (double)(sizeX - 1.0F);
         cube.maxY += (double)(sizeY - 1.0F);
         cube.minZ += offsetX;
         cube.maxZ += offsetX;
         cube.minY += offsetY;
         cube.maxY += offsetY;
      }

      cube = new CubeObject(Math.min(cube.minX, cube.maxX), Math.min(cube.minY, cube.maxY), Math.min(cube.minZ, cube.maxZ), Math.max(cube.minX, cube.maxX), Math.max(cube.minY, cube.maxY), Math.max(cube.minZ, cube.maxZ));
      return CubeObject.rotateCube(cube, direction).getAxis().getOffsetBoundingBox((double)this.field_145851_c, (double)this.field_145848_d, (double)this.field_145849_e);
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getRenderBoundingBox() {
      return this.getBoundingBox();
   }

   public void func_145841_b(NBTTagCompound nbt) {
      super.func_145841_b(nbt);
      nbt.setString("url", this.url);
      nbt.setFloat("sizeX", this.sizeX);
      nbt.setFloat("sizeY", this.sizeY);
      nbt.setInteger("render", this.renderDistance);
      nbt.setByte("offsetX", this.posX);
      nbt.setByte("offsetY", this.posY);
      nbt.setByte("rotation", this.rotation);
      nbt.setBoolean("visibleFrame", this.visibleFrame);
      nbt.setBoolean("flippedX", this.flippedX);
      nbt.setBoolean("flippedY", this.flippedY);
   }

   public void func_145839_a(NBTTagCompound nbt) {
      super.func_145839_a(nbt);
      this.url = nbt.getString("url");
      this.sizeX = nbt.getFloat("sizeX");
      this.sizeY = nbt.getFloat("sizeY");
      this.renderDistance = nbt.getInteger("render");
      this.posX = nbt.getByte("offsetX");
      this.posY = nbt.getByte("offsetY");
      this.rotation = nbt.getByte("rotation");
      this.visibleFrame = nbt.getBoolean("visibleFrame");
      this.flippedX = nbt.getBoolean("flippedX");
      this.flippedY = nbt.getBoolean("flippedY");
   }

   public void getDescriptionNBT(NBTTagCompound nbt) {
      super.getDescriptionNBT(nbt);
      nbt.setString("url", this.url);
      nbt.setFloat("sizeX", this.sizeX);
      nbt.setFloat("sizeY", this.sizeY);
      nbt.setInteger("render", this.renderDistance);
      nbt.setByte("offsetX", this.posX);
      nbt.setByte("offsetY", this.posY);
      nbt.setByte("rotation", this.rotation);
      nbt.setBoolean("visibleFrame", this.visibleFrame);
      nbt.setBoolean("flippedX", this.flippedX);
      nbt.setBoolean("flippedY", this.flippedY);
   }

   @SideOnly(Side.CLIENT)
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
      super.onDataPacket(net, pkt);
      this.url = pkt.func_148857_g().getString("url");
      this.sizeX = pkt.func_148857_g().getFloat("sizeX");
      this.sizeY = pkt.func_148857_g().getFloat("sizeY");
      this.renderDistance = pkt.func_148857_g().getInteger("render");
      this.posX = pkt.func_148857_g().getByte("offsetX");
      this.posY = pkt.func_148857_g().getByte("offsetY");
      this.rotation = pkt.func_148857_g().getByte("rotation");
      this.visibleFrame = pkt.func_148857_g().getBoolean("visibleFrame");
      this.flippedX = pkt.func_148857_g().getBoolean("flippedX");
      this.flippedY = pkt.func_148857_g().getBoolean("flippedY");
      this.initClient();
      this.updateRender();
   }
}
