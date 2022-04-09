package site.liangbai.realhomehuntforge.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class BlockDestroyEvent extends BlockEvent {
    private final FluidState newState;

    public BlockDestroyEvent(Level world, BlockPos pos, BlockState state, FluidState newState) {
        super(world, pos, state);
        this.newState = newState;
    }

    public FluidState getNewState() {
        return newState;
    }
}
