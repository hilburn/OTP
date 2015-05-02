package otp.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import otp.persist.TeleportRegistry;

import java.util.List;

public class CommandRemove implements ISubCommand
{
    @Override
    public int getPermissionLevel()
    {
        return 3;
    }

    @Override
    public String getCommandName()
    {
        return "remove";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        if (arguments.length == 2)
        {
            String player = arguments[1];
            if (TeleportRegistry.getAllNames().contains(player))
            {
                TeleportRegistry.removeName(player);
                TeleportRegistry.removeTP(player);
                TeleportRegistry.removeLogOut(player);
                sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted("otp.command.removeSuccess", player)));
                return;
            }
            throw new CommandException("otp.command.invalidPlayer", player);
        }
        throw new WrongUsageException("otp.commands.remove.syntax");
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return TeleportRegistry.getMatchingNames(args[1]);
    }
}
