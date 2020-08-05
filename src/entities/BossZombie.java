package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntityZombie;
import utils.CEUtil;

public class BossZombie extends EntityZombie implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private Zombie zom;
	private LivingEntity livboss;
	private List<Entity> minions = new ArrayList<Entity>();
	private int maxHealth = pl.getConfig().getInt("ZombieBossConfig.health");
	private List<Player> damagers = new ArrayList<Player>();
	public BossZombie(World world) {
		
		super(((CraftWorld)world).getHandle());
		
		zom = (Zombie)this.getBukkitEntity();
		LivingEntity livzom = zom;
		livzom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
		livzom.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
		livboss = zom;
		
		livzom.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,99999,1));
		
		zom.getEquipment().setHelmet(CEUtil.zbhead);
		zom.getEquipment().setChestplate(CEUtil.zbchest);
		zom.getEquipment().setLeggings(CEUtil.zblegs);
		zom.getEquipment().setBoots(CEUtil.zbfoot);
		zom.getEquipment().setItemInMainHand(CEUtil.zbmhand);
		
		livzom.setHealth(maxHealth);
		
		this.getWorld().addEntity(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);
		refreshName();
		specialAbillity();
	}
	public LivingEntity returnEntity() {
		return livboss;
	}
	@EventHandler
	public void dieBoss(EntityDeathEvent e) {
		if(e.getEntity().equals(livboss)) {
			pl.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messaje.mesajBossMoarte")));
			e.setDroppedExp(0);
			e.getDrops().clear();
			shareRewards();
			for(Entity nr : minions) {
				nr.remove();
			}
			minions.clear();
		}
	}
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(livboss)) {
			if(e.getDamager()instanceof Player) {
				if(!damagers.contains((Player) e.getDamager())) {
					damagers.add((Player) e.getDamager());
				}
			}
		}
	}
	
	public void shareRewards() {
		for(Player p : damagers) {
			if(retRandom(100)<=50) {
				pl.rew.alegeComandaBoss(p);
			}else {
				pl.rew.alegeItemBoss(p);
			}
		}
	}
	
	public void raiseMinions(Location loc) {
		minions.add(loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE));
		minions.add(loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE));
		minions.add(loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE));
	}
	
	
	public int retRandom() {
		Random rnd = new Random();
		return rnd.nextInt(100)+1;
	}
	public int retRandom(Integer bound) {
		Random rnd = new Random();
		return rnd.nextInt(bound)+1;
	}
	public void refreshName() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(!zom.isDead()) {
					zom.setCustomName(ChatColor.RED+""+ChatColor.BOLD+"Nautilus" + ChatColor.GRAY+"|"+ChatColor.RED+(int)zom.getHealth()+""+"/"+maxHealth);
				}
				
			}
			
		}.runTaskTimer(pl, 0, 10);
	}
	public void specialAbillity() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(zom.isDead()) {
					this.cancel();
					return;
				}
				Random rnd = new Random();
				switch(rnd.nextInt(2)) {
				case 0 : {raiseMinions(zom.getLocation());}
				case 1: {useAbilityThunder();}
				case 2: {teleportBehind();}
				}
			}
			
		}.runTaskTimer(pl, 0, 300);
	}
	public void useAbilityThunder() {
		if(!zom.isDead()) {
		for(Entity e :zom.getWorld().getNearbyEntities(zom.getLocation(), 10, 10, 10)) {
			if(e instanceof Player) {
				e.getWorld().strikeLightningEffect(e.getLocation());
				((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.POISON,60,2));
				((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.WITHER,60,0));
			}
		}
		}
	}
	
	public void teleportBehind() {
		if(!zom.isDead()) {
			if(zom.getTarget()!=null) {
			if(zom.getTarget()instanceof Player) {
				zom.getWorld().playSound(zom.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
				Player p = (Player) zom.getTarget();
				Vector v =p.getEyeLocation().getDirection().multiply(-1);
				zom.teleport(p.getLocation().add(v));
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,60,3));
				p.damage(20);
				zom.teleport(zom.getTarget().getEyeLocation());
			}
		}
		}
	}
}
