
package me.zeus.HungerGames.Commands;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Commands.SimpleCommand.CommandHandler;
import me.zeus.HungerGames.Objects.Tribute;



public class CMD_Gamble
{
    
    
    @CommandHandler(name = "gamble")
    public void onCommand(Player sender, String[] args)
    {
        Tribute t = Tribute.getTribute(sender);
        if (!t.hasBet())
        {
            if (!t.isSpectator())
            {
                sender.sendMessage("[§bMCTheFallen§f] §cYou can not gamble unless you are spectating!");
                return;
            }
            if (args.length != 2)
            {
                sender.sendMessage("[§bMCTheFallen§f] §cYou must supply a name to bet on, and an amount!");
                return;
            }
            else if (args.length > 2)
            {
                Player p = Bukkit.getServer().getPlayer(args[0]);
                if (p == null)
                {
                    sender.sendMessage("[§bMCTheFallen§f] §cYou can not vote for an offline player!");
                    return;
                }
                if (Tribute.getTribute(p).isSpectator())
                {
                    sender.sendMessage("[§bMCTheFallen§f] §cYou can not vote for a dead player!");
                    return;
                }
                for (Tribute tt : HG.getInstance().getPlayers().values())
                {
                    if (tt.hasBet())
                    {
                        if (args[0].equals(tt.getBet()))
                        {
                            sender.sendMessage("[§bMCTheFallen§f] §cSomeone else has already placed bets on this person!");
                            return;
                        }
                    }
                }
                try
                {
                    int bet = Integer.parseInt(args[1]);
                    if (t.getPoints() < bet)
                    {
                        sender.sendMessage("§bMCTheFallen§f] §cYou can not bet more than you have!");
                        return;
                    }
                    if (bet > 0)
                    {
                        t.setBet(args[1], bet);
                        Bukkit.getServer().broadcastMessage("§bMCTheFallen§f] §e" + sender.getName() + " bet " + bet + " on " + p.getName() + "!");
                    }
                }
                catch (NumberFormatException nfe)
                {
                    sender.sendMessage("§bMCTheFallen§f] §cInvalid amount to bet!");
                    return;
                }
            }
        }
        else
        {
            sender.sendMessage("§bMCTheFallen§f] §cYou can not bet twice!");
            return;
        }
    }
}
