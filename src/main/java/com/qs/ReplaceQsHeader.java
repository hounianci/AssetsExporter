package com.qs;

import com.common.ByteArray;
import com.common.FileByteUtils;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReplaceQsHeader {
    public static void main(String[] args) {
        File dir = new File("qs/ori");
        processDir(dir, "qs/output");
    }
    public static void processDir(File dir, String path){
        if(dir.getName().startsWith("_")){
            return;
        }
        String currPath = path+"/"+dir.getName();
        File currDir = new File(currPath);
        currDir.mkdirs();
        for(File file : dir.listFiles()){
            if(file.isDirectory()){
                processDir(file, currPath);
            }else{
                processFile(file, currPath);
            }
        }
    }
    public static void processFile(File file, String path){
        if(file.getName().startsWith("_") || !file.getName().endsWith("assetbundle")){
            return;
        }
        try(FileInputStream is = new FileInputStream(file);
            FileOutputStream os = new FileOutputStream(path+"/"+file.getName())){
            byte[] data = new byte[8];
            is.read(data);
            os.write(data);
            is.skip(4);
            os.write(new byte[]{0,0,0,0x06});
            data = new byte[0x19];
            is.read(data);
            os.write(data);
            data = new byte[4];
            //压缩大小
            is.read(data);
            int compressSize = (int) FileByteUtils.byteToInt(data, true);
            data  = FileByteUtils.intToByte(compressSize/2, true);
            os.write(data);
            //非压缩大小
            data = new byte[4];
            is.read(data);
            int uncompressSize = (int) FileByteUtils.byteToInt(data, true);
            data = FileByteUtils.intToByte(uncompressSize/2, true);
            os.write(data);
            byte[] flag = new byte[4];
            is.read(flag);
            byte[] compressData = new byte[compressSize/2];
            is.read(compressData);
            os.write(flag);
            os.write(compressData);
            int len = 0;
            data = new byte[1024*10];
            while ((len=is.read(data))!=-1){
                os.write(data, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static int calcUncompressDataSize(ByteArray compressData){
        LZ4Factory factory = LZ4Factory.fastestInstance();
        LZ4SafeDecompressor decompressor = factory.safeDecompressor();
        byte[] uncompressData = new byte[0xffffff];
        try{
            decompressor.decompress(compressData.getData(), 0, compressData.getDataLen(), uncompressData, 0);
        }catch (Exception e){
            byte[] data = compressData.getData();
            compressData.setDataLen(compressData.getDataLen()-20);
            data = compressData.getData();
            decompressor.decompress(data, 0, compressData.getDataLen(), uncompressData, 0);
        }
        int idx = 0xff-1;
        //从后往前删0，只保留最后一个
        while(uncompressData[idx] == 0){
            idx--;
        }
        return idx+2;
    }
}
