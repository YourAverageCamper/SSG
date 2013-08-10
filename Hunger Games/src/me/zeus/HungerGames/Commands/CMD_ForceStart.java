package me.zeus.HungerGames.Commands;

import org.bukkit.entity.Player;

import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Commands.SimpleCommand.CommandHandler;



public class CMD_ForceStart
{
    @CommandHandler(name="forcestart",permission="HungerGames.ForceStart")
    public void onCommand(Player sender, String[] args){
        if(!sender.hasPermission("HungerGames.ForceStart")){
            sender.sendMessage("[§bMCTheFallen§f] §cYou don't have permission to start the game!");
            return;
        }
        
        sender.sendMessage("[§bMCTheFallen§r] §6Force starting game! §4[WARNING]");
        HG.getInstance().getGame().start();
        
    }
}
