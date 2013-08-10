
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;



public class EVT_KillDeath implements Listener
{
    
    
    @EventHandler
    public void onKillDeath(PlayerDeathEvent e)
    {
        Player dead = e.getEntity();
        e.setDeathMessage("[§bMCTheFallen§r] §6You hear a cannon in the distance. A tribute has fallen.");
        
        if (!Tribute.getTribute(dead).isSpectator())
            if (dead.getKiller() != null)
            {
                Bukkit.getServer().getPluginManager().callEvent(new TributeDeathEvent(Tribute.getTribute(dead), Tribute.getTribute(dead.getKiller()), HG.getInstance().getGame().getTime()));
                for (Player p : Bukkit.getServer().getOnlinePlayers())
                {
                    p.playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
                }
            }
            else
            {
                Bukkit.getServer().getPluginManager().callEvent(new TributeDeathEvent(Tribute.getTribute(dead), null, HG.getInstance().getGame().getTime()));
            }
    }
    
    
}
