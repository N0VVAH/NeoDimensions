package net.torchednova.neodimensions.voiddimension;

import com.google.common.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.torchednova.neodimensions.Config;
import net.torchednova.neodimensions.NeoDimensions;
import net.torchednova.neodimensions.saving.SaveData;
import net.torchednova.neodimensions.utils.WorldFunctions;

import java.util.ArrayList;

public class PartsManager {
	public static ArrayList<Parts> partsList;

	public static void init()
	{
		partsList = (ArrayList<Parts>) SaveData.load(new TypeToken<ArrayList<Parts>>(){}.getType(), "parts");
		if (partsList == null)
		{
			partsList = new ArrayList<>();
		}
	}

	public static void save()
	{
		SaveData.save(partsList, "parts");
	}



	public static Parts getPart(Player p)
	{
		for (int i = 0; i < partsList.size(); i++)
		{
			if (p.getStringUUID().equals(partsList.get(i).uuid))
			{
				return partsList.get(i);
			}
		}

		return null;
	}

	public static void deleteVoidPart(Parts p, MinecraftServer server)
	{
		ServerLevel l = server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("neodimensions", "void")));
		if (l == null)
		{
			NeoDimensions.LOGGER.error("Failed to find void dimension resourcekey");
			return;
		}

		for (int i = 0; i < p.size.x; i++)
		{
			for (int ii = 0; ii < p.size.z; ii++)
			{
				for (int iii = -64; iii < p.size.y; iii++)
				{
					l.setBlock(new BlockPos(i, iii, ii), Blocks.AIR.defaultBlockState(), 3);
				}
			}
		}
		p.uuid = "";
		save();
	}

	public static Parts getPart(String uuid)
	{
		for (int i = 0; i < partsList.size(); i++)
		{
			if (uuid.equals(partsList.get(i).uuid))
			{
				return partsList.get(i);
			}
		}

		return null;
	}

	public static Parts newPart(Player p)
	{
		double x;
		double z;
		if (partsList.size() > 0) {
			x = partsList.getLast().pos.x + Config.voidXDist.getAsDouble();
			z = partsList.getLast().pos.z;
		}
		else
		{
			x = 0;
			z = 0;
		}
		double y = Config.voidYheight.getAsInt();

		if (x >= Config.voidXDist.getAsDouble() * Config.voidXamount.getAsInt())
		{
			x = 0;
			z = z + Config.voidZDist.getAsDouble();
		}
		Vec3 newPos = new Vec3(x, y, z);
		Vec3 size = new Vec3(Config.voidXDist.getAsDouble(), 320, Config.voidZDist.getAsDouble());

		partsList.add(new Parts(newPos, p.getStringUUID(), size));
		save();


		ServerLevel targetLevel = p.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("neodimensions", "void")));
		if (targetLevel == null)
		{
			NeoDimensions.LOGGER.error("Failed to find void dimension resourcekey");
		}
		else
		{
			WorldFunctions.makeSafePoint(targetLevel, partsList.getLast().center, Blocks.BEDROCK.defaultBlockState());
		}


		return partsList.getLast();
	}



}
