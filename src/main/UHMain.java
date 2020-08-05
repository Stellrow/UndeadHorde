package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import commnds.UHCommands;
import entities.BossSkeleton;
import entities.BossZombie;
import entities.CustomIllusioner;
import entities.CustomRavager;
import entities.CustomSkeleton;
import entities.CustomSpider;
import entities.CustomWither;
import entities.CustomZombie;
import events.EventsClass;
import guis.GuiEventUH;
import guis.UHGuis;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import rewardsystem.Rewards;
import utils.CEUtil;
import utils.CustomConfig;

public class UHMain extends JavaPlugin{
	public CustomConfig rewards;
	public UHGuis gui;
	public Rewards rew;
	public List<Location> spawnLocations = new ArrayList<Location>();
	public Location boss;
	//Horde Variables
	public boolean isStarted = false;
	public int nrzombie = getConfig().getInt("HordeConfig.nrZombieToSpawn");
	public int nrskeleti = getConfig().getInt("HordeConfig.nrSkeletonToSpawn");
	public int nrspider = getConfig().getInt("HordeConfig.nrSpiderToSpawn");
	public int nrravager = getConfig().getInt("HordeConfig.nrRavagerToSpawn");
	public int nrillusioner = getConfig().getInt("HordeConfig.nrIllusionerToSpawn");
	public int spawnWave1T= getConfig().getInt("HordeConfig.timpSpawnWave1");
	public int spawnWave2T = getConfig().getInt("HordeConfig.timpSpawnWave2");
	//Mesaje
	public String mesajIncepe = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messaje.hoardaIncepeIn").replaceFirst("%timpPrimAtac", spawnWave1T+""));
	public String mesajWaveNou = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messaje.waveNou").replaceFirst("%timpUrmWave", spawnWave2T+""));	
	//Skeletons
	public HashMap<Entity,CustomSkeleton> skeletons = new HashMap<Entity,CustomSkeleton>();
	//Zombies
	public HashMap<Entity,CustomZombie> zombies = new HashMap<Entity,CustomZombie>();
	//Spiders
	public HashMap<Entity,CustomSpider> spiders = new HashMap<Entity,CustomSpider>();
	//Ravagers
	public HashMap<Entity,CustomRavager> ravagers = new HashMap<Entity,CustomRavager>();
	//Illusioners
	public HashMap<Entity,CustomIllusioner> illusioners = new HashMap<Entity,CustomIllusioner>();
	//Custom Entitys Health
	public Entity newboss;
	public int czombiehp = getConfig().getInt("CustomEnityConfig.customzombiehp");
	public int cskeletonhp = getConfig().getInt("CustomEnityConfig.customskeletonhp");
	public int cspiderhp = getConfig().getInt("CustomEnityConfig.customspiderhp");
	public int cravagerhp = getConfig().getInt("CustomEnityConfig.customravagerhp");
	public int illusionerhp = getConfig().getInt("CustomEnityConfig.customillusionerhp");
	
	
	public void onEnable() {
		loadSpawns();
		loadSettings();
		CEUtil.loadEquipment();
		getCommand("undeadhorde").setExecutor(new UHCommands());
		getServer().getPluginManager().registerEvents(new EventsClass(), this);
		getServer().getPluginManager().registerEvents(new GuiEventUH(), this);
		rewards = new CustomConfig("rewards");
		rew=new Rewards();
		rew.loadRewards();
		gui=new UHGuis();
		rewards.getCfg().options().copyDefaults(true);
		rewards.save();
		CEUtil.loadEquipment();
	}
	
	
	public int returnRnd(Integer max) {
		Random rnd = new Random();
		return rnd.nextInt(max);
	}
	public void startWave1() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(mesajIncepe));
			p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_AMBIENT, 1, 1);
			p.playSound(p.getLocation(), Sound.ENTITY_SKELETON_AMBIENT, 1, 1);
		}
		getServer().broadcastMessage(mesajIncepe);
		cleanUp();
		new BukkitRunnable() {

			@Override
			public void run() {
				for(int x = 0;x<=nrzombie/4;x++) {
					CustomZombie zm = new CustomZombie(spawnLocations.get(0).getWorld());
					zombies.put(zm.getEntity(), zm);
					Location loc = spawnLocations.get(returnRnd(spawnLocations.size()));
					zm.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				}
				for(int x = 0;x<=nrskeleti/4;x++) {
					CustomSkeleton sk = new CustomSkeleton(spawnLocations.get(0).getWorld());
					skeletons.put(sk.getEntity(), sk);
					Location loc = spawnLocations.get(returnRnd(spawnLocations.size()));
					sk.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				}
				for(int x = 0;x<=nrspider/4;x++) {
					CustomSpider sk = new CustomSpider(spawnLocations.get(0).getWorld());
					spiders.put(sk.getEntity(), sk);
					Location loc = spawnLocations.get(returnRnd(spawnLocations.size()));
					sk.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				}
				startWave2();
			}
			
		}.runTaskLater(this, spawnWave1T*20);
		
	}
	public void startWave2() {
		getServer().broadcastMessage(mesajWaveNou);
		new BukkitRunnable() {

			@Override
			public void run() {
				for(int x = 0;x<=nrzombie/2;x++) {
					CustomZombie zm = new CustomZombie(spawnLocations.get(0).getWorld());
					zombies.put(zm.getEntity(), zm);
					Location loc = spawnLocations.get(returnRnd(spawnLocations.size()));
					zm.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				}
				for(int x = 0;x<=nrskeleti/2;x++) {
					CustomSkeleton sk = new CustomSkeleton(spawnLocations.get(0).getWorld());
					skeletons.put(sk.getEntity(), sk);
					Location loc = spawnLocations.get(returnRnd(spawnLocations.size()));
					sk.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				}
				for(int x = 0;x<=nrspider/2;x++) {
					CustomSpider sk = new CustomSpider(spawnLocations.get(0).getWorld());
					spiders.put(sk.getEntity(), sk);
					Location loc = spawnLocations.get(returnRnd(spawnLocations.size()));
					sk.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				}
				for(int x = 0;x<=nrravager/2;x++) {
					CustomRavager sk = new CustomRavager(spawnLocations.get(0).getWorld());
					ravagers.put(sk.getEntity(), sk);
					Location loc = spawnLocations.get(returnRnd(spawnLocations.size()));
					sk.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				}
				for(int x = 0;x<=nrillusioner/2;x++) {
					CustomIllusioner sk = new CustomIllusioner(spawnLocations.get(0).getWorld());
					illusioners.put(sk.getEntity(), sk);
					Location loc = spawnLocations.get(returnRnd(spawnLocations.size()));
					sk.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
				}
				getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messaje.bossCurand")));
				spawnBoss();
			}
			
			
		}.runTaskLater(this, spawnWave2T*20);
		
	}
	public void spawnBoss() {
		if(!getConfig().contains("HordeConfig.bossspawn")) {
			getServer().getConsoleSender().sendMessage("[UndeadHorde]Nu sa gasit locatia pentru a spawna bossul!");
			return;
		}
		Location loc = getConfig().getLocation("HordeConfig.bossspawn");
		Random rnd = new Random();
		Integer picked = rnd.nextInt(3);
		new BukkitRunnable() {

			@Override
			public void run() {
				if(picked.equals(0)) {
					BossZombie boss = new BossZombie(loc.getWorld());
					boss.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
					newboss=boss.returnEntity();
				}
				if(picked.equals(1)) {
					BossSkeleton boss = new BossSkeleton(loc.getWorld());
					boss.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
					newboss=boss.returnEntity();
				}
				if(picked.equals(2)) {
					CustomWither boss = new CustomWither(loc.getWorld());
					boss.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw());
					newboss=boss.returnEntity();
				}
				
			}
			
		}.runTaskLater(this, 60*20);
		
	}
	public void startHorde() {
		if(spawnLocations.isEmpty()) {
			getServer().getConsoleSender().sendMessage(ChatColor.RED+"[UndeadHorde] Comanda pentru pornire activata fara nici o locatie de spawn!");
			return;
		}
		isStarted = true;
		//Spawneaza Horde
		startWave1();
		
		
		
	}
	public void loadSettings() {
		//Horde
		nrzombie = getConfig().getInt("HordeConfig.nrZombieToSpawn");
		nrskeleti = getConfig().getInt("HordeConfig.nrSkeletonToSpawn");
		nrspider = getConfig().getInt("HordeConfig.nrSpiderToSpawn");
		nrravager = getConfig().getInt("HordeConfig.nrRavagerToSpawn");
		nrillusioner = getConfig().getInt("HordeConfig.nrIllusionerToSpawn");
		spawnWave1T= getConfig().getInt("HordeConfig.timpSpawnWave1");
		spawnWave2T = getConfig().getInt("HordeConfig.timpSpawnWave2");
		//Mesaje
		mesajIncepe = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messaje.hoardaIncepeIn").replaceFirst("%timpPrimAtac", spawnWave1T+""));
		mesajWaveNou = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messaje.waveNou").replaceFirst("%timpUrmWave", spawnWave2T+""));		
		//CustomHealth
		czombiehp = getConfig().getInt("CustomEnityConfig.customzombiehp");
		cskeletonhp = getConfig().getInt("CustomEnityConfig.customskeletonhp");
		cspiderhp = getConfig().getInt("CustomEnityConfig.customspiderhp");
		cravagerhp = getConfig().getInt("CustomEnityConfig.customravagerhp");
		illusionerhp = getConfig().getInt("CustomEnityConfig.customillusionerhp");
		
	}
	public void cleanUp() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(!zombies.isEmpty()) {
					for(Entity e : zombies.keySet()) {
						e.remove();
					}
					zombies.clear();
				}
				if(!skeletons.isEmpty()) {
					for(Entity e : skeletons.keySet()) {
						e.remove();
					}
					skeletons.clear();
				}
				if(!spiders.isEmpty()) {
					for(Entity e : spiders.keySet()) {
						e.remove();
					}
					spiders.clear();
				}
				if(!ravagers.isEmpty()) {
					for(Entity e : ravagers.keySet()) {
						e.remove();
					}
					ravagers.clear();
				}
				if(!illusioners.isEmpty()) {
					for(Entity e : illusioners.keySet()) {
						e.remove();
					}
					illusioners.clear();
				}
				newboss.remove();
			}
			
		}.runTaskLater(this, 600*20);
	}
	public void loadSpawns() {
		spawnLocations.clear();
		boss = getConfig().getLocation("HordeConfig.bossspawn");
		int spawnsFound = 0;
		if(getConfig().contains("HordeConfig.spawnLoc")) {
			for(String s : getConfig().getConfigurationSection("HordeConfig.spawnLoc").getKeys(false)) {
				spawnsFound++;
				spawnLocations.add((Location)getConfig().get("HordeConfig.spawnLoc."+s));
			}
			getServer().getConsoleSender().sendMessage("[UndeadHorde] Found "+spawnsFound+" spawn locations");
		}else {
			getServer().getConsoleSender().sendMessage("[UndeadHorde] No spawn locations were found!");
		}
	}

}
