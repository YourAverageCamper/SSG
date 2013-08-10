
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Enums.GameTime;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;



public class EVT_Move implements Listener
{
    
    
    @EventHandler
    public void onMove(PlayerMoveEvent e)
    {
        if (HG.getInstance().getGame().getTime() == GameTime.PREGAME)
        {
            if (e.getTo().getX() != e.getFrom().getX() || e.getTo().getZ() != e.getFrom().getZ())
            {
                if (HG.getInstance().getFrozen().contains(e.getPlayer().getName()))
                {
                    e.setTo(e.getFrom());
                }
            }
        }
    }
}
