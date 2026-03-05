package net.torchednova.neodimensions.itemsblocks.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.torchednova.neodimensions.utils.WorldFunctions;
import org.jetbrains.annotations.NotNull;

public class DeeperDarkPortalBlock extends Block {

    private ServerLevel targetLevel = null;
    private ServerLevel overworld = null;

    public DeeperDarkPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
    {
        if (player.getServer() == null) return InteractionResult.PASS;

        if (targetLevel == null)
        {
            targetLevel = player.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("neodimensions", "deeper_dark")));
            overworld = player.getServer().getLevel(Level.OVERWORLD);
            WorldFunctions.makeSafePoint(targetLevel, pos.getCenter());
        }

        if (player.getItemInHand(player.getUsedItemHand()) == ItemStack.EMPTY)
        {
            if (player.level().dimension() == Level.OVERWORLD) {

                DimensionTransition transition = new DimensionTransition(
                        targetLevel,
                        pos.getCenter(),
                        Vec3.ZERO,
                        player.getYRot(),
                        player.getXRot(),
                        false,
                        DimensionTransition.DO_NOTHING
                );
                player.changeDimension(transition);
                transition = null;
                return InteractionResult.SUCCESS;
            }
            else if (player.level().dimension() == (targetLevel).dimension())
            {

                Vec3 Upperpos = new Vec3(pos.getX(), pos.getY() + 1, pos.getZ());

                DimensionTransition transition = new DimensionTransition(
                        overworld,
                        Upperpos,
                        Vec3.ZERO,
                        player.getYRot(),
                        player.getXRot(),
                        false,
                        DimensionTransition.DO_NOTHING
                );
                player.changeDimension(transition);
                transition = null;
                return InteractionResult.SUCCESS;
            }
            else
            {

                if (player instanceof ServerPlayer)
                {
                    Component title = Component.literal("This portal only works in the Overworld and Deeper Dark").withStyle(ChatFormatting.RED);
                    ClientboundSetTitlesAnimationPacket animationPacket = new ClientboundSetTitlesAnimationPacket(10, 70, 20);

                    ClientboundSetTitleTextPacket titlePacket = new ClientboundSetTitleTextPacket(title);

                    ServerPlayer sp = (ServerPlayer) player;
                    sp.connection.send(animationPacket);
                    sp.connection.send(titlePacket);
                }


            }


        }

        return InteractionResult.PASS;
    }


}
