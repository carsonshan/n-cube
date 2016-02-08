package com.cedarsoftware.ncube

import org.junit.After
import org.junit.Before
import org.junit.Test

import java.lang.reflect.Constructor
import java.lang.reflect.Modifier

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

/**
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br/>
 *         Copyright (c) Cedar Software LLC
 *         <br/><br/>
 *         Licensed under the Apache License, Version 2.0 (the 'License')
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br/><br/>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <br/><br/>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an 'AS IS' BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
class TestGroovyTemplate
{
    @Before
    public void setUp()
    {
        TestingDatabaseHelper.setupDatabase()
    }

    @After
    public void tearDown()
    {
        TestingDatabaseHelper.tearDownDatabase()
    }

    @Test
    void testDefaultConstructorIsPrivateForSerialization()
    {
        Class c = GroovyTemplate.class
        Constructor<GroovyTemplate> con = c.getDeclaredConstructor()
        assertEquals Modifier.PRIVATE, con.modifiers & Modifier.PRIVATE
        con.accessible = true
        assertNotNull con.newInstance()
    }

    @Test
    void testTemplateThatUsesClasspath()
    {
        NCubeManager.getNCubeFromResource(ApplicationID.testAppId, 'sys.classpath.cedar.json')
        NCube template = NCubeManager.getNCubeFromResource('templateUsesClassPath.json')
        NCubeManager.addCube(ApplicationID.testAppId, template)
        String text = template.getCell([loc:'web', testType:'templateTest'])
        assert text.contains('from the web')
        assert text.contains('Hello, world."')

        text = template.getCell([loc:'file system', testType:'templateTest'])
        assert text.contains('from the file system')
        assert text.contains('Hello, world."')
    }

    @Test
    void testNCubeGroovyExpressionThatUsesClasspath()
    {
        NCubeManager.getNCubeFromResource(ApplicationID.testAppId, 'sys.classpath.cedar.json')
        NCube template = NCubeManager.getNCubeFromResource('templateUsesClassPath.json')
        NCubeManager.addCube(ApplicationID.testAppId, template)
        String text = template.getCell([testType:'expressionTest'])
        assert text.contains('Hello, world."')
    }
}
