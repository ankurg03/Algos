package fileparser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.sketches.theta.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;



public class JsonFileReaderThetaSketch {
    final ObjectMapper mapper;

    HashMap<String, UpdateSketch> visitCount;

    HashMap<String, UpdateSketch> visitorCount;


    HashMap<String, UpdateSketch> ppvCount;


    HashMap<String, UpdateSketch> cartAddsCount;

    BufferedWriter buffWriter;
    int failedInParse =0;


    public JsonFileReaderThetaSketch(String outputPath) throws IOException {
        File f = new File(outputPath);
        if (f.exists()) throw new RuntimeException("File Exist: " +outputPath);

        mapper = new ObjectMapper();
        visitCount = new HashMap<>();
        visitorCount = new HashMap<>();
        ppvCount = new HashMap<>();
        cartAddsCount = new HashMap<>();

        buffWriter = new BufferedWriter(new FileWriter(outputPath));
    }

    public void parserJson(String line) throws JsonProcessingException {

        JsonNode tree = mapper.readTree(line);

        if(tree.has("bu")) {
            visitCount.putIfAbsent(tree.get("bu").toString(), Sketches.updateSketchBuilder().setNominalEntries(65536).build());
            visitCount.get(tree.get("bu").toString()).update(tree.get("visit_ids").toString());

            visitorCount.putIfAbsent(tree.get("bu").toString(), Sketches.updateSketchBuilder().setNominalEntries(65536).build());
            visitorCount.get(tree.get("bu").toString()).update(tree.get("dev_ids").toString());

            ppvCount.putIfAbsent(tree.get("bu").toString(), Sketches.updateSketchBuilder().setNominalEntries(65536).build());
            ppvCount.get(tree.get("bu").toString()).update(tree.get("fids").toString());

            cartAddsCount.putIfAbsent(tree.get("bu").toString(), Sketches.updateSketchBuilder().setNominalEntries(65536).build());
            cartAddsCount.get(tree.get("bu").toString()).update(tree.get("cart_fids").toString());
        }
        else
            System.out.println(line);


    }

    public void read(String filePath) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.filter(JsonFileReaderThetaSketch::lineFilter).forEach(line -> {
                try {
                    parserJson(line);
                } catch (JsonProcessingException e) {
                    failedInParse++;
                }
            });

        }
    }

    private void print(Map<String, UpdateSketch>map, String name) throws IOException {



        map.forEach((a, b)-> {

            try {
                buffWriter.write((name + "_"+a +" "+(long)b.compact().getEstimate()) + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        buffWriter.flush();

        System.out.println("written for " + map.keySet());

    }
    private static boolean lineFilter(String line) {
        return true;
    }

    public static void main(String[] args) throws IOException {
        if (args.length <2) {
            args = new String[2];
            args[0] = "/tmp/test";
            args[1] = "/tmp/thetasketchres";
        }
        String input = args[0];
        String output = args[1];

        JsonFileReaderThetaSketch parser = new JsonFileReaderThetaSketch(output);

        File f = new File(input);

        // returns pathnames for files and directory
        File[] paths = f.listFiles();

        // for each pathname in pathname array
        for(File path:paths) {

            // prints file and directory paths

            long starttime = System.currentTimeMillis();
            System.out.println("Processing file "+ path.getPath());
            parser.read(path.getPath());
            long endTime = System.currentTimeMillis();
            System.out.println("Time Taken in millisecond for file "+ path + (endTime - starttime));
        }
        parser.print(parser.visitCount, "visit");
        parser.print(parser.visitorCount, "visitor");
        parser.print(parser.ppvCount, "ppv");
        parser.print(parser.cartAddsCount, "cartAdds");

    }
}
