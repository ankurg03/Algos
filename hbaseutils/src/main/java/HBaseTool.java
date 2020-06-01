import com.yahoo.memory.Memory;
import com.yahoo.sketches.theta.CompactSketch;
import com.yahoo.sketches.theta.PairwiseSetOperations;
import com.yahoo.sketches.theta.Sketch;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.HashMap;

public class HBaseTool {
    public static void main(String[] args) throws IOException {
        HBaseTool hbaseUtils = new HBaseTool();
        Configuration conf = hbaseUtils.getConfiguration();
        Connection connection = hbaseUtils.getConnection(conf);
        hbaseUtils.performOperation(connection);
    }

    private Configuration getConfiguration() {
        Configuration conf = new Configuration();
        conf.set("hbase.client.operation.timeout", "50000");
        conf.set("hbase.client.pause","1000");
        conf.set("hbase.client.retries.number","10");
        conf.set("hbase.client.scanner.timeout.period","50000");
        conf.set("hbase.master.port","60000");
        conf.set("hbase.rpc.timeout","50000");
        conf.set("hbase.security.authorization","false");
        conf.set("hbase.zookeeper.property.clientPort","2181");
        conf.set("hbase.zookeeper.quorum","prod-fstream-hbased-zkjn-05,prod-fstream-hbased-zkjn-06,prod-fstream-hbased-zkjn-07,prod-fstream-hbased-zkjn-08,prod-fstream-hbased-zkjn-09");
//        conf.set("hbase.zookeeper.quorum","stage-fstream-zk-jn-0001,stage-fstream-zk-jn-0002,stage-fstream-zk-jn-0003");

        conf.set("zookeeper.client.sasl","false");
        conf.set("zookeeper.connection.timeout.ms","50000");
        conf.set("zookeeper.recovery.retry","5");
        conf.set("zookeeper.recovery.retry.intervalmill","1000");
        conf.set("zookeeper.sasl.client","false");
        conf.set("zookeeper.session.timeout","10000");
        conf.set("zookeeper.znode.parent","/hbase-unsecure");
        return HBaseConfiguration.create(conf);
    }

    private Connection getConnection(Configuration conf) throws IOException {
        return ConnectionFactory.createConnection(conf);
    }

    private void performOperation(Connection connection) throws IOException {
//        getTableList(connection);

//        getSizeOfAllRows(connection, "probabilistic_data_tables:test.cdm_traffic.agg_master_daily_202002");
        getCount(connection, "probabilistic_data_tables:cdm_traffic.v2_agg_test_daily_202005");
//        getCount(connection, "probabilistic_data_tables:cdm_traffic.agg_master_daily_202005");

//        getSize(connection, "setdata.test.cdm.agg_mobile_model_daily_201910", "20191001_f28aaf94973d0e628711ffb1c996c70b0405b32ee7dd5a5f110fb431079f0714_46d3fab5842ac0cfe60360625733c7a5e489ffc42268cb5e28bf180cf7911ff8_20ef0f0c8d0eea98772412cea9b3b92612e3e53cb5e59152b5703165f56e8a53");
    }


    private void getTableList(Connection connection) throws IOException {
        Admin ad = connection.getAdmin();
        HTableDescriptor[] tDescriptor = ad.listTables();

        for (int k=0; k<tDescriptor.length; k++ ){
            System.out.println(tDescriptor[k].getNameAsString());

        }
    }

    //"a9f13fe76d87c628cc06cb05e8ba8a2ddc915d1c0a1bece7895653025d711626"

