
package me.zeus.HungerGames;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;



public class ConfigHandler
{
    
    
    File file;
    FileConfiguration cfg;
    
    boolean chestRegen;
    int chestRegenTime;
    
    int timeBeforeShutdown;
    int gracePeriod;
    
    String graceMSG;
    String graceEndedMSG;
    String staffMSG;
    
    List<Material> breakables;
    
    int minimumPlayers;
    
    
    
    public ConfigHandler(File f)
    {
        this.file = f;
        this.cfg = YamlConfiguration.loadConfiguration(f);
        
        gracePeriod = cfg.getInt("grace-period");
        chestRegen = cfg.getBoolean("chest-regen");
        timeBeforeShutdown = cfg.getInt("shutdown-time");
        graceMSG = cfg.getString("grace-period-message").replace("&", "§");
        graceEndedMSG = cfg.getString("grace-period-ended-message").replace("&", "§");
        staffMSG = cfg.getString("staff-join-message").replace("&", "§");
        minimumPlayers = cfg.getInt("minimum-players");
        breakables = new ArrayList<Material>();
        for (int id : cfg.getIntegerList("breakable-blocks"))
        {
            breakables.add(Material.getMaterial(id));
            System.out.println("Added: " + Material.getMaterial(id) + " to the breakable blocks list!");
        }
        
        HG.getInstance().setAutoStart(cfg.getBoolean("auto-start"));
        
        World world = Bukkit.getServer().getWorld(cfg.getString("lobby.world"));
        double x = cfg.getDouble("lobby.x");
        double y = cfg.getDouble("lobby.y");
        double z = cfg.getDouble("lobby.z");
        float pitch = Float.parseFloat(cfg.getString("lobby.pitch"));
        float yaw = Float.parseFloat(cfg.getString("lobby.yaw"));
        Location loc = new Location(world, x, y, z);
        loc.setPitch(pitch);
        loc.setYaw(yaw);
        HG.getInstance().setLobby(loc);
        
        HG.getInstance().voteTime = cfg.getInt("vote-time");
    }
    
    
    
    public boolean hasChestRegen()
    {
        return chestRegen;
    }
    
    
    
    public int getChestRegenTime()
    {
        return chestRegenTime;
    }
    
    
    
    public int getTimeBeforeShutdown()
    {
        return timeBeforeShutdown;
    }
    
    
    
    public int getGraceTime()
    {
        return gracePeriod;
    }
    
    
    
    public String getGraceMessage()
    {
        return graceMSG;
    }
    
    
    
    public String getGraceEndedMessage()
    {
        return graceEndedMSG;
    }
    
    
    
    public String getStaffMessage()
    {
        return staffMSG;
    }
    
    
    
    public List<Material> getBreakables()
    {
        return breakables;
    }
    
    
    
    public int getMinPlayers()
    {
        return minimumPlayers;
    }
}
