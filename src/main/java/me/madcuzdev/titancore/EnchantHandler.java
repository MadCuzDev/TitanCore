package me.madcuzdev.titancore;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import me.madcuzdev.titancore.enchants.*;

public class EnchantHandler {

    static void setupEnchants() {
        registerEnchants(Cubic, Cutter, MoneyBags, Casino, Quake, Spheric, Scavenger, Explosive, Jackhammer);
    }

    private static void registerEnchants(Enchantment... enchantments) {
        for (Enchantment enchant : enchantments) {
            try {
                try {
                    Field f = Enchantment.class.getDeclaredField("acceptingNew");
                    f.setAccessible(true);
                    f.set(null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Enchantment.registerEnchantment(enchant);
                } catch (IllegalArgumentException ignored){
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Cubic Cubic = new Cubic(68);
    public static Cutter Cutter = new Cutter(69);
    public static MoneyBags MoneyBags = new MoneyBags(70);
    public static Casino Casino = new Casino(71);
    public static Quake Quake = new Quake(72);
    public static Spheric Spheric = new Spheric(73);
    public static Scavenger Scavenger = new Scavenger(74);
    public static Explosive Explosive = new Explosive(75);
    public static Jackhammer Jackhammer = new Jackhammer(76);

    public ArrayList<Enchantment> getEnchants() {
        return enchantments;
    }

    private static ArrayList<Enchantment> enchantments;
    {
        enchantments = new ArrayList<>();
        enchantments.add(Cubic);
        enchantments.add(Cutter);
        enchantments.add(MoneyBags);
        enchantments.add(Casino);
        enchantments.add(Quake);
        enchantments.add(Spheric);
        enchantments.add(Scavenger);
        enchantments.add(Explosive);
        enchantments.add(Jackhammer);
    }
}
