package com.bbb;

import com.common.FileByteUtils;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ReadLZMA {

    public static void main(String[] args) {
        File lzmaDir = new File("bbb/lzmaSrc");
        File lzmaOutput = new File("bbb/lzmaTar");

        for(File file : lzmaDir.listFiles()){
            StringBuilder fileInfo = new StringBuilder();
            int idx=0;
            try(FileInputStream is = new FileInputStream(file); FileOutputStream os = new FileOutputStream(new File("bbb/lzmaTar/"+file.getName()+".lzHead"))){
                is.skip(0x26);
                idx+=0x26;
                byte[] bb = new byte[4];
                is.read(bb);
                int compressSize = (int) FileByteUtils.byteToInt(bb, true);
                is.read(bb);
                int uncompressSize = (int) FileByteUtils.byteToInt(bb, true);
                is.read(bb);
                idx+=12;
                int flag = (int) FileByteUtils.byteToInt(bb, true);
                byte[] compressData = new byte[compressSize];
                is.read(compressData);
                LZ4Factory factory = LZ4Factory.fastestInstance();
                LZ4SafeDecompressor decompressor = factory.safeDecompressor();
                byte[] uncompressData = new byte[uncompressSize];
                decompressor.decompress(compressData, 0, compressSize, uncompressData, 0);
                os.write(uncompressData);
                idx+=compressSize;
                fileInfo.append("lzmaHeader end at "+Integer.toHexString(idx));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
