package mycode.source.callable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import mycode.source.CallableSource;
import mycode.source.wiki.query.RecentChanges;
import mycode.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditedSeiyu extends CallableSource {

    @Autowired
    RecentChanges rc;
    @Autowired
    CategoryAndTemplateSeiyu cats;

    @Override
    public Map<String, Object> call(Map<Class, Long> map) throws Exception {
        Optional catsCache = cats.getCache(map, "mapList");
        Optional rcCache = rc.getCache(map, "mapList");
        Optional esCache = this.getCache(map, "mapList");
        List<Map<String, String>> esMapList;
        if (esCache.isPresent()) {
            esMapList = (List<Map<String, String>>) esCache.get();
        } else {
            esMapList = new ArrayList<>();
        }
        if (catsCache.isPresent() && rcCache.isPresent()) {
            updateTimeStamp(map, CategoryAndTemplateSeiyu.class);
            updateTimeStamp(map, RecentChanges.class);
            Set<String> attrSet = new Utility().getAttrSet((List) catsCache.get(), "title");
            Map<String, Object> result = new HashMap<>();
            ((List<Map<String, String>>) rcCache.get()).stream()
                    .filter((m) -> attrSet.contains(m.get("title")))
                    .filter((m) -> !esMapList.contains(m))
                    .map((m) -> {
                        System.out.println(this.getClass().getSimpleName() + " added: " + m);
                        return new HashMap<>(m);
                    })
                    .forEach(esMapList::add);
            result.put("mapList", esMapList);
            return result;
        }
        return null;
    }
}
