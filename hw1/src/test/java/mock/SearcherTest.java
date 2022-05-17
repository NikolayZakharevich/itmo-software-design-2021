package mock;

import actors.MasterActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import searchers.YandexSearcher;
import structures.QueryResultsMessage;
import structures.UrlWithTitle;
import searchers.GoogleSearcher;
import utils.StopException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Jsoup.class, Props.class})
public class SearcherTest {

    @Test
    public void searcherTest() throws IOException {
        final Connection connection = Mockito.mock(Connection.class);
        final Document document = Mockito.mock(Document.class);
        PowerMockito.mockStatic(Jsoup.class);
        when(Jsoup.connect(Mockito.anyString())).thenReturn(connection);
        when(connection.userAgent(Mockito.anyString())).thenReturn(connection);
        when(connection.get()).thenReturn(document);
        when(document.select(Mockito.anyString())).thenReturn(mockGoogleAnswer());

        final List<UrlWithTitle> results = new GoogleSearcher("").search();
        Assert.assertEquals(1, results.size());
        for (UrlWithTitle response : results) {
            Assert.assertNotNull(response.getUrl());
        }
    }

    @Test(expected = StopException.class)
    public void masterTest() {
        final ActorSystem system = ActorSystem.create("System");
        final QueryResultsMessage resp = new QueryResultsMessage(YandexSearcher.NAME, new ArrayList<>());
        final TestActorRef<MasterActor> master = TestActorRef.create(system, Props.create(MasterActor.class, "hello"));
        final MasterActor actor = master.underlyingActor();
        actor.onReceive(resp);
        actor.onReceive(resp);
        actor.onReceive(resp);
    }

    @Test
    public void masterTimeoutTest() {
        final ActorSystem system = ActorSystem.create("System");
        final TestActorRef<MasterActor> master = TestActorRef.create(system, Props.create(MasterActor.class,  "hello"));
        final MasterActor mockedActor = Mockito.spy(master.underlyingActor());

        QueryResultsMessage resp = new QueryResultsMessage(YandexSearcher.NAME, new ArrayList<>());
        mockedActor.onReceive(resp);
        verify(mockedActor, after(2000)).onReceive(resp);
    }

    private Elements mockGoogleAnswer() {
        Attributes attributes = new Attributes();
        attributes.put("href", "/url?q=https://yandex.com/");
        attributes.put("target", "_blank");
        Element element = new Element(Tag.valueOf("a"), "http://www.google.ru/search?q=yandex&num=5", attributes);
        element.text("Yandex");
        return new Elements(Collections.singletonList(element));
    }
}