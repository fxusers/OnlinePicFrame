package com.gamerforea.creativemd;

import java.util.UUID;

import com.gamerforea.eventhelper.util.FastUtils;
import com.mojang.authlib.GameProfile;

import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public final class ModUtils
{
   //public static final GameProfile profile = new GameProfile(UUID.fromString("148f2341-e3b5-4e3e-9517-41ffc8c41654"), "[GalactiCraft]");
   public static final GameProfile profile = new GameProfile(UUID.fromString("d5a1de10-c6e8-11e8-a8d5-f2801f1b9fd1"), "[OnLinePicFrame]");
   private static FakePlayer player = null;

   public static final FakePlayer getModFake(World world)
   {
      if (player == null)
         player = FastUtils.getFake(world, profile);
      else
         player.worldObj = world;

      return player;
   }
}
