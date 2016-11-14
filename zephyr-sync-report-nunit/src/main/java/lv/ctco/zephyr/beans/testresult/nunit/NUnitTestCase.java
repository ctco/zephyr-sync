package lv.ctco.zephyr.beans.testresult.nunit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "test-case")
public class NUnitTestCase {

    @XmlAttribute(name = "id")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "result")
    protected String result;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getResult() {
        return this.result;
    }


}
