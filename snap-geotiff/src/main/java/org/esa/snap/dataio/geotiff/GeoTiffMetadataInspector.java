package org.esa.snap.dataio.geotiff;

import it.geosolutions.imageio.plugins.tiff.TIFFField;
import it.geosolutions.imageio.plugins.tiff.TIFFTag;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFImageMetadata;
import it.geosolutions.imageioimpl.plugins.tiff.TIFFRenderedImage;
import org.esa.snap.core.dataio.MetadataInspector;
import org.esa.snap.core.datamodel.GeoCoding;
import org.esa.snap.core.datamodel.GeoPos;
import org.esa.snap.core.datamodel.PixelPos;
import org.esa.snap.core.datamodel.Product;

import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by jcoravu on 25/11/2019.
 */
public class GeoTiffMetadataInspector implements MetadataInspector {

    public GeoTiffMetadataInspector() {
    }

    @Override
    public Metadata getMetadata(Path productPath) throws IOException {
        try (GeoTiffImageReader geoTiffImageReader = GeoTiffProductReader.buildGeoTiffImageReader(productPath)) {
            Metadata metadata = new Metadata();
            metadata.setProductWidth(String.valueOf(geoTiffImageReader.getImageWidth()));
            metadata.setProductHeight(String.valueOf(geoTiffImageReader.getImageHeight()));

            TIFFImageMetadata imageMetadata = geoTiffImageReader.getImageMetadata();
            TiffFileInfo tiffInfo = new TiffFileInfo(imageMetadata.getRootIFD());
            TIFFField tagNumberField = tiffInfo.getField(Utils.PRIVATE_BEAM_TIFF_TAG_NUMBER);

            Product product = null;
            Dimension productSize = new Dimension(1, 1);
            if (tagNumberField != null && tagNumberField.getType() == TIFFTag.TIFF_ASCII) {
                String tagNumberText = tagNumberField.getAsString(0).trim();
                if (tagNumberText.contains("<Dimap_Document")) { // with DIMAP header
                    product = GeoTiffProductReader.buildProductFromDimapHeader(tagNumberText, productSize);
                }
            }
            if (product == null) {            // without DIMAP header
                TIFFRenderedImage baseImage = geoTiffImageReader.getBaseImage();
                product = GeoTiffProductReader.buildProductWithoutDimapHeader(productPath, "GeoTIFF", tiffInfo, baseImage, productSize);
            }
            for (int i = 0; i < product.getNumBands(); i++) {
                metadata.getBandList().add(product.getBandAt(i).getName());
            }

            if (tiffInfo.isGeotiff()) {
                GeoCoding geoCoding = GeoTiffProductReader.buildGeoCoding(imageMetadata, product.getSceneRasterSize());
                metadata.setGeoCoding(geoCoding);
            }

            if (metadata.getGeoCoding() != null) {
                GeoPos geoPos1 = metadata.getGeoCoding().getGeoPos(new PixelPos(0, 0), null);
                GeoPos geoPos2 = metadata.getGeoCoding().getGeoPos(new PixelPos(geoTiffImageReader.getImageWidth(), geoTiffImageReader.getImageHeight()), null);
                metadata.setLatitudeNorth(String.valueOf(geoPos1.getLat()));
                metadata.setLatitudeSouth(String.valueOf(geoPos2.getLat()));
                metadata.setLongitudeEast(String.valueOf(geoPos2.getLon()));
                metadata.setLongitudeWest(String.valueOf(geoPos1.getLon()));
                metadata.setHasGeoCoding(true);
            } else {
                metadata.setHasGeoCoding(false);
            }
            return metadata;
        } catch (IOException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new IOException(exception);
        }
    }
}