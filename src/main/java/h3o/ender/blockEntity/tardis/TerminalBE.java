package h3o.ender.blockEntity.tardis;

import java.util.List;

import h3o.ender.blockEntity.RegisterBlockEntities;
import h3o.ender.entities.Tardis;
import h3o.ender.screenHandler.AstralMapScreenHandler;
import h3o.ender.screenHandler.MaintenanceConfigScreenHandler;
import h3o.ender.screenHandler.TardisTerminalScreenHandler;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import h3o.ender.structures.tardis.Room;
import h3o.ender.tardisOs.FormattedText;
import h3o.ender.tardisOs.TardisOs;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TerminalBE extends BlockEntity
        implements GeoBlockEntity, TardisBentDependant, ExtendedScreenHandlerFactory, Inventory {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private NbtCompound tardisCircuits;
    private NbtCompound tardisInternalScheme;
    private Tardis trds;
    private String prompt = ">";
    private MutableText text = Text.empty();
    private int menu = 0;

    public TerminalBE(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.TERMINAL_BE, pos, state);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        return;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.tardisCircuits = nbt.getCompound("Circuits");
        this.tardisInternalScheme = nbt.getCompound("InternalScheme");
        this.prompt = nbt.getString("Prompt");
        this.text = FormattedText.fromNbt((NbtList) (nbt.get("Text")));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        Tardis tardis = DimensionalStorageHelper.getTardis(world.getServer(), pos);
        if (tardis != null) {
            nbt.put("Circuits", tardis.getDataTracker().get(Tardis.CIRCUITS));
            nbt.put("InternalScheme", tardis.getDataTracker().get(Tardis.INTERNAL_SCHEME));
        }
        nbt.putString("Prompt", this.prompt);
        nbt.put("Text", FormattedText.toNbt(this.text));
        super.writeNbt(nbt);
    }

    public NbtList getTardisCircuits() {
        return tardisCircuits.getList("Circuits", NbtElement.LIST_TYPE);
    }

    public List<Room> getTardisInternalScheme() {
        return Room.fromNBT(tardisInternalScheme);
    }

    @Override
    public void register() {
        this.trds = DimensionalStorageHelper.getTardis(world.getServer(), pos);
        if (trds != null) {
            trds.registerBlock(this);
        }
    }

    @Override
    public void unRegister() {
        if (this.trds != null) {
            trds.unRegisterBlock(this);
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.empty().append("tardis.console.terminal");
    }

    @Override
    public ScreenHandler createMenu(int var1, PlayerInventory var2, PlayerEntity var3) {
        if (menu == 0) {
            return new TardisTerminalScreenHandler(var1, var2, var2, getPos());
        }
        if (menu == 1) {
            menu = 0;
            return new AstralMapScreenHandler(var1, var2, var2, getPos());
        }
        if (menu == 2) {
            menu = 0;
            return new MaintenanceConfigScreenHandler(var1, var2, var2, getPos());
        }
        return null;
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
    public boolean canPlayerUse(PlayerEntity var1) {
        return true;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public String getPrompt() {
        return this.prompt;
    }

    public MutableText getText() {
        return this.text;
    }

    public void addChar(String str) {
        String[] words = prompt.split(" ");
        if (words[words.length - 1].length() > 20 || prompt.length() > 80) {
            return;
        }
        this.prompt += str;
        this.markDirty();
        ((ServerWorld) getWorld()).getChunkManager().markForUpdate(getPos());
    }

    public void optPacket(String readString) {
        if (readString.equals("exec")) {
            this.text = (FormattedText.empty().normal(prompt).endLine().assemble());
            this.text.append(TardisOs.execute(prompt.substring(1), (ServerPlayerEntity) world
                    .getClosestPlayer((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), 10, false), this));
            this.prompt = ">";
            this.markDirty();
            ((ServerWorld) getWorld()).getChunkManager().markForUpdate(getPos());
        }
        if (readString.equals("suppr")) {
            if (prompt.length() > 1) {
                this.prompt = prompt.substring(0, prompt.length() - 1);
                this.markDirty();
                ((ServerWorld) getWorld()).getChunkManager().markForUpdate(getPos());
            }
        }
    }

    public void switchMenu(int i) {
        this.menu = i;
    }

    public void switchEngineAccess() {
        trds.switchEngineAccess();
    }

}
