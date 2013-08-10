
package me.zeus.HungerGames.Commands;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Commands.SimpleCommand.CommandHandler;
import me.zeus.HungerGames.Enums.GameTime;
import me.zeus.HungerGames.Objects.Tribute;



public class CMD_Spectate
{
    
    
    @CommandHandler(name = "spectate")
    public void onCommand(Player sender, String[] args)
    {
        if (HG.getInstance().getGame().getTime() == GameTime.LOBBY || HG.getInstance().getGame().getTime() == GameTime.PREGAME || HG.getInstance().getGame().getTime() == GameTime.RESTARTING)
        {
            sender.sendMessage("[§bMCTheFallen§f] §cYou can not spectate now!");
            return;
        }
        if (!Tribute.getTribute(sender).isSpectator())
        {
            sender.sendMessage("[§bMCTheFallen§f] §cYou must be dead to spectate!");
            return;
        }
        if (args.length != 1)
        {
            sender.sendMessage("[§bMCTheFallen§f] §cYou must specify a player to teleport to.");
            return;
        }
        Player p = Bukkit.getServer().getPlayerExact(args[0]);
        if (p == null)
        {
            sender.sendMessage("[§bMCTheFallen§f] §cYou can not spectate fake players!");
            return;
        }
        sender.teleport(p.getLocation());
        sender.sendMessage("[§bMCTheFallen§f] §aNow spectating: §e" + args[0]);
    }
}
