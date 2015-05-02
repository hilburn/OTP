package otp.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandTeleport;
import net.minecraft.entity.player.EntityPlayerMP;
import otp.persist.TeleportRegistry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ParentCommand extends CommandBase
{
    public static Map<String, ISubCommand> commands = new LinkedHashMap<String, ISubCommand>();
    public static ParentCommand instance = new ParentCommand();
    public static CommandOTP teleport = new CommandOTP();

    static
    {
        register(new CommandHelp());
        register(new CommandCancel());
        register(new CommandList());
        register(new CommandCancel());
        register(new CommandRemove());
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
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
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
        }
        throw new CommandNotFoundException("otp.command.notFound");
    }





}
