package com.elinext.pts.rest.core.bl.util;

import com.elinext.pts.rest.core.util.MapUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class MapUtilTest {

    private List<String> description;

    @Before
    public void initDescription(){
        description = List.of("[ comment=lalalal; hours=8.0; reportedDate=2018-01-01T10:10;id=4], " +
                "[], " +
                "[ type=PROJECT; archived=false; changedDate=2019-07-11T15:03:45.558354;id=8; code=P008; parentId=1; name=know]");
    }

    @Test
    public void testDescriptionSize(){
       Map<String , String > descriptionMap =  MapUtil.parseDescriptionIntoMap(description);
       assertEquals(9, descriptionMap.size());
    }
}
