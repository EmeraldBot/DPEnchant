package com.etriacraft.dpenchant;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mistphizzle.donationpoints.plugin.Commands;
import com.mistphizzle.donationpoints.plugin.DonationPoints;
import com.mistphizzle.donationpoints.plugin.Methods;

public class EnchantCommand {

	DPEnchant plugin;

	public EnchantCommand(DPEnchant instance) {
		this.plugin = instance;
		init();
	}
	public static HashMap<String, String> purchases = new HashMap();

	private void init() {
		PluginCommand enchant = plugin.getCommand("enchant");
		CommandExecutor exe;

		exe = new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

				if (!(s instanceof Player)) return false;

				final ItemStack is = ((Player) s).getItemInHand();

				if (args.length < 1) {
					s.sendMessage("§cAvailable Enchantment Commands");
					s.sendMessage("§3/enchant list§f - View list of enchantments for item in hand.");
					s.sendMessage("§3/enchant <enchantment> <level>§f - Enchant the item in your hand.");
					return true;
				}


				if (args[0].equalsIgnoreCase("list")) {
					if (!DPEnchant.permission.has(s, "dpenchant.list")) {
						s.sendMessage("§cYou don't have permission to do that!.");
						return true;
					}
					if (is.getType().equals(Material.AIR)) {
						s.sendMessage("§cYou do not have an item in your hand.");
						return true;
					}
					s.sendMessage("§cAvailable Enchantments:");
					s.sendMessage("§a" + Utils.getEnchantmentList(is));
					return true;
				}
				if (args.length > 1) {
					if (!DPEnchant.permission.has(s, "dpenchant.enchant")) {
						s.sendMessage("§cYou don't have permission to do that!");
						return true;
					}
					final Enchantment ench = Enchantment.getByName(args[0].toUpperCase());

					if (ench == null) {
						s.sendMessage("§cEnchantment Invalid");
						s.sendMessage("§aAvailable Enchantments: " + Utils.getEnchantmentList(null));
						return true;
					}
					int level = 1;
					if (args.length >= 2) {
						try {
							level = Integer.parseInt(args[1]);
						} catch (NumberFormatException e) {
							s.sendMessage("§cLevel Invalid");
							return true;
						}
					}

					if (!ench.canEnchantItem(is)) {
						s.sendMessage("§cCannot enchant your§3 " + is.getType().name() + " §cwith§7 " + ench.getName());
						s.sendMessage("§aEnchantments you can use: " + Utils.getEnchantmentList(is));
						return true;
					}
					if (is.containsEnchantment(ench)) {
						s.sendMessage("§cYour item already has this enchantment.");
						return true;
					}
					if (ench.getMaxLevel() < level) {
						s.sendMessage("§cThat level is too high. §3" + ench.getName().toLowerCase() + " §ccan only be between levels §3" + ench.getStartLevel() + "§c and §3" + ench.getMaxLevel());
						return true;
					} else {
						if (!Methods.hasAccount(s.getName())) {
							s.sendMessage("§cYou do not have an account.");
							return true;
						}
						Double price = plugin.getConfig().getDouble("Enchantments." + ench.getName().toLowerCase() + "." + level);
						String date = Methods.getCurrentDate();
						String packName = ench.getName().toLowerCase();
						Double balance = Methods.getBalance(s.getName());
						
						if (price > balance) {
							s.sendMessage("§cYou do not have enough points to purchase this enchantment.");
							return true;
						}

						Methods.removePoints(price, s.getName());
						Methods.logTransaction(s.getName(), price, packName, date, "true", "false", null, "false");
						is.addEnchantment(ench, level);
						s.sendMessage("§cYou have purchased §3" + ench.getName().toLowerCase() + " " + Utils.toRomanNumeral(level) + "§c for §3" + price + " points§c.");
						s.sendMessage("§aEnchanted your§e " + is.getType().name() + " §awith " + ench.getName() + " " + Utils.toRomanNumeral(level));
						return true;
					}
				}
				return true;
			}
		}; enchant.setExecutor(exe);
	}
}
