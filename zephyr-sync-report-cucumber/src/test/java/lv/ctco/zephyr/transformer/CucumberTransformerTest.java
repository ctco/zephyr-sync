package lv.ctco.zephyr.transformer;

import lv.ctco.zephyr.beans.testresult.cucumber.Feature;
import lv.ctco.zephyr.beans.testresult.cucumber.Scenario;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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

    Scenario scenarioWithId(String id) {
        Scenario scenario = new Scenario();
        scenario.id = id;
        return scenario;
    }
}