package guis;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;

public class GuiEventUH implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	
	@EventHandler
	public void closeInv(InventoryCloseEvent e) {
		if(e.getView().getTitle().equals("Iteme Normal")) {
			for(int x = 0;x<=53;x++) {
				if(e.getInventory().getItem(x)!=null) {
					saveItemNormal(x,e.getInventory().getItem(x));
				}else {
					pl.rewards.getCfg().set("RecompenseNormal.Iteme."+x, null);
				}
			}
			pl.rewards.save();
		}
		if(e.getView().getTitle().equals("Iteme Boss")) {
			for(int x = 0;x<=53;x++) {
				if(e.getInventory().getItem(x)!=null) {
					saveItemBoss(x,e.getInventory().getItem(x));
				}else {
					pl.rewards.getCfg().set("RecompenseBoss.Iteme."+x, null);
				}
			}
			pl.rewards.save();
		}
	}
	
	
	private void saveItemNormal(Integer i,ItemStack item) {
		pl.rewards.getCfg().set("RecompenseNormal.Iteme."+i+".Item", item);
		if(!pl.rewards.getCfg().contains("RecompenseNormal.Iteme."+i+".Sansa")) {
			pl.rewards.getCfg().set("RecompenseNormal.Iteme."+i+".Sansa", 50);
		}
	}
	private void saveItemBoss(Integer i,ItemStack item) {
		pl.rewards.getCfg().set("RecompenseBoss.Iteme."+i+".Item", item);
		if(!pl.rewards.getCfg().contains("RecompenseBoss.Iteme."+i+".Sansa")) {
			pl.rewards.getCfg().set("RecompenseBoss.Iteme."+i+".Sansa", 50);
		}
	}

}
