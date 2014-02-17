package lemma.library;

public class Event {
    public String name;
    
    public boolean booleanValue;
    public String stringValue;
    public int intValue;
    public double doubleValue;
    public float floatValue;
    public Object objectValue;

    public int arraySize;
    public boolean isArray;
    public String[] stringArray;
    public boolean[] booleanArray;
    public int[] intArray;
    public float[] floatArray;
    public double[] doubleArray;


    public Event() {
        this.name = "";
        this.booleanValue = false;
        this.stringValue = "";
        this.intValue = 0;
        this.doubleValue = (double) 0;
        this.floatValue = (float) 0;
        this.objectValue = null;

        this.isArray = false;
        this.arraySize = 0;
        this.booleanArray = new boolean[0];
        this.stringArray = new String[0];
        this.intArray = new int[0];
        this.floatArray = new float[0];
        this.doubleArray = new double[0];
    }

    public Event(final String name, Object value) {
        this.isArray = (value instanceof Object[]);
        this.name = name;

        this.objectValue = value;
        this.stringValue = value.toString();

        try {
            this.booleanValue = Boolean.valueOf(value.toString()).booleanValue();
        } catch(NumberFormatException e){
            this.booleanValue = false;
        }

        try {
            this.intValue = Integer.valueOf(value.toString()).intValue();
        } catch(NumberFormatException e){
            this.intValue = 0;
        }

        try {
            this.doubleValue = Double.valueOf(value.toString()).doubleValue();
        } catch(NumberFormatException e){
            this.doubleValue = 0;
        }

        try {
            this.floatValue = Float.valueOf(value.toString()).floatValue();
        } catch(NumberFormatException e){
            this.floatValue = 0;
        }

        if(this.isArray){
            Object[] values = (Object[])value;
            this.arraySize = values.length;

            this.stringArray = new String[this.arraySize];
            for(int i = 0; i < this.arraySize; i++){
                this.stringArray[i] = values[i].toString();
            }

            this.booleanArray = new boolean[this.arraySize];
            for(int i = 0; i < this.arraySize; i++){
                try {
                    this.booleanArray[i] = Boolean.valueOf(values[i].toString()).booleanValue();
                } catch(NumberFormatException e) {
                    this.booleanArray[i] = false;
                }
            }

            this.intArray = new int[this.arraySize];
            for(int i = 0; i < this.arraySize; i++){
                try {
                    this.intArray[i] = Integer.valueOf(values[i].toString()).intValue();
                } catch(NumberFormatException e) {
                    this.intArray[i] = 0;
                }
            }

            this.doubleArray = new double[this.arraySize];
            for(int i = 0; i < this.arraySize; i++){
                try {
                    this.doubleArray[i] = Double.valueOf(values[i].toString()).doubleValue();
                } catch(NumberFormatException e) {
                    this.doubleArray[i] = (double) 0;
                }
            }

            this.floatArray = new float[this.arraySize];
            for(int i = 0; i < this.arraySize; i++){
                try {
                    this.floatArray[i] = Float.valueOf(values[i].toString()).floatValue();
                } catch(NumberFormatException e) {
                    this.floatArray[i] = (float) 0;
                }
            }
        }



    }

    public Event(final Event other) {
        this.name = other.name;
        this.stringValue = other.stringValue;
        this.intValue = other.intValue;
        this.floatValue = other.floatValue;
    }
}


