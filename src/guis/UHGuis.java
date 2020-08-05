package guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;

public class UHGuis {
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private Inventory normale = Bukkit.createInventory(null, 54,"Iteme Normal");
	private Inventory boss = Bukkit.createInventory(null, 54,"Iteme Boss");

	
	public void loadNormale() {
		normale.clear();
		if(pl.rewards.getCfg().contains("RecompenseNormal.Iteme")) {
			for(String s : pl.rewards.getCfg().getConfigurationSection("RecompenseNormal.Iteme").getKeys(false)) {
				normale.setItem(Integer.parseInt(s), pl.rewards.getCfg().getItemStack("RecompenseNormal.Iteme."+s+".Item"));
			}
		}
	}
	public void loadBoss() {
		boss.clear();
		if(pl.rewards.getCfg().contains("RecompenseBoss.Iteme")) {
			for(String s : pl.rewards.getCfg().getConfigurationSection("RecompenseBoss.Iteme").getKeys(false)) {
				boss.setItem(Integer.parseInt(s), pl.rewards.getCfg().getItemStack("RecompenseBoss.Iteme."+s+".Item"));
			}
		}
	}
	public void openNormale(Player p) {
		loadNormale();
		p.openInventory(normale);
	}
	public void openBoss(Player p) {
		loadBoss();
		p.openInventory(boss);
	}
}
