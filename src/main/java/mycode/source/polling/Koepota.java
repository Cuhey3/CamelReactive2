package mycode.source.polling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mycode.source.PollingSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Koepota extends PollingSource {

    public Koepota() {
        period = 60 * 60;
    }

    @Override
    public Map<String, Object> poll() throws Exception {
        Document get = Jsoup.connect("http://www.koepota.jp/eventschedule/").timeout(Integer.MAX_VALUE).get();
        Elements select = get.select("table");
        int max = 0;
        Element table = null;
        for (Element el : select) {
            int size = el.select("tr").size();
            if (max < size) {
                table = el;
                max = size;
            }
        }
        Elements trs = table.select("tr");
        Element trFirst = trs.remove(0);
        Elements ths = trFirst.select("th");
        List<Map<String, String>> collect = trs.stream()
                .map((tr) -> tr.select("td"))
                .map((tds) -> {
                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < tds.size(); i++) {
                        map.put(ths.get(i).text(),tds.get(i).text());
                    }
                    return map;
                }).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("mapList", collect);
        System.out.println(result);
        return result;
    }
}
