package me.bottleofglass.Jackpot.Utils;

import me.bottleofglass.Jackpot.Main;

import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

public class Util {

	public static long getTicks(String s) {

		long t = getSeconds(s);
		if (t == -1) {
			return t;
		} else {
			return t * 20;
		}
	}
	public static boolean isValid(String s) {
		String s2;
		for(char c : Main.hashMap.keySet()) {
			if (s.endsWith(String.valueOf(c))) {
				try {
					Integer.parseInt(s.substring(0, s.length() - 1));
					return true;
				} catch(NumberFormatException e) {
					return false;
				}
			}

		}
		return false;
	}
	
	public static int lastRemover(String s) {
		return Integer.parseInt(s.substring(0,s.length()-1)); //removes the time counter s/m/h/d etc from time

	}
	public static long getSeconds(String s) {
		if (s == null) {
			return -1;
		}
		long t;
		for(char c : Main.hashMap.keySet()) {
			if (s.endsWith(String.valueOf(c))) {
				return (lastRemover(s) * Main.hashMap.get(s.charAt(s.length()-1)));
			}
		}
		return -1;
		
	}
	public static String msg(String s) {
		return ChatColor.translateAlternateColorCodes('&',"&8[&6Jackpot&8]" + s);
	}
	public static String msgNoPrefix(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
    public static String timeInSeconds(long time) {

		long days = TimeUnit.MILLISECONDS.toDays(time);
		time -= TimeUnit.DAYS.toMillis(days);

		long hours = TimeUnit.MILLISECONDS.toHours(time);
		time -= TimeUnit.HOURS.toMillis(hours);

		long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
		time -= TimeUnit.MINUTES.toMillis(minutes);

		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);

        return ((days != 0) ? days + "days " : "") + ((hours != 0) ? hours + "hours " : "") + ((minutes != 0) ? minutes + "minutes " : "") + ((seconds != 0) ? seconds + "seconds " : "");
    }


}
