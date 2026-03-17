package net.blumasc.blubasics.entity.custom.helper;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class MeteorSphere {

    public static List<BlockPos> positions(int radius) {
        List<BlockPos> result = new ArrayList<>();
        int r2 = radius * radius;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x*x + y*y + z*z <= r2) {
                        result.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return result;
    }

    public static List<BlockPos> shell(int radius) {
        List<BlockPos> result = new ArrayList<>();
        int r2 = radius * radius;
        int inner2 = (radius - 1) * (radius - 1);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    int d = x*x + y*y + z*z;
                    if (d <= r2 && d > inner2) {
                        result.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return result;
    }
}
