package net.blumasc.blubasics.events;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.entity.client.chimera.ChimeraModel;
import net.blumasc.blubasics.entity.client.solarbeetle.SolarBeetleModel;
import net.blumasc.blubasics.entity.custom.ChimeraEntity;
import net.blumasc.blubasics.entity.custom.SolarBeetleEntity;
import net.blumasc.blubasics.item.client.curios.backtree.BackTreeModel;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = BluBasicsMod.MODID)
public class BluBasicsEventBus {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(SolarBeetleModel.LAYER_LOCATION, SolarBeetleModel::createBodyLayer);
        event.registerLayerDefinition(ChimeraModel.LAYER_LOCATION, ChimeraModel::createBodyLayer);


        event.registerLayerDefinition(BackTreeModel.LAYER_LOCATION, BackTreeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAtributes(EntityAttributeCreationEvent event){
        event.put(BaseModEntities.SOLAR_BEETLE.get(), SolarBeetleEntity.createAttributes().build());
        event.put(BaseModEntities.CHIMERA.get(), ChimeraEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event){
        event.register(BaseModEntities.SOLAR_BEETLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

    }
}
