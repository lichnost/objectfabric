
package part05.generated;

//==============================================================================
//                                                                              
//  THIS FILE HAS BEEN GENERATED BY OBJECTFABRIC                                
//                                                                              
//==============================================================================

@SuppressWarnings({ "hiding", "unchecked", "static-access", "unused", "cast", "rawtypes" })
public class MyClass extends org.objectfabric.TGenerated {

    public MyClass(org.objectfabric.Resource resource) {
        this(resource, new Version(FIELD_COUNT), FIELD_COUNT);
    }

    protected MyClass(org.objectfabric.Resource resource, org.objectfabric.TObject.Version shared, int length) {
        super(resource, shared, FIELD_COUNT);
    }

    public MyClass(MyClass toCopy) {
        this(toCopy.resource());

        field(toCopy.field());
        field2(toCopy.field2());
        text(toCopy.text());
    }

    public static final org.objectfabric.TType TYPE = new org.objectfabric.TType(part05.generated.MyObjectModel.instance(), part05.generated.MyObjectModel.PART05_GENERATED_MYCLASS_CLASS_ID);

    public final int field() {
        org.objectfabric.TObject.Transaction outer = current_();
        org.objectfabric.TObject.Transaction inner = startRead_(outer);
        Version v = (Version) getVersion32_(inner, FIELD_INDEX);
        int value = v != null ? v._field : 0;
        endRead_(outer, inner);
        return value;
    }

    public final void field(int value) {
        org.objectfabric.TObject.Transaction outer = current_();
        org.objectfabric.TObject.Transaction inner = startWrite_(outer);
        Version v = (Version) getOrCreateVersion_(inner);
        v._field = value;
        v.setBit(FIELD_INDEX);
        endWrite_(outer, inner);
    }

    public final int field2() {
        org.objectfabric.TObject.Transaction outer = current_();
        org.objectfabric.TObject.Transaction inner = startRead_(outer);
        Version v = (Version) getVersion32_(inner, FIELD2_INDEX);
        int value = v != null ? v._field2 : 0;
        endRead_(outer, inner);
        return value;
    }

    public final void field2(int value) {
        org.objectfabric.TObject.Transaction outer = current_();
        org.objectfabric.TObject.Transaction inner = startWrite_(outer);
        Version v = (Version) getOrCreateVersion_(inner);
        v._field2 = value;
        v.setBit(FIELD2_INDEX);
        endWrite_(outer, inner);
    }

    public final java.lang.String text() {
        org.objectfabric.TObject.Transaction outer = current_();
        org.objectfabric.TObject.Transaction inner = startRead_(outer);
        Version v = (Version) getVersion32_(inner, TEXT_INDEX);
        java.lang.String value = v != null ? v._text : null;
        endRead_(outer, inner);
        return value;
    }

    public final void text(java.lang.String value) {
        org.objectfabric.TObject.Transaction outer = current_();
        org.objectfabric.TObject.Transaction inner = startWrite_(outer);
        Version v = (Version) getOrCreateVersion_(inner);
        v._text = value;
        v.setBit(TEXT_INDEX);
        endWrite_(outer, inner);
    }

    public static final int FIELD_INDEX = 0;

    public static final java.lang.String FIELD_NAME = "field";

    public static final org.objectfabric.TType FIELD_TYPE = org.objectfabric.Immutable.INTEGER.type();

    public static final int FIELD2_INDEX = 1;

    public static final java.lang.String FIELD2_NAME = "field2";

    public static final org.objectfabric.TType FIELD2_TYPE = org.objectfabric.Immutable.INTEGER.type();

    public static final int TEXT_INDEX = 2;

    public static final java.lang.String TEXT_NAME = "text";

    public static final org.objectfabric.TType TEXT_TYPE = org.objectfabric.Immutable.STRING.type();

    public static final int FIELD_COUNT = 3;

    public static java.lang.String fieldName(int index) {
        switch (index) {
            case FIELD_INDEX:
                return FIELD_NAME;
            case FIELD2_INDEX:
                return FIELD2_NAME;
            case TEXT_INDEX:
                return TEXT_NAME;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static org.objectfabric.TType fieldType(int index) {
        switch (index) {
            case FIELD_INDEX:
                return FIELD_TYPE;
            case FIELD2_INDEX:
                return FIELD2_TYPE;
            case TEXT_INDEX:
                return TEXT_TYPE;
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
        return part05.generated.MyObjectModel.PART05_GENERATED_MYCLASS_CLASS_ID;
    }

    @Override
    protected org.objectfabric.ObjectModel objectModel_() {
        return part05.generated.MyObjectModel.instance();
    }

    protected static class Version extends org.objectfabric.TIndexed.Version32 {

        public int _field;

        public int _field2;

        public java.lang.String _text;

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
                case FIELD_INDEX:
                    return _field;
                case FIELD2_INDEX:
                    return _field2;
                case TEXT_INDEX:
                    return _text;
                default:
                    return super.getAsObject(index);
            }
        }

        @Override
        public void setAsObject(int index, java.lang.Object value) {
            switch (index) {
                case FIELD_INDEX:
                    _field = ((java.lang.Integer) value).intValue();
                    break;
                case FIELD2_INDEX:
                    _field2 = ((java.lang.Integer) value).intValue();
                    break;
                case TEXT_INDEX:
                    _text = (java.lang.String) value;
                    break;
                default:
                    super.setAsObject(index, value);
                    break;
            }
        }

        @Override
        public void merge(org.objectfabric.TObject.Version next) {
            MyClass.Version source = (MyClass.Version) next;

            if (source.hasBits()) {
                if (source.getBit(FIELD_INDEX))
                    _field = source._field;

                if (source.getBit(FIELD2_INDEX))
                    _field2 = source._field2;

                if (source.getBit(TEXT_INDEX))
                    _text = source._text;
            }

            super.merge(next);
        }

        @Override
        public void writeWrite(org.objectfabric.Writer writer, int index) {
            if (writer.interrupted())
                writer.resume();

            switch (index) {
                case FIELD_INDEX: {
                    if (!writer.canWriteInteger()) {
                        writer.interrupt(null);
                        return;
                    }

                    writer.writeInteger(_field);
                    break;
                }
                case FIELD2_INDEX: {
                    if (!writer.canWriteInteger()) {
                        writer.interrupt(null);
                        return;
                    }

                    writer.writeInteger(_field2);
                    break;
                }
                case TEXT_INDEX: {
                    writer.writeString(_text);

                    if (writer.interrupted()) {
                        writer.interrupt(null);
                        return;
                    }

                    break;
                }
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
                case FIELD_INDEX: {
                    if (!reader.canReadInteger()) {
                        reader.interrupt(null);
                        return;
                    }

                    int value = reader.readInteger();

                    for (int i = versions.length - 1; i >= 0; i--)
                        ((Version) versions[i])._field = value;

                    break;
                }
                case FIELD2_INDEX: {
                    if (!reader.canReadInteger()) {
                        reader.interrupt(null);
                        return;
                    }

                    int value = reader.readInteger();

                    for (int i = versions.length - 1; i >= 0; i--)
                        ((Version) versions[i])._field2 = value;

                    break;
                }
                case TEXT_INDEX: {
                    java.lang.String value = reader.readString();

                    if (reader.interrupted()) {
                        reader.interrupt(null);
                        return;
                    }

                    for (int i = versions.length - 1; i >= 0; i--)
                        ((Version) versions[i])._text = value;

                    break;
                }
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