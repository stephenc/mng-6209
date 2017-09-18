package localdomain.localhost.plugin1;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.apache.maven.lifecycle.Lifecycle;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.IOUtil;

@Mojo(name = "list", threadSafe = true)
public class ListMojo extends AbstractMojo {
    @Component(role = Lifecycle.class)
    private List<Lifecycle> lifecycles;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Injected by @Component:");
        for (Lifecycle l : lifecycles) {
            if (l.getId().startsWith("mng-6209-")) {
                getLog().info("  " + l.getId().substring(9));
            }
        }
        getLog().info("");
        getLog().info("On Plugin Class Loader:");
        try {
            ClassLoader tccl = ListMojo.class.getClassLoader();
            for (URL url : Collections.list(tccl.getResources("META-INF/probe.txt"))) {
                InputStream is = url.openStream();
                try {
                    getLog().info("  " + IOUtil.toString(is).trim());
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
        getLog().info("");
        getLog().info("On Thread Context Class Loader:");
        try {
            ClassLoader tccl = Thread.currentThread().getContextClassLoader();
            for (URL url : Collections.list(tccl.getResources("META-INF/probe.txt"))) {
                InputStream is = url.openStream();
                try {
                    getLog().info("  " + IOUtil.toString(is).trim());
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
}
