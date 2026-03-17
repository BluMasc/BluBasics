package net.blumasc.blubasics.util;

import net.blumasc.blubasics.BluBasicsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class BaseModTags {
    public static class Items{
        public static final TagKey<Item> SOLAR_BEETLE_FOOD = createTag("solar_beetle_food");
        public static final TagKey<Item> COMPOSTABLE_SHROOMS = createTag("compostable_shrooms");

        private static TagKey<Item> createTag(String name){
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, name));
        }
    }
    public static class Biomes{
        public static final TagKey<Biome> SOLAR_BEETLE_SPAWNABLE = createTag("solar_beetle_spawnable");
        private static TagKey<Biome> createTag(String name){
            return TagKey.create(
                    Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, name)
            );
        }
    }
    public static class EntityTypes{
        public static final TagKey<EntityType<?>> CHIMERA_LIKE = createTag("chimera_like");
        public static final TagKey<EntityType<?>> CHIMERA_LIKE_FOX = createTag("chimera_like/fox");
        public static final TagKey<EntityType<?>> CHIMERA_LIKE_CHICKEN = createTag("chimera_like/chicken");
        public static final TagKey<EntityType<?>> CHIMERA_LIKE_GOAT = createTag("chimera_like/goat");
        public static final TagKey<EntityType<?>> CHIMERA_LIKE_GUARDIAN = createTag("chimera_like/guardian");
        public static final TagKey<EntityType<?>> CHIMERA_LIKE_HOGLIN = createTag("chimera_like/hoglin");
        public static final TagKey<EntityType<?>> CHIMERA_LIKE_PHANTOM = createTag("chimera_like/phantom");
        public static final TagKey<EntityType<?>> CHIMERA_LIKE_RABBIT = createTag("chimera_like/rabbit");
        public static final TagKey<EntityType<?>> SAND_IMMUNE = createTag("sand_immune");
        private static TagKey<EntityType<?>> createTag(String name){
            return TagKey.create(
                    Registries.ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, name)
            );
        }
    }
}
