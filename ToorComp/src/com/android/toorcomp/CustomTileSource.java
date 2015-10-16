package com.android.toorcomp;

import org.osmdroid.ResourceProxy.string;
import org.osmdroid.tileprovider.tilesource.BitmapTileSourceBase;

public class CustomTileSource extends BitmapTileSourceBase {

    public CustomTileSource(String aName, string aResourceId,
            int aZoomMinLevel, int aZoomMaxLevel, int aTileSizePixels,
            String aImageFilenameEnding) {
        super(aName, aResourceId, aZoomMinLevel, aZoomMaxLevel, aTileSizePixels,
                aImageFilenameEnding);
    }

}