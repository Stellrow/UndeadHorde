package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.EntityWither;

public class CustomWither extends EntityWither implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private Wither wi;
	private LivingEntity livboss;
	private int maxHealth = pl.getConfig().getInt("WitherBossConfig.health");
	private int minionsAlive = 0;
	private List<Entity> minions = new ArrayList<Entity>();
	private List<Player> damagers = new ArrayList<Player>();
	private World world;
	
	public CustomWither(World world) {
		super(EntityTypes.WITHER, ((CraftWorld)world).getHandle());
		this.world=world;
		wi = (Wither)this.getBukkitEntity();
		
		livboss=wi;
		livboss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
		livboss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(20);
		
		
		livboss.setHealth(maxHealth);
		this.getWorld().addEntity(this);
		refreshName();
		pickSkill();
		pl.getServer().getPluginManager().registerEvents(this, pl);
		
	}
	
	
	
	//Utility
	public void refreshName() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(!wi.isDead()) {
					wi.setCustomName(ChatColor.RED+""+ChatColor.BOLD+"Arsone" + ChatColor.GRAY+"|"+ChatColor.RED+(int)wi.getHealth()+""+"/"+maxHealth);
				}
				
			}
			
		}.runTaskTimer(pl, 0, 10);
	}
	public void cleanUpMinions() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(!minions.isEmpty()) {
					for(Entity e : minions) {
						e.remove();
					}
				}
				minions.clear();
				minionsAlive=0;
			}
			
		}.runTaskLater(pl, 300*20);
		
	}
	public int retRandom(int bound) {
		Random rnd = new Random();
		return rnd.nextInt(bound);
	}
	public void spawnEffects() {
		new BukkitRunnable() {

			@Override
			public void run() {
				livboss.getWorld().playSound(livboss.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 2);
				livboss.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, livboss.getLocation(), 1);
			}
			
		}.runTaskLater(pl, 10*20);
		}
	
	
	public void anihilate(Player p) {
		if(livboss.isDead()) {
			return;
		}
		new BukkitRunnable() {

			@Override
			public void run() {
				if(p.getLocation().distance(livboss.getLocation())<=20) {
					p.setHealth(6);
				}
			}
			
		}.runTaskLater(pl, 10*20);
		
	}
	public void playTicks(Player p) {
		
		new BukkitRunnable() {
			int times = 0;
			@Override
			public void run() {
				if(livboss.isDead()) {
					return;
				}
				if(times>=10) {
					this.cancel();
					return;
				}
				times++;
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 2);
				
			}
			
		}.runTaskTimer(pl, 0, 20);
	}
	public void killAllMinions() {
		minionsAlive =0;
		for(Entity e : minions) {
			e.remove();
		}
		minions.clear();
	}
	public LivingEntity returnEntity() {
		return livboss;
	}
	//Mechanics
	
	
	//Invulnerable while minions alive
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if(e.getEntity()instanceof Player) {
			if(e.getDamager() instanceof WitherSkull) {
				WitherSkull ws = (WitherSkull) e.getDamager();
				if(ws.getShooter().equals(livboss)) {
					e.setDamage(15);
				}
			}
		}
		if(e.getEntity().equals(livboss)) {
			if(e.getDamager()instanceof Projectile) {
				Projectile proj = (Projectile) e.getDamager();
				if(proj.getShooter() instanceof Player) {
					if(minionsAlive>=1) {
				((Player)proj.getShooter()).sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("WitherBossConfig.mesajMinioniInViata")));
				}}
			}
			if(minionsAlive>=1) {
				e.setCancelled(true);
			}
			if(e.getDamager() instanceof Player) {
			//Check minions
			if(minionsAlive>=1) {
				e.setDamage(0);
				e.setCancelled(true);
				e.getDamager().sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("WitherBossConfig.mesajMinioniInViata")));
				return;
			}
			//Add to reward list
			if(!damagers.contains((Player) e.getDamager())) {
				damagers.add((Player) e.getDamager());
			}
			}	
		}
	}
	//Minion death
	@EventHandler
	public void minionDeath(EntityDeathEvent e) {
		if(minions.contains(e.getEntity())) {
			minionsAlive--;
			minions.remove(e.getEntity());
			e.setDroppedExp(0);
			e.getDrops().clear();
		}
	}
	//Boss death
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity().equals(livboss)) {
			pl.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("WitherBossConfig.mesajMoarte")));
			e.setDroppedExp(0);
			e.getDrops().clear();
			killAllMinions();
		}
	}
	//Boss breaking blocks
	@EventHandler
	public void onBreak(EntityChangeBlockEvent e) {
		if(e.getEntity().equals(livboss)) {
			e.setCancelled(true);
		}
	}
	
	
	
	//Skills
	public void pickSkill() {
		new BukkitRunnable() {

			@Override
			public void run() {
				Random rnd = new Random();
				if(livboss.isDead()) {
					this.cancel();
					return;
				}
				Integer picked = rnd.nextInt(3);
				if(picked.equals(0)) {
					if(minionsAlive>=1) {
						return;
					}
					raiseMinion();
				}
				if(picked.equals(1)) {
					poison();
				}
				if(picked.equals(2)) {
					annihilation();
				}
				
			
			}
		}.runTaskTimer(pl, 0, 20*20);
	}
	
	
	//Raise minions
	public void raiseMinion() {
		livboss.getWorld().playSound(livboss.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 2, 2);
		minionsAlive=4;
		minions.add(new WitherMinion(world).getEnt());
		minions.add(new WitherMinion(world).getEnt());
		minions.add(new WitherMinion(world).getEnt());
		minions.add(new WitherMinion(world).getEnt());
		for(Entity e : minions) {
			e.teleport(livboss.getLocation());
		}
		cleanUpMinions();
	}
	//Mass Poison
	public void poison() {
		for(Entity e : livboss.getNearbyEntities(20, 20, 20)) {
			if(e instanceof Player) {
				((Player) e).playSound(e.getLocation(), Sound.ENTITY_ENDERMAN_DEATH, 1, 1);
				((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.POISON,10*20,2));
				((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.WITHER,10*20,2));
				
			}
		}
	}
	
	//Annihilation
	public void annihilation() {
		for(Entity e : livboss.getNearbyEntities(30, 30, 30)) {
			if(e instanceof Player) {
				e.sendMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("WitherBossConfig.mesajAnihilare")));
				anihilate((Player) e);
				playTicks((Player) e);
				spawnEffects();
			}
		}
	}
	
	
	
	
	

}
