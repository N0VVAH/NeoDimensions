package net.torchednova.neodimensions.deeper_dark;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class PlayerTrackerController {
    public static ArrayList<PlayerTracker> players;

    public static void init()
    {
        players = new ArrayList<>();
    }

    public static void addPlayer(Player p)
    {
        players.add(new PlayerTracker(p, 0));
    }

    public static void removePlayer(Player p)
    {
        for (int i =0; i < players.size(); i++ )
        {
            if (players.get(i).player.getStringUUID().equals(p.getStringUUID()))
            {
                players.remove(i);
                return;
            }
        }
    }

    public static PlayerTracker getPlayer(Player p)
    {
        for (int i =0; i < players.size(); i++ )
        {
            if (players.get(i).player.getStringUUID().equals(p.getStringUUID()))
            {
                return players.get(i);
            }
        }

        return null;
    }



}
