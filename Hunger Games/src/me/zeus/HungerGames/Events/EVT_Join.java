
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Enums.GameTime;
import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class EVT_Join implements Listener
{
    
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        if (HG.getInstance().getGame().getTime() != GameTime.LOBBY)
        {
            if (HG.getInstance().getGame().getTime() == GameTime.DEATHMATCH || HG.getInstance().getGame().getTime() == GameTime.RESTARTING || HG.getInstance().getGame().getTime() == GameTime.PREGAME)
            {
                e.setJoinMessage("");
                e.getPlayer().kickPlayer("§4You can not join right now!");
                return;
            }
            e.setJoinMessage("");
            Tribute.getTribute(e.getPlayer()).setSpectator(true);
            e.getPlayer().teleport(HG.getInstance().getGame().getMap().getLocation().getLocation().getWorld().getSpawnLocation());
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            return;
        }
        Player p = e.getPlayer();
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(20);
        p.setAllowFlight(false);
        p.getInventory().clear();
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
        for (PotionEffect effect : p.getActivePotionEffects())
            p.removePotionEffect(effect.getType());
        
        if (!HG.getInstance().getPlayers().containsKey(e.getPlayer().getName()))
        {
            Tribute t = new Tribute();
            t.setName(e.getPlayer().getName());
            t.setKills(0);
            t.setDeaths(0);
            t.setPoints(100);
            t.setWins(0);
            t.setLosses(0);
            t.save();
            HG.getInstance().getPlayers().put(t.getName(), t);
        }
        
        if (e.getPlayer().isOp())
            e.setJoinMessage(HG.getInstance().getConfigHandler().getStaffMessage());
        
        e.getPlayer().teleport(HG.getInstance().getLobby());
        
        if (HG.getInstance().getScoreboard() != null)
            e.getPlayer().setScoreboard(HG.getInstance().getScoreboard());
        
    }
}
