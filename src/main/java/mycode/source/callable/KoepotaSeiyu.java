/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycode.source.callable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import mycode.source.CallableSource;
import mycode.source.polling.Koepota;
import mycode.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KoepotaSeiyu extends CallableSource {

    @Autowired
    Koepota koe;
    @Autowired
    CategoryAndTemplateSeiyu cats;

    @Override
    public Map<String, Object> call(Map<Class, Long> map) throws Exception {
        Optional catsCache = cats.getCache(map, "mapList");
        Optional koeCache = koe.getCache(map, "mapList");
        Optional ksCache = this.getCache(map, "mapList");
        List<Map<String, String>> ksMapList;
        if (ksCache.isPresent()) {
            ksMapList = (List<Map<String, String>>) ksCache.get();
        } else {
            ksMapList = new ArrayList<>();
        }
        if (catsCache.isPresent() && koeCache.isPresent()) {
            updateTimeStamp(map, CategoryAndTemplateSeiyu.class);
            updateTimeStamp(map, Koepota.class);
            String koepotaAll = (String) new Utility().getAttrSet((List) koeCache.get(), "出演者").stream().collect(Collectors.joining(" "));
            Map<String, Object> result = new HashMap<>();
            ((List<Map<String, String>>) catsCache.get()).stream()
                    .filter((m) -> koepotaAll.contains(m.get("title").replaceFirst(" \\(.+\\)", "")))
                    .filter((m) -> !ksMapList.contains(m))
                    .map((m) -> {
                        System.out.println(this.getClass().getSimpleName() + " added: " + m);
                        return new HashMap<>(m);
                    })
                    .forEach(ksMapList::add);
            result.put("mapList", ksMapList);
            return result;
        }
        return null;
    }
}
