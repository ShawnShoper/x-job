package org.x.job.executor.io;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HadoopHandler {
    private static List<Treenode> nodes = new ArrayList<>();
    private static Configuration config = new Configuration();
    private Treenode cnode = new Treenode();

    public void doIt() throws IOException {
        FileSystem fs = FileSystem.get(config);
        nodes.clear();

        Treenode node = new Treenode();
    }
}
