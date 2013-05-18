package com.etriacraft.dpenchant;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Utils {

	public static String getEnchantmentList(ItemStack iss) {
		StringBuilder sb = new StringBuilder();
		for (Enchantment ench : Enchantment.values()) {
			if (iss != null && !ench.canEnchantItem(iss)) continue;
			if (sb.length() != 0) sb.append("§a,§e ");
			sb.append("§e").append(ench.getName().toLowerCase());
		}
		return sb.toString();
	}
	
	private static String[] RVALS = {"M", "CM", "D", "CD", "C", "XC", "L",
        "XL", "X", "IX", "V", "IV", "I"};
	
	public static String toRomanNumeral(int value) {
        if (value <= 0) return "";
        String roman = "";
        for (int i = 0; i < RVALS.length; i++) {
            while (value >= IVALS[i]) {
                roman += RVALS[i];
                value -= IVALS[i];
            }
        }
        return roman;
    }
	
	private static int[] IVALS = {1000, 900, 500, 400, 100, 90,
	     50, 40, 10, 9, 5, 4, 1};
}
