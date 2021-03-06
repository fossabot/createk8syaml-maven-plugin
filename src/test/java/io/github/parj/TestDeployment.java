package io.github.parj;

import com.amihaiemil.eoyaml.YamlMapping;
import java.io.IOException;

import io.takari.maven.testing.TestMavenRuntime;
import io.takari.maven.testing.TestResources;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDeployment {
    private static YamlMapping yaml;

    @Before
    public void setUp() throws IOException {
        yaml = Setup.readYaml(CreateK8SYaml.DEPLOYMENT);
    }

    @Test
    public void testCompareString() {

        assertEquals("apiVersion: apps/v1\n" +
                "kind: Deployment\n" +
                "metadata:\n" +
                "  name: foo\n" +
                "  namespace: thisisanamespace\n" +
                "spec:\n" +
                "  selector:\n" +
                "    matchLabels:\n" +
                "      app: foo\n" +
                "  replicas: 2\n" +
                "  template:\n" +
                "    metadata:\n" +
                "      labels:\n" +
                "        app: foo\n" +
                "    spec:\n" +
                "      securityContext:\n" +
                "        runAsUser: 1000\n" +
                "        runAsNonRoot: true\n" +
                "      containers:\n" +
                "        -\n" +
                "          name: \"foo-container\"\n" +
                "          image: gcr.io/distroless/java\n" +
                "          ports:\n" +
                "            -\n" +
                "              containerPort: 5555\n" +
                "              name: \"container-port\"\n" +
                "          resources:\n" +
                "            limits:\n" +
                "              cpu: 1\n" +
                "              memory: 1G", yaml.toString());
    }


    @Test
    public void testName() {
        assertEquals("foo", yaml.yamlMapping("metadata").string("name"));
    }

    @Test
    public void testNamespace() {
        assertEquals("thisisanamespace", yaml.yamlMapping("metadata").string("namespace"));
    }

    @Test
    public void testSelectorLabel() {
        assertEquals("foo", yaml
                .yamlMapping("spec")
                .yamlMapping("selector")
                .yamlMapping("matchLabels")
                .string("app"));
    }

    @Test
    public void testMetadataLabel() {
        assertEquals("foo", yaml
                .yamlMapping("spec")
                .yamlMapping("template")
                .yamlMapping("metadata")
                .yamlMapping("labels")
                .string("app"));
    }

    @Test
    public void testContainerName() {
        assertEquals("foo-container", yaml
                .yamlMapping("spec")
                .yamlMapping("template")
                .yamlMapping("spec")
                .yamlSequence("containers")
                .yamlMapping(0)
                .string("name"));
    }

    @Test
    public void testImage() {
        assertEquals("gcr.io/distroless/java", yaml
                .yamlMapping("spec")
                .yamlMapping("template")
                .yamlMapping("spec")
                .yamlSequence("containers")
                .yamlMapping(0)
                .string("image"));
    }

    @Test
    public void testPort() {
        assertEquals(5555, yaml
                .yamlMapping("spec")
                .yamlMapping("template")
                .yamlMapping("spec")
                .yamlSequence("containers")
                .yamlMapping(0)
                .yamlSequence("ports")
                .yamlMapping(0)
                .integer("containerPort"));
    }

}