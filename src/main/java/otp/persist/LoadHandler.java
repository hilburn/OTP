package otp.persist;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import otp.Helper;

public class LoadHandler
{
    @SubscribeEvent
    public void playerLogIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            TeleportRegistry.playerLogIn((EntityPlayerMP)event.player);
            PlayerCoord coord = TeleportRegistry.getSavedTP(event.player.getCommandSenderName());
            if (coord != null)
            {
                Helper.transferPlayerToDimension((EntityPlayerMP)event.player, coord.dim, coord.x, coord.y, coord.z, ((EntityPlayerMP)event.player).mcServer.getConfigurationManager());
            }
        }
    }

    @SubscribeEvent
    public void playerLogOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            TeleportRegistry.saveLogOut((EntityPlayerMP)event.player);
        }
    }


    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event)
    {
        WorldSavedData data = event.world.loadItemData(TeleportData.class, TeleportData.KEY);
        TeleportRegistry.setTPData((TeleportData)data);
    }

    @SubscribeEvent
    public void worldSave(WorldEvent.Save event)
    {
        TeleportData teleportData = TeleportRegistry.getTPData();
        if (teleportData != null)
            event.world.setItemData(TeleportData.KEY, teleportData);
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event)
    {
        TeleportData teleportData = TeleportRegistry.getTPData();
        if (teleportData != null)
            event.world.setItemData(TeleportData.KEY, teleportData);
    }
}
