package com.creativemd.opf.block;

import com.gamerforea.eventhelper.util.EventUtils; // Babar

import com.creativemd.creativecore.common.container.SubContainer;
import com.creativemd.creativecore.common.gui.IGuiCreator;
import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.utils.CubeObject;
import com.creativemd.creativecore.core.CreativeCore;
import com.creativemd.opf.client.OPFrameClient;
import com.creativemd.opf.gui.SubContainerPic;
import com.creativemd.opf.gui.SubGuiPic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPicFrame extends BlockContainer implements IGuiCreator 
{
   public BlockPicFrame() 
   {
      super(Material.iron);
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) 
   {
      CubeObject cube = new CubeObject(0.0D, 0.0D, 0.0D, 0.05D, 1.0D, 1.0D);
      ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
      return CubeObject.rotateCube(cube, direction).getAxis().getOffsetBoundingBox((double)x, (double)y, (double)z);
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) 
   {
      TileEntity te = world.getTileEntity(x, y, z);
      if (te instanceof TileEntityPicFrame) 
      {
         return ((TileEntityPicFrame)te).getBoundingBox();
      } 
      else 
      {
         ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
         CubeObject cube = new CubeObject(0.0D, 0.0D, 0.0D, 0.05D, 1.0D, 1.0D);
         return CubeObject.rotateCube(cube, direction).getAxis().getOffsetBoundingBox((double)x, (double)y, (double)z);
      }
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) 
   {
      return Blocks.planks.getIcon(side, meta);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister registry) 
   {
   }

   @SideOnly(Side.CLIENT)
   public boolean renderAsNormalBlock() 
   {
      return false;
   }

   // Babar ==> открытие интерфейса
   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float offX, float offY, float offZ) 
   {
      if (player.isSneaking()) 
      {
         return false;
      } 
      else if (world.isRemote) 
      {
         return true;
      } 
      else 
      {
         TileEntity tileentity = world.getTileEntity(i, j, k);
         if (tileentity != null) 
         {
            // TODO gamerforEA code start
            if (EventUtils.cantBreak(player, x, y, z))
               return false;
            // TODO gamerforEA code end

            ((EntityPlayerMP)player).openGui(CreativeCore.instance, 0, world, x, y, z);
         }
      }

      return true;
   }
   // Babar <==

   public int onBlockPlaced(World world, int x, int y, int z, int side, float offX, float offY, float offZ, int meta) 
   {
      return side;
   }

   public boolean isOpaqueCube() 
   {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public int getRenderType() 
   {
      return OPFrameClient.modelID;
   }

   public boolean isNormalCube() 
   {
      return false;
   }

   public TileEntity createNewTileEntity(World world, int meta) 
   {
      return new TileEntityPicFrame();
   }

   @SideOnly(Side.CLIENT)
   public SubGui getGui(EntityPlayer player, ItemStack stack, World world, int x, int y, int z) 
   {
      TileEntity te = world.getTileEntity(x, y, z);
      return te instanceof TileEntityPicFrame ? new SubGuiPic((TileEntityPicFrame)te) : null;
   }

   public SubContainer getContainer(EntityPlayer player, ItemStack stack, World world, int x, int y, int z) 
   {
      TileEntity te = world.getTileEntity(x, y, z);
      return te instanceof TileEntityPicFrame ? new SubContainerPic((TileEntityPicFrame)te, player) : null;
   }
}