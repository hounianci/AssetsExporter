package com.analyze;

import com.common.Lz4Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TryLz4Decode {
    public static void main(String[] args) {
        File file = new File("lz4/loadingprefab.b.node_0.blockComData.tryRep");
        try(FileInputStream is = new FileInputStream(file);
            FileOutputStream os = new FileOutputStream("lz4/loadingprefab.b.node_0.blockComData.tryUn")){
            byte[] compressData = new byte[(int) file.length()];
            is.read(compressData);
            byte[] uncompressData = new byte[0xffff];
            try {
                uncompressData = Lz4Util.decodeLz4(uncompressData);
            }catch (Exception e){
                e.printStackTrace();
            }
            os.write(uncompressData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
