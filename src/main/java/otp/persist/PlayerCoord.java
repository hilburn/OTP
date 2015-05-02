package otp.persist;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;

public class PlayerCoord
{
    public double x, y, z;
    public int dim;
    public String name;

    public PlayerCoord(double x, double y, double z, int dim, String player)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        this.name = player;
    }

    public PlayerCoord(EntityPlayerMP player)
    {
        this(player.posX, player.posY, player.posZ, player.worldObj.provider.dimensionId, player.getCommandSenderName());
    }

    public PlayerCoord(NBTTagCompound tagCompound)
    {
        this.x = tagCompound.getDouble("x");
        this.y = tagCompound.getDouble("y");
        this.z = tagCompound.getDouble("z");
        this.dim = tagCompound.getInteger("d");
        this.name = tagCompound.getString("n");
    }

    public PlayerCoord(ChunkCoordinates spawnPoint, int dim, String name)
    {
        this(spawnPoint.posX, spawnPoint.posY, spawnPoint.posZ, dim, name);
    }

    public NBTTagCompound writeToNBT()
    {
        NBTTagCompound result = new NBTTagCompound();
        result.setDouble("x", x);
        result.setDouble("z", z);
        result.setDouble("y", y);
        result.setByte("d", (byte)dim);
        result.setString("n", name);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof PlayerCoord)
        {
            PlayerCoord other = (PlayerCoord)obj;
            return other.name.equals(name);
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public String toString()
    {
        return name + ": x: " + x + ", y: " + y + ", z: " + z + ", dim: " + DimensionManager.getProvider(dim).getDimensionName();
    }
}
