package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.player.Rankup;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
        String result = null;
        switch(params.toLowerCase()) {
            case "tokens" -> result = Numbers.formattedNumber(pyrexPlayer.getTokens());
            case "gems" -> result = Numbers.formattedNumber(pyrexPlayer.getGems());
            case "points" -> result = Numbers.formattedNumber(pyrexPlayer.getPyrexPoints());
            case "rank" -> result = String.valueOf(pyrexPlayer.getRank());
            case "rank_next" -> result = String.valueOf(Math.min(pyrexPlayer.getRank() + 1, Rankup.getMaxRankup()));
            case "rank_percentage" -> result = Numbers.formatDouble(Rankup.getRankUpPercentage(Objects.requireNonNull(player.getPlayer())));
            case "rank_progressbar" -> result = Rankup.getRankUpProgressBar(Objects.requireNonNull(player.getPlayer()));
        }
        return result;
    }

}
