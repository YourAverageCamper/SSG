
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;



public class EVT_ItemPickup implements Listener
{
    
    
    @EventHandler
    public void onPickup(PlayerPickupItemEvent e)
    {
        if (Tribute.getTribute(e.getPlayer()).isSpectator())
        {
            e.setCancelled(true);
            return;
        }
    }
}
