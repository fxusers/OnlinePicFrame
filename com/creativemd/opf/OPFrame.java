package com.creativemd.opf;

import java.io.File;

import com.creativemd.opf.block.BlockPicFrame;
import com.creativemd.opf.block.TileEntityPicFrame;
import com.creativemd.opf.client.OPFrameClient;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

@Mod(
   modid = "opframe",
   version = "0.1",
   name = "OnlinePictureFrame"
)
public class OPFrame {
   public static final String modid = "opframe";
   public static final String version = "0.1";
   public static Block frame = (new BlockPicFrame()).setBlockName("opFrame");
   
   // Babar ==>
   public static int maxX = 100; // Размер картины по горизонтали
   public static int maxY = 100; // Размер картины по вертикали
   // Babar <==

   @SideOnly(Side.CLIENT)
   public void initClient() {
      OPFrameClient.initClient();
   }

   @EventHandler
   public void init(FMLInitializationEvent evt) {
      GameRegistry.registerBlock(frame, "opFrame");
      GameRegistry.registerTileEntity(TileEntityPicFrame.class, "OPFrameTileEntity");
      if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
         this.initClient();
      }

      // Babar ==>
      Configuration config = new Configuration(new File("mods/Config/OnlinePicFrame/opf.cfg"));
      try
      {
         config.load();
         maxX = config.get("general", "maxX", 100).getInt(100);
         maxY = config.get("general", "maxY", 100).getInt(100);
         config.save();
      }
      catch (Throwable throwable)
      {
         System.err.println("Failed load config opf.cfg. Use default values.");
         throwable.printStackTrace();
      }
      finally
      {
         config.save();
      }      
      // GameRegistry.addRecipe(new ItemStack(frame), new Object[]{"AXA", "XLX", "AXA", 'X', Blocks.planks, 'L', Items.iron_ingot, 'A', Blocks.wool});
      // Babar <==
   }
}
