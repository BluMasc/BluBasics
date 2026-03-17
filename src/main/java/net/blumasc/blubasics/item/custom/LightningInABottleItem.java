package net.blumasc.blubasics.item.custom;

import net.blumasc.blubasics.entity.custom.projectile.LightningArcEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class LightningInABottleItem extends Item {
    public LightningInABottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        boolean success = castChainLightning(player, 40.0d, 3.0f, 5, 8.0d);
        if (success && !player.hasInfiniteMaterials()){
            stack.shrink(1);
        }
        return super.use(level, player, usedHand);
    }

    public static LivingEntity findTarget(LivingEntity player, double range) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        Vec3 end = eyePos.add(lookVec.scale(range));

        EntityHitResult hit = ProjectileUtil.getEntityHitResult(
                player.level(),
                player,
                eyePos,
                end,
                player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(5.0D),
                e -> e instanceof LivingEntity && e != player
        );

        return hit != null ? (LivingEntity) hit.getEntity() : null;
    }

    public static void spawnVisualLightning(Level level, Vec3 from, Vec3 to) {
        LightningArcEntity arc = new LightningArcEntity(
                level,
                from,
                to
        );

        level.addFreshEntity(arc);
    }

    public static boolean castChainLightning(
            LivingEntity player,
            double range,
            float damage,
            int maxChains,
            double chainRadius
    ) {
        Level level = player.level();
        if (!(level instanceof ServerLevel sl)) return false;

        LivingEntity firstTarget = findTarget(player, range);
        if (firstTarget == null) return false;

        Set<LivingEntity> hit = new HashSet<>();
        Map<LivingEntity, Candidate> candidates = new HashMap<>();

        Vec3 playerPos = player.getEyePosition();
        Vec3 firstPos = firstTarget.position().add(0, 1, 0);

        Holder<DamageType> lightningHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(DamageTypes.LIGHTNING_BOLT);

        DamageSource lightning = new DamageSource(
                lightningHolder,
                player
        );

        spawnVisualLightning(level, playerPos, firstPos);
        firstTarget.hurt(lightning, damage);
        if(firstTarget instanceof Creeper c){
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            bolt.moveTo(c.position());
            c.thunderHit(sl, bolt);
            c.setRemainingFireTicks(0);
        }
        hit.add(firstTarget);
        hit.add(player);

        seedCandidates(level, firstTarget, firstPos, candidates, hit, player, chainRadius);

        int chains = 0;

        while (!candidates.isEmpty() && chains < maxChains) {
            Map.Entry<LivingEntity, Candidate> next = candidates.entrySet()
                    .stream()
                    .min(Comparator.comparingDouble(e -> e.getValue().distance))
                    .orElse(null);

            if (next == null) break;

            LivingEntity entity = next.getKey();
            Candidate data = next.getValue();

            candidates.remove(entity);
            if (!entity.isAlive()) continue;

            Vec3 targetPos = entity.position().add(0, 1, 0);

            spawnVisualLightning(level, data.fromPos, targetPos);
            entity.hurt(lightning, damage);
            if(entity instanceof Creeper c){
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
                bolt.moveTo(c.position());
                c.thunderHit(sl, bolt);
                c.setRemainingFireTicks(0);
            }

            hit.add(entity);
            chains++;

            seedCandidates(level, entity, targetPos, candidates, hit, player, chainRadius);
        }
        return true;
    }

    private static void seedCandidates(
            Level level,
            LivingEntity source,
            Vec3 sourcePos,
            Map<LivingEntity, Candidate> candidates,
            Set<LivingEntity> hit,
            LivingEntity player,
            double radius
    ) {
        for (LivingEntity e : level.getEntitiesOfClass(
                LivingEntity.class,
                source.getBoundingBox().inflate(radius),
                ent -> ent.isAlive() && ent != player && !hit.contains(ent)
        )) {
            Vec3 ePos = e.position().add(0, 1, 0);
            double dist = sourcePos.distanceTo(ePos);

            Candidate existing = candidates.get(e);
            if (existing == null || dist < existing.distance) {
                candidates.put(e, new Candidate(dist, sourcePos));
            }
        }
    }

    private static class Candidate {
        final double distance;
        final Vec3 fromPos;

        Candidate(double distance, Vec3 fromPos) {
            this.distance = distance;
            this.fromPos = fromPos;
        }
    }
}