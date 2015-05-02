package otp.persist;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;
import java.util.Map;

public class TeleportRegistry
{
    public static TeleportRegistry instance = new TeleportRegistry();

    private TeleportData teleportData;

    public static PlayerCoord getSavedTP(String name)
    {
        PlayerCoord tp = instance.teleportData.getTP(name);
        if (tp != null) instance.teleportData.removeTP(name);
        return tp;
    }

    public static void saveTP(double x, double y, double z, int dim, String name)
    {
        PlayerCoord coord = new PlayerCoord(x, y, z, dim, name);
        instance.teleportData.putTP(coord);
    }

    public static void saveTP(String name, PlayerCoord coord)
    {
        coord.name = name;
        instance.teleportData.putTP(coord);
    }

    public static void removeTP(String name)
    {
        instance.teleportData.removeTP(name);
    }

    public static Map<String, PlayerCoord> getPendingTP()
    {
        return instance.teleportData.teleports;
    }


    public static Map<String, PlayerCoord> getSavedLogOuts()
    {
        return instance.teleportData.logOuts;
    }

    public static void removeName(String name)
    {

    }

    public static void playerLogIn(EntityPlayerMP player)
    {
        instance.teleportData.addName(player.getCommandSenderName());
        instance.teleportData.removeLogOut(player.getCommandSenderName());
    }

    public static void saveLogOut(EntityPlayerMP player)
    {
        instance.teleportData.addLogOut(new PlayerCoord(player));
    }

    public static void setTPData(TeleportData data)
    {
        if (data == null)
        {
            if (instance.teleportData != null) return;
            data = new TeleportData();
        }
        instance.teleportData = data;
    }

    public static TeleportData getTPData()
    {
        return instance.teleportData;
    }

    public static List<String> getAllNames()
    {
        return instance.teleportData.names;
    }

    public static List<String> getMatchingNames(String partial)
    {
        return instance.teleportData.getNames(partial);
    }


    public static void clear()
    {
        instance.teleportData.clear();
    }

    public static void clearTP()
    {
        instance.teleportData.clearTP();
    }

    public static void clearNames()
    {
        instance.teleportData.clearNames();
    }

    public static void clearLogouts()
    {
        instance.teleportData.clearLogOuts();
    }

    public static void removeLogOut(String player)
    {
        instance.teleportData.removeLogOut(player);
    }
}
