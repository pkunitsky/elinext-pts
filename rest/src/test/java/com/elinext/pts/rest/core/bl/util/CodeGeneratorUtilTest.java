package com.elinext.pts.rest.core.bl.util;

import com.elinext.pts.rest.core.util.CodeGenerator;
import com.elinext.pts.rest.model.entity.Project;
import com.elinext.pts.rest.model.entity.Type;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

@RunWith(SpringJUnit4ClassRunner.class)
public class CodeGeneratorUtilTest {

    private Project project;
    private Project projectWithCode;

    @Before
    public void initProject() {
        project = new Project();
        project.setId(1L);
        project.setType(Type.PROJECT);
    }

    @Before
    public void initProjectWithCode() {
        projectWithCode = new Project();
        projectWithCode.setId(1L);
        projectWithCode.setType(Type.PROJECT);
        projectWithCode.setCode("P002");
    }

    @Test
    public void testCodeGenerator() {
        String code = CodeGenerator.getCode(project, 99);
        String withCode = CodeGenerator.getCode(projectWithCode, 1);
        assertEquals("P100", code);
        assertEquals("P002", withCode);
    }
}
