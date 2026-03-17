package net.blumasc.blubasics.manager;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RageManager extends SavedData {

    private final Map<UUID, Float> playerDamage = new HashMap<>();

    public static final String DATA_NAME = "rage_damage";
    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag damageTag = new CompoundTag();
        for (Map.Entry<UUID, Float> entry : playerDamage.entrySet()) {
            CompoundTag data = new CompoundTag();
            data.putFloat("damageCount", entry.getValue());
            damageTag.put(entry.getKey().toString(), data);
        }
        compoundTag.put("damageTag", damageTag);
        return compoundTag;
    }

    public static RageManager create(){
        return new RageManager();
    }

    public static RageManager get(ServerLevel world) {
        ServerLevel overworld = world.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(new Factory<>(RageManager::create, RageManager::load), DATA_NAME);
    }

    public static RageManager load(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        RageManager rm = RageManager.create();
        if(tag.contains("damageTag")) {
            CompoundTag damageTag = tag.getCompound("damageTag");
            for(String key : damageTag.getAllKeys()) {
                UUID playerUUID = UUID.fromString(key);
                CompoundTag pTag = damageTag.getCompound(key);
                rm.playerDamage.put(playerUUID, pTag.getFloat("damageCount"));
            }

        }
        return  rm;
    }

    public void saveDamage(UUID player, float damage)
    {
        float oldDamage = playerDamage.computeIfAbsent(player, (id) -> 0.0f);
        playerDamage.replace(player, oldDamage+damage);
        this.setDirty();
    }
    public float getDamage(UUID player)
    {
        return playerDamage.getOrDefault(player, 0.0f);
    }
    public void removeDamage(UUID player)
    {
        playerDamage.remove(player);
        this.setDirty();
    }
}
