package com.assetBundle.asset;

import com.assetBundle.ObjectReader;
import com.common.ArrayInputStream;

import static com.common.StreamUtil.*;

public class Texture2D extends Texture{

    private int width;
    private int height;
    private int textureFormat;
    private  int mipCount;

    public Texture2D(ObjectReader reader) throws Exception {
        super(reader);
        ArrayInputStream is = reader.getIs();
        width = readInt(is);
        height = readInt(is);
        int completeImageSize = readInt(is);
        textureFormat = readInt(is);
        mipCount = readInt(is);
        boolean isReadable = readBool(is);
        is.alignStream();
        int imageCount = readInt(is);
        int textureDimension = readInt(is);
        GLTextureSettings textureSettings = new GLTextureSettings(is);
        if(getVersion()[0]>3){
            int lightmapFormat = readInt(is);
        }
        if(getVersion()[0]>3){
            int myColorSpace = readInt(is);
        }
        int imageDataSize = readInt(is);
        if(imageDataSize==0 && getVersion()[0]>5){
            streamingInfo = new StreamingInfo(reader);
        }
    }
}
