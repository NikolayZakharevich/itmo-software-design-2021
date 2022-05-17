package actors;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.routing.RoundRobinPool;
import structures.QueryResultsMessage;
import structures.StartMessage;
import structures.TimeoutMessage;
import structures.UrlWithTitle;
import searchers.GoogleSearcher;
import searchers.MailRuSearcher;
import searchers.YandexSearcher;
import utils.StopException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static utils.Constants.DURATION_SECOND;


public class MasterActor extends UntypedAbstractActor {
    private final static Duration RECEIVE_TIMEOUT = DURATION_SECOND;

    private final ActorRef childRouter;
    private final String query;

    private static final int SEARCHERS_NUM = 3;
    private final List<QueryResultsMessage> responses = new ArrayList<>();

    public MasterActor(String query) {
        this.query = query;
        childRouter = getContext()
                .actorOf(new RoundRobinPool(SEARCHERS_NUM).props(Props.create(ChildActor.class)), "childRouter");
        getContext().setReceiveTimeout(RECEIVE_TIMEOUT);
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof StartMessage) {
            childRouter.tell(new GoogleSearcher(query), getSelf());
            childRouter.tell(new YandexSearcher(query), getSelf());
            childRouter.tell(new MailRuSearcher(query), getSelf());
        } else if (message instanceof QueryResultsMessage) {
            responses.add((QueryResultsMessage) message);
            if (responses.size() == SEARCHERS_NUM) {
                context().stop(self());
            }
        } else if (message instanceof TimeoutMessage) {
            context().stop(self());
        }
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(false, DeciderBuilder
                .match(StopException.class, e -> (SupervisorStrategy.Directive) OneForOneStrategy.stop())
                .build());
    }
}