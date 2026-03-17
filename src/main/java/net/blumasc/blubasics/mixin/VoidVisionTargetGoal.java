package net.blumasc.blubasics.mixin;

import net.blumasc.blubasics.effect.BaseModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class VoidVisionTargetGoal extends TargetGoal {

    public VoidVisionTargetGoal(Mob mob, boolean mustSee) {
        super(mob, mustSee);
    }

    @Inject(
            method = "canUse",
            at = @At("HEAD"),
            cancellable = true
    )
    public void canUse(CallbackInfoReturnable<Boolean> cir) {
        if(this.mob.hasEffect(BaseModEffects.VOID_EFFECT)){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
