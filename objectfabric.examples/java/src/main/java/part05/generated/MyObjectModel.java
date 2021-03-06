
package part05.generated;

//==============================================================================
//                                                                              
//  THIS FILE HAS BEEN GENERATED BY OBJECTFABRIC                                
//                                                                              
//==============================================================================

@SuppressWarnings({ "hiding", "unchecked", "static-access", "rawtypes" })
public final class MyObjectModel extends org.objectfabric.ObjectModel {

    private static final byte[] UID = { -121, 36, 118, 53, 94, -96, 69, -100, -1, 4, -88, -100, 81, 43, -55, -39 };

    // volatile not needed, models have no state
    private static MyObjectModel _instance;

    private static final java.lang.Object _lock = new java.lang.Object();

    protected MyObjectModel() {
    }

    public static MyObjectModel instance() {
        if (_instance == null) {
            synchronized (_lock) {
                if (_instance == null)
                    _instance = new MyObjectModel();
            }
        }

        return _instance;
    }

    public static byte[] uid() {
        byte[] copy = new byte[UID.length];
        arraycopy(UID, copy);
        return copy;
    }

    @Override
    protected byte[] uid_() {
        return UID;
    }

    /**
     * Registers this object model so that its classes can be serialized by the
     * system.
     */
    public static void register() {
        register(instance());
    }

    @Override
    protected java.lang.String objectFabricVersion() {
        return "0.9";
    }

    public static final int CLASS_COUNT = 10;

    public static final int PART05_GENERATED_BATTERY_CLASS_ID = 0;

    public static final int PART05_GENERATED_CAR_CLASS_ID = 1;

    public static final int PART05_GENERATED_DOOR_CLASS_ID = 2;

    public static final int PART05_GENERATED_ELECTRICCAR_CLASS_ID = 3;

    public static final int PART05_GENERATED_KEY_CLASS_ID = 4;

    public static final int PART05_GENERATED_MYCLASS_CLASS_ID = 5;

    public static final int PART05_GENERATED_SUBPACKAGE_OTHERCAR_CLASS_ID = 6;

    public static final int PART05_GENERATED_SETTINGS_CLASS_ID = 7;

    public static final int PART05_GENERATED_USER_CLASS_ID = 8;

    public static final int PART05_GENERATED_VROOM_CLASS_ID = 9;

    public static final int METHOD_COUNT = 0;

    @Override
    protected java.lang.Class getClass(int classId, org.objectfabric.TType[] genericParameters) {
        switch (classId) {
            case PART05_GENERATED_BATTERY_CLASS_ID:
                return part05.generated.Battery.class;
            case PART05_GENERATED_CAR_CLASS_ID:
                return part05.generated.Car.class;
            case PART05_GENERATED_DOOR_CLASS_ID:
                return part05.generated.Door.class;
            case PART05_GENERATED_ELECTRICCAR_CLASS_ID:
                return part05.generated.ElectricCar.class;
            case PART05_GENERATED_KEY_CLASS_ID:
                return part05.generated.Key.class;
            case PART05_GENERATED_MYCLASS_CLASS_ID:
                return part05.generated.MyClass.class;
            case PART05_GENERATED_SUBPACKAGE_OTHERCAR_CLASS_ID:
                return part05.generated.subPackage.OtherCar.class;
            case PART05_GENERATED_SETTINGS_CLASS_ID:
                return part05.generated.Settings.class;
            case PART05_GENERATED_USER_CLASS_ID:
                return part05.generated.User.class;
            case PART05_GENERATED_VROOM_CLASS_ID:
                return part05.generated.Vroom.class;
        }

        return super.getClass(classId, genericParameters);
    }

    @Override
    protected org.objectfabric.TObject createInstance(org.objectfabric.Resource resource, int classId, org.objectfabric.TType[] genericParameters) {
        switch (classId) {
            case PART05_GENERATED_BATTERY_CLASS_ID:
                return new part05.generated.Battery(resource, 0);
            case PART05_GENERATED_CAR_CLASS_ID:
                return new part05.generated.Car(resource, null, null, null);
            case PART05_GENERATED_DOOR_CLASS_ID:
                return new part05.generated.Door(resource);
            case PART05_GENERATED_ELECTRICCAR_CLASS_ID:
                return new part05.generated.ElectricCar(resource, null, null, null);
            case PART05_GENERATED_KEY_CLASS_ID:
                return new part05.generated.Key(resource);
            case PART05_GENERATED_MYCLASS_CLASS_ID:
                return new part05.generated.MyClass(resource);
            case PART05_GENERATED_SUBPACKAGE_OTHERCAR_CLASS_ID:
                return new part05.generated.subPackage.OtherCar(resource);
            case PART05_GENERATED_SETTINGS_CLASS_ID:
                return new part05.generated.Settings(resource);
            case PART05_GENERATED_USER_CLASS_ID:
                return new part05.generated.User(resource);
            case PART05_GENERATED_VROOM_CLASS_ID:
                return new part05.generated.Vroom(resource);
        }

        return super.createInstance(resource, classId, genericParameters);
    }

}
