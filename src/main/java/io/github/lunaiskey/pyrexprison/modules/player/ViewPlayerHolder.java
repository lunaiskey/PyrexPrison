package io.github.lunaiskey.pyrexprison.modules.player;

import io.github.lunaiskey.pyrexprison.gui.PyrexHolder;
import io.github.lunaiskey.pyrexprison.gui.PyrexInvType;
import org.bukkit.entity.Player;

public class ViewPlayerHolder extends PyrexHolder {

    private Player player;

    public ViewPlayerHolder(String name, int size, PyrexInvType invType, Player player) {
        super(name, size, invType);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
