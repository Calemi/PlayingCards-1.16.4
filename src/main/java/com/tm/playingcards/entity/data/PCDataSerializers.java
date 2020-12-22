package com.tm.playingcards.entity.data;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import com.tm.playingcards.util.ArrayHelper;

public class PCDataSerializers {

    public static final IDataSerializer<Byte[]> STACK = new IDataSerializer<Byte[]>() {

        @Override
        public void write(PacketBuffer buf, Byte[] value) {
            buf.writeByteArray(ArrayHelper.toPrimitive(value));
        }

        @Override
        public Byte[] read(PacketBuffer buf) {
            return ArrayHelper.toObject(buf.readByteArray());
        }

        @Override
        public Byte[] copyValue(Byte[] value) {
            return ArrayHelper.clone(value);
        }
    };
}
