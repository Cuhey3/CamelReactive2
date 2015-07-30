package mycode.source.callable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mycode.source.CallableSource;
import mycode.source.wiki.query.RecentChanges;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewPages extends CallableSource {

    @Autowired
    RecentChanges rc;

    @Override
    public Map<String, Object> call(Map<Class, Long> map) throws Exception {
        Optional rcCache = rc.getCache(map, "mapList");
        Optional npCache = this.getCache(map, "mapList");
        List<Map<String, String>> npMapList;
        if (npCache.isPresent()) {
            npMapList = (List<Map<String, String>>) npCache.get();
        } else {
            npMapList = new ArrayList<>();
        }
        if (rcCache.isPresent()) {
            updateTimeStamp(map, RecentChanges.class);
            ((List<Map<String, String>>) rcCache.get()).stream()
                    .filter((m)
                            -> m.get("type").equals("new") && !m.containsKey("redirect"))
                    .filter((m)
                            -> !npMapList.contains(m))
                    .map((m) -> {
                        System.out.println(this.getClass().getSimpleName() + " added: " + m);
                        return new HashMap<>(m);
                    })
                    .forEach(npMapList::add);
            Map<String, Object> result = new HashMap<>();
            result.put("mapList", npMapList);
            return result;
        }
        return null;
    }
}
