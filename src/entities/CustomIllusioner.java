package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntityIllagerIllusioner;
import net.minecraft.server.v1_15_R1.EntityTypes;
import utils.CEUtil;

public class CustomIllusioner extends EntityIllagerIllusioner implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private Illusioner ill;
	private LivingEntity livill;
	

	public CustomIllusioner(World world) {
		super(EntityTypes.ILLUSIONER, ((CraftWorld)world).getHandle());
		ill=(Illusioner)this.getBukkitEntity();
		LivingEntity illusioner = ill;
		livill=illusioner;
		
		illusioner.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(150);
		illusioner.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);
		illusioner.setHealth(150);
		
		
		illusioner.getEquipment().setItemInMainHand(CEUtil.imhand);
		
		this.getWorld().addEntity(this);
		pl.getServer().getPluginManager().registerEvents(this, pl);
		
		
	}
	public void kill() {
		ill.remove();
	}
	
	public LivingEntity getEntity() {
		return livill;
	}
	
	private List<Player> damagers = new ArrayList<Player>();
	@EventHandler
	public void onDmg(EntityDamageByEntityEvent e) {
		if(e.getEntity().equals(livill)) {
			if(e.getDamager()instanceof Player) {
				if(!damagers.contains((Player) e.getDamager())) {
					damagers.add((Player) e.getDamager());
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity().equals(livill)) {
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
