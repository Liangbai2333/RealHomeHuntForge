package site.liangbai.realhomehuntforge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import site.liangbai.realhomehuntforge.event.BlockDestroyEvent;

@Mixin(Level.class)
public abstract class MixinLevel {
    @Inject(
            method = "destroyBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void inject_destroyBlock(BlockPos pos, boolean drop, Entity entity, int flag, CallbackInfoReturnable<Boolean> cir, BlockState blockstate, FluidState fluidstate) {
        BlockDestroyEvent event = new BlockDestroyEvent((Level) (Object) this, pos, blockstate, fluidstate);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
