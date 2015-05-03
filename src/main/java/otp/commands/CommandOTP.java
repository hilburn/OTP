package otp.commands;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.DimensionManager;
import otp.Helper;
import otp.persist.PlayerCoord;
import otp.persist.TeleportRegistry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommandOTP implements ISubCommand
{
    private static String[] helpTags = new String[]{"tpMe", "tpToOther", "tpToLoc", "tpToDim"};

    @Override
    public int getPermissionLevel()
    {
        return 3;
    }

    @Override
    public String getCommandName()
    {
        return null;
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments)
    {
        String player = null;
        PlayerCoord target = null;
        if (!TeleportRegistry.getAllNames().contains(arguments[0]))
        {
            throw new PlayerNotFoundException();
        }
        if (arguments.length == 1)
        {
            player = sender.getCommandSenderName();
            try
            {
                target = new PlayerCoord(CommandBase.getPlayer(sender, arguments[0]));
            } catch (PlayerNotFoundException e)
            {
                target = TeleportRegistry.getSavedLogOuts().get(arguments[0]);
                if (target == null)
                    throw new CommandException(arguments[0] + " is not a valid player");
            }
        } else
        {
            player = arguments[0];
            Integer dim = null;
            if (sender instanceof EntityPlayerMP)
            {
                dim = ((EntityPlayerMP)sender).dimension;
            }
            if (arguments.length == 5)
            {
                dim = CommandBase.parseInt(sender, arguments[4]);
            }
            if (arguments.length == 2)
            {
                if (TeleportRegistry.getAllNames().contains(arguments[1]))
                {
                    try
                    {
                        target = new PlayerCoord(CommandBase.getPlayer(sender, arguments[1]));
                    } catch (PlayerNotFoundException e)
                    {
                        target = TeleportRegistry.getSavedLogOuts().get(arguments[1]);
                    }
                } else
                {
                    Set<Integer> dims = new HashSet<Integer>(Arrays.asList(DimensionManager.getIDs()));
                    dim = CommandBase.parseInt(sender, arguments[1]);
                    if (dims.contains(dim))
                    {
                        target = new PlayerCoord(DimensionManager.getProvider(dim).getSpawnPoint(), dim, player);
                    }
                }
            } else if (arguments.length == 4 || arguments.length == 5)
            {
                if (dim == null) throw new WrongUsageException(ParentCommand.textMap.get("tpToLoc.syntax"));
                target = new PlayerCoord(CommandBase.parseDouble(sender, arguments[1]), CommandBase.parseDouble(sender, arguments[2]), CommandBase.parseDouble(sender, arguments[3]), dim, player);
            }
        }
        if (target == null) throw new WrongUsageException(ParentCommand.textMap.get("help.info"));
        teleportPlayer(sender, player, target);
    }

    public static void teleportPlayer(ICommandSender sender, String player, PlayerCoord target)
    {
        try
        {
            EntityPlayerMP playerTarget = CommandBase.getPlayer(sender, player);
            Helper.teleportPlayer(playerTarget, target, playerTarget.mcServer.getConfigurationManager());
        } catch (PlayerNotFoundException e)
        {
            TeleportRegistry.saveTP(player, target);
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 2)
        {
            return TeleportRegistry.getMatchingNames(args[1]);
        }
        return null;
    }

    public void printHelp(ICommandSender sender)
    {
        for (String tag : helpTags)
        {
            sender.addChatMessage(new ChatComponentText(ParentCommand.textMap.get(tag + ".syntax")));
            sender.addChatMessage(new ChatComponentText(ParentCommand.textMap.get(tag + ".info")));
        }
    }
}
