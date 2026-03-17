package net.blumasc.blubasics;

import net.blumasc.blubasics.block.BaseModBlockEntities;
import net.blumasc.blubasics.block.entity.client.VoidBlockEntityRenderer;
import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.entity.client.*;
import net.blumasc.blubasics.entity.client.chimera.ChimeraRenderer;
import net.blumasc.blubasics.entity.client.solarbeetle.SolarBeetleRenderer;
import net.blumasc.blubasics.item.BaseModItems;
import net.blumasc.blubasics.item.client.curios.backtree.BackTreeRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = BluBasicsMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = BluBasicsMod.MODID, value = Dist.CLIENT)
public class BluBasicsModClient {
    public BluBasicsModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {

        EntityRenderers.register(BaseModEntities.SOLAR_BEETLE.get(), SolarBeetleRenderer::new);
        EntityRenderers.register(BaseModEntities.CHIMERA.get(), ChimeraRenderer::new);

        EntityRenderers.register(BaseModEntities.LIGHTNING_ARC.get(), LightningArcRenderer::new);
        EntityRenderers.register(BaseModEntities.METEOR.get(), MeteoriteRenderer::new);
        EntityRenderers.register(BaseModEntities.PICKAXE_BOOMERANG.get(), PickaxeBoomerangRenderer::new);
        EntityRenderers.register(BaseModEntities.DRIPSTONE_SPIKE.get(), SpikeEntityRenderer::new);
        EntityRenderers.register(BaseModEntities.SHARD_PROJECTILE.get(), ShardProjectileRenderer::new);
        EntityRenderers.register(BaseModEntities.THROWN_BLOCK.get(), BlockProjectileEntityRenderer::new);

        CuriosRendererRegistry.register(BaseModItems.SPINE_TREE.get(), BackTreeRenderer::new);

    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(BaseModBlockEntities.VOID_BLOCK_BE.get(), VoidBlockEntityRenderer::new);
    }
}
