package org.spek.maven.plugin;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.spek.console.cmd.CmdPackage;
import org.spek.console.cmd.Options;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mojo(name = "spek", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.TEST)
public class SpekMojo extends AbstractMojo {

    @Parameter(defaultValue = "", property = "packageName")
    private String packageName = "";

    @Parameter(defaultValue = "true", property = "text")
    private Boolean text = true;

    @Parameter(defaultValue = "false", property = "html")
    private Boolean html = false;

    @Parameter(property = "outputFile")
    private String outputFile = null;

    @Parameter(property = "cssFile")
    private String cssFile = null;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Override
    public void execute() {

        updateContextClassLoader();

        Options options = new Options(
                packageName,
                text,
                html,
                (outputFile != null ? outputFile : ""),
                (cssFile != null ? cssFile : ""));

        CmdPackage.runSpecs(options);
    }

    private void updateContextClassLoader() {
        try {
            Set<URL> urls = new HashSet<>();
            List<String> elements = project.getTestClasspathElements();
            for (String element : elements) {
                urls.add(new File(element).toURI().toURL());
            }

            ClassLoader contextClassLoader = URLClassLoader.newInstance(
                    urls.toArray(new URL[0]),
                    Thread.currentThread().getContextClassLoader());

            Thread.currentThread().setContextClassLoader(contextClassLoader);

        } catch (DependencyResolutionRequiredException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}


