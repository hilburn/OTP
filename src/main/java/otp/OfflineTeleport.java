package otp;

import java.util.Map;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import otp.commands.ParentCommand;
import otp.persist.LoadHandler;
import otp.reference.Reference;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION_FULL)
public class OfflineTeleport
{

    @Instance(Reference.ID)
    public static OfflineTeleport instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        //config(event.getSuggestedConfigurationFile());

        LoadHandler handler = new LoadHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);

    }

    @NetworkCheckHandler
    public final boolean networkCheck(Map<String, String> remoteVersions, Side side)
    {
        return side.isClient() || remoteVersions.containsKey(Reference.ID);
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent event)
    {
        event.registerServerCommand(ParentCommand.instance);
    }
}