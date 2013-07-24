package lemma.library;

public class EventFilter {

    class filter_t {
        public String eventName;
        public EventHandler callback;
    }

    private int nFilters;
    private filter_t filters[];
    private String eventNames[];

    public EventFilter(){
        this.nFilters = 0;
        this.filters = new filter_t[64];
        for ( int i = 0; i < filters.length; i++){
            filters[i] = new filter_t();
        }

        this.eventNames = new String[64];
        for ( int i = 0; i < eventNames.length; i++){
            eventNames[i] = "";
        }
    }
    public void add(String eventName, EventHandler callback){
        int filterNumber = nFilters++;
        filters[filterNumber].eventName = eventName;
        filters[filterNumber].callback = callback;        // Assign callback identity here

        this.eventNames[filterNumber] = filters[filterNumber].eventName;
    }
    public void handle(Event event){
        for(int i = 0; i < nFilters; i++) {
            if (filters[i].eventName.equals(event.name)) {
                filters[i].callback.callback(event);                 // Now runs placeholder method, should run custom callback
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
