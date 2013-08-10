
package me.zeus.HungerGames.Commands;


import me.zeus.HungerGames.Commands.SimpleCommand.CommandHandler;
import me.zeus.HungerGames.Objects.GameMap;
import me.zeus.HungerGames.Objects.Podium;

import org.bukkit.entity.Player;



public class CMD_AddSpawn
{
    
    
    @CommandHandler(name = "addspawn")
    public void onCommand(Player sender, String[] args)
    {
        if (!sender.hasPermission("HungerGames.SetSpawn"))
        {
            sender.sendMessage("[§bMCTheFallen§f] §cYou don't have permission to do this!");
            return;
        }
        GameMap map = GameMap.getMap(sender.getWorld().getName());
        map.getPodiums().add(new Podium(sender.getLocation()));
        map.save();
        sender.sendMessage("[§bMCTheFallen§f] §aSpawn location added.");
    }
}
