package org.esa.snap.core.dataio.debug;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.snap.core.dataio.ProductWriter;
import org.esa.snap.core.dataio.ProductWriterListener;
import org.esa.snap.core.dataio.ProductWriterPlugIn;
import org.esa.snap.core.datamodel.Band;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData;
import org.esa.snap.core.datamodel.ProductNode;
import org.esa.snap.core.util.SystemUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DevNullProductWriter implements ProductWriter {

    private final ProductWriterPlugIn plugIn;
    private final Logger logger;
    private final ArrayList<ProductWriterListener> listeners;

    private Object output;
    private boolean incremental;

    public DevNullProductWriter(ProductWriterPlugIn plugin) {
        this.plugIn = plugin;
        this.logger = SystemUtils.LOG;
        listeners = new ArrayList<>();
        logger.info("Writer constructor");
    }

    @Override
    public ProductWriterPlugIn getWriterPlugIn() {
        logger.info("getWriterPlugIn");
        return plugIn;
    }

    @Override
    public Object getOutput() {
        logger.info("getOutput");
        return output;
    }

    @Override
    public void writeProductNodes(Product product, Object output) {
        final long threadId = Thread.currentThread().getId();
        logger.info("writeProductNodes: " + product.getName() + " -> " + output.toString() + "  thread: " + threadId);
        this.output = output;
    }

    @Override
    public void writeBandRasterData(Band sourceBand, int sourceOffsetX, int sourceOffsetY, int sourceWidth, int sourceHeight, ProductData sourceBuffer, ProgressMonitor pm) {
        final long threadId = Thread.currentThread().getId();
        logger.info("writeBandRasterData: " + sourceBand.getName() + " -> " + sourceOffsetX + " " + sourceOffsetY
                + " " + sourceWidth + " " + sourceHeight + "  thread: " + threadId);
    }

    @Override
    public void flush() {
        logger.info("flush");
    }

    @Override
    public void close() throws IOException {
        logger.info("close");
    }

    @Override
    public boolean shouldWrite(ProductNode node) {
        logger.info("shouldWrite: " + node.getName());
        return true;
    }

    @Override
    public boolean isIncrementalMode() {
        logger.info("isIncrementalMode: " + incremental);
        return incremental;
    }

    @Override
    public void setIncrementalMode(boolean enabled) {
        logger.info("setIncrementalMode: " + enabled);
        incremental = enabled;
    }

    @Override
    public void deleteOutput() {
        logger.info("deleteOutput");
    }

    @Override
    public void removeBand(Band band) {
        logger.info("removeBand: " + band.getName());
    }

    @Override
    public void setFormatName(String formatName) {
        logger.info("setFormatName: " + formatName);
    }

    @Override
    public void prepareWriting(ProgressMonitor pm) {
        for (ProductWriterListener productWriterListener : listeners) {
            //productWriterListener.aboutToWriteProduct(pm);
        }
        logger.info("prepareWriting");
    }

    @Override
    public void addProductWriterListener(ProductWriterListener productWriterListener) {
        listeners.add(productWriterListener);
        logger.info("addProductWriterListener");
    }

    @Override
    public void removeProductWriterListener(ProductWriterListener productWriterListener) {
        listeners.remove(productWriterListener);
        logger.info("removeProductWriterListener");
    }
}
