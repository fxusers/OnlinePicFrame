package com.creativemd.opf.gui;

import com.creativemd.creativecore.common.gui.SubGui;
import com.creativemd.creativecore.common.gui.controls.GuiButton;
import com.creativemd.creativecore.common.gui.controls.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.GuiSteppedSlider;
import com.creativemd.creativecore.common.gui.controls.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.ControlClickEvent;
import com.creativemd.opf.block.TileEntityPicFrame;
import com.creativemd.opf.client.DownloadThread;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import javax.vecmath.Vector2f;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTTagCompound;

@SideOnly(Side.CLIENT)
public class SubGuiPic extends SubGui {
   public TileEntityPicFrame frame;

   public SubGuiPic(TileEntityPicFrame frame) {
      super(200, 200);
      this.frame = frame;
   }

   public void createControls() {
      GuiTextfield url = new GuiTextfield("url", this.frame.url, 5, 5, 164, 20);
      url.maxLength = 512;
      this.controls.add(url);
      this.controls.add((new GuiTextfield("sizeX", this.frame.sizeX + "", 5, 30, 40, 20)).setFloatOnly());
      this.controls.add((new GuiTextfield("sizeY", this.frame.sizeY + "", 50, 30, 40, 20)).setFloatOnly());
      this.controls.add(new GuiButton("reX", "x->y", 95, 30, 50));
      this.controls.add(new GuiButton("reY", "y->x", 145, 30, 50));
      this.controls.add(new GuiCheckBox("flipX", "flip (x-axis)", 5, 50, this.frame.flippedX));
      this.controls.add(new GuiCheckBox("flipY", "flip (y-axis)", 80, 50, this.frame.flippedY));
      this.controls.add(new GuiStateButton("posX", this.frame.posX, 5, 70, 70, 20, new String[]{"left (x)", "center (x)", "right (x)"}));
      this.controls.add(new GuiStateButton("posY", this.frame.posY, 80, 70, 70, 20, new String[]{"left (y)", "center (y)", "right (y)"}));
      this.controls.add(new GuiStateButton("rotation", this.frame.rotation, 5, 100, 80, 20, new String[]{"rotation: 0", "rotation: 1", "rotation: 2", "rotation: 3"}));
      this.controls.add(new GuiCheckBox("visibleFrame", "visible Frame", 90, 105, this.frame.visibleFrame));
      this.controls.add(new GuiLabel("render distance (blocks):", 5, 125));
      this.controls.add(new GuiSteppedSlider("renderDistance", 5, 140, 100, 20, 5, 1024, this.frame.renderDistance));
      this.controls.add(new GuiButton("Save", 120, 140, 50));
   }

   @CustomEventSubscribe
   public void onClicked(ControlClickEvent event) {
      GuiTextfield sizeYField;
      if (event.source.is("reX") || event.source.is("reY")) {
         GuiTextfield sizeXField = (GuiTextfield)this.getControl("sizeX");
         sizeYField = (GuiTextfield)this.getControl("sizeY");
         float x = 1.0F;

         try {
            x = Float.parseFloat(sizeXField.text);
         } catch (Exception var19) {
            x = 1.0F;
         }

         float y = 1.0F;

         try {
            y = Float.parseFloat(sizeYField.text);
         } catch (Exception var18) {
            y = 1.0F;
         }

         Vector2f size = (Vector2f)DownloadThread.loadedImagesSize.get(this.frame.url);
         if (size != null) {
            if (event.source.is("reX")) {
               sizeYField.text = "" + size.y / (size.x / x);
            } else {
               sizeXField.text = "" + size.x / (size.y / y);
            }
         }
      }

      if (event.source.is("Save")) {
         NBTTagCompound nbt = new NBTTagCompound();
         sizeYField = (GuiTextfield)this.getControl("url");
         GuiTextfield sizeX = (GuiTextfield)this.getControl("sizeX");
         GuiTextfield sizeY = (GuiTextfield)this.getControl("sizeY");
         GuiStateButton buttonPosX = (GuiStateButton)this.getControl("posX");
         GuiStateButton buttonPosY = (GuiStateButton)this.getControl("posY");
         GuiStateButton rotation = (GuiStateButton)this.getControl("rotation");
         GuiCheckBox flipX = (GuiCheckBox)this.getControl("flipX");
         GuiCheckBox flipY = (GuiCheckBox)this.getControl("flipY");
         GuiCheckBox visibleFrame = (GuiCheckBox)this.getControl("visibleFrame");
         GuiSteppedSlider renderDistance = (GuiSteppedSlider)this.getControl("renderDistance");
         nbt.setByte("posX", (byte)buttonPosX.getState());
         nbt.setByte("posY", (byte)buttonPosY.getState());
         nbt.setByte("rotation", (byte)rotation.getState());
         nbt.setBoolean("flippedX", flipX.value);
         nbt.setBoolean("flippedY", flipY.value);
         nbt.setBoolean("visibleFrame", visibleFrame.value);
         nbt.setInteger("render", (int)renderDistance.value);
         nbt.setString("url", sizeYField.text);
         float x = 1.0F;
         float y = 1.0F;

         try {
            x = Float.parseFloat(sizeX.text);
         } catch (Exception var17) {
            x = 1.0F;
         }

         x = Math.min(x, 50.0F);

         try {
            y = Float.parseFloat(sizeY.text);
         } catch (Exception var16) {
            y = 1.0F;
         }

         y = Math.min(y, 50.0F);
         nbt.setFloat("x", x);
         nbt.setFloat("y", y);
         this.sendPacketToServer(0, nbt);
      }

   }

   public void drawOverlay(FontRenderer fontRenderer) {
   }
}
