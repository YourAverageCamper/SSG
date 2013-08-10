
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;



public class EVT_Chat implements Listener
{
    
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e)
    {
        e.setFormat("[" + Tribute.getTribute(e.getPlayer()).getPoints() + "] " + e.getPlayer().getDisplayName() + ": " + e.getMessage());
    }
}
