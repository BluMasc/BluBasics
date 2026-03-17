package net.blumasc.blubasics.mixin;

import net.blumasc.blubasics.item.BaseModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.Optional;

@Mixin(BlockBehaviour.class)
public abstract class LeafWalkerLeavesMixin {

    @Inject(
            method = "getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!state.is(BlockTags.LEAVES)) return;
        if (!(context instanceof EntityCollisionContext ectx)) return;
        if (!(ectx.getEntity() instanceof Player player)) return;
        if (isWearingLeafWalker(player)) {
            cir.setReturnValue(Shapes.empty());
        }
    }

    private static boolean isWearingLeafWalker(Player player) {
        Optional<ICuriosItemHandler> curiosInventoryOpt = CuriosApi.getCuriosInventory(player);
        if (curiosInventoryOpt.isPresent()) {
            Map<String, ICurioStacksHandler> curios = curiosInventoryOpt.get().getCurios();
            for (ICurioStacksHandler slotInventory : curios.values()) {
                for (int slot = 0; slot < slotInventory.getSlots(); slot++) {
                    ItemStack stack = slotInventory.getStacks().getStackInSlot(slot);
                    if (!stack.isEmpty() && stack.is(BaseModItems.SPINE_TREE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
