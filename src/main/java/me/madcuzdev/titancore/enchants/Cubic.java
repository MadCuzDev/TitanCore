package me.madcuzdev.titancore.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Cubic extends Enchantment {
	
public Cubic(int id) {
super(id);
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
return 5000;
}
 
@Override
public String getName() {
return "Cubic";
}
 
@Override
public int getId(){
return 68;
}
 
@Override
public int getStartLevel() {
return 1;
}
}