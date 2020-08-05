package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
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

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntityZombie;
import utils.CEUtil;

public class CustomZombie extends EntityZombie implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private LivingEntity livz;
	private Zombie zom;
	public CustomZombie(World world) {
		
		super(((CraftWorld)world).getHandle());
		
		zom = (Zombie)this.getBukkitEntity();
		LivingEntity livzom = zom;
		livz=livzom;
		livzom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(pl.czombiehp);
		livzom.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10);
		
		
		livzom.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,99999,1));
		
		zom.getEquipment().setHelmet(CEUtil.zhead);
		zom.getEquipment().setChestplate(CEUtil.zchest);
		zom.getEquipment().setLeggings(CEUtil.zlegs);
		zom.getEquipment().setBoots(CEUtil.zfoot);
		zom.getEquipment().setItemInMainHand(CEUtil.zmhand);
		
		livzom.setHealth(pl.czombiehp);
		
		this.getWorld().addEntity(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);
		
	}
	public LivingEntity getEntity() {
		return livz;
	}
	public int retRandom() {
		Random rnd = new Random();
		return rnd.nextInt(100)+1;
	}
	public void kill() {
		zom.remove();
	}
	
	
	private List<Player> damagers = new ArrayList<Player>();
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(livz)) {
			if(e.getDamager()instanceof Player) {
				if(!damagers.contains((Player) e.getDamager())) {
					damagers.add((Player) e.getDamager());
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity().equals(livz)) {
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
