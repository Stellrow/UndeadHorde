package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntitySkeleton;
import net.minecraft.server.v1_15_R1.EntityTypes;
import utils.CEUtil;

public class CustomSkeleton extends EntitySkeleton implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private Skeleton sk;
	private LivingEntity livsk;
	
	public CustomSkeleton(World world) {
		super(EntityTypes.SKELETON, ((CraftWorld) world).getHandle());
		
		
		sk = (Skeleton)this.getBukkitEntity();
		LivingEntity skeleton = sk;
		livsk=skeleton;
		skeleton.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(pl.cskeletonhp);
		
		sk.getEquipment().setHelmet(CEUtil.shead);
		sk.getEquipment().setChestplate(CEUtil.schest);
		sk.getEquipment().setLeggings(CEUtil.slegs);
		sk.getEquipment().setBoots(CEUtil.sfoot);
		sk.getEquipment().setItemInMainHand(CEUtil.smhand);
		
		sk.getEquipment().setBootsDropChance(0);
		
		
		skeleton.setHealth(pl.cskeletonhp);
		
		this.getWorld().addEntity(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	public void kill() {
		sk.remove();
	}
	public LivingEntity getEntity() {
		return livsk;
	}
	
	private List<Player> damagers = new ArrayList<Player>();
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(livsk)) {
			if(e.getDamager()instanceof Player) {
				if(!damagers.contains((Player) e.getDamager())) {
					damagers.add((Player) e.getDamager());
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity().equals(livsk)) {
			shareRewards();
			e.setDroppedExp(0);
			e.getDrops().clear();
		}
	}
	public void shareRewards() {
		for(Player p : damagers) {
			if(retRandom(100)<=50) {
				pl.rew.alegeComandaNormal(p);
			}else {
				pl.rew.alegeItemNormal(p);
			}
		}
	}
	public int retRandom(Integer bound) {
		Random rnd = new Random();
		return rnd.nextInt(bound)+1;
	}
	
	

	
	

}
