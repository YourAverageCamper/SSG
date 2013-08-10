
package me.zeus.HungerGames.Commands;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Commands.SimpleCommand.CommandHandler;



public class CMD_SetLobby
{
    
    
    @CommandHandler(name = "setlobby")
    public void onCommand(Player sender, String[] args)
    {
        if (sender.hasPermission("HungerGames.SetLobby"))
        {
            HG.getInstance().setLobby(sender.getLocation());
            Location loc = sender.getLocation();
            HG.getInstance().getConfig().set("lobby.world", loc.getWorld().getName());
            HG.getInstance().getConfig().set("lobby.x", loc.getX());
            HG.getInstance().getConfig().set("lobby.y", loc.getY());
            HG.getInstance().getConfig().set("lobby.z", loc.getZ());
            HG.getInstance().getConfig().set("lobby.pitch", loc.getPitch());
            HG.getInstance().getConfig().set("lobby.yaw", loc.getYaw());
            HG.getInstance().saveConfig();
            sender.sendMessage("[§bMCTheFallen§f] §aLobby set!");
        }
    }
}
