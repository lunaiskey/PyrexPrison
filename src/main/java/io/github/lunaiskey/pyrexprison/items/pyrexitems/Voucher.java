package io.github.lunaiskey.pyrexprison.items.pyrexitems;

import io.github.lunaiskey.pyrexprison.items.ItemID;
import io.github.lunaiskey.pyrexprison.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.player.Currency;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Voucher extends PyrexItem {

    private BigInteger amount;
    private CurrencyType type;

    public Voucher(BigInteger amount, CurrencyType type) {
        super(ItemID.VOUCHER, "   &7- &6&lBank Note &7-", null, Material.PAPER);
        this.amount = amount;
        this.type = type;
    }

    public Voucher(Long amount, CurrencyType type) {
        this(BigInteger.valueOf(amount),type);
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(getMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtil.color(getName()));
        List<String> lore = new ArrayList<>(List.of(
                " ",
                "&7• Type: %type%",
                "&7• Amount: %amount%",
                " ",
                "&eRight Click to redeem!"));
        String strType = "&7Unknown";
        String strAmount = CurrencyType.getUnicode(type)+ Numbers.formattedNumber(amount);
        switch (type) {
            case TOKENS -> {
                strType = "&eTokens";
                strAmount = "&e"+CurrencyType.getUnicode(type)+"&f"+Numbers.formattedNumber(amount);
            }
            case GEMS -> {
                strType = "&aGems";
                strAmount = "&a"+CurrencyType.getUnicode(type)+"&f"+Numbers.formattedNumber(amount);
            }
            case PYREX_POINTS -> {
                strType = "&dPyrex Points";
                strAmount = "&d"+CurrencyType.getUnicode(type)+"&f"+Numbers.formattedNumber(amount);
            }
        }
        for (int i = 0;i<lore.size();i++) {
            String line = lore.get(i);
            if (line.contains("%type%")) {
                line = line.replace("%type%",strType);
            }
            if (line.contains("%amount%")) {
                line = line.replace("%amount%",strAmount);
            }
            lore.set(i,StringUtil.color(line));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        item = NBTTags.setCurrencyVoucherTags(item,amount,type);
        return item;

    }

    @Override
    public void onBlockBreak(BlockBreakEvent e) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent e) {

    }

    @Override
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Currency.giveCurrency(e.getPlayer().getUniqueId(), type,amount);
        e.getItem().setAmount(e.getItem().getAmount()-1);
        switch(type) {
            case TOKENS -> p.sendMessage(StringUtil.color("&eRedeemed "+CurrencyType.getUnicode(type)+"&f"+ Numbers.formattedNumber(amount)+" &eTokens."));
            case GEMS -> p.sendMessage(StringUtil.color("&aRedeemed "+CurrencyType.getUnicode(type)+"&f"+Numbers.formattedNumber(amount)+" &aGems."));
            case PYREX_POINTS -> p.sendMessage(StringUtil.color("&dRedeemed "+CurrencyType.getUnicode(type)+"&f"+Numbers.formattedNumber(amount)+" &dPyrex Points."));
        }
    }
}
