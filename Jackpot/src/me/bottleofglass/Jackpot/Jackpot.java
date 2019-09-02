package me.bottleofglass.Jackpot;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.bottleofglass.Jackpot.Utils.Util;
import net.milkbowl.vault.economy.Economy;

public class Jackpot {
	
	public int prizepool;
	
	public int ticket;
	
	long time;
	
	private Main main;
	
	FileConfiguration config;
	
	BukkitTask reminder;
	
	public boolean isRunning = false;
	
	Economy econ;
	
	int timeRemaining;
	
	List<OfflinePlayer> playerList = new ArrayList<>();
	
	long seconds;
	
	
	public Jackpot(int startprize, int ticketprice, String timeString, Main J ) {
		
		this.main = J;
		
		this.econ = J.econ;
		
		prizepool = startprize;
		
		ticket = ticketprice;
		
		time = Util.getMS(timeString);
		
		seconds = Util.getSeconds(timeString);
		config = main.getConfig();
		
	}
	
	public void start() {
		
		isRunning = true;
		
		BukkitRunnable timer = new BukkitRunnable() {
			public void run() {
				stop();
			}
		};
		timer.runTaskLater(main, time);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			public void run() {seconds--;}
		}, 0, 20);
		
		
		
		
		reminder = Bukkit.getScheduler().runTaskTimer(main, new Runnable() {
			public void run() {
				main.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&6Time remaining until jackpot winner is announced: " +
						Util.getRemainingTime(seconds)));
				
			}
		} , 0, Util.getMS(config.getString("reminderDelay")));
		
		
		
	}
	
	public OfflinePlayer stop() {
		isRunning = false;
		reminder.cancel();
		return reward();
		
	}
	
	private OfflinePlayer reward() {
		
		if (playerList.isEmpty()) {
			main.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&8Nobody joined the Jackpot event :("));
			return null;
		}
		OfflinePlayer winner = playerList.get((int) Math.round(Math.random()* (playerList.size()- 1)));
		
		main.econ.depositPlayer(winner, prizepool);
		
		main.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&b" + winner.getName() + " has been drawn the winner of the jackpot and has won &a" + prizepool + "$"));
		
		return winner;
		
		
	}
	
	public void join(OfflinePlayer p) {
		main.econ.withdrawPlayer(p, ticket);
		
		playerList.add(p);
	}

}
