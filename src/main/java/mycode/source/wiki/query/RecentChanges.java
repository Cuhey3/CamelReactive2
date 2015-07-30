package mycode.source.wiki.query;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import mycode.source.PollingSource;
import mycode.source.wiki.util.WikiParse;
import org.springframework.stereotype.Component;

@Component
public class RecentChanges extends PollingSource {

    public RecentChanges() {
        period = 60;
    }

    @Override
    public Map<String, Object> poll() throws IOException {
        Map<String, Object> sourceFields = new HashMap<>();
        sourceFields.put("mapList", WikiParse.builder()
                .param("action=query&list=recentchanges&format=xml&rcnamespace=0&rclimit=500&rcprop=timestamp|title|redirect&rctoponly=true")
                .list("recentchanges")
                .map("rc")
                .build().getMapList());
        return sourceFields;
    }
}
