
package part05.generated;

//==============================================================================
//                                                                              
//  THIS FILE HAS BEEN GENERATED BY OBJECTFABRIC                                
//                                                                              
//==============================================================================

@SuppressWarnings({ "hiding", "unchecked", "static-access", "unused", "cast", "rawtypes" })
public class Door extends org.objectfabric.TGenerated {

    public Door(org.objectfabric.Resource resource) {
        this(resource, new Version(FIELD_COUNT), FIELD_COUNT);
    }

    protected Door(org.objectfabric.Resource resource, org.objectfabric.TObject.Version shared, int length) {
        super(resource, shared, FIELD_COUNT);
    }

    public Door(Door toCopy) {
        this(toCopy.resource());

    }

    public static final org.objectfabric.TType TYPE = new org.objectfabric.TType(part05.generated.MyObjectModel.instance(), part05.generated.MyObjectModel.PART05_GENERATED_DOOR_CLASS_ID);

    public static final int FIELD_COUNT = 0;

    public static java.lang.String fieldName(int index) {
        switch (index) {
            default:
                throw new IllegalArgumentException();
        }
    }

    public static org.objectfabric.TType fieldType(int index) {
        switch (index) {
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    protected org.objectfabric.TObject.Version createVersion_() {
        Version version = new Version(0);
        version.setObject(this);
        return version;
    }

    @Override
    protected int classId_() {
        return part05.generated.MyObjectModel.PART05_GENERATED_DOOR_CLASS_ID;
    }

    @Override
    protected org.objectfabric.ObjectModel objectModel_() {
        return part05.generated.MyObjectModel.instance();
    }

    protected static class Version extends org.objectfabric.TIndexed.Version32 {

        static {
        }

        public Version(int length) {
            super(length);
        }

        @Override
        public java.lang.String getFieldName(int index) {
            return fieldName(index);
        }

        @Override
        public org.objectfabric.TType getFieldType(int index) {
            return fieldType(index);
        }

        @Override
        public java.lang.Object getAsObject(int index) {
            switch (index) {
                default:
                    return super.getAsObject(index);
            }
        }

        @Override
        public void setAsObject(int index, java.lang.Object value) {
            switch (index) {
                default:
                    super.setAsObject(index, value);
                    break;
            }
        }

        @Override
        public void merge(org.objectfabric.TObject.Version next) {
            super.merge(next);
        }

        @Override
        public void writeWrite(org.objectfabric.Writer writer, int index) {
            if (writer.interrupted())
                writer.resume();

            switch (index) {
                default: {
                    super.writeWrite(writer, index);

                    if (writer.interrupted()) {
                        writer.interrupt(null);
                        return;
                    }

                    break;
                }
            }
        }

        @Override
        public void readWrite(org.objectfabric.Reader reader, int index, java.lang.Object[] versions) {
            if (reader.interrupted())
                reader.resume();

            switch (index) {
                default: {
                    super.readWrite(reader, index, versions);

                    if (reader.interrupted()) {
                        reader.interrupt(null);
                        return;
                    }

                    break;
                }
            }
        }
    }
}