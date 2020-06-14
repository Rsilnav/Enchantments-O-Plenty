package com.rafaelsn.enchantmentsoplenty;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.rafaelsn.enchantmentsoplenty.EnchantmentsOPlenty.ObjectHolders.MIRROR;
import static com.rafaelsn.enchantmentsoplenty.EnchantmentsOPlenty.SWORD;
import static net.minecraft.util.SoundEvents.BLOCK_BELL_USE;

@Mod.EventBusSubscriber(modid = EnchantmentsOPlenty.MODID)
public class MirrorEnchantment extends Enchantment {

    public MirrorEnchantment() {
        super(Rarity.RARE, SWORD, new EquipmentSlotType[]{
                EquipmentSlotType.MAINHAND
        });
        this.setRegistryName("mirror");
    }

    @Override
    public int getMinEnchantability(int level) {
        return 15;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (MIRROR == null) return;

        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity.world.isRemote) return;
        ServerWorld world = (ServerWorld) livingEntity.world;

        if (livingEntity instanceof WitherEntity) return;
        if (livingEntity instanceof EnderDragonEntity) return;
        if (livingEntity instanceof ElderGuardianEntity) return;
        if (livingEntity instanceof EvokerEntity) return;

        if (!(event.getSource().getTrueSource() instanceof PlayerEntity)) return;
        PlayerEntity playerEntity = (PlayerEntity) event.getSource().getTrueSource();
        int level = EnchantmentHelper.getEnchantmentLevel(MIRROR, playerEntity.getHeldItemMainhand().getStack());
        if (level == 0) return;

        final Random rand = new Random();
        if (rand.nextFloat() > 0.3f) return;

        Entity childEntity = livingEntity.getType().create(world);
        if (childEntity != null) {
            childEntity.setLocationAndAngles(livingEntity.getPosX() + (rand.nextFloat() - 0.5f) * 2, livingEntity.getPosY() + 0.5D, livingEntity.getPosZ() + (rand.nextFloat() - 0.5f) * 2, rand.nextFloat() * 360.0F, 0.0F);
            world.summonEntity(childEntity);
            childEntity.playSound(BLOCK_BELL_USE, 2.0f, 1.0f);
        }
    }
}
