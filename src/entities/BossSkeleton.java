package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntityTypes;
import utils.CEUtil;

public class BossSkeleton extends EntitySkeleton implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private Skeleton sk;
	private LivingEntity livboss;
	private List<Player> damagers = new ArrayList<Player>();
	private int maxHealth = pl.getConfig().getInt("SkeletonBossConfig.health");
	
	public BossSkeleton(World var1) {
		super(EntityTypes.SKELETON, ((CraftWorld)var1).getHandle());
		
		sk=(Skeleton)this.getBukkitEntity();
		livboss = sk;
		livboss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
		livboss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,999,0));
		//Equipment
		sk.getEquipment().setHelmet(CEUtil.sbhead);
		sk.getEquipment().setChestplate(CEUtil.sbchest);
		sk.getEquipment().setLeggings(CEUtil.sblegs);
		sk.getEquipment().setBoots(CEUtil.sbfoot);
		sk.getEquipment().setItemInMainHand(CEUtil.sbhand);
		
		
		livboss.setHealth(maxHealth);
		
		this.getWorld().addEntity(this);
		refreshName();
		pl.getServer().getPluginManager().registerEvents(this, pl);
		
		
		
	}
	public LivingEntity returnEntity() {
		return livboss;
	}
	//Update display name
	public void refreshName() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(!sk.isDead()) {
					sk.setCustomName(ChatColor.AQUA+""+ChatColor.BOLD+"Stradus" + ChatColor.GRAY+"|"+ChatColor.RED+(int)sk.getHealth()+""+"/"+maxHealth);
				}
				
			}
			
		}.runTaskTimer(pl, 0, 10);
	}
	//Skills
	//Deflect arrow
	@EventHandler
	public void arrow(ProjectileHitEvent e) {
			if(e.getEntity() instanceof Arrow) {
				if(retRandom(100)<=50) {
					if(e.getEntity().getShooter() instanceof Player) {
						 Player p = (Player) e.getEntity().getShooter();
						 Vector dir = p.getEyeLocation().getDirection();
						 Arrow proj = livboss.launchProjectile(Arrow.class,dir.multiply(-1));
						 
						 proj.setPickupStatus(PickupStatus.DISALLOWED);
						 proj.setVelocity(proj.getVelocity().multiply(2));
						 
						 
						 
						 
					}
				}
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
	//Arrow Rain
	
	//Death
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity().equals(livboss)) {
			for(Entity en : e.getEntity().getNearbyEntities(5, 5, 5)) {
				if(en instanceof SkeletonHorse) {
					en.remove();
				}
			}
			pl.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', pl.getConfig().getString("Messaje.mesajBossMoarte")));
			e.setDroppedExp(0);
			e.getDrops().clear();
			shareRewards();
		}
	}
	//Utils
	public int retRandom(Integer bound) {
		Random rnd = new Random();
		return rnd.nextInt(bound);
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

	}

