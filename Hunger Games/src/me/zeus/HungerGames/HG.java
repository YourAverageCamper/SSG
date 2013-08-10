
package me.zeus.HungerGames;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import me.zeus.HungerGames.Commands.CMD_AddSpawn;
import me.zeus.HungerGames.Commands.CMD_DeathMatch;
import me.zeus.HungerGames.Commands.CMD_DeleteSpawn;
import me.zeus.HungerGames.Commands.CMD_ForceStart;
import me.zeus.HungerGames.Commands.CMD_Gamble;
import me.zeus.HungerGames.Commands.CMD_Points;
import me.zeus.HungerGames.Commands.CMD_SetLobby;
import me.zeus.HungerGames.Commands.CMD_Spectate;
import me.zeus.HungerGames.Commands.CMD_Vote;
import me.zeus.HungerGames.Commands.SimpleCommand;
import me.zeus.HungerGames.Enums.GameTime;
import me.zeus.HungerGames.Events.EVT_Block;
import me.zeus.HungerGames.Events.EVT_Chat;
import me.zeus.HungerGames.Events.EVT_Damage;
import me.zeus.HungerGames.Events.EVT_Hax;
import me.zeus.HungerGames.Events.EVT_Interact;
import me.zeus.HungerGames.Events.EVT_ItemDrop;
import me.zeus.HungerGames.Events.EVT_ItemPickup;
import me.zeus.HungerGames.Events.EVT_Join;
import me.zeus.HungerGames.Events.EVT_KillDeath;
import me.zeus.HungerGames.Events.EVT_Move;
import me.zeus.HungerGames.Events.EVT_PVP;
import me.zeus.HungerGames.Events.EVT_Ping;
import me.zeus.HungerGames.Events.EVT_Quit;
import me.zeus.HungerGames.Events.EVT_Respawn;
import me.zeus.HungerGames.Events.EVT_TributeDeath;
import me.zeus.HungerGames.Objects.Countdown;
import me.zeus.HungerGames.Objects.Game;
import me.zeus.HungerGames.Objects.GameMap;
import me.zeus.HungerGames.Objects.Podium;
import me.zeus.HungerGames.Objects.TempMap;
import me.zeus.HungerGames.Objects.Tribute;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;



public class HG extends JavaPlugin
{
    
    
    private static HG instance;
    ConfigHandler cfgHandler;
    Game game;
    File rootDir;
    File playersDir;
    File mapsDir;
    File config;
    File chestsFile;
    Map<String, Tribute> players;
    boolean firstTime;
    boolean autoStart;
    List<String> voters;
    Map<String, GameMap> maps;
    Map<String, TempMap> tempMaps;
    Location lobby;
    
    Scoreboard sb;
    Objective votesObjective;
    Score map1;
    Score map2;
    Score map3;
    Score map4;
    public int voteTime;
    
    List<String> worldNames;
    Set<String> frozen;
    List<Location> chests;
    List<ItemStack> tier1;
    List<ItemStack> tier2;
    List<ItemStack> tier3;
    
    
    
    /*
     * (non-Javadoc)
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
    public void onEnable()
    {
        instance = this;
        tier1 = new ArrayList<ItemStack>();
        tier2 = new ArrayList<ItemStack>();
        tier3 = new ArrayList<ItemStack>();
        voters = new ArrayList<String>();
        maps = new HashMap<String, GameMap>();
        tempMaps = new HashMap<String, TempMap>();
        players = new HashMap<String, Tribute>();
        game = new Game();
        game.setGameTime(GameTime.LOBBY);
        frozen = new HashSet<String>();
        chests = new ArrayList<Location>();
        
        init_files();
        cfgHandler = new ConfigHandler(config);
        worldNames = getConfig().getStringList("worlds");
        init_data();
        init_events();
        init_commands();
        init_chests();
        init_maps();
        
        File[] worlds = getServer().getWorldContainer().listFiles();
        for (File f : worlds)
            for (String w : worldNames)
                if (f.getName().equals(w))
                {
                    if (Bukkit.getWorld(w) == null)
                        try
                        {
                            Bukkit.createWorld(new WorldCreator(w));
                            System.out.println("Creating world " + w + " because it is null.");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    if (GameMap.getMap(w) == null)
                    {
                        GameMap gm = new GameMap();
                        gm.setName(w);
                        gm.setLocation(Bukkit.getWorld(w).getSpawnLocation());
                        gm.save();
                        maps.put(w, gm);
                        Bukkit.broadcastMessage("§4Created map: " + w + ". Must be configured first!");
                    }
                }
        
        if (autoStart)
            init_vote();
        
        init_items();
    }
    
    
    
    /*
     * (non-Javadoc)
     * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
     */
    @Override
    public void onDisable()
    {
        instance = null;
        for (Tribute t : players.values())
            try
            {
                File f = new File(playersDir + "/" + t.getName() + ".dat");
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(t);
                oos.close();
                System.out.println("Saved data for player: " + t.getName());
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                System.out.println("Error saving player data: " + t.getName() + "!");
            }
    }
    
    
    
