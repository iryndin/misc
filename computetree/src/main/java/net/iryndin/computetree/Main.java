package net.iryndin.computetree;

import com.google.common.base.Optional;
import net.iryndin.computetree.api.INode;
import net.iryndin.computetree.api.INodeRegistry;
import net.iryndin.computetree.api.ITreeResult;
import net.iryndin.computetree.core.TreeComputation;
import net.iryndin.computetree.core.YamlTreeReader;
import net.iryndin.computetree.node.DebugNode;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author iryndin
 * @since 25/01/17
 */
public class Main {

    final static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        String yaml;
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("tree.yaml")) {
            yaml = IOUtils.toString(is, StandardCharsets.UTF_8);
        }
        YamlTreeReader ytr = new YamlTreeReader(yaml);
        String start = ytr.getStartNodeId();
        INodeRegistry registry = ytr.createNodeRegistry();
        try (TreeComputation tc = new TreeComputation(registry)) {
            Optional<ITreeResult> tr = tc.compute(start);
            if (tr.isPresent()) {
                log.info("TreeResult: " + tr.get());
            } else {
                log.info("TreeResult is absent!");
            }
        }
    }

    /*
    static INodeRegistry createINodeRegistry() {

        return new INodeRegistry() {
            @Override
            public Optional<INode> getNode(String id) {
                log.debug("Try to get node by id: '{}'", id);
                switch (id) {
                    case "valid": return Optional.of(new DebugNode("valid"));
                    case "wanted": return Optional.of(new DebugNode("wanted"));
                }
                log.debug("No nodes by id: '{}'", id);
                return Optional.absent();
            }

            @Override
            public Optional<List<String>> getNodeResults(String id, String result) {
                switch (id) {
                    case "valid":
                        switch (result) {
                            case "success": return Optional.of(Arrays.asList("wanted", "terror"));
                            case "notValid": return Optional.of(Arrays.asList("end"));
                            default: return Optional.absent();
                        }
                    case "wanted":
                        switch (result) {
                            case "success": return Optional.of(Arrays.asList("wantedLocal", "wantedIntl"));
                            default: return Optional.absent();
                        }
                }
                return Optional.absent();
            }
        };
    }
    */
}
