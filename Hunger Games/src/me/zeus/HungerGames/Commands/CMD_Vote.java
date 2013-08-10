
package me.zeus.HungerGames.Commands;


import org.bukkit.entity.Player;

import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Commands.SimpleCommand.CommandHandler;



public class CMD_Vote
{
    
    
    @CommandHandler(name = "vote")
    public void onVote(Player sender, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage("[§bMCTheFallen§f] §cYou must specify a map to vote for!");
            sender.sendMessage("[§bMCTheFallen§f] §31) §d" + HG.getInstance().getMap(1).getPlayer().getName());
            sender.sendMessage("[§bMCTheFallen§f] §32) §d" + HG.getInstance().getMap(2).getPlayer().getName());
            sender.sendMessage("[§bMCTheFallen§f] §33) §d" + HG.getInstance().getMap(3).getPlayer().getName());
            sender.sendMessage("[§bMCTheFallen§f] §34) §d" + HG.getInstance().getMap(4).getPlayer().getName());
            return;
        }
        if (!HG.getInstance().getVoted().contains(sender.getName()))
        {
            try
            {
                if (Integer.parseInt(args[0]) == 0 || Integer.parseInt(args[0]) > 4)
                {
                    return;
                }
                HG.getInstance().getMap(Integer.parseInt(args[0])).setScore(HG.getInstance().getMap(Integer.parseInt(args[0])).getScore() + 1);
                sender.sendMessage("[§bMCTheFallen§f] §aVoted for: " + args[0]);
                if (!sender.isOp())
                {
                    HG.getInstance().getVoted().add(sender.getName());
                }
            }
            catch (NumberFormatException nfe)
            {
                sender.sendMessage("[§bMCTheFallen§f] §cThat's not even a number!");
                return;
            }
            return;
        }
        else
        {
            sender.sendMessage("§cYou can not vote twice!");
            return;
        }
    }
}
