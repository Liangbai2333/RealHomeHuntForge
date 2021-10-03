package site.liangbai.realhomehuntforge.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import site.liangbai.realhomehuntforge.event.BlockDestroyEvent;

@Mixin(World.class)
public abstract class MixinLevel {
    @Inject(
            method = "destroyBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void inject_destroyBlock(BlockPos pos, boolean drop, Entity entity, int flag, CallbackInfoReturnable<Boolean> cir, BlockState blockstate, FluidState fluidstate) {
        BlockDestroyEvent event = new BlockDestroyEvent((IWorld) this, pos, blockstate, fluidstate);
        if (MinecraftForge.EVENT_BUS.post(event)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
