package me.bottleofglass.Jackpot.Commands;


import me.bottleofglass.Jackpot.Jackpot;
import me.bottleofglass.Jackpot.Main;
import me.bottleofglass.Jackpot.Utils.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JackpotCommand implements TabExecutor {
	
	Main main;
	
	public JackpotCommand(Main m) {
		main = m;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] args) {
		if(!command.getName().equalsIgnoreCase("jackpot")) return null;
		ArrayList<String> list = new ArrayList<>();
		if(args.length == 1) {
			for (String s : Main.cmdArgs) {
				if(s.startsWith(args[0])){
					list.add(s);
				}
			}
			Collections.sort(list);
		} else if (args.length == 3 && args[0].equalsIgnoreCase("start")) {
			try {
				Integer.parseInt(args[2]);
				for (char c: Main.hashMap.keySet()) {
					list.add(args[2] + c);
				}
			} catch(NumberFormatException e) {
				return list;
			}
		}
		return list;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length >= 1) {
			
			switch (args[0]) {
			case "help":
				sender.sendMessage(Util.msgNoPrefix("&l&c-->&8[&6Jackpot&8]&c<--\n&r&8-&c/jackpot help -> shows this list of commands\n"
						+ "&8-&c/jackpot start <prize> <time> -> starts a jackpot with the given prize money and time\n"
						+ "&8-&c/jackpot join -> joins the jackpot for &a" + main.ticketPrice + "$" ));
				break;
			case "start":
				if(args.length >= 3 ) {
					if (sender.hasPermission("jackpot.user.start")) {
						if (main.jackpot != null && main.jackpot.isRunning) {
							sender.sendMessage(Util.msg("&eThere's already a jackpot running"));
							return true;
						}
						if(!Util.isValid(args[2])) {
							sender.sendMessage(Util.msg("&cInvalid Time"));
							return true;
						}
						try {
							main.jackpot = new Jackpot(Integer.parseInt(args[1]),
									main.ticketPrice,
									args[2],
									main);
						} catch(NumberFormatException e) {
							sender.sendMessage(Util.msg("&cThe prize needs to be a number!"));
							return true;
						}
						main.jackpot.start();
					} else {
						sender.hasPermission(Util.msg("&cYou need the permission jackpot.user.start to use this command"));
						return true;
					}
				} else {
					sender.sendMessage(Util.msgNoPrefix("&8-&c/jackpot start <prize> <time> -> starts a jackpot with the given prize money and time"));
					return true;
				}
				break;
			case "join":
				if (!(sender instanceof Player)) {
					sender.sendMessage(Util.msg("&eOnly players may execute this command"));
					return true;
				}
				Player p = (Player) sender;
				if(!p.hasPermission("jackpot.user.join")) {
					sender.hasPermission(Util.msg("&cYou need the permission jackpot.user.join to use this command"));
					return true;
				}
				if(!(Main.getEconomy().has(p, main.jackpot.ticket))) {
					p.sendMessage(Util.msg("&cYou need &a" + main.jackpot.ticket + "$&c to join the jackpot"));
					return true;
				}
				sender.sendMessage(Util.msg("&aYou have joined the jackpot!"));
				OfflinePlayer op = (OfflinePlayer) sender;
				main.jackpot.join(op);
				sender.sendMessage(Util.msg("&a" + main.jackpot.ticket + "$ has been deducted from you account"));
				break;
			case "end":
				OfflinePlayer op2 = main.jackpot.stop();
				
				if(op2 != null) {
					op2.getPlayer().sendMessage(Util.msg("&a" + main.jackpot.prizepool + " has been addded to your account"));
				}
				
				break;
				default: sender.sendMessage(Util.msg("&cInvalid Argument"));

			}
			
		} else {
			return false;
		}
		
		return true;
	}

}
