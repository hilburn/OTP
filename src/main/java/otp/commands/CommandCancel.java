package otp.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
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
                throw new CommandException(player + " is not a valid player");
        }
        throw new WrongUsageException(ParentCommand.textMap.get("cancel.syntax"));
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
