package me.madcuzdev.titancore.enchants;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class NoDrop extends Enchantment {

public NoDrop(int id) {
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
return 1;
}
 
@Override
public String getName() {
return "NoDrop";
}
 
@Override
public int getId(){
return 60;
}
 
@Override
public int getStartLevel() {
return 1;
}
}