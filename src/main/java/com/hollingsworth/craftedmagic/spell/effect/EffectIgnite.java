package com.hollingsworth.craftedmagic.spell.effect;

import com.hollingsworth.craftedmagic.ModConfig;
import com.hollingsworth.craftedmagic.spell.augment.AugmentType;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.ArrayList;

public class EffectIgnite  extends EffectType{

    public EffectIgnite() {
        super(ModConfig.EffectIgniteID, "Ignite");
    }

    @Override
    public void onResolve(RayTraceResult rayTraceResult, World world, LivingEntity shooter, ArrayList<AugmentType> enhancements) {
        if(rayTraceResult instanceof EntityRayTraceResult){
            ((EntityRayTraceResult) rayTraceResult).getEntity().setFire(2);
        }else if(rayTraceResult instanceof BlockRayTraceResult && world.getBlockState(((BlockRayTraceResult) rayTraceResult).getPos().up()) == Blocks.AIR.getDefaultState()){
            world.setBlockState(((BlockRayTraceResult) rayTraceResult).getPos().up(), Blocks.FIRE.getDefaultState());
        }
    }

    @Override
    public int getManaCost() {
        return 0;
    }
}
