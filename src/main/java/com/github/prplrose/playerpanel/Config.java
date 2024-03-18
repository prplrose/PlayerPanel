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
    public static ConfigRecord<Boolean> hostWebpage = new ConfigRecord<>("host_webpage", true);
    private static final List<ConfigRecord<?>> records = new ArrayList<>();

    public static void init(){
        records.add(port);
        records.add(hostWebpage);
    }

    public static void load(Path dir) throws IOException {
        configFile = dir.resolve("config.txt").toFile();
        NbtCompound nbtCompound = new NbtCompound();
        writeNbt(nbtCompound);

        if(configFile.exists()){
            FileReader fileReader = new FileReader(configFile);
            StringBuilder stringBuffer = new StringBuilder();
            int ch;
            while ((ch=fileReader.read()) != -1){
                stringBuffer.append((char)ch);
            }
            try {
                nbtCompound = NbtHelper.fromNbtProviderString(stringBuffer.toString());
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
            fileReader.close();
            readNbt(nbtCompound);
        }else {
            File f;
            f = dir.resolve("").toFile();
            f.mkdir();
            f = dir.resolve("webpage").toFile();
            f.mkdir();
            //TODO: default config
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
        printWriter.write(NbtHelper.toFormattedString(nbtCompound, true));
        printWriter.close();
    }

}
