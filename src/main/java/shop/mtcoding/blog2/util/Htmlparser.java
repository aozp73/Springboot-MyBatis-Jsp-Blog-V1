package shop.mtcoding.blog2.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Htmlparser {

    public static String getThumbnail(String html) {
        Document doc = Jsoup.parse(html);
        Elements els = doc.select("img");
        String thumbnail = "";

        if (els.size() == 0) {
            thumbnail = "/images/dora.png";
        } else {
            Element el = els.get(0);
            thumbnail = el.attr("src");
        }

        return thumbnail;
    }
}
