package com.github.prplrose.playerpanel;

import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;

public class ConfigRecord<V> {
    final static Logger LOGGER = PlayerPanel.LOGGER;
    String key;
    V value;

    public ConfigRecord(String key, V defaultValue) {
        this.key = key;
        this.value = defaultValue;
    }

    @SuppressWarnings("unchecked")
    public void read(NbtCompound nbtCompound) {
        if (!nbtCompound.contains(this.key)){
            LOGGER.info("No item " + this.key + " of type " + this.value.getClass() + " in config");
            return;
        }
        if (this.value instanceof Integer){
            this.value = (V) (Integer.valueOf(nbtCompound.getInt(this.key)));
        } else if (this.value instanceof String) {
            this.value = (V) nbtCompound.getString(this.key);
        } else if (this.value instanceof Boolean) {
            this.value = (V) Boolean.valueOf(nbtCompound.getBoolean(this.key));
        }else {
            throw new RuntimeException("Value of " + this.value.getClass().getName() + " " + this.key + " is not and instance of valid object");
        }
    }

    public void write(NbtCompound nbtCompound){
        if (this.value instanceof Integer){
            nbtCompound.putInt(this.key, (Integer) this.value);
        } else if (this.value instanceof String) {
            nbtCompound.putString(this.key, (String) this.value);
        } else if (this.value instanceof Boolean) {
            nbtCompound.putBoolean(this.key, (Boolean) this.value);
        }
    }

    public V get(){
        return this.value;
    }

}
