package me.bottleofglass.Jackpot.Commands;



import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.bottleofglass.Jackpot.Jackpot;
import me.bottleofglass.Jackpot.Main;
import me.bottleofglass.Jackpot.Utils.Util;

public class JackpotCommand implements CommandExecutor {
	
	Main main;
	
	public JackpotCommand(Main m) {
		main = m;
		main.getCommand("jackpot").setExecutor(this);
	}
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length >= 1) {
			
			switch (args[0]) {
			case "help":
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&l&6Jackpot Commands\n&r/&8jackpot help - shows this list of commands\n"
						+ "&8/jackpot start <Prize> <time> - starts a jackpot with the given prize money and time\n"
						+ "&8/jackpot join - "));
				break;
			case "start":
				if(args.length >= 2 && sender.hasPermission(main.perm)) {
					if (main.jackpot != null && main.jackpot.isRunning == true) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eThere's already a jackpot running"));
						return true;
						
					}
					if(Util.getTicks(args[2]) == -1) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid Time"));
					}
					main.jackpot = new Jackpot(Integer.parseInt(args[1]),
							main.getConfig().getInt("ticketPrice"),
							args[2],
							main);
					main.jackpot.start();
					try {
						
					} catch (NullPointerException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eMissing Arguments" + e.getLocalizedMessage()));
						return true;
					}
					
				} else if (!sender.hasPermission(main.perm)) {
					sender.hasPermission(ChatColor.translateAlternateColorCodes('&', "&eNo Permission!"));
				} else {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNot Enough Arguments"));
				}
				break;
			case "join":
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eOnly players may execute this command"));
					break;
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&You have joined the jackpot!"));
				OfflinePlayer op = (OfflinePlayer) sender;
				main.jackpot.join(op);
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + main.jackpot.ticket + "$ has been deducted from you account"));
			case "end":
				OfflinePlayer p = main.jackpot.stop();
				
				if(p != null) {
					p.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&a" + main.jackpot.prizepool + " has been addded to your account"));
				}
				
				break;
				default: sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInvalid Argument"));

			}
			
		} else {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eNot Enough Arguments"));
		}
		
		return true;
	}

}
