//Copyright (c) 2014, IDEO 

package lemma.library;

import org.junit.Test;

public class EventFilterTest {
    @Test
    public void noOpsWhenNoHandlersAreSetUp() {
        EventFilter eventFilter = new EventFilter();
        eventFilter.handle(new Event());
    }

    @Test
    public void handlesNullEvents() {
        EventFilter eventFilter = new EventFilter();
        eventFilter.add("some_event", new EventHandler() {
            @Override
            public void callback(Event event) {
            }
        });
        eventFilter.handle(null);
    }
}
