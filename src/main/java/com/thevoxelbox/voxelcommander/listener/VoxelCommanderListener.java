package com.thevoxelbox.voxelcommander.listener;

import com.thevoxelbox.voxelcommander.VoxelCommander;
import com.thevoxelbox.voxelcommander.util.CommandSenderPrintStream;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 */
public class VoxelCommanderListener implements Listener
{
    VoxelCommander plugin;

    public VoxelCommanderListener(VoxelCommander plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        CommandSenderPrintStream playerMessages = new CommandSenderPrintStream(player);
        Block clickedBlock = event.getClickedBlock();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem() && event.getItem().getType() == plugin.getToolMaterial())
        {
            if (clickedBlock != null && clickedBlock.getState() != null && clickedBlock.getState() instanceof CommandBlock)
            {
                if (player.hasPermission(VoxelCommander.USE_PERMISSION))
                {
                    CommandBlock commandBlock = (CommandBlock) clickedBlock.getState();

                    commandBlock.setCommand(plugin.getCommand(player));
                    commandBlock.update();

                    playerMessages.println("Command on Command Block set to \"" + plugin.getCommand(player) + "\"");
                    event.setCancelled(true);
                }
            }
        }

        playerMessages.close();
    }
}
