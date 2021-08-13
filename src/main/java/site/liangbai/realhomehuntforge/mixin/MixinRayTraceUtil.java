package site.liangbai.realhomehuntforge.mixin;

import com.craftingdead.core.util.RayTraceUtil;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import site.liangbai.realhomehuntforge.event.BlockRayTraceEvent;

import java.util.Optional;

/**
 * The type Mixin ray trace util.
 *
 * @author 靓白
 */
@Mixin(RayTraceUtil.class)
public abstract class MixinRayTraceUtil {
    /**
     * @author Liangbai
     * @reason Liangbai low
     */
    @Overwrite
    public static Optional<BlockRayTraceResult> rayTraceBlocksPiercing(Vector3d start,
                                                                       double distance,
                                                                       Vector3d look,
                                                                       RayTraceContext.BlockMode blockMode,
                                                                       RayTraceContext.FluidMode fluidMode,
                                                                       World world) {
        Vector3d newStart = start;
        Vector3d end = start.add(look.scale(distance));
        boolean pierceableBlock = false;
        BlockRayTraceResult blockRayTraceResult = null;
        BlockPos lastBlockPos = null;
        do {
            if (newStart.distanceTo(start) >= distance) {
                break;
            }

            RayTraceContext context = new RayTraceContext(newStart, end, blockMode, fluidMode, null);
            blockRayTraceResult = world.clip(context);

            if (blockRayTraceResult != null) {
                // Not sure about this one, but I have a concern about inaccuracy of Double which could lead
                // to an endless loop
                BlockPos blockPos = blockRayTraceResult.getBlockPos();
                if (lastBlockPos != null && lastBlockPos.equals(blockPos)) {
                    break;
                }
                lastBlockPos = blockPos;

                BlockState blockState = world.getBlockState(blockPos);
                pierceableBlock = !blockState.canOcclude();

                BlockRayTraceEvent.TryPierceableBlock event = new BlockRayTraceEvent.TryPierceableBlock(world, blockRayTraceResult, pierceableBlock);
                MinecraftForge.EVENT_BUS.post(event);
                pierceableBlock = event.isPierceable();

                if (pierceableBlock) {
                    Vector3d hitVec = blockRayTraceResult.getLocation();
                    VoxelShape shape = context.getBlockShape(blockState, world, blockPos);
                    if (!shape.isEmpty()) {
                        AxisAlignedBB bb = shape.bounds();
                        double xDist = look.x() < 0d ? hitVec.x() - bb.minX - blockPos.getX()
                                : blockPos.getX() - hitVec.x() + bb.maxX;
                        double yDist = look.y() < 0d ? hitVec.y() - bb.minY - blockPos.getY()
                                : blockPos.getY() - hitVec.y() + bb.maxY;
                        double zDist = look.z() < 0d ? hitVec.z() - bb.minZ - blockPos.getZ()
                                : blockPos.getZ() - hitVec.z() + bb.maxZ;
                        double xRayDist =
                                Math.abs(look.x()) != 0d ? xDist / Math.abs(look.x()) : Double.MAX_VALUE;
                        double yRayDist =
                                Math.abs(look.y()) != 0d ? yDist / Math.abs(look.y()) : Double.MAX_VALUE;
                        double zRayDist =
                                Math.abs(look.z()) != 0d ? zDist / Math.abs(look.z()) : Double.MAX_VALUE;

                        double rayDist = Math.min(xRayDist, Math.min(zRayDist, yRayDist));
                        newStart = hitVec.add(look.scale(rayDist));
                    }
                }
            }
        } while (pierceableBlock);

        return Optional.ofNullable(blockRayTraceResult);
    }
}
