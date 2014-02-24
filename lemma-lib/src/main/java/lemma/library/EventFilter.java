package lemma.library;

public class EventFilter {

    class filter_t {
        public String eventName;
        public EventHandler callback;
    }

    private static int MAX_FILTERS = 64;

    private int nFilters;
    private filter_t filters[];
    private String eventNames[];

    public EventFilter(){
        this.nFilters = 0;
        this.filters = new filter_t[MAX_FILTERS];
        for ( int i = 0; i < filters.length; i++){
            filters[i] = new filter_t();
        }

        this.eventNames = new String[MAX_FILTERS];
        for ( int i = 0; i < eventNames.length; i++){
            eventNames[i] = "";
        }
    }
    public void add(String eventName, EventHandler callback){
        if(this.nFilters >= MAX_FILTERS){
            System.out.println("Lemma can only hear up to "+MAX_FILTERS+" topics. Unable to add '" + eventName +"'.");
        } else {
            int filterNumber = nFilters++;
            filters[filterNumber].eventName = eventName;
            filters[filterNumber].callback = callback;

            this.eventNames[filterNumber] = filters[filterNumber].eventName;
        }
    }
    public void handle(Event event){
        if(event == null) { return; }
        for(int i = 0; i < nFilters; i++) {
            if (filters[i].eventName.equals(event.name)) {
                filters[i].callback.callback(event);
            }
        }
    }
    public int count(){
       return this.nFilters;
    }
    public String[] events(){
        return this.eventNames;
    }
}
