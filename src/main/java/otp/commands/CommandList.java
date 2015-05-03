package otp.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import otp.persist.TeleportRegistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandList implements ISubCommand
{
    @Override
    public int getPermissionLevel()
    {
        return 3;
    }

    @Override
    public String getCommandName()
    {
        return "list";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        String list = null;
        if (arguments.length == 1)
            list = "tp";
        else if (arguments.length == 2)
        {
            list = arguments[1];
        }
        if (list != null)
        {
            if (list.equals("tp"))
                list(sender, TeleportRegistry.getPendingTP().values(), list);
            else if (list.equals("logouts"))
                list(sender, TeleportRegistry.getSavedLogOuts().values(), list);
            else if (list.equals("players"))
                list(sender, TeleportRegistry.getAllNames(), list);
            else
                throw new WrongUsageException(ParentCommand.textMap.get("list.syntax"));
            return;
        }
        throw new WrongUsageException(ParentCommand.textMap.get("list.syntax"));
    }

    public void list(ICommandSender sender, Collection collection, String loc)
    {
        sender.addChatMessage(new ChatComponentText(ParentCommand.textMap.get("list." + loc)));
        for (Object o : collection) sender.addChatMessage(new ChatComponentText(o.toString()));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return CommandBase.getListOfStringsFromIterableMatchingLastWord(args, Arrays.asList("otp", "logouts", "players"));
    }
}
