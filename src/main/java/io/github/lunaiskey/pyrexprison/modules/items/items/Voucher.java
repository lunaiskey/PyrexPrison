package io.github.lunaiskey.pyrexprison.modules.items.items;

import io.github.lunaiskey.pyrexprison.modules.items.ItemID;
import io.github.lunaiskey.pyrexprison.modules.items.PyrexItem;
import io.github.lunaiskey.pyrexprison.util.nms.NBTTags;
import io.github.lunaiskey.pyrexprison.modules.player.Currency;
import io.github.lunaiskey.pyrexprison.modules.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.util.ItemBuilder;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Voucher extends PyrexItem {

    private BigInteger amount;
    private CurrencyType type;
    private String playerName;

    public Voucher(BigInteger amount, CurrencyType type, String playerName) {
        super(ItemID.VOUCHER, "   &7- &6&lBank Note &7-", null, Material.PAPER);
        this.amount = amount;
        this.type = type;
    }

    public Voucher(CurrencyType type,BigInteger amount) {
        this(amount,type,null);
    }

    public Voucher(Long amount, CurrencyType type, String playerName) {
        this(BigInteger.valueOf(amount),type,playerName);
    }

    public Voucher(ItemStack item) {
        super(ItemID.VOUCHER, "   &7- &6&lBank Note &7-", null, Material.PAPER);
        Pair<CurrencyType, BigInteger> pair = NBTTags.getVoucherValue(item);
        this.amount = pair.getRight();
        this.type = pair.getLeft();
    }

    @Override
    public ItemStack getItemStack() {
        String name = StringUtil.color(getName());
        String strType = "INVALID";
        String strAmount = "INVALID";
        switch (type) {
            case TOKENS -> {
                strType = "&eTokens";
                strAmount = "&e"+type.getUnicode()+"&f"+Numbers.formattedNumber(amount);
            }
            case GEMS -> {
                strType = "&aGems";
                strAmount = "&a"+type.getUnicode()+"&f"+Numbers.formattedNumber(amount);
            }
            case PYREX_POINTS -> {
                strType = "&dPyrex Points";
                strAmount = "&d"+type.getUnicode()+"&f"+Numbers.formattedNumber(amount);
            }
        }
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(StringUtil.color("&7• Type: "+strType));
        lore.add(StringUtil.color("&7• Amount: "+strAmount));
        lore.add(" ");
        lore.add(StringUtil.color("&eRight Click to redeem!"));
        ItemStack item = ItemBuilder.createItem(name,getMaterial(),lore);
        item = NBTTags.addPyrexData(item,"id",getItemID().name());
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
        if (type != null && amount != null) {
            ItemStack item = e.getItem();
            Currency.giveCurrency(e.getPlayer().getUniqueId(), type,amount);
            item.setAmount(e.getItem().getAmount()-1);
            switch(type) {
                case TOKENS -> p.sendMessage(StringUtil.color("&eRedeemed "+type.getUnicode()+"&f"+ Numbers.formattedNumber(amount)+" &eTokens."));
                case GEMS -> p.sendMessage(StringUtil.color("&aRedeemed "+type.getUnicode()+"&f"+Numbers.formattedNumber(amount)+" &aGems."));
                case PYREX_POINTS -> p.sendMessage(StringUtil.color("&dRedeemed "+type.getUnicode()+"&f"+Numbers.formattedNumber(amount)+" &dPyrex Points."));
            }
        } else {
            p.sendMessage(StringUtil.color("&cInvalid Voucher..."));
        }
    }
}
