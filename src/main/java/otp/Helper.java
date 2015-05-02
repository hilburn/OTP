package otp;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import otp.persist.PlayerCoord;

public class Helper
{
    public static void teleportPlayer(EntityPlayerMP player, PlayerCoord coord, ServerConfigurationManager manager)
    {
        if (player.getEntityWorld().provider.dimensionId == coord.dim)
        {
            player.setPositionAndUpdate(coord.x, coord.y, coord.z);
        } else
        {
            transferPlayerToDimension(player, coord.dim, coord.x, coord.y, coord.z, manager);
        }
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, ServerConfigurationManager manager)
    {
        transferPlayerToDimension(player, dimension, player.posX, player.posY, player.posZ, manager);
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimension, double x, double y, double z, ServerConfigurationManager manager)
    {
        int oldDim = player.dimension;
        WorldServer worldserver = manager.getServerInstance().worldServerForDimension(player.dimension);
        player.dimension = dimension;
        WorldServer worldserver1 = manager.getServerInstance().worldServerForDimension(player.dimension);
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo()
                .getTerrainType(), player.theItemInWorldManager.getGameType()));
        worldserver.removePlayerEntityDangerously(player);
        if (player.riddenByEntity != null) {
            player.riddenByEntity.mountEntity(null);
        }
        if (player.ridingEntity != null) {
            player.mountEntity(null);
        }
        player.isDead = false;
        transferEntityToWorld(player, worldserver, worldserver1);
        manager.func_72375_a(player, worldserver);
        player.playerNetServerHandler.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(worldserver1);
        manager.updateTimeAndWeatherForPlayer(player, worldserver1);
        manager.syncPlayerInventory(player);

        for (PotionEffect potioneffect : (Iterable<PotionEffect>)player.getActivePotionEffects())
        {
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
    }

    public static void transferEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld)
    {
        WorldProvider pOld = oldWorld.provider;
        WorldProvider pNew = newWorld.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double x = entity.posX * moveFactor;
        double z = entity.posZ * moveFactor;

        oldWorld.theProfiler.startSection("placing");
        x = MathHelper.clamp_double(x, -29999872, 29999872);
        z = MathHelper.clamp_double(z, -29999872, 29999872);

        if (entity.isEntityAlive()) {
            entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
            newWorld.spawnEntityInWorld(entity);
            newWorld.updateEntityWithOptionalForce(entity, false);
        }

        oldWorld.theProfiler.endSection();

        entity.setWorld(newWorld);
    }
}
