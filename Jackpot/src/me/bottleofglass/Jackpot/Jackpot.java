package me.bottleofglass.Jackpot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

import me.bottleofglass.Jackpot.Utils.Util;
import net.milkbowl.vault.economy.Economy;

public class Jackpot {
	
	public int prizepool; //Prize Money
	
	public int ticket; //The money each player needs before being able to join
	
	long time; //Time on ticks to use on methods
	
	private Main main; //The main plugin
	
	FileConfiguration config; //config file
	
	BukkitTask reminder; //The task that announces the running jackpot
	
	public boolean isRunning = false; //The boolean that holds if the Jackpot is running or not, there can only be 1 jackpot at a time
	
	Economy econ; //Vault Economy

	long endTime;
	
	List<OfflinePlayer> playerList = new ArrayList<>(); // List of players that joined

	BukkitTask endTask;
	
	
	public Jackpot(int startprize, int ticketprice, String timeString, Main J ) {
		
		this.main = J;
		
		this.econ = J.econ;
		
		prizepool = startprize;
		
		ticket = ticketprice;
		
		time = Util.getTicks(timeString);
	}
	
	public void start() {
		
		isRunning = true;

		endTask = Bukkit.getScheduler().runTaskLater(main, () -> {
			stop();
		}, time);
		endTime = System.currentTimeMillis() + (TimeUnit.SECONDS.toMillis(time/20));
		main.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&6Time remaining until jackpot winner is announced: " +
				Util.timeInSeconds(TimeUnit.SECONDS.toMillis(time/20))));
		reminder = Bukkit.getScheduler().runTaskTimer(main, () -> main.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&l&6Time remaining until jackpot winner is announced: " +
				Util.timeInSeconds(endTime-System.currentTimeMillis()))), Util.getTicks(main.reminderDelay), Util.getTicks(main.reminderDelay));
		main.getServer().broadcastMessage(Util.msg("&l&4Reminder delay: " + main.reminderDelay));
	}
	
	public OfflinePlayer stop() {
		isRunning = false;
		reminder.cancel();
		endTask.cancel();
		return reward();
	}
	
	private OfflinePlayer reward() {
		
		if (playerList.isEmpty()) {
			main.getServer().broadcastMessage(Util.msg("&8Nobody joined the Jackpot event :("));
			return null;
		}
		OfflinePlayer winner = playerList.get((int) Math.round(Math.random()* (playerList.size()- 1)));
		
		Main.getEconomy().depositPlayer(winner, prizepool);
		
		main.getServer().broadcastMessage(Util.msg("&b" + winner.getName() + " has been drawn the winner of the jackpot and has won &a" + prizepool + "$"));
		
		return winner;
		
		
	}

	public void join(OfflinePlayer p) {
		main.econ.withdrawPlayer(p, ticket);
		prizepool = prizepool+ ticket;
		playerList.add(p);
	}

}
