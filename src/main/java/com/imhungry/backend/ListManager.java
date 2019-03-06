package com.imhungry.backend;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by calebthomas on 3/1/19.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ListManager {

    @Getter
    private List<HungryList> hungryLists;

    public ListManager() {
        hungryLists = new ArrayList<>();
        hungryLists.add(new HungryList(HungryList.ListType.FAVORITE.toString()));
        hungryLists.add(new HungryList(HungryList.ListType.EXPLORE.toString()));
        hungryLists.add(new HungryList(HungryList.ListType.BLOCK.toString()));
    }


}
