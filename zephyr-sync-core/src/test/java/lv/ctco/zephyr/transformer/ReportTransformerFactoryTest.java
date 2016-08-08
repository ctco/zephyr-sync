package lv.ctco.zephyr.transformer;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class ReportTransformerFactoryTest {

    @Test
    public void testGetTransformer_FromSPI() throws Exception {
        ReportTransformer transformer = ReportTransformerFactory.getInstance().getTransformer("junit");
        assertThat(transformer, is(not(nullValue())));
    }
}