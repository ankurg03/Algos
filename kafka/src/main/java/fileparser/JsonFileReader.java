package fileparser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

public class JsonFileReader {
    final ObjectMapper mapper;
    HashSet<String> visit;
    HashMap<String,Long> visitCount;
    HashSet<String> visitor;
    HashMap<String,Long> visitorCount;

    HashSet<String> ppv;
    HashMap<String,Long> ppvCount;

    HashSet<String> cartAdds;
    HashMap<String,Long> cartAddsCount;

    BufferedWriter buffWriter;
    int failedInParse =0;

    public JsonFileReader(String outputPath) throws IOException {
        File f = new File(outputPath);
        if (f.exists()) throw new RuntimeException("File Exist: " +outputPath);

        mapper = new ObjectMapper();

        visit = new HashSet();
        visitCount = new HashMap<>();

        visitor = new HashSet();
        visitorCount = new HashMap<>();

        ppv = new HashSet();
        ppvCount = new HashMap<>();

        cartAdds = new HashSet<>();
        cartAddsCount = new HashMap<>();

        buffWriter = new BufferedWriter(new FileWriter(outputPath));
    }

    public void parserJson(String line) throws JsonProcessingException {

        JsonNode tree = mapper.readTree(line);

        if (!visit.contains(tree.get("bu").toString() + "_" + tree.get("visit_ids"))) {
            visit.add(tree.get("bu").toString() + "_" + tree.get("visit_ids"));
            visitCount.merge(tree.get("bu").toString(), 1L, (a, b) -> a+b);
        }

        if (!visitor.contains(tree.get("bu").toString() + "_" + tree.get("dev_ids"))) {
            visitor.add(tree.get("bu").toString() + "_" + tree.get("dev_ids"));
            visitorCount.merge(tree.get("bu").toString(), 1L, (a, b) -> a+b);
        }

        if (!ppv.contains(tree.get("bu").toString() + "_" + tree.get("fids"))) {
            ppv.add(tree.get("bu").toString() + "_" + tree.get("fids"));
            ppvCount.merge(tree.get("bu").toString(), 1L, (a, b) -> a+b);
        }

        if (!cartAdds.contains(tree.get("bu").toString() + "_" + tree.get("cart_fids"))) {
            cartAdds.add(tree.get("bu").toString() + "_" + tree.get("cart_fids"));
            cartAddsCount.merge(tree.get("bu").toString(), 1L, (a, b) -> a+b);
        }


    }

    public void read(String filePath) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.filter(JsonFileReader::lineFilter).forEach(line -> {
                try {
                    parserJson(line);
                } catch (JsonProcessingException e) {
                    failedInParse++;
                }
            });

        }
    }

    private void print(Map<String, Long>map, String name) throws IOException {

        AtomicLong total = new AtomicLong();

        map.forEach((a, b)-> {
            total.addAndGet(b);
            try {
                buffWriter.write((name + "_"+a +" "+b) + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        buffWriter.flush();

        System.out.println("unique count"+total.get());

    }
    private static boolean lineFilter(String line) {
        return true;
    }

    public static void main(String[] args) throws IOException {

        if (args.length <2) {
            args = new String[2];
            args[0] = "/tmp/test";
            args[1] = "/tmp/thetasketchres2";
        }

        String input = args[0];
        String output = args[1];
        JsonFileReader parser = new JsonFileReader(output);

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
