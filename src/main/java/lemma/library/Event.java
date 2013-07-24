package lemma.library;

public class Event {
    public String name;
    public String stringValue;
    public int intValue;
    public float floatValue;

    public Event(){
        this.name = "";
        this.stringValue = "";
        this.intValue = 0;
        this.floatValue = (float)0;
    }
    public Event( final String name, final String value ){
        this.name = name;
        this.stringValue = value;
		try {
        	this.intValue = Integer.parseInt( value );
		}
		catch (NumberFormatException e){
			this.intValue = 0;
		}
		try {
			this.floatValue = Float.parseFloat( value );
		}
		catch (NumberFormatException e){
			this.floatValue = 0;
		}
    }
    public Event( final String name, final int value ){
        this.name = name;
        this.stringValue = String.valueOf( value );
        this.intValue = value;
        this.floatValue = (float)value;
    }
    public Event( final String name, final float value ){
        this.name = name;
        this.stringValue = String.valueOf( value );
        this.intValue = (int) value;
        this.floatValue =  value;
    }
    public Event( final Event other ){
        this.name = other.name;
        this.stringValue = other.stringValue;
        this.intValue = other.intValue;
        this.floatValue = other.floatValue;
    }
}


