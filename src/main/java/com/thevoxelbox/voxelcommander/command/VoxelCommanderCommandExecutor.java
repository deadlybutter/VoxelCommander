package com.thevoxelbox.voxelcommander.command;

import com.google.common.base.Joiner;
import com.thevoxelbox.voxelcommander.VoxelCommander;
import com.thevoxelbox.voxelcommander.util.ArgumentParser;
import com.thevoxelbox.voxelcommander.util.CommandSenderPrintStream;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kohsuke.args4j.Argument;

/**
 *
 */
public class VoxelCommanderCommandExecutor implements CommandExecutor
{
    VoxelCommander plugin;

    public VoxelCommanderCommandExecutor(VoxelCommander plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            return false;
        }
        Player player = (Player) sender;

        VoxelCommanderCommandOptions options = new VoxelCommanderCommandOptions();
        CommandSenderPrintStream playerMessages = new CommandSenderPrintStream(sender);
        if (!ArgumentParser.parse(options, args, playerMessages, label))
        {
            if (options.commands != null)
            {
                boolean isCorrect = true;
                if (options.commands.length > 0 && plugin.getBannedCommands().contains(options.commands[0].toLowerCase()))
                {
                    playerMessages.println("\"" + options.commands[0] + "\" is not allowed for usage!");
                    isCorrect = false;
                }

                if (isCorrect)
                {
                    plugin.setPlayerCommand(player, Joiner.on(' ').skipNulls().join(options.commands));
                    playerMessages.println("Command set to \"" + plugin.getCommand(player) + "\"");
                    playerMessages.println("You can now use " + plugin.getToolMaterial().name() + " to apply command to Command Blocks.");
                }
            }
        }
        playerMessages.close();
        return true;
    }

    /**
     * Argument parsing options container.
     */
    private class VoxelCommanderCommandOptions
    {
        @Argument(usage = "Command to be set using the VoxelCommander tool.", metaVar = "<command>", multiValued = true)
        String[] commands = { };
    }
}
