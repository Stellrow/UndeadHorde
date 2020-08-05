package utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;

public class CEUtil {
	private static UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	//Zombie
	public static ItemStack zhead,zchest,zlegs,zfoot,zmhand,zohand;
	//Skeleton
	public static ItemStack shead,schest,slegs,sfoot,smhand;
	//Illusioner
	public static ItemStack imhand;
	//Zombie Boss
	public static ItemStack zbhead,zbchest,zblegs,zbfoot,zbmhand;
	//Skeleton Boss
	public static ItemStack sbhead,sbchest,sblegs,sbfoot,sbhand;
	
	public static void loadEquipment() {
		//Zombie
		zhead = loadEquipment("ZombieEquipment","head");
		zchest = loadEquipment("ZombieEquipment","chest");
		zlegs = loadEquipment("ZombieEquipment","legs");
		zfoot = loadEquipment("ZombieEquipment","foot");
		zmhand = loadEquipment("ZombieEquipment","mainHand");
		zohand = loadEquipment("ZombieEquipment","offHand");
		//Skeleton
		shead = loadEquipment("SkeletonEquipment","head");
		schest = loadEquipment("SkeletonEquipment","chest");
		slegs = loadEquipment("SkeletonEquipment","legs");
		sfoot = loadEquipment("SkeletonEquipment","foot");
		smhand = loadEquipment("SkeletonEquipment","mainHand");
		//Illusioner
		imhand = loadEquipment("IllusionerEquipment","mainHand");
		//Zombie Boss
		zbhead = loadEquipment("ZombieBossConfig","head");
		zbchest= loadEquipment("ZombieBossConfig","chest");
		zblegs= loadEquipment("ZombieBossConfig","legs");
		zbfoot= loadEquipment("ZombieBossConfig","foot");
		zbmhand = loadEquipment("ZombieBossConfig","mainHand");
		//Skeleton Boss
		sbhead = loadEquipment("SkeletonBossConfig","head");
		sbchest= loadEquipment("SkeletonBossConfig","chest");
		sblegs= loadEquipment("SkeletonBossConfig","legs");
		sbfoot= loadEquipment("SkeletonBossConfig","foot");
		sbhand = loadEquipment("SkeletonBossConfig","mainHand");
	}
	public static ItemStack loadEquipment(String path,String part) {
		ItemStack toRet = new ItemStack(Material.valueOf(pl.getConfig().getString(path+"."+part+".Type")));
		ItemMeta im = toRet.getItemMeta();
		if(pl.getConfig().contains(path+"."+part+".Enchantment")) {
			for(String s : pl.getConfig().getStringList(path+"."+part+".Enchantment")) {
				String[] enchantment = s.split(" ");
				im.addEnchant(Enchantment.getByKey(NamespacedKey.minecraft(enchantment[0])), Integer.parseInt(enchantment[1]), true);
			}
		}
		toRet.setItemMeta(im);
		
		return toRet;
	}
	
	public static HashMap<Player, Double> sortByValue(HashMap<Player, Double> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<Player, Double> > list = 
               new LinkedList<Map.Entry<Player, Double> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<Player, Double> >() { 
            public int compare(Map.Entry<Player, Double> o1,  
                               Map.Entry<Player, Double> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<Player, Double> temp = new LinkedHashMap<Player, Double>(); 
        for (Map.Entry<Player, Double> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    }
	public static void rewardPlayer(Player p) {
		
	}
	

}
