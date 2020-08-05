package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntitySpider;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class CustomSpider extends EntitySpider implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private Spider sp;
	private LivingEntity livsp;

	public CustomSpider(World world) {
		super(EntityTypes.SPIDER, ((CraftWorld)world).getHandle());
		sp = (Spider)this.getBukkitEntity();
		LivingEntity spider = sp;
		livsp=sp;
		
		spider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(pl.cspiderhp);
		spider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
		spider.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
		
		spider.setHealth(pl.cspiderhp);
		this.getWorld().addEntity(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	public LivingEntity getEntity() {
		return livsp;
	}
	public void kill() {
		sp.remove();
	}
	
	
	private List<Player> damagers = new ArrayList<Player>();
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(livsp)) {
			if(e.getDamager()instanceof Player) {
				if(!damagers.contains((Player) e.getDamager())) {
					damagers.add((Player) e.getDamager());
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity().equals(livsp)) {
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
