package com.thevoxelbox.voxelcommander;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoxelCommander extends JavaPlugin implements Listener {

    public final String USE_PERMISSION = "voxelcommander.use";

    public List<String> bannedCommands = new ArrayList<String>();
    public HashMap<String, String> playerCommands = new HashMap<String, String>(); //Player Name, Command

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("vcom"))      {
            if(!(sender instanceof Player)){
                sender.sendMessage("Player only command!");
                return true;
            }
            String command = "";
            for (String arg : args) {
                command += arg + " ";
            }
            if(bannedCommands.contains(command)){
                sender.sendMessage("That command is not allowed, please contact an administrator if you need it.");
                return true;
            }
            playerCommands.put(sender.getName(), command);
            sender.sendMessage("Command set to -> " + command);
            sender.sendMessage("Use a blaze rod (ID 369) on the command block you wish to set!");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        final Player player = event.getPlayer();
        final Block clickedBlock = event.getClickedBlock();
        if(clickedBlock == null){
            return;
        }
        if(event.hasItem()){
            getServer().broadcastMessage("1");
            if(event.getItem().getType() == Material.BLAZE_ROD){
                getServer().broadcastMessage("2");
                if(clickedBlock.getType() == Material.COMMAND){
                    getServer().broadcastMessage("3");
                    if(player.hasPermission(USE_PERMISSION)){
                        getServer().broadcastMessage("4");
                        String command = playerCommands.get(player.getName());
                        if(command == null){
                            getServer().broadcastMessage("4.5");
                            player.sendMessage("You need to set a command with /vcom [command here]");
                            event.setCancelled(true);
                            return;
                        }
                        getServer().broadcastMessage("5");
                        CommandBlock commandBlock = (CommandBlock) clickedBlock.getState();
                        getServer().broadcastMessage("6");
                        commandBlock.setCommand("/" + command);
                        commandBlock.update();
                        getServer().broadcastMessage("7");
                        player.sendMessage("Command set to " + command);
                        getServer().broadcastMessage("annnd it equals " + commandBlock.getCommand());
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
