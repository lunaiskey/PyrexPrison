package io.github.lunaiskey.pyrexprison;

import io.github.lunaiskey.pyrexprison.player.Currency;
import io.github.lunaiskey.pyrexprison.player.CurrencyType;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.player.Rankup;
import io.github.lunaiskey.pyrexprison.util.Numbers;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
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
        if (pyrexPlayer != null) {
            return switch(params.toLowerCase()) {
                case "tokens" -> Numbers.formattedNumber(pyrexPlayer.getTokens());
                case "gems" -> Numbers.formattedNumber(pyrexPlayer.getGems());
                case "points" -> Numbers.formattedNumber(pyrexPlayer.getPyrexPoints());
                case "unicode_tokens" -> CurrencyType.getUnicode(CurrencyType.TOKENS);
                case "unicode_gems" -> CurrencyType.getUnicode(CurrencyType.GEMS);
                case "unicode_points" -> CurrencyType.getUnicode(CurrencyType.PYREX_POINTS);
                case "rank" -> String.valueOf(pyrexPlayer.getRank());
                case "rank_next" -> String.valueOf(Math.min(pyrexPlayer.getRank() + 1, Rankup.getMaxRankup()));
                case "rank_percentage" -> Numbers.formatDouble(Rankup.getRankUpPercentage(Objects.requireNonNull(player.getPlayer())));
                case "rank_progressbar" -> Rankup.getRankUpProgressBar(Objects.requireNonNull(player.getPlayer()));
                case "gemstone_progress" -> String.valueOf(pyrexPlayer.getGemstoneCount());
                case "gemstone_max" -> String.valueOf(PyrexPrison.getPlugin().getPlayerManager().getGemstoneCountMax((Player) player));
                default -> null;
            };
        } else {
            return null;
        }
    }

}
