
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Enums.GameTime;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class EVT_Respawn implements Listener
{
    
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e)
    {
        if (HG.getInstance().getGame().getTime() != GameTime.LOBBY)
        {
            e.setRespawnLocation(HG.getInstance().getGame().getMap().getPodiums().get(0).getLocation());
            final Player p = e.getPlayer();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HG.getInstance(), new Runnable()
            {
                
                
                @Override
                public void run()
                {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
                    p.getActivePotionEffects().clear();
                    p.setHealth(p.getMaxHealth());
                    p.setFoodLevel(20);
                    p.setAllowFlight(true);
                }
            }, 20L);
        }
    }
}
