package lv.ctco.zephyr.beans.jira;

import lv.ctco.zephyr.util.ObjectTransformer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class IssueLinkTest {

    @Test
    public void testSerialize() throws Exception {
        String json = ObjectTransformer.serialize(new IssueLink("aaa", "bbb", "Reference"));
        System.out.println(json);
        assertThat(json, is(not(nullValue())));
    }
}