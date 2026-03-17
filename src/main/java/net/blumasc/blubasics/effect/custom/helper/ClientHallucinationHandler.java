package net.blumasc.blubasics.effect.custom.helper;

import net.blumasc.blubasics.effect.BaseModEffects;
import net.blumasc.blubasics.entity.client.fakeMob.FakeMob;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ClientHallucinationHandler {

    private static final RandomSource RANDOM = RandomSource.create();
    private static final List<FakeMob> ACTIVE_MOBS = new ArrayList<>();

    public static void tickPlayer(Player player) {
        if (!player.hasEffect(BaseModEffects.HALLUCINATION)) {
            clear();
        }
    }

    public static void tick(Player player, int amplifier) {
        if(!player.hasEffect(BaseModEffects.HALLUCINATION))
        {
            clear();
            return;
        }
        if (RANDOM.nextInt(120/(amplifier+1)) == 0) {
            spawnFakeMob(player);
        }
        if (RANDOM.nextInt(80/(amplifier+1)) == 0) {
            SoundEvent e;
            switch(RANDOM.nextInt(7))
            {
                case 1: e = SoundEvents.CREEPER_PRIMED; break;
                case 2: e = SoundEvents.SKELETON_AMBIENT; break;
                case 3: e = SoundEvents.SPIDER_AMBIENT; break;
                case 4: e = SoundEvents.WITCH_AMBIENT; break;
                case 5: e = SoundEvents.SILVERFISH_AMBIENT; break;
                case 6: e = SoundEvents.BLAZE_AMBIENT; break;
                case 7: e = SoundEvents.GHAST_SHOOT; break;
                default: e = SoundEvents.ZOMBIE_AMBIENT;
            }
            player.playSound(
                    e,
                    1.0f,
                    0.8f + player.getRandom().nextFloat() * 0.4f
            );
        }

        ACTIVE_MOBS.removeIf(fake -> fake.tick(player));
    }

    private static void spawnFakeMob(Player player) {
        Level level = player.level();

        BlockPos pos = getSpawnPos(level, player.position(), RANDOM);

        if(level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {

            FakeMob fake = new FakeMob(level, pos.getBottomCenter(), RANDOM);
            ACTIVE_MOBS.add(fake);
        }
    }
    public static List<FakeMob> getActiveMobs()
    {
        return ACTIVE_MOBS;
    }
    public static void clear() {
        ACTIVE_MOBS.clear();
    }

    public static BlockPos getSpawnPos(Level level, Vec3 origin, RandomSource rand) {
        double x = origin.x + rand.nextDouble() * 12 - 6;
        double z = origin.z + rand.nextDouble() * 12 - 6;

        double y = origin.y + 4;

        BlockPos pos = new BlockPos((int) x, (int) y, (int) z);

        while (pos.getY() > origin.y-3 && !level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {
            pos = pos.below();
        }

        return pos;
    }
}
