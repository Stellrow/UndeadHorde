package commnds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


import main.UHMain;
import utils.CEUtil;

public class UHCommands implements CommandExecutor{
	
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);

	@Override
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args) {
		if(sender instanceof Player) {
		Player p = (Player) sender;
		if(args.length>=2&&args[0].equalsIgnoreCase("editreward")) {
			if(p.hasPermission("undeadhorde.editrewards")) {
			if(args[1].equalsIgnoreCase("normal")) {
				pl.gui.openNormale(p);
				return true;
			}
			if(args[1].equalsIgnoreCase("boss")) {
				pl.gui.openBoss(p);
				return true;
			}
		}else {
			p.sendMessage(ChatColor.RED+"Nu ai permisiune!");
		}
		}
		if(args.length>=1&&args[0].equalsIgnoreCase("start")) {
			if(p.hasPermission("undeadhorde.start")) {
			pl.startHorde();
			return true;
		}else {
			p.sendMessage(ChatColor.RED+"Nu ai permisiune!");
		}
		}
		if(args.length>=1&&args[0].equalsIgnoreCase("reload")) {
			if(p.hasPermission("undeadhorde.reload")) {
			pl.reloadConfig();
			pl.loadSpawns();
			pl.loadSettings();
			pl.rewards.reload();
			pl.rew.loadRewards();
			CEUtil.loadEquipment();
			p.sendMessage(ChatColor.GRAY+"[UndeadHorde]Reincarcat cu succes!");
			}else {
				p.sendMessage(ChatColor.RED+"Nu ai permisiune!");
			}
			return true;
		}
		if(args.length>=2&&args[0].equalsIgnoreCase("setspawn")) {
			if(p.hasPermission("undeadhorde.setspawn")) {
			p.sendMessage(ChatColor.GRAY+"Locatia "+ChatColor.GREEN+args[1] +ChatColor.GRAY+" a fost salvata cu succes in lista de spawnuri");
			pl.getConfig().set("HordeConfig.spawnLoc."+args[1], p.getLocation());
			if(pl.spawnLocations.contains(p.getLocation())) {
			pl.spawnLocations.remove(p.getLocation());
			pl.spawnLocations.add(p.getLocation());
			}else {
				pl.spawnLocations.add(p.getLocation());
			}
			pl.saveConfig();
			return true;
		}else {
			p.sendMessage(ChatColor.RED+"Nu ai permisiune!");
		}
		}
		if(args.length>=1&&args[0].equalsIgnoreCase("setboss")) {
			if(p.hasPermission("undeadhorde.setspawn")) {
			p.sendMessage(ChatColor.GREEN+"Spawnul bossului a fost setat!");
			pl.getConfig().set("HordeConfig.bossspawn", p.getLocation());
			pl.saveConfig();
			}else {
				p.sendMessage(ChatColor.RED+"Nu ai permisiune!");
			}
		}
		
		}
		return true;
	}

}
