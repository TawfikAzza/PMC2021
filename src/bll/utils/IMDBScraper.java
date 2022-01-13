package bll.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.List;

public class IMDBScraper {

    private WebClient webClient;
    private HtmlPage page;

    public IMDBScraper(String url) throws IOException {
        webClient = new WebClient();
        page = getPage(url);
    }

    private HtmlPage getPage(String url) throws IOException {
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        return webClient.getPage(url);
    }

    public String extractTitle(){
        List<DomText> items = page.getByXPath("//*[@id=\"__next\"]/main/div/section[1]/section/div[3]/section/section/div[1]/div[1]/h1");
        String title = items.get(0).toString();
        return title;
    }

    public String extractRating() {
        List<DomText> items = page.getByXPath("//*[@id=\"__next\"]/main/div/section[1]/section/div[3]/section/section/div[1]/div[2]/div/div[1]/a/div/div/div[2]/div[1]/span[1]/text()");
        String rating = items.get(0).toString();
        return rating;
    }
}
