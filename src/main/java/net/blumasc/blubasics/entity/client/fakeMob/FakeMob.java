package net.blumasc.blubasics.entity.client.fakeMob;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FakeMob {

    private final Mob zombie;
    private int lifeTicks = 200;

    public FakeMob(Level level, Vec3 pos, RandomSource rand) {
        Mob zombie1;
        switch(rand.nextInt(8))
        {
            case 1: zombie1 = new Skeleton(EntityType.SKELETON, level); break;
            case 2: zombie1 = new Creeper(EntityType.CREEPER, level); break;
            case 3: zombie1 = new Spider(EntityType.SPIDER,level); break;
            case 4: zombie1 = new Witch(EntityType.WITCH, level); break;
            case 5: zombie1 = new EnderMan(EntityType.ENDERMAN, level); break;
            case 6: zombie1 = new Slime(EntityType.SLIME, level); break;
            case 7: zombie1 = new WitherSkeleton(EntityType.WITHER_SKELETON, level); break;
            default: zombie1 = new Zombie(EntityType.ZOMBIE, level);
        }

        zombie = zombie1;
        zombie.setPos(pos);
        zombie.setSilent(true);
        lifeTicks = rand.nextInt(120);
    }

    public boolean tick(Player player) {
        lifeTicks--;

        zombie.getLookControl().setLookAt(player);

        float yaw = (float) Math.toDegrees(
                Math.atan2(
                        player.getZ() - zombie.getZ(),
                        player.getX() - zombie.getX()
                )
        ) - 90.0F;

        zombie.yRotO = zombie.getYRot();
        zombie.xRotO = zombie.getXRot();
        zombie.yBodyRotO = zombie.yBodyRot;
        zombie.yHeadRotO = zombie.yHeadRot;

        zombie.setYRot(yaw);
        zombie.yBodyRot = yaw;
        zombie.yHeadRot = yaw;

        return lifeTicks <= 0;
    }

    public Entity getEntity() {
        return zombie;
    }
}
