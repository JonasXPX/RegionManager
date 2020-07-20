package br.com.endcraft.regionmanager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

import static br.com.endcraft.regionmanager.RegionManager.getDatabase;

public class RegionCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(t("Comandos liberados"));
            sender.sendMessage(t("-> /rmg deny <regionName> <IdItem>"));
            sender.sendMessage(t("-> /rmg allow <regionName> <IdItemBloqueado>"));
            sender.sendMessage(t("-> /rmg list <regionName> "));
            return true;
        }

        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args[0].equalsIgnoreCase("deny") && args.length >= 3) {
            try {
                String regionName = args[1];
                Long itemId = Long.valueOf(args[2]);
                getDatabase().setItemIdBlocked(itemId, regionName, player.getWorld().getName());
                if (verifyForErrors(sender)) {
                    sender.sendMessage(t("Item '" + itemId + "' foi bloqueado na region '" + regionName + "'"));
                }
            } catch (NumberFormatException ex) {
                sender.sendMessage(t("Digite um número correto, /rmg deny <nome da region [texto_junto]> <id do item [numeral]>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("allow") && args.length >= 3) {
            try {
                String regionName = args[1];
                Long itemId = Long.valueOf(args[2]);
                getDatabase().removeBlockedItemFromRegion(itemId, regionName, player.getWorld().getName());
                if (verifyForErrors(sender)) {
                    sender.sendMessage(t("Item '" + itemId + "' foi liberado na region '" + regionName + "'"));
                }
            } catch (NumberFormatException ex) {
                sender.sendMessage(t("Digite um número correto, /rmg allow <nome da region [text_junto]> <id do item [numeral]>"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("list") && args.length >= 2) {
            String regionName = args[1];
            sender.sendMessage(formatToTextVersion(regionName, player.getWorld().getName()));
            verifyForErrors(sender);
            return true;
        }

        return false;
    }

    private boolean verifyForErrors(CommandSender sender) {
        ErrorMessage errorMessage = ErrorMessage.getInstance();
        if (errorMessage.hasError()) {
            sender.sendMessage(errorMessage.getError());
            return false;
        }
        return true;
    }

    private String formatToTextVersion(String regionName, String worldName) {

        return getDatabase().getBlockedItemsByRegion(regionName, worldName).stream()
                .map(aLong -> t("ItemId: " + aLong + "\n"))
                .collect(Collectors.joining());
    }

    private String t(String message) {
        return ChatColor.GREEN + message;
    }
}
