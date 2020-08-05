package utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;


public class CustomConfig {
	private static UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	private File file;
	private FileConfiguration filecfg;
	public CustomConfig(String name) {
		createConfig(name);
	}
	
	private void createConfig(String name) {

		
		file = new File(pl.getDataFolder(),"rewards.yml");
		if(!file.exists()) {
				file.getParentFile().mkdirs();
				pl.saveResource("rewards.yml", false);
			}
		loadConfig();
	}
	private void loadConfig() {
		filecfg = YamlConfiguration.loadConfiguration(file);
	}
	public FileConfiguration getCfg() {
		return filecfg;
	}
	public void save() {
		try {
			filecfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void reload() {
		loadConfig();
	}
	

}
