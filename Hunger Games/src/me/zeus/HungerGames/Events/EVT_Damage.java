
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;



public class EVT_Damage implements Listener
{
    
    
    @EventHandler
    public void onDamge(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            Player p = (Player) e.getEntity();
            if (Tribute.getTribute(p).isSpectator())
                e.setCancelled(true);
        }
    }
}
