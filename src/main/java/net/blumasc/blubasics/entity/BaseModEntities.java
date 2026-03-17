package net.blumasc.blubasics.entity;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.custom.projectile.*;
import net.blumasc.blubasics.entity.custom.ChimeraEntity;
import net.blumasc.blubasics.entity.custom.SolarBeetleEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BaseModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, BluBasicsMod.MODID);

    public static final Supplier<EntityType<SolarBeetleEntity>> SOLAR_BEETLE =
            ENTITY_TYPES.register("solar_beetle", () -> EntityType.Builder.of(SolarBeetleEntity::new, MobCategory.CREATURE).sized(0.8f,0.6f).build("solar_beetle"));

    public static final Supplier<EntityType<ChimeraEntity>> CHIMERA =
            ENTITY_TYPES.register("chimera", () -> EntityType.Builder.of(ChimeraEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 1.2f).build("chimera"));

    public static final Supplier<EntityType<LightningArcEntity>> LIGHTNING_ARC =
            ENTITY_TYPES.register("lightning_arc", () ->
                    EntityType.Builder.<LightningArcEntity>of(LightningArcEntity::new, MobCategory.MISC)
                            .sized(0.1F, 0.1F)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("lightning_arc"));

    public static final Supplier<EntityType<MeteoriteEntity>> METEOR =
            ENTITY_TYPES.register("meteor", () ->
                    EntityType.Builder.<MeteoriteEntity>of(MeteoriteEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("meteor")
            );

    public static final Supplier<EntityType<PickaxeBoomerangEntity>> PICKAXE_BOOMERANG =
            ENTITY_TYPES.register("pickaxe_boomerang", () ->
                    EntityType.Builder.<PickaxeBoomerangEntity>of(PickaxeBoomerangEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("pickaxe_boomerang")
            );

    public static final Supplier<EntityType<SpikeEntity>> DRIPSTONE_SPIKE =
            ENTITY_TYPES.register("dripstone_spike", () ->
                    EntityType.Builder.<SpikeEntity>of(SpikeEntity::new, MobCategory.MISC)
                            .sized(1.0F, 1.0F)
                            .clientTrackingRange(128)
                            .updateInterval(1)
                            .build("dripstone_spike")
            );

    public static final Supplier<EntityType<ShardProjectileEntity>> SHARD_PROJECTILE =
            ENTITY_TYPES.register("shard_projectile", () -> EntityType.Builder.<ShardProjectileEntity>of(ShardProjectileEntity::new, MobCategory.MISC)
                    .sized(0.3f, 0.3f).clientTrackingRange(4).updateInterval(20).build("shard_projectile"));

    public static final Supplier<EntityType<BlockProjectileEntity>> THROWN_BLOCK =
            ENTITY_TYPES.register("thrown_block", () -> EntityType.Builder.<BlockProjectileEntity>of(BlockProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).clientTrackingRange(4).updateInterval(20).build("thrown_block"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
