package me.madcuzdev.titancore;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import me.madcuzdev.titancore.enchants.*;

public class EnchantHandler {

    static void setupEnchants() {
        registerEnchants(NoDrop, Cubic, Cutter, Yes);
    }

    private static void registerEnchants(Enchantment... enchantments) {
        for (Enchantment ench : enchantments) {
            try {
                try {
                    Field f = Enchantment.class.getDeclaredField("acceptingNew");
                    f.setAccessible(true);
                    f.set(null, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Enchantment.registerEnchantment(ench);
                } catch (IllegalArgumentException ignored){
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static NoDrop NoDrop = new NoDrop(60);
    public static Cubic Cubic = new Cubic(68);
    public static Cutter Cutter = new Cutter(69);
    public static Yes Yes = new Yes(70);

    public ArrayList<Enchantment> getEnchants() {
        return enchantments;
    }

    private static ArrayList<Enchantment> enchantments;
    {
        enchantments = new ArrayList<>();
        enchantments.add(NoDrop);
        enchantments.add(Cubic);
        enchantments.add(Cutter);
        enchantments.add(Yes);
    }
}
