
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;

import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;



public class EVT_Ping implements Listener
{
    
    
    @EventHandler
    public void onPing(ServerListPingEvent e)
    {
        switch (HG.getInstance().getGame().getTime())
        {
            case DEATHMATCH:
                e.setMotd("[§bMCTheFallen§f] §4Deathmatch");
                break;
            case GAME:
                e.setMotd("[§bMCTheFallen§f] §aGame In Progress");
                break;
            case LOBBY:
                e.setMotd("[§bMCTheFallen§f] §bLobby");
                break;
            case PREGAME:
                e.setMotd("[§bMCTheFallen§f] §ePregame");
                break;
            case RESTARTING:
                e.setMotd("[§bMCTheFallen§f] §5Restarting!");
                break;
            default:
                break;
        }
    }
}
