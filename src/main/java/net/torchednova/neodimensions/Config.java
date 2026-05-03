package net.torchednova.neodimensions;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue voidXDist = BUILDER
        .comment("Whether to log the dirt block on common setup")
        .defineInRange("voidXDist", 500000.0, 1, Double.MAX_VALUE);

    public static final ModConfigSpec.IntValue voidXamount =BUILDER
        .comment("Whether to log the dirt block on common setup")
        .defineInRange("voidXamount", 50, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue voidZDist =BUILDER
        .comment("Whether to log the dirt block on common setup")
        .defineInRange("voidZDist", 500000.0, 1, Double.MAX_VALUE);

    public static final ModConfigSpec.IntValue voidZamount = BUILDER
        .comment("Whether to log the dirt block on common setup")
        .defineInRange("voidZamount", 50, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue voidYheight =BUILDER
        .comment("Whether to log the dirt block on common setup")
        .defineInRange("voidYheight", 100, 1, Integer.MAX_VALUE);


    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}
