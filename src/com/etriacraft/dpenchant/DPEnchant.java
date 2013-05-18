package com.etriacraft.dpenchant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mistphizzle.donationpoints.plugin.Commands;
import com.mistphizzle.donationpoints.plugin.DonationPoints;


public class DPEnchant extends JavaPlugin {

	protected static Logger log;

	public static DPEnchant instance;
	EnchantCommand ecmd;

	public static Permission permission = null;

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	File configFile;
	FileConfiguration config;

	@Override
	public void onEnable() {
		instance = this;
		this.log = this.getLogger();

		configFile = new File(getDataFolder(), "config.yml");

		ecmd = new EnchantCommand(this);

		try {
			firstRun();
		} catch (Exception e) {
			e.printStackTrace();
		}

		setupPermissions();

		config = new YamlConfiguration();
		loadYamls();

	}

	@Override
	public void onDisable() {

	}

	public static DPEnchant getInstance() {
		return instance;
	}

	// Methods
	public void firstRun() throws Exception {
		if (!configFile.exists()) {
			configFile.getParentFile().mkdirs();
			copy(getResource("config.yml"), configFile);
			log.info("Config not found. Generating.");
		}
	}

	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf))>0) {
				out.write(buf,0,len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveYamls() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void configReload() {
		reloadConfig();
	}
	
	private void loadYamls() {
		try {
			config.load(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
