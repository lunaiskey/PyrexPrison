package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PyrexExpansion extends PlaceholderExpansion {

    private final PyrexPrison plugin;

    public PyrexExpansion(PyrexPrison plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "pyrex";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lunaiskey";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        PyrexPlayer pyrexPlayer = plugin.getPlayerManager().getPlayerMap().get(player.getUniqueId());
        if (params.equalsIgnoreCase("tokens")) {
            return Numbers.formattedNumber(pyrexPlayer.getTokens());
        }
        if (params.equalsIgnoreCase("gems")) {
            return Numbers.formattedNumber(pyrexPlayer.getGems());
        }
        if (params.equalsIgnoreCase("points")) {
            return Numbers.formattedNumber(pyrexPlayer.getPyrexPoints());
        }
        return null;
    }

}
