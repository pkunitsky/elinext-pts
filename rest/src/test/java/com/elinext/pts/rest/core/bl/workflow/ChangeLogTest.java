package com.elinext.pts.rest.core.bl.workflow;

import com.elinext.pts.rest.core.dal.repository.ChangeLogRepository;
import com.elinext.pts.rest.model.entity.ChangeLog;
import com.elinext.pts.rest.model.entity.EventType;
import com.elinext.pts.rest.model.entity.Type;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ChangeLogTest {

    @Mock
    private ChangeLogRepository changeLogRepository;

    private List<ChangeLog> changeLogList;
    private ChangeLog changeLog;

    @Before
    public void initChangeLogList() {
        changeLog = new ChangeLog();
        changeLog.setEventType(EventType.CREATED);
        changeLog.setDescription("[ comment=lalalal; hours=8.0; reportedDate=2018-01-01T10:10;id=4]");
        changeLog.setEntityId(1L);
        changeLog.setEntityType(Type.PROJECT);
        changeLogList = new ArrayList<>();
        changeLogList.add(changeLog);
    }

    @Test
    public void testChangeLogSize() {
        when(changeLogRepository.findByEntityIdAndEntityTypeInAndUserEmail(1L, List.of(Type.PROJECT), "lolol@mail.ru")).thenReturn(changeLogList);
        List<ChangeLog> actualList = changeLogRepository.findByEntityIdAndEntityTypeInAndUserEmail(1L, List.of(Type.PROJECT), "lolol@mail.ru");
        assertEquals(1, actualList.size());
    }

    @Test
    public void testDescription() {
        when(changeLogRepository.findByEntityIdAndEntityTypeInAndUserEmail(1L, List.of(Type.PROJECT), "lolol@mail.ru")).thenReturn(changeLogList);
        List<ChangeLog> actualList = changeLogRepository.findByEntityIdAndEntityTypeInAndUserEmail(1L, List.of(Type.PROJECT), "lolol@mail.ru");
        assertEquals(actualList.get(0).getDescription(),"[ comment=lalalal; hours=8.0; reportedDate=2018-01-01T10:10;id=4]");
    }
}
