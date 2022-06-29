package io.github.lunaiskey.pyrexprison.leaderboards;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.player.PyrexPlayer;
import org.bukkit.Bukkit;

import java.util.*;

public class LeaderboardStorage {

    private Map<UUID, PyrexPlayer> playerMap = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap();
    private LinkedHashMap<UUID,BigIntegerEntry> tokenTopCache = new LinkedHashMap<>();
    private LinkedHashMap<UUID,LongEntry> gemsTopCache = new LinkedHashMap<>();
    private LinkedHashMap<UUID,LongEntry> rankTopCache = new LinkedHashMap<>();


    public void startTasks() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(PyrexPrison.getPlugin(), this::calculateTokensTop,0L,20*60*10L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(PyrexPrison.getPlugin(), this::calculateGemsTop,0L,20*60*10L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(PyrexPrison.getPlugin(), this::calculateRankTop,0L,20*60*10L);
    }

    public void calculateTokensTop() {
        final List<BigIntegerEntry> entries = new LinkedList<>();
        for (UUID uuid : playerMap.keySet()) {
            PyrexPlayer pyrexPlayer = playerMap.get(uuid);
            entries.add(new BigIntegerEntry(uuid, pyrexPlayer.getName(),pyrexPlayer.getTokens()));
        }
        final LinkedHashMap<UUID,BigIntegerEntry> sortedMap = new LinkedHashMap<>();
        entries.sort((entry1,entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        for (BigIntegerEntry entry : entries) {
            sortedMap.put(entry.getUUID(),entry);
        }
        tokenTopCache = sortedMap;
    }

    public void calculateGemsTop() {
        final List<LongEntry> entries = new LinkedList<>();
        for (UUID uuid : playerMap.keySet()) {
            PyrexPlayer pyrexPlayer = playerMap.get(uuid);
            entries.add(new LongEntry(uuid, pyrexPlayer.getName(),pyrexPlayer.getGems()));
        }
        final LinkedHashMap<UUID,LongEntry> sortedMap = new LinkedHashMap<>();
        entries.sort((entry1,entry2) -> Long.compare(entry2.getValue(),entry1.getValue()));
        for (LongEntry entry : entries) {
            sortedMap.put(entry.getUUID(),entry);
        }
        gemsTopCache = sortedMap;
    }

    public void calculateRankTop() {
        final List<LongEntry> entries = new LinkedList<>();
        for (UUID uuid : playerMap.keySet()) {
            PyrexPlayer pyrexPlayer = playerMap.get(uuid);
            entries.add(new LongEntry(uuid, pyrexPlayer.getName(),pyrexPlayer.getRank()));
        }
        final LinkedHashMap<UUID,LongEntry> sortedMap = new LinkedHashMap<>();
        entries.sort((entry1,entry2) -> Long.compare(entry2.getValue(),entry1.getValue()));
        for (LongEntry entry : entries) {
            sortedMap.put(entry.getUUID(),entry);
        }
        rankTopCache = sortedMap;
    }

    public LinkedHashMap<UUID, BigIntegerEntry> getTokenTopCache() {
        return tokenTopCache;
    }

    public LinkedHashMap<UUID, LongEntry> getGemsTopCache() {
        return gemsTopCache;
    }

    public LinkedHashMap<UUID, LongEntry> getRankTopCache() {
        return rankTopCache;
    }


}

