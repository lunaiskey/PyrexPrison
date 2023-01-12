package io.github.lunaiskey.pyrexprison.modules.boosters;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import io.github.lunaiskey.pyrexprison.modules.player.PyrexPlayer;
import io.github.lunaiskey.pyrexprison.modules.boosters.gui.PersonalBoosterGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class Boosters {

    public void scheduleTask() {
        Bukkit.getScheduler().runTaskTimer(PyrexPrison.getPlugin(),() -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                PyrexPlayer pyrexPlayer = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
                if (pyrexPlayer != null) {
                    List<Booster> boosters = pyrexPlayer.getBoosters();
                    if (boosters.size() > 0) {
                        for (int i = boosters.size()-1; i>=0;i--) {
                            Booster booster = boosters.get(i);
                            if (!booster.isActive()) {
                                continue;
                            }
                            if (System.currentTimeMillis() > booster.getStartTime()+(booster.getLength()*1000L)) {
                                boosters.remove(i);
                            }
                        }
                    }
                    Inventory inv = p.getOpenInventory().getTopInventory();
                    if (inv.getHolder() instanceof PyrexHolder) {
                        PyrexHolder holder = (PyrexHolder) inv.getHolder();
                        if (holder.getInvType() == PyrexInvType.PERSONAL_BOOSTER) {
                            new PersonalBoosterGUI(p).updateGUI(p);
                        }
                    }
                }
            }

        },0L,20L);
    }
}
