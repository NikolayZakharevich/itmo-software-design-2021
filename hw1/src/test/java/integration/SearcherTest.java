package integration;

import akka.actor.Props;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;
import searchers.AbstractSearcher;
import searchers.GoogleSearcher;
import searchers.YandexSearcher;
import structures.UrlWithTitle;

import java.io.IOException;
import java.util.List;

@PrepareForTest({Jsoup.class, Props.class})
public class SearcherTest {

    @Test
    public void googleSearcherTest() throws IOException {
        final List<UrlWithTitle> results = new GoogleSearcher("hello").search();
        Assert.assertEquals(AbstractSearcher.URLS_COUNT, results.size());
        for (UrlWithTitle result : results) {
            Assert.assertNotNull(result.getUrl());
        }
    }

    @Test
    public void yandexSearcherTest() throws IOException {
        final List<UrlWithTitle> results = new YandexSearcher("hello").search();
        Assert.assertEquals(AbstractSearcher.URLS_COUNT, results.size());
        for (UrlWithTitle result : results) {
            System.out.println(result);
            Assert.assertNotNull(result.getUrl());
        }
    }

}
