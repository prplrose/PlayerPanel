package com.github.prplrose.playerpanel;

import net.minecraft.nbt.NbtCompound;

public class ConfigRecord<V> {
    String key;
    V value;

    public ConfigRecord(String key, V defaultValue) {
        this.key = key;
        this.value = defaultValue;
    }

    @SuppressWarnings("unchecked")
    public V read(NbtCompound nbtCompound){
        if (!nbtCompound.contains(this.key)){
            return this.value;
        }
        if (this.value instanceof Integer){
            this.value = (V) (Integer.valueOf(nbtCompound.getInt(this.key)));
        } else if (this.value instanceof String) {
            this.value = (V) nbtCompound.getString(this.key);
        }else{
            throw new RuntimeException("Value of " + this.value.getClass().getName() + " " + this.key + " is not and instance of valid object");
        }
        return this.value;
    }

    public void write(NbtCompound nbtCompound){
        if (this.value instanceof Integer){
            nbtCompound.putInt(this.key, (Integer) this.value);
        } else if (this.value instanceof String) {
            nbtCompound.putString(this.key, (String) this.value);
        }
    }

    public V get(){
        return this.value;
    }

}
