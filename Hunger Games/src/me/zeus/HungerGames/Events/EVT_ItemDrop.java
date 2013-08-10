
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;



public class EVT_ItemDrop implements Listener
{
    
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e)
    {
        if (Tribute.getTribute(e.getPlayer()).isSpectator())
        {
            e.setCancelled(true);
            return;
        }
    }
}
