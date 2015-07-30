/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mycode.source.instant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import mycode.source.InstantSource;
import mycode.source.wiki.util.WikiParse;
import org.springframework.stereotype.Component;

@Component
public class SeiyuTemplateIncludePages implements InstantSource {

    @Getter
    private final List<Map<String, String>> mapList = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void ready() throws IOException {
        List<Map<String, String>> mapList1 = WikiParse.builder()
                .param("action=query&list=backlinks&bltitle=Template:%E5%A3%B0%E5%84%AA&format=xml&bllimit=500&blnamespace=0&continue=")
                .list("backlinks")
                .map("bl")
                .continueElement("blcontinue")
                .build().getMapList();
        mapList.clear();
        mapList.addAll(mapList1);
    }
}
