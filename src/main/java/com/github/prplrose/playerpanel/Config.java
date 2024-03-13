package com.github.prplrose.playerpanel;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {

    static File configFile;
    public static ConfigRecord<Integer> port = new ConfigRecord<>("port", 8001);
    public static ConfigRecord<String> webroot = new ConfigRecord<>("webroot", "/tmp");
    //public static ConfigRecord<Integer> listeners = new ConfigRecord<Integer>("max_connections_at_a_time", 10);
    private static final List<ConfigRecord<?>> records = new ArrayList<>();

    public static void init(){
        records.add(port);
        records.add(webroot);
//        records.add(listeners);
    }

    public static void load(Path dir) throws IOException, CommandSyntaxException {
        configFile = dir.resolve("PlayerPanel/config.txt").toFile();
        NbtCompound nbtCompound = new NbtCompound();
        writeNbt(nbtCompound);

        if(configFile.exists()){
            FileReader fileReader = new FileReader(configFile);
            StringBuilder stringBuffer = new StringBuilder();
            int ch;
            while ((ch=fileReader.read()) != -1){
                stringBuffer.append((char)ch);
            }
            nbtCompound = NbtHelper.fromNbtProviderString(stringBuffer.toString());
            fileReader.close();
            readNbt(nbtCompound);
        }
        saveConfig();
    }

    /**
     * Writes config values to nbtCompound
     *
     * @return nbtCompound with config values written in it
     * **/
    public static NbtCompound writeNbt(NbtCompound nbtCompound){
        for(ConfigRecord<?> record: records){
            record.write(nbtCompound);
        }
        return nbtCompound;
    }

    /**
     * Reads nbtCompound and writes its values to Config static values.
     * **/
    private static void readNbt(NbtCompound nbtCompound) {
        for (ConfigRecord<?> record: records) {
            record.read(nbtCompound);
        }
    }


    private static void saveConfig() throws IOException{
        NbtCompound nbtCompound = writeNbt(new NbtCompound());
        PrintWriter printWriter = new PrintWriter(configFile);
        printWriter.write(NbtHelper.toFormattedString(nbtCompound));
        printWriter.close();
    }

}
