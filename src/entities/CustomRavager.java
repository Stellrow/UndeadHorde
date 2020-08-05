package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Ravager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntityRavager;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class CustomRavager extends EntityRavager implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private Ravager rv;
	private LivingEntity livrv;
	
	public CustomRavager(World world) {
		super(EntityTypes.RAVAGER, ((CraftWorld)world).getHandle());
		rv=(Ravager)this.getBukkitEntity();
		LivingEntity ravager = rv;
		livrv=ravager;
		
		ravager.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(pl.cravagerhp);
		
		ravager.setHealth(pl.cravagerhp);
		
		this.getWorld().addEntity(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	public void kill() {
		rv.remove();
	}
	public LivingEntity getEntity() {
		return livrv;
	}
	
	private List<Player> damagers = new ArrayList<Player>();
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(livrv)) {
			if(e.getDamager()instanceof Player) {
				if(!damagers.contains((Player) e.getDamager())) {
					damagers.add((Player) e.getDamager());
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity().equals(livrv)) {
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
