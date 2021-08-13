package site.liangbai.realhomehuntforge.event;

import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

/**
 * The type Ray trace event.
 *
 * @author 靓白
 */
public class BlockRayTraceEvent extends Event {
    private final World level;
    private final BlockRayTraceResult rayTraceResult;

    public BlockRayTraceEvent(World level, BlockRayTraceResult rayTraceResult) {
        this.level = level;
        this.rayTraceResult = rayTraceResult;
    }

    public World getLevel() {
        return level;
    }

    public BlockRayTraceResult getRayTraceResult() {
        return rayTraceResult;
    }

    /**
     * 在RayTrace时, 遇到类似玻璃这样的可刺穿方块, 默认为不能打击
     * 所以你可以通过这个事件让玻璃变得可以打击以及修改其他的方块是否可以被打击.
     * 如果pierceable被设置为false, 那么打击的方块即为当前方块
     * 这个事件可能在同一tick内触发非常多次
     *
     * @author 靓白
     */
    public static class TryPierceableBlock extends BlockRayTraceEvent {
        private boolean pierceable;

        public TryPierceableBlock(World level, BlockRayTraceResult rayTraceResult, boolean pierceable) {
            super(level, rayTraceResult);
            this.pierceable = pierceable;
        }

        public boolean isPierceable() {
            return pierceable;
        }

        public void setPierceable(boolean pierceable) {
            this.pierceable = pierceable;
        }
    }
}
