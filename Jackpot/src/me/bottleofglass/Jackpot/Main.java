package me.bottleofglass.Jackpot;

import java.util.logging.Logger;

import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.bottleofglass.Jackpot.Commands.JackpotCommand;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Economy econ = null;
	public Permission perm = new Permission("jackpot.admin");
	public Jackpot jackpot;
	
	@Override
	public void onEnable() {
		
		this.getServer().getPluginManager().addPermission(perm);
		
		new JackpotCommand(this);
		
		this.getConfig().addDefault("reminderDelay", "15m");
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
		}
	}
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
