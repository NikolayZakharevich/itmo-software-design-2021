import actors.MasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import structures.StartMessage;

import java.io.PrintWriter;


public class RequestHandler {
    private final PrintWriter out;

    public RequestHandler(PrintWriter out) {
        this.out = out;
    }

    public void getResponses(String query) {
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef master = system.actorOf(Props.create(MasterActor.class, out, query));
        master.tell(new StartMessage(), ActorRef.noSender());
    }
}