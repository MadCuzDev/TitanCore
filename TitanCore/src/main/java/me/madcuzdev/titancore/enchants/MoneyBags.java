package me.madcuzdev.titancore.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class MoneyBags extends Enchantment {

	public MoneyBags(int id) {
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
	return 500;
	}
	 
	@Override
	public String getName() {
	return "MoneyBags";
	}
	 
	@Override
	public int getId(){
	return 70;
	}
	 
	@Override
	public int getStartLevel() {
	return 1;
	}
	}