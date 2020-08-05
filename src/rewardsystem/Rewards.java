package rewardsystem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;
import utils.RandomCollection;

public class Rewards {
	private RandomCollection<CommandReward> comenziNormale = new RandomCollection<CommandReward>();
	private RandomCollection<ItemStack> itemeNormale = new RandomCollection<ItemStack>();
	private RandomCollection<ItemStack> itemeBoss = new RandomCollection<ItemStack>();
	private RandomCollection<CommandReward> comenziBoss = new RandomCollection<CommandReward>();
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	public Rewards() {
		loadRewards();
	}
	
	
	public void loadRewards() {
		//Comenzi
		comenziNormale.clear();
		if(pl.rewards.getCfg().contains("RecompenseNormal.Comenzi")) {
			for(String s : pl.rewards.getCfg().getConfigurationSection("RecompenseNormal.Comenzi").getKeys(false)) {
				comenziNormale.add(pl.rewards.getCfg().getDouble("RecompenseNormal.Comenzi."+s+".Sansa"),new CommandReward("RecompenseNormal.Comenzi."+s+".Comenzi"));
			}
		}
		comenziBoss.clear();
		if(pl.rewards.getCfg().contains("RecompenseBoss.Comenzi")) {
			for(String s : pl.rewards.getCfg().getConfigurationSection("RecompenseBoss.Comenzi").getKeys(false)) {
				comenziBoss.add(pl.rewards.getCfg().getDouble("RecompenseBoss.Comenzi."+s+".Sansa"),new CommandReward("RecompenseBoss.Comenzi."+s+".Comenzi"));
			}
		}
		//Iteme
		itemeNormale.clear();
		if(pl.rewards.getCfg().contains("RecompenseNormal.Iteme")) {
			for(String s : pl.rewards.getCfg().getConfigurationSection("RecompenseNormal.Iteme").getKeys(false)) {
				itemeNormale.add(pl.rewards.getCfg().getDouble("RecompenseNormal.Iteme."+s+".Sansa"), pl.rewards.getCfg().getItemStack("RecompenseNormal.Iteme."+s+".Item"));
			}
		}
		itemeBoss.clear();
		if(pl.rewards.getCfg().contains("RecompenseBoss.Iteme")) {
			for(String s : pl.rewards.getCfg().getConfigurationSection("RecompenseBoss.Iteme").getKeys(false)) {
				itemeBoss.add(pl.rewards.getCfg().getDouble("RecompenseBoss.Iteme."+s+".Sansa"), pl.rewards.getCfg().getItemStack("RecompenseBoss.Iteme."+s+".Item"));
			}
		}
	}
	public void alegeItemNormal(Player p) {
		p.getInventory().addItem(itemeNormale.next());
	}
	public void alegeItemBoss(Player p) {
		p.getInventory().addItem(itemeBoss.next());
	}
	public void alegeComandaNormal(Player p) {
		comenziNormale.next().executeCommands(p);
	}
	public void alegeComandaBoss(Player p) {
		comenziBoss.next().executeCommands(p);
	}
	

}
