package mycode.source.polling;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import mycode.source.PollingSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Zukan extends PollingSource {

    public Zukan() {
        period = 60 * 60 * 12;
    }

    @Override
    public Map<String, Object> poll() throws Exception {
        Pattern p = Pattern.compile("^「(.+)」.+【声優図鑑】$");
        Set<String> set = new LinkedHashSet<>();
        try {
            for (int i = 1;; i++) {
                Document get = Jsoup.connect("http://ddnavi.com/search/%E5%A3%B0%E5%84%AA%E5%9B%B3%E9%91%91/page/" + i + "/").ignoreHttpErrors(false).timeout(Integer.MAX_VALUE).get();
                Elements select = get.select(".info h3 a");
                select.stream().map((el) -> el.text())
                        .map((text) -> p.matcher(text))
                        .filter((matcher) -> matcher.find())
                        .map((matcher) -> matcher.group(1))
                        .forEach(set::add);
            }
        } catch (Throwable t) {
            Map<String, Object> result = new HashMap<>();
            result.put("set", set);
            return result;
        }
    }
}
