package com.thevoxelbox.voxelcommander.util;

import org.bukkit.command.CommandSender;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 *
 */
public class CommandSenderPrintStream extends PrintStream
{
    private CommandSender commandSender;

    /**
     *
     * @param commandSender CommandSender output will be sent to.
     */
    public CommandSenderPrintStream(CommandSender commandSender)
    {
        super(new ByteArrayOutputStream());
        this.commandSender = commandSender;
    }

    @Override
    public void flush()
    {
        Scanner scanner = new Scanner(out.toString());
        while (scanner.hasNextLine())
        {
            commandSender.sendMessage(scanner.nextLine());
        }
        out = new ByteArrayOutputStream();
        super.flush();
    }

    @Override
    public void close()
    {
        flush();
        super.close();
    }
}
