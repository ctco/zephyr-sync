package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.beans.testresult.cucumber.Feature;
import lv.ctco.zephyr.beans.testresult.cucumber.Scenario;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class CucumberTransformerTest {

    private CucumberTransformer transformer;

    @Before
    public void setUp() throws Exception {
        transformer = new CucumberTransformer();
    }

    @Test
    public void testGenerateUniqueId() throws Exception {
        assertThat(transformer.generateUniqueId(new Feature(), scenarioWithId("")), is(""));
        assertThat(transformer.generateUniqueId(new Feature(), scenarioWithId("test;2")), is("T2"));
        assertThat(transformer.generateUniqueId(new Feature(), scenarioWithId("import-identities-from-sniam;import-an-identity-in-iiq-from-sniam-with-\\u0027life-cycle-state\\u0027;dxrstate-mapping-to-\\u0027life-cycle-state\\u0027;2")), is("IIFSIAIIIFSWUCSDMTUCS2"));
        assertThat(transformer.generateUniqueId(new Feature(), scenarioWithId("import-identities-from-sniam")), is("IIFS"));
    }

    @Test
    public void nameAndSuiteName() {
        assertThat(transformer.transform("[{\"name\":null, \"elements\":[{\"name\":null, \"id\":\"123\"}]}]").get(0).getName(), is(nullValue()));
        assertThat(transformer.transform("[{\"name\":\"fe\", \"elements\":[{\"name\":null, \"id\":\"123\"}]}]").get(0).getName(), is("fe"));
        assertThat(transformer.transform("[{\"name\":null, \"elements\":[{\"name\":\"sce\", \"id\":\"123\"}]}]").get(0).getName(), is("sce"));
        assertThat(transformer.transform("[{\"name\":\"fe\", \"elements\":[{\"name\":\"sce\", \"id\":\"123\"}]}]").get(0).getName(), is("sce"));

        assertThat(transformer.transform("[{\"name\":null, \"elements\":[{\"name\":null, \"id\":\"123\"}]}]").get(0).getSuiteName(), is(nullValue()));
        assertThat(transformer.transform("[{\"name\":\"fe\", \"elements\":[{\"name\":null, \"id\":\"123\"}]}]").get(0).getSuiteName(), is("fe"));
        assertThat(transformer.transform("[{\"name\":null, \"elements\":[{\"name\":\"sce\", \"id\":\"123\"}]}]").get(0).getSuiteName(), is(nullValue()));
        assertThat(transformer.transform("[{\"name\":\"fe\", \"elements\":[{\"name\":\"sce\", \"id\":\"123\"}]}]").get(0).getSuiteName(), is("fe"));
    }

    Scenario scenarioWithId(String id) {
        Scenario scenario = new Scenario();
        scenario.id = id;
        return scenario;
    }
}
