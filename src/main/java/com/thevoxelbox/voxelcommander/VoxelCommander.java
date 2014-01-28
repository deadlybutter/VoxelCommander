package com.thevoxelbox.voxelcommander;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.thevoxelbox.voxelcommander.command.VoxelCommanderCommandExecutor;
import com.thevoxelbox.voxelcommander.listener.VoxelCommanderListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VoxelCommander extends JavaPlugin implements Listener
{

    public static final String USE_PERMISSION = "voxelcommander.use";

    private List<String> bannedCommands = new ArrayList<>();
    private HashMap<Player, String> playerCommands = new HashMap<>();
    private Material toolMaterial;

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();
        bannedCommands = (List<String>) getConfig().getList("banned-commands");
        toolMaterial = Material.getMaterial(getConfig().getString("tool-material"));
        Preconditions.checkNotNull(bannedCommands, "Banned command List must not be null.");
        Preconditions.checkNotNull(toolMaterial, "Tool Material must be defined.");

        getCommand("voxelcommander").setExecutor(new VoxelCommanderCommandExecutor(this));

        Bukkit.getPluginManager().registerEvents(new VoxelCommanderListener(this), this);
    }

    public List<String> getBannedCommands()
    {
        return ImmutableList.copyOf(bannedCommands);
    }

    public void setPlayerCommand(Player player, String command)
    {
        playerCommands.put(player, command);
    }

    public String getCommand(Player player)
    {
        return Optional.fromNullable(playerCommands.get(player)).or("");
    }

    public Material getToolMaterial()
    {
        return toolMaterial;
    }
}
