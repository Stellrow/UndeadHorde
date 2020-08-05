package events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import main.UHMain;

public class EventsClass implements Listener{
	private UHMain pl = JavaPlugin.getPlugin(UHMain.class);
	@EventHandler
	public void onkill(EntityDeathEvent e) {
			if(pl.zombies.containsKey(e.getEntity())) {
				pl.zombies.remove(e.getEntity());
			}
			if(pl.skeletons.containsKey(e.getEntity())) {
				pl.skeletons.remove(e.getEntity());
			}
			if(pl.spiders.containsKey(e.getEntity())) {
				pl.spiders.remove(e.getEntity());
			}
			if(pl.ravagers.containsKey(e.getEntity())) {
				pl.ravagers.remove(e.getEntity());
			}
			if(pl.illusioners.containsKey(e.getEntity())) {
				pl.illusioners.remove(e.getEntity());
			}
		
	}

}