    /*
     * Create and load files
     */
    private void init_files()
    {
        rootDir = new File(getDataFolder() + "");
        if (!rootDir.exists())
        {
            rootDir.mkdir();
            firstTime = true;
        }
        
        playersDir = new File(getDataFolder() + "/players/");
        if (!playersDir.exists())
            playersDir.mkdir();
        
        config = new File(getDataFolder() + "/config.yml");
        if (!config.exists())
            saveDefaultConfig();
        
        chestsFile = new File(getDataFolder() + "/chests.yml");
        if (!chestsFile.exists())
            try
            {
                chestsFile.createNewFile();
                FileConfiguration fc = YamlConfiguration.loadConfiguration(chestsFile);
                fc.set("tier-one", new ArrayList<Integer>());
                fc.set("tier-two", new ArrayList<Integer>());
                fc.set("tier-three", new ArrayList<Integer>());
                fc.save(chestsFile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        
        mapsDir = new File(getDataFolder() + "/maps/");
        if (!mapsDir.exists())
            mapsDir.mkdir();
    }
    
    
    
    /*
     * Start the main game countdown
     */
    private void init_countdown()
    {
        
        if (Bukkit.getServer().getOnlinePlayers().length >= cfgHandler.getMinPlayers())
        {
            game.setGameTime(GameTime.PREGAME);
            Podium[] podium = game.getMap().getPodiums().toArray(new Podium[game.getMap().getPodiums().size() - 1]);
            for (int i = 0; i < podium.length; i++)
                for (Player p : Bukkit.getServer().getOnlinePlayers())
                {
                    if (!frozen.contains(p.getName()))
                        frozen.add(p.getName());
                    if (!podium[i].isOccupied())
                    {
                        p.teleport(podium[i].getLocation());
                        podium[i].setOccupied(true);
                    }
                    else
                        continue;
                }
            Countdown c = new Countdown();
            c.startCountdown(getConfig().getInt("countdown-time"));
        }
        else
        {
            voters.clear();
            init_vote();
            Bukkit.broadcastMessage("[§bMCTheFallen§f] §5Not enough players to start game!");
        }
    }
    
    
    
    /*
     * Register events
     */
    private void init_events()
    {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EVT_PVP(), this);
        pm.registerEvents(new EVT_Join(), this);
        pm.registerEvents(new EVT_Block(), this);
        pm.registerEvents(new EVT_Move(), this);
        pm.registerEvents(new EVT_Interact(), this);
        pm.registerEvents(new EVT_KillDeath(), this);
        pm.registerEvents(new EVT_Damage(), this);
        pm.registerEvents(new EVT_Respawn(), this);
        pm.registerEvents(new EVT_ItemDrop(), this);
        pm.registerEvents(new EVT_ItemPickup(), this);
        pm.registerEvents(new EVT_TributeDeath(), this);
        pm.registerEvents(new EVT_Ping(), this);
        pm.registerEvents(new EVT_Quit(), this);
        pm.registerEvents(new EVT_Chat(), this);
        pm.registerEvents(new EVT_Hax(), this);
    }
    
    
    
    /*
     * Load and grab data
     */
    private void init_data()
    {
        File[] playerFiles = playersDir.listFiles();
        for (int i = 0; i < playerFiles.length; i++)
        {
            File f = playerFiles[i];
            try
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                Tribute t = (Tribute) ois.readObject();
                players.put(t.getName(), t);
                ois.close();
                System.out.println("Loaded player data for " + t.getName());
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        
        File[] mapFiles = mapsDir.listFiles();
        for (int i = 0; i < mapFiles.length; i++)
        {
            File f = mapFiles[i];
            try
            {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                GameMap gm = (GameMap) ois.readObject();
                maps.put(gm.getName(), gm);
                ois.close();
                System.out.println("Map " + gm.getName() + " is ready to be played!");
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    
    
    /*
     * Starts the map voting
     */
    private void init_vote()
    {
        sb = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        votesObjective = sb.registerNewObjective("map_votes", "dummy");
        votesObjective.setDisplayName("§aNext Map:");
        votesObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        Set<String> temp = new HashSet<String>();
        for (String key : maps.keySet())
            temp.add(key);
        
        final Map<Score, String> voteMaps = new HashMap<Score, String>();
        
        
        final String m_1 = getRandomMap(temp);
        temp.remove(m_1);
        final String m_2 = getRandomMap(temp);
        temp.remove(m_2);
        final String m_3 = getRandomMap(temp);
        temp.remove(m_3);
        final String m_4 = getRandomMap(temp);
        temp.remove(m_4);
        
        map1 = votesObjective.getScore(Bukkit.getOfflinePlayer(m_1));
        map1.setScore(0);
        voteMaps.put(map1, m_1);
        map2 = votesObjective.getScore(Bukkit.getOfflinePlayer(m_2));
        voteMaps.put(map2, m_2);
        map2.setScore(0);
        map3 = votesObjective.getScore(Bukkit.getOfflinePlayer(m_3));
        voteMaps.put(map3, m_3);
        map3.setScore(0);
        map4 = votesObjective.getScore(Bukkit.getOfflinePlayer(m_4));
        voteMaps.put(map4, m_4);
        map4.setScore(0);
        
        for (Player p : getServer().getOnlinePlayers())
            p.setScoreboard(sb);
        
        final int task1 = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            
            
            @Override
            public void run()
            {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage("[§bMCTheFallen§f] §31) §d" + m_1);
                Bukkit.broadcastMessage("[§bMCTheFallen§f] §32) §d" + m_2);
                Bukkit.broadcastMessage("[§bMCTheFallen§f] §33) §d" + m_3);
                Bukkit.broadcastMessage("[§bMCTheFallen§f] §34) §d" + m_4);
                Bukkit.broadcastMessage("[§bMCTheFallen§f] §e§lVote for the next map with /vote <id> !");
                Bukkit.broadcastMessage("");
            }
        }, 0L, 20L * 15);
        
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
        {
            
            
            @Override
            public void run()
            {
                getServer().getScheduler().cancelTask(task1);
                for (Player p : getServer().getOnlinePlayers())
                {
                    voters.add(p.getName());
                    p.setScoreboard(getServer().getScoreboardManager().getNewScoreboard());
                }
                Integer[] i = new Integer[] { map1.getScore(), map2.getScore(), map3.getScore(), map4.getScore() };
                
                List<Score> scores = new ArrayList<Score>();
                scores.addAll(Arrays.asList(map1, map2, map3, map4));
                for (Score m : scores)
                    if (findLargest(i) == m.getScore())
                    {
                        String s = voteMaps.get(m);
                        Bukkit.broadcastMessage("§cVoting is over! Next map is...§e" + voteMaps.get(m));
                        if (tempMaps.containsKey(s))
                        {
                            Bukkit.broadcastMessage("[§bMCTheFallen§f] §3Map: §e" + tempMaps.get(s).getName());
                            Bukkit.broadcastMessage("[§bMCTheFallen§f] §3Description: §e" + tempMaps.get(s).getDescription());
                            Bukkit.broadcastMessage("[§bMCTheFallen§f] §3Author(s): §e" + tempMaps.get(s).getAuthors());
                            Bukkit.broadcastMessage("[§bMCTheFallen§f] §3Link: §e" + tempMaps.get(s).getDownload());
                        }
                        game.setMap(s);
                        Bukkit.getWorld(s).setAutoSave(false);
                        init_countdown();
                        break;
                    }
            }
        }, 20L * voteTime);
        
    }
    
    
    
    /*
     * Register commands
     */
    private void init_commands()
    {
        SimpleCommand.registerCommands(this, new CMD_AddSpawn());
        SimpleCommand.registerCommands(this, new CMD_DeleteSpawn());
        SimpleCommand.registerCommands(this, new CMD_ForceStart());
        SimpleCommand.registerCommands(this, new CMD_Vote());
        SimpleCommand.registerCommands(this, new CMD_SetLobby());
        SimpleCommand.registerCommands(this, new CMD_Points());
        SimpleCommand.registerCommands(this, new CMD_Gamble());
        SimpleCommand.registerCommands(this, new CMD_DeathMatch());
        SimpleCommand.registerCommands(this, new CMD_Spectate());
    }
    
    
    
    private void init_chests()
    {
        FileConfiguration fc = YamlConfiguration.loadConfiguration(chestsFile);
        
        List<Integer> ids1 = fc.getIntegerList("tier-one");
        for (int i : ids1)
            try
            {
                ItemStack is = new ItemStack(Material.getMaterial(i), 1);
                tier1.add(is);
            }
            catch (Exception e)
            {
                
            }
        
        List<Integer> ids2 = fc.getIntegerList("tier-two");
        for (int i : ids2)
            try
            {
                ItemStack is = new ItemStack(Material.getMaterial(i), 1);
                tier2.add(is);
            }
            catch (Exception e)
            {
                
            }
        
        List<Integer> ids3 = fc.getIntegerList("tier-three");
        for (int i : ids3)
            try
            {
                ItemStack is = new ItemStack(Material.getMaterial(i), 1);
                tier3.add(is);
            }
            catch (Exception e)
            {
                
            }
    }
    
    
    
    private void init_items()
    {
        ItemStack tom = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta meta = tom.getItemMeta();
        meta.setDisplayName("§eTomahawk");
        meta.setLore(Arrays.asList("§5Throw at enemy for mass damage!"));
        tom.setItemMeta(meta);
        
        Iterator<Recipe> iter = getServer().recipeIterator();
        while (iter.hasNext())
        {
            Recipe r = iter.next();
            if (r.getResult().getType().equals(Material.IRON_AXE))
                iter.remove();
        }
        ShapedRecipe recipe = new ShapedRecipe(tom);
        recipe.shape(new String[] { "bb ", "ba ", " a " });
        recipe.setIngredient('b', Material.IRON_INGOT);
        recipe.setIngredient('a', Material.STICK);
        getServer().addRecipe(recipe);
    }
    
    
    
    private void init_maps()
    {
        if (getConfig() == null)
            saveDefaultConfig();
        
        ConfigurationSection cs = getConfig().getConfigurationSection("maps");
        for (String key : cs.getKeys(false))
        {
            String name = getConfig().getString("maps." + key + ".name");
            String description = getConfig().getString("maps." + key + ".description");
            String authors = getConfig().getString("maps." + key + ".authors");
            String download = getConfig().getString("maps." + key + ".download");
            tempMaps.put(key, new TempMap(name, description, authors, download));
        }
        
    }
    
    
    
    /*
     * Set the game to start automatically
     */
    public void setAutoStart(boolean boolean1)
    {
        this.autoStart = boolean1;
    }
    
    
    
    /*
     * Set the lobby location
     */
    public void setLobby(Location loc)
    {
        this.lobby = loc;
    }
    
    
    
    /*----------------------------------
     * 									|
     * 			GETTTERS				|
     * 									|
     *----------------------------------*/
    
    public static HG getInstance()
    {
        return instance;
    }
    
    
    
    public ConfigHandler getConfigHandler()
    {
        return cfgHandler;
    }
    
    
    
    public Map<String, Tribute> getPlayers()
    {
        return players;
    }
    
    
    
    public Game getGame()
    {
        return game;
    }
    
    
    
    public List<String> getVoted()
    {
        return voters;
    }
    
    
    
    public Location getLobby()
    {
        return lobby;
    }
    
    
    
    public Map<String, GameMap> getMaps()
    {
        return maps;
    }
    
    
    
    public String getRandomMap(Set<String> maps)
    {
        String[] a = maps.toArray(new String[maps.size() - 1]);
        int select = new Random().nextInt((a.length));
        return a[select];
    }
    
    
    
    public Score getMap(int number)
    {
        switch (number)
        {
            case 1:
                return map1;
            case 2:
                return map2;
            case 3:
                return map3;
            case 4:
                return map4;
        }
        return null;
    }
    
    
    
    public static int findLargest(Integer[] numbers)
    {
        int largest = numbers[0];
        for (int i = 1; i < numbers.length; i++)
            if (numbers[i] > largest)
                largest = numbers[i];
        return largest;
    }
    
    
    
    public Scoreboard getScoreboard()
    {
        return sb;
    }
    
    
    
    public Set<String> getFrozen()
    {
        return frozen;
    }
    
    
    
    public List<Location> getChests()
    {
        return chests;
    }
    
    
    
    public List<ItemStack> getTierOne()
    {
        return tier1;
    }
    
    
    
    public List<ItemStack> getTierTwo()
    {
        return tier2;
    }
    
    
    
    public List<ItemStack> getTierThree()
    {
        return tier3;
    }
}
