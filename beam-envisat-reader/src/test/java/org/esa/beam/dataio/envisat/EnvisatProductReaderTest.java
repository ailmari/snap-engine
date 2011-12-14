package org.esa.beam.dataio.envisat;

import org.esa.beam.framework.datamodel.GeoPos;
import org.esa.beam.framework.datamodel.PixelPos;
import org.esa.beam.framework.datamodel.Product;
import org.esa.beam.framework.datamodel.TiePointGrid;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.Assert.*;

public class EnvisatProductReaderTest {

    private EnvisatProductReaderPlugIn readerPlugIn;

    @Before
    public void setUp() {
        readerPlugIn = new EnvisatProductReaderPlugIn();
    }

    @Test
    public void testAatsrGeoLocation() throws IOException, URISyntaxException {
        final EnvisatProductReader reader = (EnvisatProductReader) readerPlugIn.createReaderInstance();

        try {
            final Product product = reader.readProductNodes(
                    new File(getClass().getResource(
                            "ATS_TOA_1PRMAP20050504_080932_000000482037_00020_16607_0001.N1").toURI()), null);
            assertEquals(512, product.getSceneRasterWidth());
            assertEquals(320, product.getSceneRasterHeight());

            final TiePointGrid latGrid = product.getTiePointGrid("latitude");
            final TiePointGrid lonGrid = product.getTiePointGrid("longitude");
            assertNotNull(latGrid);
            assertNotNull(lonGrid);

            final ProductFile productFile = reader.getProductFile();
            assertTrue(productFile.storesPixelsInChronologicalOrder());

            final int colCount = 512;
            final int rowCount = 320;
            final float[] lats = new float[colCount * rowCount];
            final float[] lons = new float[colCount * rowCount];
            readFloats("image_latgrid_ATS_TOA_1PRMAP20050504_080932_000000482037_00020_16607_0001.txt", lats);
            readFloats("image_longrid_ATS_TOA_1PRMAP20050504_080932_000000482037_00020_16607_0001.txt", lons);

            final GeoPos geoPos = new GeoPos();

            product.getGeoCoding().getGeoPos(new PixelPos(0.0F + 1.0F, 1.0F), geoPos);
            assertEquals(44.550718F, geoPos.getLat(), 1.0E-5F);
            assertEquals(32.878792F, geoPos.getLon(), 1.0E-5F);

            product.getGeoCoding().getGeoPos(new PixelPos(5.0F + 1.0F, 1.0F), geoPos);
            assertEquals(44.541008F, geoPos.getLat(), 1.0E-5F);
            assertEquals(32.940249F, geoPos.getLon(), 1.0E-5F);

            for (int i = 0; i < rowCount; i++) {
                for (int j = 0, k = colCount - 1; j < colCount; j++, k--) {
                    product.getGeoCoding().getGeoPos(new PixelPos(j + 1.0F, i + 1.0F), geoPos);
                    assertEquals(lats[i * colCount + k], geoPos.getLat(), 1.2E-5F);
                    assertEquals(lons[i * colCount + k], geoPos.getLon(), 1.2E-5F);
                }
            }
        } finally {
            reader.close();
        }
    }

    private void readFloats(String resourceName, float[] floats) {
        final Scanner scanner = new Scanner(getClass().getResourceAsStream(resourceName), "US-ASCII");
        scanner.useLocale(Locale.US);

        try {
            for (int i = 0; i < floats.length; i++) {
                floats[i] = scanner.nextFloat();
            }
        } finally {
            scanner.close();
        }
    }
}
