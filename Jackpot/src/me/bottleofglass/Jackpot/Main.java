package me.bottleofglass.Jackpot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.bottleofglass.Jackpot.Commands.JackpotCommand;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	
	private static final Logger log = Logger.getLogger("Minecraft");
	public static Economy econ = null;
	public Jackpot jackpot = null;
	public int ticketPrice;
	public String reminderDelay;
	public static HashMap<Character, Integer> hashMap = new HashMap<>();
	public static ArrayList<String> cmdArgs = new ArrayList<>();
    private File configFile;
    private FileConfiguration customConfig;

	@Override
	public void onEnable() {
        createCustomConfig();
		hashMap.put('s',1);
		hashMap.put('m', 60);
		hashMap.put('h', 3600);
		hashMap.put('d', 86400);
		hashMap.put('w', 604800);
		cmdArgs.add("help");
		cmdArgs.add("join");
		cmdArgs.add("start");
		cmdArgs.add("end");
		this.getServer().getPluginManager().addPermission(new Permission("jackpot.user.start"));
		saveDefaultConfig();
		ticketPrice = this.getConfig().getInt("ticketPrice");
		reminderDelay = this.getConfig().getString("reminderDelay");
		log.info("Got ticket price: " + ticketPrice);
		log.info("got reminder delay : " + reminderDelay);
		this.getConfig().addDefault("reminderDelay", "15m"); // puts default
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
		}
		getCommand("jackpot").setExecutor(new JackpotCommand(this));
		createCustomConfig();
		if(customConfig.get("jackpot") != null) {
		    jackpot = (Jackpot) customConfig.get("jackpot");

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
    private void createCustomConfig() {
        configFile = new File(getDataFolder(), "custom.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("custom.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
	    customConfig.set("jackpot", jackpot);
        try {
            customConfig.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }
    public static Economy getEconomy() {
		return econ;
	}

}
