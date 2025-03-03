package com.minecolonies.coremod.colony.fields.registry;

import com.minecolonies.api.colony.fields.IField;
import com.minecolonies.api.colony.fields.registry.FieldRegistries;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.api.util.Log;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * The field manager class responsible for creating field instances from NBT data, etc.
 */
public final class FieldDataManager
{
    private static final String TAG_FIELD_NAME     = "name";
    private static final String TAG_FIELD_POSITION = "position";
    private static final String TAG_FIELD_DATA     = "data";

    private FieldDataManager()
    {
    }

    /**
     * Creates a field instance from NBT compound data.
     *
     * @param compound the input compound data.
     * @return the created field instance.
     */
    public static IField compoundToField(final @NotNull CompoundTag compound)
    {
        ResourceLocation fieldName = new ResourceLocation(compound.getString(TAG_FIELD_NAME));
        BlockPos position = BlockPosUtil.read(compound, TAG_FIELD_POSITION);

        IField field = resourceLocationToField(fieldName, position);
        if (field != null)
        {
            field.deserializeNBT(compound.getCompound(TAG_FIELD_DATA));
        }
        return field;
    }

    /**
     * Creates a field instance from a field type and position.
     *
     * @param fieldName the field registry entry name.
     * @param position  the position of the field.
     * @return the field instance.
     */
    public static IField resourceLocationToField(final @NotNull ResourceLocation fieldName, final @NotNull BlockPos position)
    {
        final FieldRegistries.FieldEntry fieldEntry = FieldRegistries.getFieldRegistry().getValue(fieldName);

        if (fieldEntry == null)
        {
            Log.getLogger().error("Unknown field type '{}'.", fieldName);
            return null;
        }

        return fieldEntry.produceField(position);
    }

    /**
     * Creates a field instance from a complete network buffer.
     *
     * @param buf the buffer, still containing the field registry type and position.
     * @return the field instance.
     */
    public static IField bufferToField(final @NotNull FriendlyByteBuf buf)
    {
        final FieldRegistries.FieldEntry fieldType = buf.readRegistryIdSafe(FieldRegistries.FieldEntry.class);
        final BlockPos position = buf.readBlockPos();
        final IField field = fieldType.produceField(position);
        field.deserialize(buf);
        return field;
    }

    /**
     * Creates a network buffer from a field instance.
     *
     * @param field the field instance.
     * @return the network buffer.
     */
    public static FriendlyByteBuf fieldToBuffer(final @NotNull IField field)
    {
        final FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeRegistryId(FieldRegistries.getFieldRegistry(), field.getFieldType());
        buf.writeBlockPos(field.getPosition());
        field.serialize(buf);
        return buf;
    }

    /**
     * Creates NBT compound data from a field instance.
     *
     * @param field the field instance.
     * @return the NBT compound.
     */
    public static CompoundTag fieldToCompound(final @NotNull IField field)
    {
        final CompoundTag compound = new CompoundTag();
        compound.putString(TAG_FIELD_NAME, field.getFieldType().getRegistryName().toString());
        BlockPosUtil.write(compound, TAG_FIELD_POSITION, field.getPosition());
        compound.put(TAG_FIELD_DATA, field.serializeNBT());
        return compound;
    }
}
