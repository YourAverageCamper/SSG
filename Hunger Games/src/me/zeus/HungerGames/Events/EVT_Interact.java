
package me.zeus.HungerGames.Events;


import java.util.Random;

import me.zeus.HungerGames.HG;
import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;



public class EVT_Interact implements Listener
{
    
    
    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        Tribute trib = Tribute.getTribute(e.getPlayer());
        
        if (trib.isSpectator())
            e.setCancelled(true);
        
        if (e.getAction() == Action.RIGHT_CLICK_AIR)
        {
            if (e.getPlayer().getItemInHand().getType().equals(Material.IRON_AXE))
            {
                if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
                    e.getPlayer().setItemInHand(null);
                Projectile projectile = e.getPlayer().launchProjectile(Snowball.class);
                projectile.setBounce(false);
                projectile.setVelocity(e.getPlayer().getLocation().getDirection().multiply(2.0));
            }
        }
        else if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
            if (!trib.isSpectator())
            {
                if (e.getClickedBlock() == null)
                    return;
                doChest(e.getClickedBlock());
            }
    }
    
    
    
    public void doChest(Block b)
    {
        if (b.getState() instanceof Chest)
        {
            Chest chest = (Chest) b.getState();
            
            if (HG.getInstance().getChests().contains(b.getLocation()))
                return;
            
            HG.getInstance().getChests().add(b.getLocation());
            
            
            int chance = new Random().nextInt(50) + 1;
            if (chance <= 10)
                fillChest(chest, 3);
            else if (chance <= 15)
                fillChest(chest, 2);
            else if (chance <= 75)
                fillChest(chest, 1);
        }
    }
    
    
    
    public void fillChest(Chest chest, int tier)
    {
        chest.getInventory().clear();
        int amtItems = new Random().nextInt(5) + 1;
        if (tier == 1)
            for (int i = 0; i < amtItems; i++)
            {
                int slot = new Random().nextInt(chest.getInventory().getSize());
                ItemStack[] items = HG.getInstance().getTierOne().toArray(new ItemStack[HG.getInstance().getTierOne().size() - 1]);
                int select = new Random().nextInt(items.length);
                chest.getInventory().setItem(slot, items[select]);
            }
        else if (tier == 2)
            for (int i = 0; i < amtItems; i++)
            {
                int slot = new Random().nextInt(chest.getInventory().getSize());
                ItemStack[] items = HG.getInstance().getTierTwo().toArray(new ItemStack[HG.getInstance().getTierTwo().size() - 1]);
                int select = new Random().nextInt(items.length);
                chest.getInventory().setItem(slot, items[select]);
            }
        else if (tier == 3)
            for (int i = 0; i < amtItems; i++)
            {
                int slot = new Random().nextInt(chest.getInventory().getSize());
                ItemStack[] items = HG.getInstance().getTierThree().toArray(new ItemStack[HG.getInstance().getTierThree().size() - 1]);
                int select = new Random().nextInt(items.length);
                chest.getInventory().setItem(slot, items[select]);
            }
    }
}
