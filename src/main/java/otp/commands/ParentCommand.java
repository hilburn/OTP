package otp.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import otp.persist.TeleportRegistry;

import java.util.*;
import java.util.regex.Pattern;

public class ParentCommand extends CommandBase
{
    public static Map<String, ISubCommand> commands = new LinkedHashMap<String, ISubCommand>();
    public static ParentCommand instance = new ParentCommand();
    public static CommandOTP teleport = new CommandOTP();
    public static Map<String, String> textMap = new HashMap<String, String>();

    static
    {
        register(new CommandHelp());
        register(new CommandCancel());
        register(new CommandList());
        register(new CommandCancel());
        register(new CommandRemove());

        textMap.put("info.help.start", "Available commands are:");
        textMap.put("help.syntax", "/otp help [command]");
        textMap.put("help.info", "For help with all available commands.");
        textMap.put("clear.syntax", "/otp clear [tp|logouts|names]");
        textMap.put("clear.info", "Clears the selected list - default is tp.");
        textMap.put("list.syntax", "/otp list [tp|logouts|names]");
        textMap.put("list.info", "Lists every stored value in selected list - default is tp.");
        textMap.put("remove.syntax", "/otp remove <player>");
        textMap.put("remove.info", "Removes player from all saved lists.");
        textMap.put("tpMe.syntax", "/otp <player>");
        textMap.put("tpMe.info", "Teleport command user to other player.");
        textMap.put("tpToOther.syntax", "/otp <player> <otherplayer>");
        textMap.put("tpToOther.info", "Teleports player to other player's location.");
        textMap.put("tpToLoc.syntax", "/otp <player> <x> <y> <z> [dim]");
        textMap.put("tpToLoc.info", "Teleports player to x, y, z coords, dim defaults to sender's dimension.");
        textMap.put("tpToDim.syntax", "/otp <player> <dim>");
        textMap.put("tpToDim.info", "Teleports player to the spawn point of <dim>.");

        textMap.put("list.tp", "Saved Teleport List:");
        textMap.put("list.logouts", "Saved Logout List:");
        textMap.put("list.names", "Saved Player List:");

        textMap.put("clear.tp", "Teleport list cleared.");
        textMap.put("clear.logouts", "Logout list cleared");
        textMap.put("clear.names", "Player list cleared:");

        textMap.put("removeSuccess", " removed successfully.");

        textMap.put("cancel.syntax", "/otp cancel <player>");
    }

    public static void register(ISubCommand command)
    {
        commands.put(command.getCommandName(), command);
    }

    public static boolean commandExists(String name)
    {
        return commands.containsKey(name);
    }

    @Override
    public String getCommandName()
    {
        return "otp";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/" + getCommandName() + " help";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            String subCommand = args[0];
            Pattern pattern = Pattern.compile("^" + subCommand, Pattern.CASE_INSENSITIVE);
            List result = new ArrayList();
            for (ISubCommand command : commands.values())
            {
                if (pattern.matcher(command.getCommandName()).find())
                    result.add(command.getCommandName());
            }
            result.addAll(TeleportRegistry.getMatchingNames(args[0]));
            return result;
        } else if (commands.containsKey(args[0]))
        {
            return commands.get(args[0]).addTabCompletionOptions(sender, args);
        } else if (TeleportRegistry.getAllNames().contains(args[0]))
        {
            return teleport.addTabCompletionOptions(sender, args);
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 1)
        {
            args = new String[]{"help"};
        }
        ISubCommand command = commands.get(args[0]);
        if (command != null)
        {
            if (sender.canCommandSenderUseCommand(command.getPermissionLevel(), "otp " + command.getCommandName()) ||
                    (sender instanceof EntityPlayerMP && command.getPermissionLevel() <= 0))
            {
                command.handleCommand(sender, args);
                return;
            }
            throw new CommandException("commands.generic.permission");
        }
        if (TeleportRegistry.getAllNames().contains(args[0]))
        {
            teleport.handleCommand(sender, args);
            return;
        }
        throw new CommandNotFoundException("Command not found");
    }


}
