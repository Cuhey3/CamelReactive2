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
import java.util.Set;
import java.util.stream.Collectors;
import mycode.source.CallableSource;
import mycode.source.polling.Koepota;
import mycode.source.polling.Zukan;
import mycode.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZukanSeiyu extends CallableSource {

    @Autowired
    Zukan zukan;
    @Autowired
    CategoryAndTemplateSeiyu cats;

    @Override
    public Map<String, Object> call(Map<Class, Long> map) throws Exception {
        Optional catsCache = cats.getCache(map, "mapList");
        Optional zukanCache = zukan.getCache(map, "set");
        Optional zsCache = this.getCache(map, "mapList");
        List<Map<String, String>> zsMapList;
        if (zsCache.isPresent()) {
            zsMapList = (List<Map<String, String>>) zsCache.get();
        } else {
            zsMapList = new ArrayList<>();
        }
        if (catsCache.isPresent() && zukanCache.isPresent()) {
            updateTimeStamp(map, CategoryAndTemplateSeiyu.class);
            updateTimeStamp(map, Zukan.class);
            Set<String> zukanSet = (Set<String>) zukanCache.get();
            Map<String, Object> result = new HashMap<>();
            ((List<Map<String, String>>) catsCache.get()).stream()
                    .filter((m) -> zukanSet.contains(m.get("title").replaceFirst(" \\(.+\\)", "")))
                    .filter((m) -> !zsMapList.contains(m))
                    .map((m) -> {
                        System.out.println(this.getClass().getSimpleName() + " added: " + m);
                        return new HashMap<>(m);
                    })
                    .forEach(zsMapList::add);
            result.put("mapList", zsMapList);
            return result;
        }
        return null;
    }
}
