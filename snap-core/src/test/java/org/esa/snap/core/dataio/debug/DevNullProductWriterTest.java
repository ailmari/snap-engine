package org.esa.snap.core.dataio.debug;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.snap.core.dataio.ProductWriterListener;
import org.esa.snap.core.datamodel.Band;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.datamodel.ProductData;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DevNullProductWriterTest {

    private DevNullProductWriter writer;

    @Before
    public void setUp() {
        writer = new DevNullProductWriter(null);
    }

    @Test
    public void testConstructAndGetPlugin() {
        final DevNullProductWriterPlugin plugin = new DevNullProductWriterPlugin();

        final DevNullProductWriter writer = new DevNullProductWriter(plugin);
        assertSame(plugin, writer.getWriterPlugIn());
    }

    @Test
    public void testWriteProductNodesAndGetOutput() {
        final Product product = new Product("bla", "bli", 3, 4);
        final File file = new File("/put/it/there");

        writer.writeProductNodes(product, file);
        final File output = (File) writer.getOutput();
        assertEquals(file.getAbsolutePath(), output.getAbsolutePath());
    }

    @Test
    public void testShouldWrite() {
        final Band band = new Band("!me", ProductData.TYPE_INT8, 3, 4);

        assertTrue(writer.shouldWrite(band));
    }

    @Test
    public void testSetIsIncrementalMode() {
        writer.setIncrementalMode(true);
        assertTrue(writer.isIncrementalMode());

        writer.setIncrementalMode(false);
        assertFalse(writer.isIncrementalMode());
    }

    @Test
    public void testPrepareWriting_noListener() {
        writer.prepareWriting(ProgressMonitor.NULL);
    }

    @Test
    public void testAddRemoveListener() {
        final ProductWriterListener listener = mock(ProductWriterListener.class);

        writer.addProductWriterListener(listener);
        writer.prepareWriting(ProgressMonitor.NULL);

        verify(listener, times(1)).aboutToWriteProduct(any());

        writer.removeProductWriterListener(listener);
        writer.prepareWriting(ProgressMonitor.NULL);

        verifyNoMoreInteractions(listener);
    }
}
