
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Enums.GameTime;
import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;



public class EVT_Quit implements Listener
{
    
    
    @EventHandler
    public void onLeave(PlayerQuitEvent e)
    {
        Tribute t = Tribute.getTribute(e.getPlayer());
        if (HG.getInstance().getGame().getTime() != GameTime.LOBBY)
        {
            if (!t.isSpectator())
            {
                Bukkit.getServer().getPluginManager().callEvent(new TributeDeathEvent(t, null, HG.getInstance().getGame().getTime()));
            }
        }
    }
}
