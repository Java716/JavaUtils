package me.StevenLawson.TotalFreedomMod;

import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.DEVELOPERS;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.FOP_DEVELOPERS;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.SPECIAL_EXECS;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.SYS_ADMINS;
import static me.StevenLawson.TotalFreedomMod.TFM_Util.WEB_DEVELOPERS;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum TFM_PlayerRank
{
    DEVELOPER("a " + ChatColor.DARK_PURPLE + "TotalFreedom Developer", ChatColor.DARK_PURPLE + "(TF Dev)"),
    FOP_DEVELOPER("a " + ChatColor.DARK_PURPLE + "Developer", ChatColor.DARK_PURPLE + "(Dev)"),
    WEB_DEVELOPER("a " + ChatColor.GREEN + "Web Developer", ChatColor.GREEN + "(Web Dev)"),
    SPEC_EXEC("a " + ChatColor.DARK_RED + "Special Executive", ChatColor.DARK_RED+ "(Special Exec)"),
    SYS_ADMIN("a " + ChatColor.YELLOW+ "System Admin", ChatColor.DARK_RED + "(System Admin)"),
    OWNER("the " + ChatColor.DARK_AQUA + "Owner and the Lead Developer", ChatColor.DARK_AQUA + "(Owner)"),
    IMPOSTOR("a " + ChatColor.GRAY + ChatColor.UNDERLINE + "FAKE/IMPOSTER", ChatColor.GRAY.toString() + ChatColor.UNDERLINE + "(FAKE/IMP)"),
    NON_OP("a " + ChatColor.GREEN + "Non-OP", ChatColor.GREEN.toString()),
    OP("an " + ChatColor.AQUA + "OP", ChatColor.AQUA + "(OP)"),
    SUPER("a " + ChatColor.GREEN + "Super Admin", ChatColor.GREEN + "(Super)"),
    TELNET("a " + ChatColor.LIGHT_PURPLE+ "Telnet Admin", ChatColor.LIGHT_PURPLE + "(Telnet)"),
    SENIOR("a " + ChatColor.BLUE + "Senior Admin", ChatColor.BLUE + "(Senior)"),
    CONSOLE("the " + ChatColor.DARK_PURPLE + "Console", ChatColor.DARK_PURPLE + "(Console)");
    private String loginMessage;
    private String prefix;

    private TFM_PlayerRank(String loginMessage, String prefix)
    {
        this.loginMessage = loginMessage;
        this.prefix = prefix;
    }

    public static String getLoginMessage(CommandSender sender)
    {
        // Handle console
        if (!(sender instanceof Player))
        {
            return fromSender(sender).getLoginMessage();
        }

        // Handle admins
        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);
        if (entry == null)
        {
            // Player is not an admin
            return fromSender(sender).getLoginMessage();
        }

        // Custom login message
        final String loginMessage = entry.getCustomLoginMessage();

        if (loginMessage == null || loginMessage.isEmpty())
        {
            return fromSender(sender).getLoginMessage();
        }

        return ChatColor.translateAlternateColorCodes('&', loginMessage);
    }

    public static TFM_PlayerRank fromSender(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return CONSOLE;
        }

        if (TFM_AdminList.isAdminImpostor((Player) sender))
        {
            return IMPOSTOR;
        }

        else if (sender.getName().equals("Java716"))
        {
            return OWNER;
        }

        else if (sender.getName().equals("Zen_Miner"))
        {
            return OWNER;
        }
        
        else if (SYS_ADMINS.contains(sender.getName()))
        {
            return SYS_ADMIN;
        }

        else if (SPECIAL_EXECS.contains(sender.getName()))
        {
            return SPEC_EXEC;
        }

        else if (FOP_DEVELOPERS.contains(sender.getName()))
        {
            return FOP_DEVELOPER;
        }
        
        else if (WEB_DEVELOPERS.contains(sender.getName()))
        {
            return WEB_DEVELOPER;
        }

        else if (DEVELOPERS.contains(sender.getName()))
        {
            return DEVELOPER;
        }

        final TFM_Admin entry = TFM_AdminList.getEntry((Player) sender);

        final TFM_PlayerRank rank;

        if (entry != null && entry.isActivated())
        {
            if (TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName()))
            {
                return OWNER;
            }
            if (entry.isSeniorAdmin())
            {
                rank = SENIOR;
            }
            else if (entry.isTelnetAdmin())
            {
                rank = TELNET;
            }
            else
            {
                rank = SUPER;
            }
        }
        else
        {
            if (sender.isOp())
            {
                rank = OP;
            }
            else
            {
                rank = NON_OP;
            }

        }
        return rank;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public String getLoginMessage()
    {
        return loginMessage;
    }
}
