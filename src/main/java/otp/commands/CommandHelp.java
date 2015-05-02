package otp.commands;

import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp implements ISubCommand
{

    public static final String PREFIX = "\u00A7";//§
    public static final String YELLOW = PREFIX + "e";
    public static final String WHITE = PREFIX + "f";

    @Override
    public int getPermissionLevel()
    {
        return -1;
    }

    /* ISubCommand */
    @Override
    public String getCommandName()
    {
        return "help";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        switch (arguments.length)
        {
            case 1:
                StringBuilder output = new StringBuilder(StatCollector.translateToLocal("otp.command.info.help.start") + " ");
                List<String> commands = new ArrayList<String>();
                for (ISubCommand command : ParentCommand.commands.values())
                {
                    commands.add(command.getCommandName());
                }
                output.append("/otp "+ YELLOW + "<player>" + WHITE +", /otp "+ YELLOW + "<player> "+ YELLOW+"<otherplayer|here>" + WHITE +
                        ", /otp "+ YELLOW + "<player> "+ YELLOW + "<x> "+ YELLOW + "<y> "+ YELLOW + "<z> "+ YELLOW + "[dim]" + WHITE +
                        ", /otp "+ YELLOW + "<player> "+YELLOW+"<dim>" + WHITE +", ");
                for (int i = 0; i < commands.size() - 1; i++)
                {
                    output.append("/otp " + YELLOW + commands.get(i) + WHITE + ", ");
                }
                output.delete(output.length() - 2, output.length());
                output.append(" and /otp " + YELLOW + commands.get(commands.size() - 1) + WHITE + ".");
                sender.addChatMessage(new ChatComponentText(output.toString()));
                break;
            case 2:
                String commandName = arguments[1];
                if (!ParentCommand.commandExists(commandName))
                {
                    if (commandName.equals("otp"))
                    {
                        ParentCommand.teleport.printHelp(sender);
                        return;
                    }
                    throw new CommandNotFoundException("otp.command.notFound");
                }
                sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("otp.command." + commandName + ".info")));
                sender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("otp.command." + commandName + ".syntax")));
                break;
            default:
                throw new WrongUsageException("otp.command." + getCommandName() + ".syntax");
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 2)
        {
            List<String> results = ParentCommand.instance.addTabCompletionOptions(sender, new String[]{args[1]});
            if ("otp".startsWith(args[1])) results.add("otp");
            return results;
        }
        return null;
    }
}
