package dk.emilmadsen.namerrapi.service;

import dk.emilmadsen.namerrapi.model.NameCount;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class NameSeederTests {

    private NameSeeder nameSeeder = new NameSeeder(null, null);
    private ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void parse_html() throws IOException {

        String html = FileUtils.readFileToString(new File(classLoader.getResource("dst_name_response.html").getFile()), "UTF-8");
        List<NameCount> result = nameSeeder.parseCountHtml(html);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(28762, result.get(0).getMaleCount());
        Assertions.assertEquals(392, result.get(0).getFemaleCount());
        Assertions.assertEquals(28590, result.get(1).getMaleCount());
        Assertions.assertEquals(403, result.get(1).getFemaleCount());

    }

}
