
package me.zeus.HungerGames.Commands;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Commands.SimpleCommand.CommandHandler;

import org.bukkit.entity.Player;



public class CMD_DeleteSpawn
{
    
    
    @CommandHandler(name = "delspawn")
    public void onCommand(Player sender, String[] args)
    {
        if (!sender.hasPermission("HungerGames.DeleteSpawn"))
        {
            sender.sendMessage("[§bMCTheFallen§f] §cYou don't have permission to do this!");
            return;
        }
        HG.getInstance().getGame().getMap().getPodiums().clear();
        HG.getInstance().getGame().getMap().save();
        sender.sendMessage("[§bMCTheFallen§f] §cDeleted podiums.");
    }
}
