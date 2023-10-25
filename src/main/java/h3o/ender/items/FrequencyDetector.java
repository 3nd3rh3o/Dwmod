package h3o.ender.items;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import h3o.ender.screenHandler.FrequencyDetectorScreenHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.command.argument.RegistryPredicateArgumentType.RegistryPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;

public class FrequencyDetector extends Item implements ExtendedScreenHandlerFactory, Inventory {

    private static final List<String> OVERWORLD_FREQ = new ArrayList<>();
    static {
        OVERWORLD_FREQ.add("dwmod:gallifreyan_beacon");
    }

    public FrequencyDetector(Settings settings) {
        super(settings.maxCount(1));
    }

    public static List<String> getStructForW(String world) {
        if (world.equals("minecraft:overworld")) {
            return OVERWORLD_FREQ;
        }
        return new ArrayList<>();
    }

    private BlockPos locateStruct(ServerWorld world, PlayerEntity player, String structname) {
        Registry<Structure> registry = ((ServerWorld) world).getRegistryManager().get(RegistryKeys.STRUCTURE);
        CommandContext<ServerCommandSource> context = ((ServerWorld) world).getServer().getCommandManager()
                .getDispatcher()
                .parse("locate structure " + structname, world.getServer().getCommandSource()).getContext()
                .build(null);
        RegistryPredicate<Structure> registryPredicate;
        try {
            registryPredicate = RegistryPredicateArgumentType.getPredicate(context, "structure", RegistryKeys.STRUCTURE,
                    null);
            RegistryEntryList<Structure> registryEntryList = getStructureListForPredicate(registryPredicate, registry)
                    .orElseThrow();
            return ((ServerWorld) world).getChunkManager().getChunkGenerator()
                    .locateStructure(((ServerWorld) world), registryEntryList, player.getBlockPos(), 100, false)
                    .getFirst();
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            NbtCompound userNbt = user.getStackInHand(hand).getNbt();
            String registryKey = world.getRegistryKey().getValue().toString();
            if (userNbt == null || !registryKey.equals(userNbt.getString("dim"))) {
                NbtCompound nbt = new NbtCompound();
                nbt.putInt("settings", -1);
                nbt.putString("dim", world.getRegistryKey().getValue().toString());
                user.getStackInHand(hand).setNbt(nbt);
                userNbt = nbt;
            }
            if (user.isSneaking()) {
                user.openHandledScreen(this);
            } else {
                List<Text> textStream = new ArrayList<>();
                textStream.add(Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
                textStream.add(Text.translatable("item.dwmod.frequency_detector").setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
                textStream.add(Text.literal("]: ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
                MutableText responce = Text.empty();
                if (userNbt.getInt("settings") == -1) {
                    textStream.add(Text.literal("No frequency set!").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                } else {
                    BlockPos pos = locateStruct(((ServerWorld) world), user,
                            getStructForW(registryKey).get(userNbt.getInt("settings")));
                    textStream.add(Text.literal(" Located signal source at [").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
                    textStream.add(Text.literal("x: "+pos.getX()).setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    textStream.add(Text.literal(", ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
                    textStream.add(Text.literal("z: "+pos.getZ()).setStyle(Style.EMPTY.withColor(Formatting.DARK_BLUE)));
                    textStream.add(Text.literal("]").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
                    
                }
                textStream.forEach(snippet -> responce.append(snippet));
                user.sendMessage(responce);
            }

            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    private static Optional<? extends RegistryEntryList.ListBacked<Structure>> getStructureListForPredicate(
            RegistryPredicateArgumentType.RegistryPredicate<Structure> predicate,
            Registry<Structure> structureRegistry) {
        return predicate.getKey().map(key -> structureRegistry.getEntry((RegistryKey<Structure>) key)
                .map(entry -> RegistryEntryList.of(entry)), structureRegistry::getEntryList);
    }

    @Override
    public ScreenHandler createMenu(int synId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FrequencyDetectorScreenHandler(synId, playerInventory, this);
    }

    @Override
    public void clear() {
        return;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getStack(int var1) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int var1, int var2) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int var1) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int var1, ItemStack var2) {
        return;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity var1) {
        return true;
    }

    @Override
    public Text getDisplayName() {
        return this.getName();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeString(player.getWorld().getRegistryKey().getValue().toString());
    }
}