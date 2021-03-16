package com.analyze;

import com.common.ByteArray;
import com.common.Lz4Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AnalyzeLz4File {
    static Map<String, Integer> keys = new HashMap<>();
    public static void main(String[] args) {
        File base = new File("lz4/benaelf.blockComData");
        File decode = new File("lz4/cavern01.b.node_0.blockComData");
        //信息
        File output = new File("lz4/"+decode.getName()+".lz4Info");
        //尝试还原压缩数据
        File output2 = new File("lz4/"+decode.getName()+".lz4Com");
        ByteArray compressData = new ByteArray();
        try(FileInputStream baseIs = new FileInputStream(base);
            FileInputStream decodeIs = new FileInputStream(decode);
            FileOutputStream os = new FileOutputStream(output);
            FileOutputStream os2 = new FileOutputStream(output2);){
            int idx = 0;
            for(int i=0; i<10000000; i++){
                int token = baseIs.read();
                int decToken = decodeIs.read();
                int len = (token>>>4)&0xf;
                os.write(buildDiffInfo(token, decToken, idx).getBytes());
                idx+=1;
                os2.write(token);
                compressData.addData(token);
                if(len==0xf){
                    int subLen = baseIs.read();
                    int decSubLen = decodeIs.read();
                    compressData.addData(subLen);
                    os2.write(subLen);
                    os.write(buildDiffInfo(subLen, decSubLen, idx).getBytes());
                    idx+=1;
                    len += subLen;
                }
                baseIs.skip(len);
                byte[] data = new byte[len];
                decodeIs.read(data);
                compressData.addData(data);
                os2.write(data);
                idx+=len;
                byte[] offset = new byte[2];
                baseIs.read(offset);
                os2.write(offset);
                byte[] decOffset = new byte[2];
                decodeIs.read(decOffset);
                for(int j=0; j<2; j++){
                    os.write(buildDiffInfo(offset[j], decOffset[j], idx).getBytes());
                    compressData.addData(offset[j]);
                    idx+=1;
                }
                byte[] tail = new byte[]{0x70,0,0,0,0,0,0,0};
                byte[] currentCompressData = compressData.getData();
                byte[] tmpCurrentCompressData = new byte[currentCompressData.length+tail.length];
                System.arraycopy(currentCompressData, 0, tmpCurrentCompressData, 0, currentCompressData.length);
                System.arraycopy(tail, 0, tmpCurrentCompressData, currentCompressData.length, tail.length);
                byte[] uncompressData = new byte[0xffff];
                Lz4Util.decodeLz4(tmpCurrentCompressData, uncompressData);
                //尝试还原未压缩数据
                File output3 = new File("lz4/"+decode.getName()+".lz4Uncom");
                try(FileOutputStream os3 = new FileOutputStream(output3)){
                    os3.write(uncompressData);
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("have "+keys.size()+" keys.");
    }

    public static String buildDiffInfo(int f, int t, int pos){
        f = f&0xff;
        t = t&0xff;
        StringBuilder s = new StringBuilder();
        s.append("at:0x"+Integer.toHexString(pos)+", ");
        s.append("f:0x"+Integer.toHexString(f)+", ");
        s.append("to:0x"+Integer.toHexString(t)+", ");
        String k = Integer.toHexString(f^t);
        s.append("diff:0x"+k+"");
        keys.put(k, keys.getOrDefault(k, 0)+1);
        s.append("\n");
        return s.toString();
    }
}
