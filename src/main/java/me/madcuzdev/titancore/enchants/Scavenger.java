package me.madcuzdev.titancore.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Scavenger extends Enchantment {

	public Scavenger(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	 
	@Override
	public boolean canEnchantItem(ItemStack item) {
	return true;
	}
	 
	@Override
	public boolean conflictsWith(Enchantment other) {
	return false;
	}
	 
	@Override
	public EnchantmentTarget getItemTarget() {
	return null;
	}
	 
	@Override
	public int getMaxLevel() {
	return 100;
	}
	 
	@Override
	public String getName() {
	return "Scavenger";
	}
	 
	@Override
	public int getId(){
	return 74;
	}
	 
	@Override
	public int getStartLevel() {
	return 1;
	}
	}