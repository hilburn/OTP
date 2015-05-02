package otp.commands;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import otp.Helper;
import otp.persist.TeleportRegistry;

import java.util.List;

public class OTPCommand extends CommandBase
{
    public static OTPCommand instance = new OTPCommand();

    @Override
    public String getCommandName()
    {
        return "otp";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/otp <player> [x y z] [dim id]";
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args)
    {
        double x = Double.MIN_VALUE, y = Double.MIN_VALUE, z = Double.MIN_VALUE;
        int dim = Integer.MIN_VALUE;
        switch (args.length)
        {
            case 0:
                //CommandHelp.instance.handleCommand(sender, args);
                break;
            case 5:
                dim = CommandBase.parseInt(sender,args[4]);
            case 4:
                x = CommandBase.parseDouble(sender, args[1])+0.5;
                y = CommandBase.parseDouble(sender, args[2]);
                z = CommandBase.parseDouble(sender, args[3])+0.5;
            case 1:
                EntityPlayerMP playerSender = CommandBase.getCommandSenderAsPlayer(sender);
                if (dim == Integer.MIN_VALUE) dim = playerSender.getEntityWorld().provider.dimensionId;
                if (z == Double.MIN_VALUE)
                {
                    x = playerSender.posX;
                    y = playerSender.posY;
                    z = playerSender.posZ;
                }
                try
                {
                    EntityPlayerMP player = CommandBase.getPlayer(sender, args[0]);
                    if (player.equals(playerSender)) player = playerSender;
                    player.mountEntity(null);
                    if (dim == player.dimension)
                    {
                        player.setPositionAndUpdate(x, y, z);
                    } else
                    {
                        Helper.transferPlayerToDimension(player, dim, x, y, z, playerSender.mcServer.getConfigurationManager());
                    }
                    break;
                } catch (PlayerNotFoundException t)
                {
                    TeleportRegistry.saveTP(x, y, z, dim, args[0]);
                }
                break;
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return TeleportRegistry.getMatchingNames(args[0]);
        }
        return null;
    }

}
