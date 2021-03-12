package com.common;

import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.util.Arrays;

public class Lz4Util {

    public static byte[] decodeLz4(byte[] compressData, int uncompressSize){
        byte[] uncompressData = new byte[uncompressSize];
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        decompressor.decompress(compressData, 0, compressData.length, uncompressData, 0);
        return uncompressData;
    }
    public static byte[] decodeLz4(byte[] compressData){
        byte[] uncompressData = decodeLz4(compressData, 0xffff);
        int lastZeroIndex = uncompressData.length-1;
        while(uncompressData[lastZeroIndex]==0){
            lastZeroIndex--;
        }
        return Arrays.copyOf(uncompressData, lastZeroIndex+1);
    }
}
