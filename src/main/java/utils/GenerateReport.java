package utils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

public class GenerateReport {

    public static void main(String[] args) throws IOException {

        File reportOutputDirectory = new File("target/report");  //report file name
        List<String> jsonFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.find(Paths.get("target")  //feature file path
                , Integer.MAX_VALUE
                , (path, attr) -> attr.isRegularFile() && path.toString().endsWith("cucumber.json"))) {
            paths.forEach(path -> {
                jsonFiles.add(path.toFile().getAbsolutePath());
            });
        }
        String buildNumber = "1";
        String projectName = "PayMe_APIs";
        String userType = "testing";
        String environment = "sit";
        String version = "12";

        boolean runWithJenkins = false;
        boolean parallelTesting = false;
        Configuration configuration = new Configuration(reportOutputDirectory, projectName);

        // optional configuration
        configuration.setParallelTesting(parallelTesting);
        configuration.setRunWithJenkins(runWithJenkins);
        configuration.setBuildNumber(buildNumber);


        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();

    }
}





