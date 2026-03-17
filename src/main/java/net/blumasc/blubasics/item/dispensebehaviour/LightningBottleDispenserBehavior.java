package net.blumasc.blubasics.item.dispensebehaviour;

import net.blumasc.blubasics.entity.custom.projectile.LightningArcEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class LightningBottleDispenserBehavior extends DefaultDispenseItemBehavior {

    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        Level level = source.level();
        if(!(level instanceof ServerLevel sl)) return stack;
        BlockPos pos = source.pos();
        Direction facing = source.state().getValue(net.minecraft.world.level.block.DispenserBlock.FACING);

        Vec3 start = new Vec3(
                pos.getX() + 0.5 + facing.getStepX() * 1.1,
                pos.getY() + 0.5 + facing.getStepY() * 1.1,
                pos.getZ() + 0.5 + facing.getStepZ() * 1.1
        );

        LivingEntity firstTarget = findFirstTarget(level, start, facing, 6.0);

        if (firstTarget != null) {
            castChainLightning(level, start, firstTarget, 3.0f, 5, 8.0);
        } else {
            Vec3 end = start.add(facing.getStepX() * 5.0, facing.getStepY() * 5.0, facing.getStepZ() * 5.0);
            spawnVisualLightning(level, start, end);
        }

        stack.shrink(1);
        return stack;
    }

    private LivingEntity findFirstTarget(Level level, Vec3 start, Direction facing, double range) {
        Vec3 end = start.add(facing.getStepX() * range, facing.getStepY() * range, facing.getStepZ() * range);
        return level.getEntitiesOfClass(LivingEntity.class,
                new net.minecraft.world.phys.AABB(start, end).inflate(3.0),
                LivingEntity::isAlive
        ).stream().findFirst().orElse(null);
    }

    private void castChainLightning(Level level, Vec3 startPos, LivingEntity firstTarget, float damage, int maxChains, double chainRadius) {
        Set<LivingEntity> hit = new HashSet<>();
        Map<LivingEntity, Candidate> candidates = new HashMap<>();

        Vec3 targetPos = firstTarget.getEyePosition();

        spawnVisualLightning(level, startPos, targetPos);
        firstTarget.hurt(level.damageSources().lightningBolt(), damage);
        if(firstTarget instanceof Creeper c && level instanceof ServerLevel sl){
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
            bolt.moveTo(c.position());
            c.thunderHit(sl, bolt);
            c.setRemainingFireTicks(0);
        }
        hit.add(firstTarget);

        seedCandidates(level, firstTarget, targetPos, candidates, hit, chainRadius);

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

            Vec3 posEntity = entity.getEyePosition();
            spawnVisualLightning(level, data.fromPos, posEntity);
            entity.hurt(level.damageSources().lightningBolt(), damage);
            if(entity instanceof Creeper c && level instanceof ServerLevel sl){
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
                bolt.moveTo(c.position());
                c.thunderHit(sl, bolt);
                c.setRemainingFireTicks(0);
            }

            hit.add(entity);
            chains++;
            seedCandidates(level, entity, posEntity, candidates, hit, chainRadius);
        }
    }

    private void seedCandidates(Level level, LivingEntity source, Vec3 sourcePos, Map<LivingEntity, Candidate> candidates, Set<LivingEntity> hit, double radius) {
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, source.getBoundingBox().inflate(radius), ent -> ent.isAlive() && !hit.contains(ent))) {
            Vec3 ePos = e.position().add(0, 1, 0);
            double dist = sourcePos.distanceTo(ePos);

            Candidate existing = candidates.get(e);
            if (existing == null || dist < existing.distance) {
                candidates.put(e, new Candidate(dist, sourcePos));
            }
        }
    }

    private void spawnVisualLightning(Level level, Vec3 from, Vec3 to) {
        LightningArcEntity arc = new LightningArcEntity(level, from, to);
        level.addFreshEntity(arc);
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
