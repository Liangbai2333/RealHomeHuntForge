package site.liangbai.realhomehuntforge.event;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class BlockDestroyEvent extends BlockEvent {
    private final FluidState newState;

    public BlockDestroyEvent(IWorld world, BlockPos pos, BlockState state, FluidState newState) {
        super(world, pos, state);
        this.newState = newState;
    }

    public FluidState getNewState() {
        return newState;
    }
}
