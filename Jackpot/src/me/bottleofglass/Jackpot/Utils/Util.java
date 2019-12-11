package me.bottleofglass.Jackpot.Utils;

public class Util {
	
	public static long getTicks(String s) {
		
		long t;
		switch(s.charAt(s.length()-1)) { // takes inputs like 1h/2h/3h etc and returns time in ticks
		case 'h':
			t = lastRemover(s)* 72000;
			break;
		case 'm':
			t = lastRemover(s) * 1200;
			break;
		case 'd':
			t = lastRemover(s) * 1728000;
			break;
		case 'w':
			t = lastRemover(s) * 12096000;
			break;
			default:
				t = -1;
				break;
		}
		return t;
	}
	
	public static int lastRemover(String s) {
		return Integer.parseInt(s.substring(0, s.length()-1)); //removes the time counter s/m/h/d etc from time
	}
	public static long getSeconds(String s) { 
		long t;
		switch(s.charAt(s.length()-1)) {
		case 'h':
			t = lastRemover(s)* 3600;
			break;
		case 'm':
			t = lastRemover(s) * 60;
			break;
		case 'd':
			t = lastRemover(s) * 86400;
			break;
		case 'w':
			t = lastRemover(s) * 604800;
			break;
			default:
				t = -1;
				break;
	}
		return t;
		
	}
	public static String getRemainingTime(long t) {
		String time;
		if(t > 604800) {
			time = Math.round(t/ 604800) + "weeks";
			} else if (t > 86400) {
				time = Math.round(t / 86400) + "days";
		} else if (t > 3600) {
			time = Math.round(t / 3600) + "hours";
		} else if (t > 60) {
			time = (Math.round(t / 60)+1) + "minutes";
		} else {
			time = t + "seconds";
		}
		return time;
	}

}