    private void getCount(Connection connection, String tableName) throws IOException {
        Scan scan = new Scan();
        HashMap<String, CompactSketch> hashMap = new HashMap<>();

        Table table = connection.getTable(TableName.valueOf(tableName));
        ResultScanner scanner = table.getScanner(scan);

        for (Result rr : scanner) {

            if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("visit_ids")) ) {
                if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("platform")) &&
                        Bytes.toString(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("platform"))) == "APP"){
                    String key = "APP_visit";
                    if(!hashMap.containsKey(key)) {
                        hashMap.put(key, Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("visit_ids")))).compact());
                    }
                    else
                        hashMap.put(key, PairwiseSetOperations.union(hashMap.get(key),Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("visit_ids")))).compact()));
                }

                if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("bu"))) {
                    String key = "visit_" + Bytes.toString(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("bu")));
                    if (!hashMap.containsKey(key)) {
                        hashMap.put(key, Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("visit_ids")))).compact());
                    } else
                        hashMap.put(key, PairwiseSetOperations.union(hashMap.get(key), Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("visit_ids")))).compact()));
                }
            }

            if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("dev_ids"))) {
                if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("platform")) &&
                        Bytes.toString(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("platform"))) == "APP") {
                    String key = "APP_visitor";
                    if(!hashMap.containsKey(key)) {
                        hashMap.put(key, Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("dev_ids")))).compact());
                    }
                    else
                        hashMap.put(key, PairwiseSetOperations.union(hashMap.get(key),Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("dev_ids")))).compact()));
                }
                if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("bu"))) {
                    String key = "visitor_" + Bytes.toString(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("bu")));
                    if (!hashMap.containsKey(key)) {
                        hashMap.put(key, Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("dev_ids")))).compact());
                    } else
                        hashMap.put(key, PairwiseSetOperations.union(hashMap.get(key), Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("dev_ids")))).compact()));
                }

            }
            if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("fids"))) {
                if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("platform")) &&
                        Bytes.toString(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("platform"))) == "APP") {
                    String key = "APP_ppv";
                    if(!hashMap.containsKey(key)) {
                        hashMap.put(key, Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("fids")))).compact());
                    }
                    else
                        hashMap.put(key, PairwiseSetOperations.union(hashMap.get(key),Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("fids")))).compact()));
                }
                if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("bu"))) {
                    String key = "ppv_" + Bytes.toString(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("bu")));
                    if (!hashMap.containsKey(key)) {
                        hashMap.put(key, Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("fids")))).compact());
                    } else
                        hashMap.put(key, PairwiseSetOperations.union(hashMap.get(key), Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("fids")))).compact()));
                }
            }
            if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("cart_fids"))) {
                if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("platform")) &&
                        Bytes.toString(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("platform"))) == "APP") {
                    String key = "APP_cartAdd";
                    if(!hashMap.containsKey(key)) {
                        hashMap.put(key, Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("cart_fids")))).compact());
                    }
                    else
                        hashMap.put(key, PairwiseSetOperations.union(hashMap.get(key),Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("cart_fids")))).compact()));
                }
                if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("bu"))) {
                    String key = "cartAdd_" + Bytes.toString(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("bu")));
                    if (!hashMap.containsKey(key)) {
                        hashMap.put(key, Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("cart_fids")))).compact());
                    } else
                        hashMap.put(key, PairwiseSetOperations.union(hashMap.get(key), Sketch.wrap(Memory.wrap(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("cart_fids")))).compact()));
                }
            }
        }

        hashMap.forEach((k,v) ->
                System.out.println(k + " "+ v.getEstimate()));

        System.out.println("APP counts");
        hashMap.forEach((k,v) -> {
            if (k.startsWith("cartAdd_")) {
                System.out.println(k + " " + PairwiseSetOperations.intersect(hashMap.get("APP_cartAdd"), v).getEstimate());
            }
            if (k.startsWith("ppv_")) {
                System.out.println(k + " " + PairwiseSetOperations.intersect(hashMap.get("APP_ppv"), v).getEstimate());
            }
            if (k.startsWith("visitor_")) {
                System.out.println(k + " " + PairwiseSetOperations.intersect(hashMap.get("APP_visitor"), v).getEstimate());
            }
            if (k.startsWith("visit_")) {
                System.out.println(k + " " + PairwiseSetOperations.intersect(hashMap.get("APP_visit"), v).getEstimate());
            }

        });
        System.out.println("TOTAL");
        System.out.println("Total App cart" + hashMap.get("APP_cartAdd").getEstimate());
        System.out.println("Total App ppv" + hashMap.get("APP_ppv").getEstimate());
        System.out.println("Total App visit" + hashMap.get("APP_visit").getEstimate());
        System.out.println("Total App visitor" + hashMap.get("APP_visitor").getEstimate());
    }


    private void getSizeOfAllRows(Connection connection, String tableName) throws IOException {
        Scan scan = new Scan();
//        scan.setFilter(new FirstKeyOnlyFilter());
        Table table = connection.getTable(TableName.valueOf(tableName));
        ResultScanner scanner = table.getScanner(scan);

        for (Result rr : scanner) {

            if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("visit_ids"))) {
                if (rr.getValue(Bytes.toBytes("cf"),Bytes.toBytes("visit_ids")).length > 9000) {
                    System.out.print(Bytes.toString(rr.getRow()));
                    System.out.print("-");
                    System.out.print(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("visit_ids")).length);
                }
            }
            if (rr.getFamilyMap(Bytes.toBytes("cf")).containsKey(Bytes.toBytes("dev_ids"))) {
                if (rr.getValue(Bytes.toBytes("cf"),Bytes.toBytes("visit_ids")).length > 9000) {
                    System.out.print("-");
                    System.out.print(rr.getValue(Bytes.toBytes("cf"), Bytes.toBytes("dev_ids")).length);
                }
            }
            if (rr.getValue(Bytes.toBytes("cf"),Bytes.toBytes("visit_ids")).length > 9000) {
                System.out.println();
            }

        }
    }

    private void getSize(Connection connection, String tableName, String row) throws IOException {

        Table table = connection.getTable(TableName.valueOf(tableName));



        Get g = new Get(Bytes.toBytes(row));
        Result result = table.get(g);

        byte [] value = result.getValue(Bytes.toBytes("cf"),Bytes.toBytes("visits"));
        System.out.println("size " + value.length);
    }
}

