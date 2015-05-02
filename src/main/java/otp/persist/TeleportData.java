package otp.persist;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.WorldSavedData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class TeleportData extends WorldSavedData
{
    public static final String KEY = "otp.tpdata";

    public HashMap<String, PlayerCoord> teleports;
    public HashMap<String, PlayerCoord> logOuts;
    public List<String> names;

    public TeleportData()
    {
        this(KEY);
    }

    public TeleportData(String string)
    {
        super(string);
        teleports = new HashMap<String, PlayerCoord>();
        logOuts = new HashMap<String, PlayerCoord>();
        names = new ArrayList<String>();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        NBTTagList list = tagCompound.getTagList("tp", 10);
        for (int i = 0; i < list.tagCount(); i++)
        {
            putTP(new PlayerCoord(list.getCompoundTagAt(i)));
        }
        list = tagCompound.getTagList("lo", 10);
        for (int i = 0; i < list.tagCount(); i++)
        {
            addLogOut(new PlayerCoord(list.getCompoundTagAt(i)));
        }
        list = tagCompound.getTagList("names", 8);
        for (int i = 0; i < list.tagCount(); i++)
        {
            names.add(list.getStringTagAt(i));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        NBTTagList list = new NBTTagList();
        for (PlayerCoord coord : teleports.values())
        {
            list.appendTag(coord.writeToNBT());
        }
        tagCompound.setTag("tp", list);
        list = new NBTTagList();
        for (PlayerCoord coord : logOuts.values())
        {
            list.appendTag(coord.writeToNBT());
        }
        tagCompound.setTag("lo", list);
        list = new NBTTagList();
        for (String name : names)
        {
            list.appendTag(new NBTTagString(name));
        }
        tagCompound.setTag("names", list);
    }

    public PlayerCoord getTP(String name)
    {
        return teleports.get(name);
    }

    public void putTP(PlayerCoord playerCoord)
    {
        teleports.put(playerCoord.name, playerCoord);
        markDirty();
    }

    public void removeTP(String name)
    {
        teleports.remove(name);
        markDirty();
    }

    public void addLogOut(PlayerCoord coord)
    {
        logOuts.put(coord.name, coord);
        markDirty();
    }

    public PlayerCoord getLogOut(String name)
    {
        return logOuts.get(name);
    }

    public void removeName(String name)
    {
        names.remove(name);
        markDirty();
    }

    public List<String> getNames(String partialName)
    {
        List<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile("^"+partialName, Pattern.CASE_INSENSITIVE);
        for (String name : names) if (pattern.matcher(name).find()) result.add(name);
        return result;
    }

    public void addName(String name)
    {
        if (!names.contains(name))
        {
            names.add(name);
            Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
            markDirty();
        }
    }

    public void clear()
    {
        teleports.clear();
        logOuts.clear();
        names.clear();
        markDirty();
    }

    public void removeLogOut(String name)
    {
        logOuts.remove(name);
        markDirty();
    }

    public void clearTP()
    {
        teleports.clear();
        markDirty();
    }

    public void clearNames()
    {
        names.clear();
        markDirty();
    }

    public void clearLogOuts()
    {
        logOuts.clear();
        markDirty();
    }
}
