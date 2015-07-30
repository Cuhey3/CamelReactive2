package mycode.source.callable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import mycode.source.CallableSource;
import mycode.source.wiki.query.SeiyuCategoryMembers;
import mycode.source.instant.SeiyuTemplateIncludePages;
import mycode.util.Utility;
import org.apache.camel.Body;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryAndTemplateSeiyu extends CallableSource {

    @Autowired
    SeiyuTemplateIncludePages stip;
    @Autowired
    SeiyuCategoryMembers scm;

    @Override
    public Map<String, Object> call(@Body Map<Class, Long> map) throws IOException {
        stip.ready();
        Set<String> attrSet = new Utility().getAttrSet(stip.getMapList(), "title");

        Optional scmCache = scm.getCache(map, "mapList");
        if (scmCache.isPresent()) {
            updateTimeStamp(map, SeiyuCategoryMembers.class);
            Map<String, Object> result = new HashMap<>();
            result.put("mapList", ((List<Map<String, String>>) scmCache.get()).stream()
                    .filter((m)
                            -> attrSet.contains(m.get("title")))
                    .map((m) -> {
                        System.out.println(this.getClass().getSimpleName() + " added: " + m);
                        return new HashMap<>(m);
                    })
                    .collect(Collectors.toList()));
            return result;
        }
        return null;
    }
}
