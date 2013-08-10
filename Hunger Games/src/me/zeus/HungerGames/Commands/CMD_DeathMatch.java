
package me.zeus.HungerGames.Commands;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Commands.SimpleCommand.CommandHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;



public class CMD_DeathMatch
{
    
    
    @CommandHandler(name = "deathmatch")
    public void on(Player sender, String[] args)
    {
        if (sender.hasPermission("HungerGames.DeathMatch"))
        {
            Bukkit.broadcastMessage("[§bMCTheFallen§r] §6" + sender.getName() + " forced deathmatch to happen!");
            HG.getInstance().getGame().init_deathmatch_countdown();
        }
    }
}
