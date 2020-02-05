package org.esa.snap.core.dataio.debug;

import org.esa.snap.core.dataio.ProductWriter;
import org.esa.snap.core.util.io.SnapFileFilter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.esa.snap.core.dataio.EncodeQualification.Preservation.FULL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DevNullProductWriterPluginTest {

    private DevNullProductWriterPlugin plugin;

    @Before
    public void setUp() {
        plugin = new DevNullProductWriterPlugin();
    }

    @Test
    public void testGetEncodeQualification() {
        assertEquals(FULL, plugin.getEncodeQualification(null).getPreservation());
    }

    @Test
    public void testGetOutputTypes() {
        final Class[] outputTypes = plugin.getOutputTypes();
        assertEquals(2, outputTypes.length);
        assertEquals(String.class, outputTypes[0]);
        assertEquals(File.class, outputTypes[1]);
    }

    @Test
    public void testGetFormatNames() {
        final String[] formatNames = plugin.getFormatNames();
        assertEquals(1, formatNames.length);
        assertEquals("NULL-Type", formatNames[0]);
    }

    @Test
    public void testGetDefaultFileExtensions() {
        final String[] extensions = plugin.getDefaultFileExtensions();
        assertEquals(1, extensions.length);
        assertEquals(".NULL", extensions[0]);
    }

    @Test
    public void testGetDescription() {
        assertEquals("no-op (i.e. dev NULL) writer", plugin.getDescription(null));
    }

    @Test
    public void testGetProductFileFilter() {
        final SnapFileFilter productFileFilter = plugin.getProductFileFilter();
        assertTrue(productFileFilter instanceof DevNullProductWriterPlugin.AcceptAllFileFilter);
    }

    @Test
    public void testCreateWriterInstance() {
        final ProductWriter writer = plugin.createWriterInstance();
        assertTrue(writer instanceof DevNullProductWriter);
    }
}
