package net.torchednova.neodimensions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.torchednova.neodimensions.NeoDimensions;
import net.torchednova.neodimensions.utils.WorldFunctions;
import net.torchednova.neodimensions.voiddimension.Parts;
import net.torchednova.neodimensions.voiddimension.PartsManager;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.UUID;

public class neodim {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
			Commands.literal("neodim").requires(source -> source.hasPermission(2))
				.then(Commands.literal("tp")
					.then(Commands.argument("player", EntityArgument.player())
						.executes(context ->
							{
								ServerPlayer p = EntityArgument.getPlayer(context, "player");

								Parts part = PartsManager.getPart(p);
								if (part == null) part = PartsManager.newPart(p);
								if (part == null)
								{
									NeoDimensions.LOGGER.error("Failed to both load Player Part in Void dimension and then failed to create one");
									return 1;
								}

								ServerLevel targetLevel = p.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("neodimensions", "void")));
								if (targetLevel == null)
								{
									NeoDimensions.LOGGER.error("Failed to find void dimension resourcekey");
									return 1;
								}

								Vec3 Upperpos = new Vec3(part.center.x, part.center.y + 1, part.center.z);


								DimensionTransition transition = new DimensionTransition(
									targetLevel,
									Upperpos,
									Vec3.ZERO,
									p.getYRot(),
									p.getXRot(),
									false,
									DimensionTransition.DO_NOTHING
								);

								p.changeDimension(transition);
								return 1;
							}
						)
					)
				)
				.then(Commands.literal("tpoffline")
					.then(Commands.argument("player", StringArgumentType.greedyString())
						.executes(context ->
							{
								String p = StringArgumentType.getString(context, "player");

								Parts part = PartsManager.getPart(p);
								if (part == null)
								{
									NeoDimensions.LOGGER.error("Failed to both load Player Part in Void dimension and then failed to create one");
									return 1;
								}

								ServerLevel targetLevel = context.getSource().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("neodimensions", "void")));
								if (targetLevel == null)
								{
									NeoDimensions.LOGGER.error("Failed to find void dimension resourcekey");
									return 1;
								}

								Vec3 Upperpos = new Vec3(part.center.x, part.center.y + 1, part.center.z);


								DimensionTransition transition = new DimensionTransition(
									targetLevel,
									Upperpos,
									Vec3.ZERO,
									0,
									0,
									false,
									DimensionTransition.DO_NOTHING
								);

								context.getSource().getEntity().changeDimension(transition);
								return 1;
							}
						)
					)
				)
				.then(Commands.literal("forcenew")
						.executes(context ->
							{
								ServerPlayer p = context.getSource().getPlayer();

								Parts part = PartsManager.getPart(UUID.randomUUID().toString());
								if (part == null) part = PartsManager.newPart(p);
								if (part == null)
								{
									NeoDimensions.LOGGER.error("Failed to both load Player Part in Void dimension and then failed to create one");
									return 1;
								}

								ServerLevel targetLevel = p.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("neodimensions", "void")));
								if (targetLevel == null)
								{
									NeoDimensions.LOGGER.error("Failed to find void dimension resourcekey");
									return 1;
								}

								Vec3 Upperpos = new Vec3(part.center.x, part.center.y + 1, part.center.z);


								DimensionTransition transition = new DimensionTransition(
									targetLevel,
									Upperpos,
									Vec3.ZERO,
									p.getYRot(),
									p.getXRot(),
									false,
									DimensionTransition.DO_NOTHING
								);

								p.changeDimension(transition);
								return 1;
							}

					)
				)
				.then(Commands.literal("delete")
					.then(Commands.argument("player", StringArgumentType.string())
						.executes(context ->
							{
								String p = StringArgumentType.getString(context, "player");
								Parts part = PartsManager.getPart(p);
								if (part == null)
								{
									NeoDimensions.LOGGER.error("Player part in void dim not found");
									context.getSource().sendSuccess(() -> Component.literal("Player part in void dim not found"), false);
									return 1;
								}

								PartsManager.deleteVoidPart(part, context.getSource().getServer());
								return 1;
							}
						)
					)
				)
				.then(Commands.literal("reset")
					.then(Commands.argument("player", StringArgumentType.greedyString())
						.executes(context ->
							{
								String p = StringArgumentType.getString(context, "player");

								Parts part = PartsManager.getPart(p);
								if (part == null)
								{
									NeoDimensions.LOGGER.error("Player part in void dim not found");
									context.getSource().sendSuccess(() -> Component.literal("Player part in void dim not found"), false);
									return 1;
								}
								ServerLevel targetLevel = context.getSource().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("neodimensions", "void")));
								if (targetLevel == null)
								{
									NeoDimensions.LOGGER.error("Failed to find void dimension resourcekey");
									context.getSource().sendSuccess(() -> Component.literal("Failed to find void dimension resourcekey"), false);
								}
								else
								{
									WorldFunctions.makeSafePoint(targetLevel, part.center, Blocks.BEDROCK.defaultBlockState());
								}

								return 1;
							}
						)
					)
				)
		);
	}
}
