
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;



public class EVT_Block implements Listener
{
    
    
    @EventHandler
    public void onBreak(BlockBreakEvent e)
    {
        if (Tribute.getTribute(e.getPlayer()).isSpectator())
        {
            e.setCancelled(true);
            return;
        }
        Material type = e.getBlock().getType();
        for (Material mat : HG.getInstance().getConfigHandler().getBreakables())
        {
            if (type.equals(mat))
            {
                continue;
            }
            if (!e.getPlayer().isOp())
                e.setCancelled(true);
        }
    }
    
    
    
    @EventHandler
    public void onBreak(BlockPlaceEvent e)
    {
        if (Tribute.getTribute(e.getPlayer()).isSpectator())
        {
            e.setCancelled(true);
            return;
        }
        Material type = e.getBlock().getType();
        for (Material mat : HG.getInstance().getConfigHandler().getBreakables())
        {
            if (type.equals(mat))
            {
                continue;
            }
            if (!e.getPlayer().isOp())
            {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cYou may not break blocks!");
            }
        }
    }
}
