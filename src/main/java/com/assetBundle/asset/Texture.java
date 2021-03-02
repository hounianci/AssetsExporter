package com.assetBundle.asset;

import com.assetBundle.ObjectReader;
import com.common.StreamUtil;

public class Texture extends NamedAsObject{
    public Texture(ObjectReader reader) throws Exception {
        super(reader);
        int forcedFallbackFormat = StreamUtil.readInt(reader.getIs());
        boolean downscaleFallback = StreamUtil.readBool(reader.getIs());
        reader.getIs().alignStream();
    }
}
