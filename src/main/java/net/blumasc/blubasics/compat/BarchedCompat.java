package net.blumasc.blubasics.compat;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.fml.ModList;

// BarchedCompat.java
public class BarchedCompat {

    public static boolean isLoaded() {
        return ModList.get().isLoaded("barched");
    }

    // Calls the .spear() method Barched injects into Item.Properties via Mixin
    // Parameters mirror iron spear — adjust floats to your tier (see table below)
    public static Item.Properties applySpearComponent(Item.Properties props, Tiers tier,
                                                      float swingSeconds, float kineticDamageMult, float delaySeconds,
                                                      float damageCondDuration, float damageCondMinSpeed,
                                                      float knockbackCondDuration, float knockbackCondMinSpeed,
                                                      float dismountCondDuration, float dismountCondMinRelativeSpeed) {
        try {
            // Item.Properties implements Item$PropertiesBridge at runtime via Mixin
            // so the .spear() method is directly on the properties object
            java.lang.reflect.Method spearMethod = props.getClass().getMethod("spear",
                Tiers.class,
                float.class, float.class, float.class,
                float.class, float.class,
                float.class, float.class,
                float.class, float.class
            );
            return (Item.Properties) spearMethod.invoke(props,
                tier,
                swingSeconds, kineticDamageMult, delaySeconds,
                damageCondDuration, damageCondMinSpeed,
                knockbackCondDuration, knockbackCondMinSpeed,
                dismountCondDuration, dismountCondMinRelativeSpeed
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to apply spear component", e);
        }
    }
    public static ItemAttributeModifiers buildSpearAttributes(Tier tier, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                Item.BASE_ATTACK_DAMAGE_ID,
                                attackDamage + tier.getAttackDamageBonus(),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                Item.BASE_ATTACK_SPEED_ID,
                                attackSpeed,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }
}