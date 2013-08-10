
package me.zeus.HungerGames.Events;


import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



public class EVT_PVP implements Listener
{
    
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageByEntityEvent e)
    {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player)
        {
            Player p = (Player) e.getEntity();
            Player p2 = (Player) e.getDamager();
            if (HG.getInstance().getGame().isGrace())
                e.setCancelled(true);
            if (Tribute.getTribute(p).isSpectator() || Tribute.getTribute(p2).isSpectator())
                e.setCancelled(true);
        }
        if (e.getDamager() instanceof Snowball)
        {
            e.setDamage(12.0);
        }
        if (e.getDamager() instanceof Arrow)
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 5, 1));
    }
}
