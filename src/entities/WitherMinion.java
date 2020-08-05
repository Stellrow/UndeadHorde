package entities;

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import main.UHMain;
import net.minecraft.server.v1_15_R1.EntitySkeletonWither;
import net.minecraft.server.v1_15_R1.EntityTypes;


public class WitherMinion extends EntitySkeletonWither implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private WitherSkeleton sw;
	private LivingEntity liv;

	public WitherMinion(World world) {
		super(EntityTypes.WITHER_SKELETON, ((CraftWorld)world).getHandle());
		
		sw = (WitherSkeleton)this.getBukkitEntity();
		liv =sw;
		liv.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100);
		liv.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
		liv.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
		liv.setHealth(100);
		
		this.getWorld().addEntity(this);
		checkTarget();
		pl.getServer().getPluginManager().registerEvents(this, pl);
		
	}
	
	@EventHandler
	public void witherDmg(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof WitherSkull) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(e.getEntity().equals(liv)) {
			e.setDroppedExp(0);
			e.getDrops().clear();
		}
	}
	public LivingEntity getEnt() {
		return liv;
	}
	public void checkTarget() {
		new BukkitRunnable() {

			@Override
			public void run() {
				if(sw.isDead()) {
					this.cancel();
					return;
				}
				if(sw.getTarget() instanceof Wither) {
					sw.setTarget(null);
				}
				
			}
			
		}.runTaskTimer(pl, 0, 10);
		
	}

}
