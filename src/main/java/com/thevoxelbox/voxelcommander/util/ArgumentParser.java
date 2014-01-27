package com.thevoxelbox.voxelcommander.util;

import org.bukkit.ChatColor;
import org.bukkit.util.ChatPaginator;
import org.kohsuke.args4j.ClassParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.OptionHandler;

import java.io.PrintStream;
import java.util.List;

/**
 *
 */
public class ArgumentParser
{
    private ArgumentParser()
    {
    }

    public static boolean parse(Object bean, String[] args, PrintStream outputStream, Object... additionalBeans)
    {
        return parse(bean, args, outputStream, "COMMAND", additionalBeans);
    }

    /**
     * Parses arguments supplied into supplied POJO and writes messages generated by this Method into the supplied output stream.
     *
     * @param bean         POJO bean arguments are getting parsed into.
     * @param args         Arguments.
     * @param outputStream Stream to write messages from the parsing process into.
     * @return true if parsing was used for internal processing.
     */
    public static boolean parse(Object bean, String[] args, PrintStream outputStream, String commandName, Object... additionalBeans)
    {
        CmdLineParser parser = new CmdLineParser(bean);
        parser.setUsageWidth(ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
        ParserHelpBean helpBean = new ParserHelpBean();
        new ClassParser().parse(helpBean, parser);
        for (Object additonalBean : additionalBeans)
        {
            new ClassParser().parse(additonalBean, parser);
        }
        try
        {
            parser.parseArgument(args);
            if (helpBean.help != null && helpBean.help)
            {
                List<OptionHandler> options = parser.getOptions();
                List<OptionHandler> arguments = parser.getArguments();

                outputStream.print("/" + commandName);

                for (OptionHandler handler : options)
                {
                    printSingleLineOption(handler, outputStream);
                }

                for (OptionHandler handler : arguments)
                {
                    printSingleLineOption(handler, outputStream);
                }

                outputStream.println();

                for (OptionHandler handler : options)
                {
                    printOption(handler, outputStream);
                }

                for (OptionHandler handler : arguments)
                {
                    printOption(handler, outputStream);
                }

                return true;
            }
        }
        catch (CmdLineException e)
        {
            outputStream.println(ChatColor.RED + "[ERROR]" + ChatColor.RESET + " " + e.getMessage());
            return true;
        }
        return false;
    }

    private static void printOption(OptionHandler handler, PrintStream outputStream)
    {
        if (handler.option.usage() == null || handler.option.usage().isEmpty() || handler.option.hidden())
        {
            return;
        }

        outputStream.print(ChatColor.GOLD);
        outputStream.print(handler.getNameAndMeta(null));
        outputStream.print(ChatColor.RESET);
        outputStream.print(" : ");
        outputStream.print(handler.option.usage());
        outputStream.println();
    }

    private static void printSingleLineOption(OptionHandler handler, PrintStream outputStream)
    {
        outputStream.print(' ');
        boolean optional = !handler.option.required() && !handler.option.hidden();
        if (optional)
        {
            outputStream.print("[");
        }
        outputStream.print(handler.getNameAndMeta(null));
        if (handler.option.isMultiValued())
        {
            outputStream.print("...");
        }
        if (optional)
        {
            outputStream.print("]");
        }
    }

    private static class ParserHelpBean
    {
        @Option(name = "--help", usage = "Display help information.", required = false)
        Boolean help = null;
    }
}