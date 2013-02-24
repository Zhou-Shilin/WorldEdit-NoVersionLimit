package com.sk89q.worldedit.forge;

import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldVector;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class WorldEditForgeListener {
    @ForgeSubscribe
    public void onCommandEvent(CommandEvent event) {
        if ((event.sender instanceof EntityPlayerMP)) {
            String[] split = new String[event.parameters.length + 1];
            System.arraycopy(event.parameters, 0, split, 1, event.parameters.length);
            split[0] = ("/" + event.command.getCommandName());

            WorldEditMod.inst.getWorldEdit().handleCommand(WorldEditMod.inst.wrapPlayer((EntityPlayerMP) event.sender), split);
        }
    }

    @ForgeSubscribe
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useItem == Result.DENY) {
            return;
        }

        LocalPlayer player = WorldEditMod.inst.wrapPlayer((EntityPlayerMP) event.entityPlayer);
        LocalWorld world = WorldEditMod.inst.getWorld(event.entityPlayer.worldObj);
        WorldEdit we = WorldEditMod.inst.getWorldEdit();

        PlayerInteractEvent.Action action = event.action;
        if (action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            WorldVector pos = new WorldVector(world, event.x, event.y, event.z);

            if (we.handleBlockLeftClick(player, pos)) {
                event.setCanceled(true);
            }

            if (we.handleArmSwing(player)) {
                event.setCanceled(true);
            }
        } else if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            WorldVector pos = new WorldVector(world, event.x, event.y, event.z);

            if (we.handleBlockRightClick(player, pos)) {
                event.setCanceled(true);
            }

            if (we.handleRightClick(player))
                event.setCanceled(true);
        } else if ((action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) && (we.handleRightClick(player))) {
            event.setCanceled(true);
        }
    }
}