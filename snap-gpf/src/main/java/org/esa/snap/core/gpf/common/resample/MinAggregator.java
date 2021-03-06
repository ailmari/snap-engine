package org.esa.snap.core.gpf.common.resample;

import org.esa.snap.core.datamodel.RasterDataNode;
import org.esa.snap.core.image.ImageManager;

/**
 * Created by obarrile on 12/04/2019.
 */
public class MinAggregator implements Downsampling {

    @Override
    public String getName() {
        return "Min";
    }

    @Override
    public boolean isCompatible(RasterDataNode rasterDataNode, int dataBufferType) {
        return true;
    }

    @Override
    public Aggregator createDownsampler(RasterDataNode rasterDataNode, int dataBufferType) {
        return AggregatorFactory.createAggregator(AggregationType.Min,dataBufferType);
    }

    public static class Spi extends DownsamplerSpi {
        public Spi() {
            super(MinAggregator.class,"Min");
        }
    }
}
