package jcn.itemeditor;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Relore implements CommandExecutor {
    Economy economy;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Bukkit.getPluginManager().getPlugin("ItemEditor").getDataFolder(), "config.yml"));
        String price = config.getString("price");
        Player player = (Player) commandSender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (player.getInventory().getItemInMainHand().getType().isAir()) {
            player.sendMessage("В руке ничего нет");
            return false;
        }

        if (strings.length == 0) {
            player.sendMessage("Неправильнное использование");
            return false;
        }

        String newName = String.join(" ", strings);
        String[] lines = newName.split("//");
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> loreLines = new ArrayList<>();

        for (String line : lines) {
            loreLines.add(ChatColor.translateAlternateColorCodes('&', line.trim()));

            if (price.equals("lvl")) {
                if (player.getLevel() < 10) {player.sendMessage("У вас недостаточно опыта");}
                else {
                    assert itemMeta != null;
                    itemMeta.setLore(loreLines);
                    itemStack.setItemMeta(itemMeta);
                    player.setLevel(player.getLevel() - 10);
                    player.sendMessage("Предмету в вашей руке был добавлен лор");
                }
            }

            if(price.equals("money")){
                if (economy.getBalance(player) < 100) {player.sendMessage("У вас недостаточно денег");}
                else{
                    economy.withdrawPlayer(player, 100);
                    assert itemMeta != null;
                    itemMeta.setLore(loreLines);
                    itemStack.setItemMeta(itemMeta);
                    player.sendMessage("Предмету в вашей руке был добавлен лор");
                }
            }
            return false;
        }
        return false;
    }
}