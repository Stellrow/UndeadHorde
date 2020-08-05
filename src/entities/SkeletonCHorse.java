package entities;

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.SkeletonHorse;

import net.minecraft.server.v1_15_R1.EntityHorseSkeleton;
import net.minecraft.server.v1_15_R1.EntityTypes;

public class SkeletonCHorse extends EntityHorseSkeleton{
	private LivingEntity livhorse;
	private SkeletonHorse hs;

	public SkeletonCHorse(World var1) {
		super(EntityTypes.SKELETON_HORSE, ((CraftWorld)var1).getHandle());
		
		hs = (SkeletonHorse) this.getBukkitEntity();
		livhorse = hs;
		livhorse.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
		livhorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
		livhorse.setHealth(500);
		
		this.getWorld().addEntity(this);
		
	}
	public LivingEntity getHorse() {
		return livhorse;
	}

}
