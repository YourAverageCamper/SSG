
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;



public class EVT_TributeDeath implements Listener
{
    
    
    @EventHandler
    public void onDeath(TributeDeathEvent e)
    {
        Tribute killed = e.getTribute();
        int points = getLostPoints(killed.getPoints());
        
        
        if (e.getKiller() == null)
        {
            HG.getInstance().getGame().setAlive(HG.getInstance().getGame().getAlive() - 1);
            killed.setSpectator(true);
            killed.setDeaths(killed.getDeaths() + 1);
            killed.getPlayer().sendMessage("[§bMCTheFallen§f] §cYou died and lost §e" + points + " §cpoints!");
            killed.setPoints(killed.getPoints() - points);
            if (HG.getInstance().getGame().getAlive() == 4)
                HG.getInstance().getGame().init_deathmatch_countdown();
            else if (HG.getInstance().getGame().getAlive() == 1)
            {
                for (Player p : Bukkit.getServer().getOnlinePlayers())
                    p.setAllowFlight(true);
                //!f
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HG.getInstance(), new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for(Tribute t : HG.getInstance().getPlayers().values())
                            if(!t.isSpectator())
                                if(t.getPlayer() != null){
                                    Bukkit.broadcastMessage("[§bMCTheFallen§r] §6" + t.getName() + " has won the Hunger Games!!!");
                                    for(Tribute tt : HG.getInstance().getPlayers().values())
                                        if(tt.getPlayer() != null)
                                            if(tt.getBet().equals(t.getName()))
                                                tt.getPlayer().sendMessage("[§bMCTheFallen§f] §6You gambled and won " + tt.getBetAmount());
                                }
                    }
                }, 0L, 20L * 3);
                //f
                HG.getInstance().getGame().end();
            }
            return;
        }
        final Tribute killer = e.getKiller();
        
        killed.getPlayer().teleport(HG.getInstance().getGame().getMap().getLocation().getLocation().getWorld().getSpawnLocation());
        killed.setSpectator(true);
        
        killed.setDeaths(killed.getDeaths() + 1);
        
        killed.setPoints(killed.getPoints() - points);
        killer.setPoints(killer.getPoints() + points);
        killed.getPlayer().sendMessage("[§bMCTheFallen§f] §cYou died and lost §e" + points + " §cpoints!");
        killer.getPlayer().sendMessage("[§bMCTheFallen§f] §aYou killed §e" + killed.getName() + " §aand gained §e" + points + " §apoints!");
        
        HG.getInstance().getGame().setAlive(HG.getInstance().getGame().getAlive() - 1);
        
        if (HG.getInstance().getGame().getAlive() == 4)
            HG.getInstance().getGame().init_deathmatch_countdown();
        else if (HG.getInstance().getGame().getAlive() == 1)
        {
            for (Player p : Bukkit.getServer().getOnlinePlayers())
                p.setAllowFlight(true);
            //!f
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HG.getInstance(), new Runnable()
            {
                @Override
                public void run()
                {
                    Bukkit.broadcastMessage("[§bMCTheFallen§r] §6" + killer.getName() + " has won the Hunger Games!!!");
                }
            }, 0L, 20L * 3);
            //f
            HG.getInstance().getGame().end();
        }
    }
    
    
    
    public int getLostPoints(int points)
    {
        if (points >= 100)
        {
            int noh = points / 100;
            double lost = noh * (0.5);
            return (int) (lost * 10);
        }
        if (points > 0 && points <= 25)
            return 1;
        if (points > 26 && points <= 50)
            return 2;
        if (points > 50 && points <= 75)
            return 3;
        if (points > 76 && points <= 99)
            return 4;
        return 0;
    }
}
