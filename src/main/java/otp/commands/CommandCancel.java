package otp.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.StatCollector;
import otp.persist.TeleportRegistry;

import java.util.ArrayList;
import java.util.List;

public class CommandCancel implements ISubCommand
{
    @Override
    public int getPermissionLevel()
    {
        return 3;
    }

    @Override
    public String getCommandName()
    {
        return "cancel";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        if (arguments.length == 2)
        {
            String player = arguments[1];
            if (TeleportRegistry.getPendingTP().keySet().contains(player))
                TeleportRegistry.removeTP(player);
            else
                throw new CommandException("otp.command.invalidPlayer", player);
        }
        throw new CommandException("otp.command.syntax.cancel");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        List<String> list = new ArrayList<String>();
        if (args.length == 2)
        {
            list.addAll(CommandBase.getListOfStringsFromIterableMatchingLastWord(args, TeleportRegistry.getPendingTP().keySet()));
        }
        return list;
    }
}
