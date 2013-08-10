
package me.zeus.HungerGames.Objects;


import java.util.Random;

import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Enums.GameTime;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;



public class Game
{
    
    
    //=======================================================
    
    int alive;
    boolean grace;
    GameMap map;
    GameTime time;
    
    
    
    public Game()
    {
        alive = 0;
    }
    
    
    
    //=======================================================
    
    
    public void start()
    {
        alive = Bukkit.getServer().getOnlinePlayers().length;
        time = GameTime.GAME;
        
        if (map == null)
        {
            GameMap[] maps = HG.getInstance().getMaps().values().toArray(new GameMap[HG.getInstance().getMaps().values().size() - 1]);
            int select = new Random().nextInt(maps.length);
            map = maps[select];
            Bukkit.getServer().broadcastMessage("§f[§bMCTheFallen§f] §e§lForce selecting map: §r" + map.getName());
        }
        
        for (Player p : Bukkit.getServer().getOnlinePlayers())
        {
            for (Podium pp : map.podiums)
            {
                if (!pp.isOccupied())
                {
                    p.teleport(pp.getLocation());
                    pp.setOccupied(true);
                }
            }
            p.getInventory().clear();
            p.getActivePotionEffects().clear();
            p.setHealth(20.0);
            p.setFoodLevel(20);
            HG.getInstance().getFrozen().remove(p.getName());
        }
        map.getLocation().getLocation().getWorld().setTime(0);
        Bukkit.getServer().broadcastMessage(HG.getInstance().getConfigHandler().getGraceMessage());
        setGrace(true);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable()
        {
            
            
            @Override
            public void run()
            {
                setGrace(false);
                Bukkit.getServer().broadcastMessage(HG.getInstance().getConfigHandler().getGraceEndedMessage());
                for (Player p : Bukkit.getServer().getOnlinePlayers())
                    p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 1.0F, 2.0F);
            }
        }, 20L * HG.getInstance().getConfigHandler().getGraceTime());
        
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable()
        
        {
            
            
            @Override
            public void run()
            {
                init_deathmatch_countdown();
            }
        }, 20L * 60 * 30);
    }
    
    
    
    public void end()
    {
        //!f
        time = GameTime.RESTARTING;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "stop");
            }
        }, 20L * HG.getInstance().getConfigHandler().getTimeBeforeShutdown());
        //f
    }
    
    
    
    public void init_deathmatch()
    {
        time = GameTime.DEATHMATCH;
        for (Tribute t : HG.getInstance().getPlayers().values())
        {
            if (t.getPlayer() != null)
            {
                t.getPlayer().teleport(Bukkit.getWorld(HG.getInstance().getGame().getMap().getName()).getSpawnLocation());
                t.getPlayer().setHealth(t.getPlayer().getMaxHealth());
            }
        }
        Bukkit.broadcastMessage("§f[§bMCTheFallen§f] §4Fight to the death!");
    }
    
    
    
    public void init_deathmatch_countdown()
    {
        Countdown2 cd = new Countdown2();
        cd.startCountdown(60);
    }
    
    
    
    public void setAlive(int amt)
    {
        this.alive = amt;
    }
    
    
    
    public void setGrace(boolean b)
    {
        this.grace = b;
    }
    
    
    
    public void setMap(String name)
    {
        this.map = GameMap.getMap(name);
    }
    
    
    
    public void setGameTime(GameTime time)
    {
        this.time = time;
    }
    
    
    
    //=======================================================
    
    
    public boolean isGrace()
    {
        return grace;
    }
    
    
    
    public GameMap getMap()
    {
        return map;
    }
    
    
    
    public int getAlive()
    {
        return alive;
    }
    
    
    
    public GameTime getTime()
    {
        return time;
    }
    
    
    //=======================================================
    
}
