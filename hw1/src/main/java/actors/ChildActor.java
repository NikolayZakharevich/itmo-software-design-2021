package actors;

import akka.actor.UntypedAbstractActor;
import structures.QueryResultsMessage;
import searchers.AbstractSearcher;


public class ChildActor extends UntypedAbstractActor {

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof AbstractSearcher) {
            AbstractSearcher searcher = (AbstractSearcher) o;
            getSender().tell(
                    new QueryResultsMessage(searcher.getName(), searcher.search()),
                    self()
            );
        }
    }
}