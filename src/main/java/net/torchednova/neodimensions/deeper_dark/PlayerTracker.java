package net.torchednova.neodimensions.deeper_dark;

import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class PlayerTracker {
    public PlayerTracker(Player player, int ticks)
    {
        this.player = player;
        this.ticks = ticks;
    }
    public Player player;
    public int ticks;
}
