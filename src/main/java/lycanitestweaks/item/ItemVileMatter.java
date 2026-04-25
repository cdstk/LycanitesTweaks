package lycanitestweaks.item;

import com.lycanitesmobs.core.entity.ExtendedPlayer;
import com.lycanitesmobs.core.info.CreatureInfo;
import com.lycanitesmobs.core.info.CreatureManager;
import com.lycanitesmobs.core.info.ElementInfo;
import com.lycanitesmobs.core.info.ItemConfig;
import com.lycanitesmobs.core.item.ItemBase;
import com.lycanitesmobs.core.pets.SummonSet;
import lycanitestweaks.capability.toggleableitem.IToggleableItem;
import lycanitestweaks.capability.toggleableitem.ToggleableItem;
import lycanitestweaks.handlers.ForgeConfigHandler;
import lycanitestweaks.item.base.ItemPassive;
import lycanitestweaks.mixin.vanilla.Entity_AccessorMixin;
import lycanitestweaks.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemVileMatter extends ItemPassive  {

    private static final String NBT_CREATURE_TYPE_NAME = "creatureTypeName";
    private static final String NBT_CREATURE_SUBSPECIES = "Subspecies";

    private final static Map<ItemStack, Collection<ElementInfo>> STACK_ELEMENTS = new HashMap<>();
    private final static Map<ItemStack, Collection<Potion>> STACK_POTIONS = new HashMap<>();
    private final static Set<ItemStack> STACK_BURNING = new HashSet<>();

    public ItemVileMatter(String name) {
        super(name);
        this.addPropertyOverride(new ResourceLocation("active"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack itemStack, World world, EntityLivingBase entity) {
                IToggleableItem toggleableItem = ToggleableItem.getForItemStack(itemStack);
                if(toggleableItem != null && toggleableItem.isAbilityToggled()) {
                    return 1.0F;
                }
                return 0.0F;
            }
        });
    }

    @Override
    public boolean isEnabled() {
        return ForgeConfigHandler.server.customStaffConfig.registerVileMatter;
    }

    @Override
    public boolean hasSubscriber() {
        return true;
    }

    @Override
    public boolean isToggleable() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack,  World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        super.addInformation(itemStack, world, tooltip, tooltipFlag);

        boolean beastiary = ForgeConfigHandler.clientFeaturesMixinConfig.beastiaryGUILT;
        String storedCreature = this.getCreatureTypeName(itemStack);
        StringBuilder rawStrings = new StringBuilder();
        rawStrings.append(I18n.format("item.lycanitestweaks.vilematter.description"));
        rawStrings.append("\n").append(I18n.format("lycanitestweaks.ability.vileaura.tooltip0"));

        if(CreatureManager.getInstance().getCreature(storedCreature) != null) {
            storedCreature = CreatureManager.getInstance().getCreature(storedCreature).getTitle();
        }

        if(storedCreature.isEmpty()) {
            if(beastiary)
                rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.vilematter.vileaura.beastiary"));
            else
                rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.vilematter.vileaura.summon"));
        }
        else {
            rawStrings.append("\n").append(I18n.format("lycanitestweaks.ability.vileaura.tooltip1", storedCreature));
            if(GuiScreen.isShiftKeyDown()) {
                if(beastiary)
                    rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.vilematter.vileaura.beastiary"));
                else
                    rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.vilematter.vileaura.summon"));
            }
            else {
                rawStrings.append("\n").append(I18n.format("item.lycanitestweaks.tooltip.expand", "SHIFT"));
            }
        }

        List<String> formattedDescriptionList = Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(rawStrings.toString(), ItemBase.DESCRIPTION_WIDTH);
        tooltip.addAll(formattedDescriptionList);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack repairStack) {
        if(ForgeConfigHandler.server.customStaffConfig.vileMatterRepairables) {
            ResourceLocation resourceLocation = repairStack.getItem().getRegistryName();
            if (resourceLocation != null && ItemConfig.mediumEquipmentManaItems.contains(repairStack.getItem().getRegistryName().toString())) {
                return true;
            }
        }
        return super.getIsRepairable(itemStack, repairStack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int inventorySlot, boolean isCurrentItem) {
        if(entityIn instanceof EntityLivingBase) {
            this.tickAbility(stack, (EntityLivingBase) entityIn);
        }
    }

    @Override
    public void tickAbility(ItemStack stack, EntityLivingBase entity) {
        super.tickAbility(stack, entity);
        if (entity.world.isRemote) return;
        if (this.getCreatureTypeName(stack).isEmpty()) return;

        if(entity.ticksExisted % ForgeConfigHandler.server.customStaffConfig.vileAuraTickRate != 0) {
            return;
        }
        if(!STACK_ELEMENTS.containsKey(stack)) {
            CreatureInfo creatureInfo = CreatureManager.getInstance().getCreature(this.getCreatureTypeName(stack));
            if (creatureInfo != null) {
                int subspecies = this.getEntitySubspecies(stack);
                List<ElementInfo> elementInfos = (subspecies == 0)
                        ? creatureInfo.elements
                        : creatureInfo.getSubspecies(subspecies).elements;
                this.setDebuffs(elementInfos, stack);
            }
        }
        Collection<ElementInfo> elementInfos = STACK_ELEMENTS.get(stack);
        if(elementInfos == null || elementInfos.isEmpty()) return;

        IToggleableItem toggleableItem = ToggleableItem.getForItemStack(stack);
        if(toggleableItem == null || !toggleableItem.isAbilityToggled()) return;

        double range = entity instanceof EntityPlayer
                ? Helpers.getPlayerInteractionReach((EntityPlayer) entity, EnumHand.MAIN_HAND) * ForgeConfigHandler.server.customStaffConfig.vileAuraRangePlayer
                : ForgeConfigHandler.server.customStaffConfig.vileAuraRangeOther;
        List<EntityLivingBase> aoeTargets = entity.getEntityWorld().getEntitiesWithinAABB(
                EntityLivingBase.class,
                entity.getEntityBoundingBox().grow(range),
                target -> target != entity && entity.canEntityBeSeen(target)
        );

        if (STACK_BURNING.contains(stack) && entity.isBurning() && entity instanceof Entity_AccessorMixin) {
            aoeTargets.forEach(entityLivingBase -> entityLivingBase.setFire(((Entity_AccessorMixin) entity).lycanitesTweaks$getFireTicks() / 20));
        }

        STACK_POTIONS.getOrDefault(stack, Collections.emptyList()).stream().filter(entity::isPotionActive).forEach(potion -> {
            PotionEffect potionEffect = entity.getActivePotionEffect(potion);
            aoeTargets.forEach(target -> {
                // Try not to reset cycle dependent potions
                if(!target.isPotionActive(potion) || target.getActivePotionEffect(potion).getAmplifier() < potionEffect.getAmplifier())
                    target.addPotionEffect(new PotionEffect(potionEffect));
            });
        });

        elementInfos.forEach(elementInfo ->
                elementInfo.debuffEntity(entity, 20 * ForgeConfigHandler.server.customStaffConfig.vileAuraDuration, 0));
    }

    // ==================================================
    //                    Item Use
    // ==================================================
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack itemStack = player.getHeldItem(hand);

        if(!player.world.isRemote) {
            if(player.isSneaking()) {
                this.setDebuffsFromPlayer(player, itemStack);
            }
            else {
                IToggleableItem toggleableItem = ToggleableItem.getForItemStack(itemStack);
                if(toggleableItem != null) {
                    toggleableItem.toggleAbility(!toggleableItem.isAbilityToggled(), itemStack, player);
                }
            }
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemStack);
    }

    private void clearDebuffs() {
        STACK_ELEMENTS.clear();
        STACK_POTIONS.clear();
        STACK_BURNING.clear();
    }

    private void setDebuffs(Collection<ElementInfo> elementInfos, ItemStack itemStack) {
        STACK_ELEMENTS.put(itemStack, elementInfos);
        List<Potion> potions = new ArrayList<>();
        elementInfos.forEach(elementInfo -> elementInfo.debuffs.forEach(debuff -> {
            if(debuff.equalsIgnoreCase("burning")) {
                STACK_BURNING.add(itemStack);
            }
            else {
                Potion potion = GameRegistry.findRegistry(Potion.class).getValue(new ResourceLocation(debuff));
                if (potion != null && !potions.contains(potion)) potions.add(potion);
            }
        }));
        STACK_POTIONS.put(itemStack, potions);
    }

    private void setDebuffsFromPlayer(EntityPlayer player, ItemStack itemStack) {
        ExtendedPlayer playerExt = ExtendedPlayer.getForPlayer(player);
        if (playerExt != null) {
            CreatureInfo creatureInfo = playerExt.selectedCreature;
            String title = "";
            String storedCreatureType = this.getCreatureTypeName(itemStack);
            if(creatureInfo != null) {
                if((creatureInfo.isTameable() || creatureInfo.isSummonable() || player.isCreative())
                        && playerExt.getBeastiary().hasKnowledgeRank(creatureInfo.getName(), 2)) {
                    List<ElementInfo> elementInfos = (playerExt.selectedSubspecies == 0)
                            ? creatureInfo.elements
                            : creatureInfo.getSubspecies(playerExt.selectedSubspecies).elements;
                    this.setDebuffs(elementInfos, itemStack);
                    this.setStoredCreature(itemStack, creatureInfo.getName(), playerExt.selectedSubspecies);
                    title = creatureInfo.getTitle();
                }
            }

            // Fallback if Client doesn't update Server
            if(storedCreatureType.isEmpty()) {
                SummonSet summonSet = playerExt.getSelectedSummonSet();
                creatureInfo = summonSet.getCreatureInfo();
                if (summonSet.isUseable() && !summonSet.summonType.equals(storedCreatureType)) {
                    this.setDebuffs(summonSet.getCreatureInfo().elements, itemStack);
                    this.setStoredCreature(itemStack, summonSet.summonType, summonSet.subspecies);
                    title = creatureInfo.getTitle();
                }
            }

            if(title.isEmpty()) {
                player.sendStatusMessage(new TextComponentTranslation("message.passiveability.vileaura.linkfail"), true);
            }
            else {
                player.sendStatusMessage(new TextComponentTranslation("message.passiveability.vileaura.linkset", title), true);
            }
        }
    }

    public void setStoredCreature(ItemStack itemStack, String creatureType, int subspecies) {
        this.setTagString(itemStack, NBT_CREATURE_TYPE_NAME, creatureType);
        this.setTagInt(itemStack, NBT_CREATURE_SUBSPECIES, subspecies);
    }

    public String getCreatureTypeName(ItemStack itemStack){
        return this.getTagString(itemStack, NBT_CREATURE_TYPE_NAME);
    }

    public int getEntitySubspecies(ItemStack itemStack){
        return this.getTagInt(itemStack, NBT_CREATURE_SUBSPECIES);
    }

    @SubscribeEvent
    public void onSwapAwayItem(LivingEquipmentChangeEvent event){
        if(event.getFrom().getItem() == this) {
            this.clearDebuffs();
        }
    }
}
