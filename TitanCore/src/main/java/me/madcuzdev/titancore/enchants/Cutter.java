package me.madcuzdev.titancore.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class Cutter extends Enchantment {

public Cutter(int id) {
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
return "Cutter";
}
 
@Override
public int getId(){
return 69;
}
 
@Override
public int getStartLevel() {
return 1;
}
}