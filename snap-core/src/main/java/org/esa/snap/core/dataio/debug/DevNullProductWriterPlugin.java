package org.esa.snap.core.dataio.debug;

import org.esa.snap.core.dataio.EncodeQualification;
import org.esa.snap.core.dataio.ProductWriter;
import org.esa.snap.core.dataio.ProductWriterPlugIn;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.util.io.SnapFileFilter;

import java.io.File;
import java.util.Locale;

public class DevNullProductWriterPlugin implements ProductWriterPlugIn {

    @Override
    public EncodeQualification getEncodeQualification(Product product) {
        return new EncodeQualification(EncodeQualification.Preservation.FULL);
    }

    @Override
    public Class[] getOutputTypes() {
        return new Class[]{String.class, File.class};
    }

    @Override
    public ProductWriter createWriterInstance() {
        return new DevNullProductWriter(this);
    }

    @Override
    public String[] getFormatNames() {
        return new String[]{"NULL-Type"};
    }

    @Override
    public String[] getDefaultFileExtensions() {
        return new String[]{".NULL"};
    }

    @Override
    public String getDescription(Locale locale) {
        return "no-op (i.e. dev NULL) writer";
    }

    @Override
    public SnapFileFilter getProductFileFilter() {
        return new AcceptAllFileFilter();
    }

    static class AcceptAllFileFilter extends SnapFileFilter {
        @Override
        public boolean accept(File file) {
            return true;
        }
    }
}
