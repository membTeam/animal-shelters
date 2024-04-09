package ru.animals.testUtils;

import static org.junit.jupiter.api.Assertions.*;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.Test;
import ru.animals.utilsDEVL.FileAPI;

import java.io.IOException;

public class ReadDataFromPomXMLTest {

    @Test
    public void getArtifactID() throws XmlPullParserException, IOException {
        var artifactId = FileAPI.rootArtifactID();

        assertTrue(artifactId.length() >0);
    }

}
