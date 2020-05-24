package kafkaclient;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class KafkaConsumerExample {

    private final static String TOPIC = "TrafficMasterV2KafkaSinkTest2";
    private final static String BOOTSTRAP_SERVERS =
            "10.34.29.76:9092,10.33.18.178:9092,10.33.51.147:9092";
    private static String  FILE_PATH = "/grid/vdb/ankur/payload_agg_";
    static Sink sinkType = Sink.PRINT;



    private static BufferedWriter buffWriter;
    private static boolean endOffsetReached = false;
    private static boolean [] endOffsetOfPartitions = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,
            false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};


///* 1
    static long [] startOffsets = {3049027, 3049034, 3049039, 3049030, 3049041, 3049030, 3049030, 3049030, 3049023, 3049027, 3049030, 3049021, 3049029, 3049039, 3049032, 3049035, 3049027, 3049023, 3049017, 3049023, 3049036, 3049039, 3049042, 3049025, 3049033, 3049040, 3049025, 3049020, 3049038, 3049031};
    static long [] endOffsets =   {3649027,3649034,3649039,3649030,3649041,3649030,3649030,3649030,3649023,3649027,3649030,3649021,3649029,3649039,3649032,3649035,3649027,3649023,3649017,3649023,3649036,3649039,3649042,3649025,3649033,3649040,3649025,3649020,3649038,3649031};
    private static int got =0;
    private static long recordCount =0;
//    missing 14 records



    public static void updateOffsets(int runId) {

        switch (runId) {
            case 2:
                long [] startOffsets = {3649028, 3649035, 3649040, 3649031, 3649042, 3649031, 3649031, 3649031, 3649024, 3649028, 3649031, 3649022, 3649030, 3649040, 3649033, 3649036, 3649028, 3649024, 3649018, 3649024, 3649037, 3649040, 3649043, 3649026, 3649034, 3649041, 3649026, 3649021, 3649039, 3649032};
                long [] endOffsets =   {4249028, 4249035, 4249040, 4249031, 4249042, 4249031, 4249031, 4249031, 4249024, 4249028, 4249031, 4249022, 4249030, 4249040, 4249033, 4249036, 4249028, 4249024, 4249018, 4249024, 4249037, 4249040, 4249043, 4249026, 4249034, 4249041, 4249026, 4249021, 4249039, 4249032};
                updateStartEndOffset(startOffsets, endOffsets);
                break;
            case 3:
                long [] startOffsets3 = {4249029, 4249036, 4249041, 4249032, 4249043, 4249032, 4249032, 4249032, 4249025, 4249029, 4249032, 4249023, 4249031, 4249041, 4249034, 4249037, 4249029, 4249025, 4249019, 4249025, 4249038, 4249041, 4249044, 4249027, 4249035, 4249042, 4249027, 4249022, 4249040, 4249033};
                long [] endOffsets3 =  {4849028, 4849035, 4849040, 4849031, 4849042, 4849031, 4849031, 4849031, 4849024, 4849028, 4849031, 4849022, 4849030, 4849040, 4849033, 4849036, 4849028, 4849024, 4849018, 4849024, 4849037, 4849040, 4849043, 4849026, 4849034, 4849041, 4849026, 4849021, 4849039, 4849032};
                updateStartEndOffset(startOffsets3, endOffsets3);
                break;
            case 4:
                long [] startOffsets4 = {4849029, 4849036, 4849041, 4849032, 4849043, 4849032, 4849032, 4849032, 4849025, 4849029, 4849032, 4849023, 4849031, 4849041, 4849034, 4849037, 4849029, 4849025, 4849019, 4849025, 4849038, 4849041, 4849044, 4849027, 4849035, 4849042, 4849027, 4849022, 4849040, 4849033};
                long [] endOffsets4 =  {5449028, 5449035, 5449040, 5449031, 5449042, 5449031, 5449031, 5449031, 5449024, 5449028, 5449031, 5449022, 5449030, 5449040, 5449033, 5449036, 5449028, 5449024, 5449018, 5449024, 5449037, 5449040, 5449043, 5449026, 5449034, 5449041, 5449026, 5449021, 5449039, 5449032};
                updateStartEndOffset(startOffsets4, endOffsets4);
                break;
            case 5:
                long [] startOffsets5 = {5449029, 5449036, 5449041, 5449032, 5449043, 5449032, 5449032, 5449032, 5449025, 5449029, 5449032, 5449023, 5449031, 5449041, 5449034, 5449037, 5449029, 5449025, 5449019, 5449025, 5449038, 5449041, 5449044, 5449027, 5449035, 5449042, 5449027, 5449022, 5449040, 5449033};
                long [] endOffsets5 =  {6049028, 6049035, 6049040, 6049031, 6049042, 6049031, 6049031, 6049031, 6049024, 6049028, 6049031, 6049022, 6049030, 6049040, 6049033, 6049036, 6049028, 6049024, 6049018, 6049024, 6049037, 6049040, 6049043, 6049026, 6049034, 6049041, 6049026, 6049021, 6049039, 6049032};
                updateStartEndOffset(startOffsets5, endOffsets5);
                break;
            case 6:
                long [] startOffsets6 = {6049029, 6049036, 6049041, 6049032, 6049043, 6049032, 6049032, 6049032, 6049025, 6049029, 6049032, 6049023, 6049031, 6049041, 6049034, 6049037, 6049029, 6049025, 6049019, 6049025, 6049038, 6049041, 6049044, 6049027, 6049035, 6049042, 6049027, 6049022, 6049040, 6049033};
                long [] endOffsets6 =  {6649028, 6649035, 6649040, 6649031, 6649042, 6649031, 6649031, 6649031, 6649024, 6649028, 6649031, 6649022, 6649030, 6649040, 6649033, 6649036, 6649028, 6649024, 6649018, 6649024, 6649037, 6649040, 6649043, 6649026, 6649034, 6649041, 6649026, 6649021, 6649039, 6649032};
                updateStartEndOffset(startOffsets6, endOffsets6);
                break;
            case 7:
                long [] startOffsets7 = {6649029, 6649036, 6649041, 6649032, 6649043, 6649032, 6649032, 6649032, 6649025, 6649029, 6649032, 6649023, 6649031, 6649041, 6649034, 6649037, 6649029, 6649025, 6649019, 6649025, 6649038, 6649041, 6649044, 6649027, 6649035, 6649042, 6649027, 6649022, 6649040, 6649033};
                long [] endOffsets7 =  {7249028, 7249035, 7249040, 7249031, 7249042, 7249031, 7249031, 7249031, 7249024, 7249028, 7249031, 7249022, 7249030, 7249040, 7249033, 7249036, 7249028, 7249024, 7249018, 7249024, 7249037, 7249040, 7249043, 7249026, 7249034, 7249041, 7249026, 7249021, 7249039, 7249032};
                updateStartEndOffset(startOffsets7, endOffsets7);
                break;
            case 8:
                long [] startOffsets8 = {7249029, 7249036, 7249041, 7249032, 7249043, 7249032, 7249032, 7249032, 7249025, 7249029, 7249032, 7249023, 7249031, 7249041, 7249034, 7249037, 7249029, 7249025, 7249019, 7249025, 7249038, 7249041, 7249044, 7249027, 7249035, 7249042, 7249027, 7249022, 7249040, 7249033};
                long [] endOffsets8 =  {7849028, 7849035, 7849040, 7849031, 7849042, 7849031, 7849031, 7849031, 7849024, 7849028, 7849031, 7849022, 7849030, 7849040, 7849033, 7849036, 7849028, 7849024, 7849018, 7849024, 7849037, 7849040, 7849043, 7849026, 7849034, 7849041, 7849026, 7849021, 7849039, 7849032};
                updateStartEndOffset(startOffsets8, endOffsets8);
                break;
            case 9:
                long [] startOffsets9 = {7849029, 7849036, 7849041, 7849032, 7849043, 7849032, 7849032, 7849032, 7849025, 7849029, 7849032, 7849023, 7849031, 7849041, 7849034, 7849037, 7849029, 7849025, 7849019, 7849025, 7849038, 7849041, 7849044, 7849027, 7849035, 7849042, 7849027, 7849022, 7849040, 7849033};
                long [] endOffsets9 =  {8449028, 8449035, 8449040, 8449031, 8449042, 8449031, 8449031, 8449031, 8449024, 8449028, 8449031, 8449022, 8449030, 8449040, 8449033, 8449036, 8449028, 8449024, 8449018, 8449024, 8449037, 8449040, 8449043, 8449026, 8449034, 8449041, 8449026, 8449021, 8449039, 8449032};
                updateStartEndOffset(startOffsets9, endOffsets9);
                break;
            case 10:
                long [] startOffsets10 = {8449029, 8449036, 8449041, 8449032, 8449043, 8449032, 8449032, 8449032, 8449025, 8449029, 8449032, 8449023, 8449031, 8449041, 8449034, 8449037, 8449029, 8449025, 8449019, 8449025, 8449038, 8449041, 8449044, 8449027, 8449035, 8449042, 8449027, 8449022, 8449040, 8449033};
                long [] endOffsets10 =  {9049028, 9049035, 9049040, 9049031, 9049042, 9049031, 9049031, 9049031, 9049024, 9049028, 9049031, 9049022, 9049030, 9049040, 9049033, 9049036, 9049028, 9049024, 9049018, 9049024, 9049037, 9049040, 9049043, 9049026, 9049034, 9049041, 9049026, 9049021, 9049039, 9049032};
                updateStartEndOffset(startOffsets10, endOffsets10);
                break;
            case 11:
                long [] startOffsets11 = {9049029, 9049036, 9049041, 9049032, 9049043, 9049032, 9049032, 9049032, 9049025, 9049029, 9049032, 9049023, 9049031, 9049041, 9049034, 9049037, 9049029, 9049025, 9049019, 9049025, 9049038, 9049041, 9049044, 9049027, 9049035, 9049042, 9049027, 9049022, 9049040, 9049033};
                long [] endOffsets11 =  {9649028, 9649035, 9649040, 9649031, 9649042, 9649031, 9649031, 9649031, 9649024, 9649028, 9649031, 9649022, 9649030, 9649040, 9649033, 9649036, 9649028, 9649024, 9649018, 9649024, 9649037, 9649040, 9649043, 9649026, 9649034, 9649041, 9649026, 9649021, 9649039, 9649032};
                updateStartEndOffset(startOffsets11, endOffsets11);
                break;
            case 12:
                long [] startOffsets12 = {9649029, 9649036, 9649041, 9649032, 9649043, 9649032, 9649032, 9649032, 9649025, 9649029, 9649032, 9649023, 9649031, 9649041, 9649034, 9649037, 9649029, 9649025, 9649019, 9649025, 9649038, 9649041, 9649044, 9649027, 9649035, 9649042, 9649027, 9649022, 9649040, 9649033};
                long [] endOffsets12 =  {10249028, 10249035, 10249040, 10249031, 10249042, 10249031, 10249031, 10249031, 10249024, 10249028, 10249031, 10249022, 10249030, 10249040, 10249033, 10249036, 10249028, 10249024, 10249018, 10249024, 10249037, 10249040, 10249043, 10249026, 10249034, 10249041, 10249026, 10249021, 10249039, 10249032};
                updateStartEndOffset(startOffsets12, endOffsets12);
                break;
            case 13:
                long [] startOffsets13 = {10249029, 10249036, 10249041, 10249032, 10249043, 10249032, 10249032, 10249032, 10249025, 10249029, 10249032, 10249023, 10249031, 10249041, 10249034, 10249037, 10249029, 10249025, 10249019, 10249025, 10249038, 10249041, 10249044, 10249027, 10249035, 10249042, 10249027, 10249022, 10249040, 10249033};
                long [] endOffsets13 =  {10849028, 10849035, 10849040, 10849031, 10849042, 10849031, 10849031, 10849031, 10849024, 10849028, 10849031, 10849022, 10849030, 10849040, 10849033, 10849036, 10849028, 10849024, 10849018, 10849024, 10849037, 10849040, 10849043, 10849026, 10849034, 10849041, 10849026, 10849021, 10849039, 10849032};
                updateStartEndOffset(startOffsets13, endOffsets13);
                break;
            case 14:
                long [] startOffsets14 = {10849029, 10849036, 10849041, 10849032, 10849043, 10849032, 10849032, 10849032, 10849025, 10849029, 10849032, 10849023, 10849031, 10849041, 10849034, 10849037, 10849029, 10849025, 10849019, 10849025, 10849038, 10849041, 10849044, 10849027, 10849035, 10849042, 10849027, 10849022, 10849040, 10849033};
                long [] endOffsets14 =  {11449028, 11449035, 11449040, 11449031, 11449042, 11449031, 11449031, 11449031, 11449024, 11449028, 11449031, 11449022, 11449030, 11449040, 11449033, 11449036, 11449028, 11449024, 11449018, 11449024, 11449037, 11449040, 11449043, 11449026, 11449034, 11449041, 11449026, 11449021, 11449039, 11449032};
                updateStartEndOffset(startOffsets14, endOffsets14);
                break;
            case 15:
                long [] startOffsets15 = {11449029, 11449036, 11449041, 11449032, 11449043, 11449032, 11449032, 11449032, 11449025, 11449029, 11449032, 11449023, 11449031, 11449041, 11449034, 11449037, 11449029, 11449025, 11449019, 11449025, 11449038, 11449041, 11449044, 11449027, 11449035, 11449042, 11449027, 11449022, 11449040, 11449033};
                long [] endOffsets15 =  {12049028, 12049035, 12049040, 12049031, 12049042, 12049031, 12049031, 12049031, 12049024, 12049028, 12049031, 12049022, 12049030, 12049040, 12049033, 12049036, 12049028, 12049024, 12049018, 12049024, 12049037, 12049040, 12049043, 12049026, 12049034, 12049041, 12049026, 12049021, 12049039, 12049032};
                updateStartEndOffset(startOffsets15, endOffsets15);
                break;
            case 16:
                long [] startOffsets16 = {12049029, 12049036, 12049041, 12049032, 12049043, 12049032, 12049032, 12049032, 12049025, 12049029, 12049032, 12049023, 12049031, 12049041, 12049034, 12049037, 12049029, 12049025, 12049019, 12049025, 12049038, 12049041, 12049044, 12049027, 12049035, 12049042, 12049027, 12049022, 12049040, 12049033};
                long [] endOffsets16 =  {12649028, 12649035, 12649040, 12649031, 12649042, 12649031, 12649031, 12649031, 12649024, 12649028, 12649031, 12649022, 12649030, 12649040, 12649033, 12649036, 12649028, 12649024, 12649018, 12649024, 12649037, 12649040, 12649043, 12649026, 12649034, 12649041, 12649026, 12649021, 12649039, 12649032};
                updateStartEndOffset(startOffsets16, endOffsets16);
                break;
            case 17:
                long [] startOffsets17 = {12649029, 12649036, 12649041, 12649032, 12649043, 12649032, 12649032, 12649032, 12649025, 12649029, 12649032, 12649023, 12649031, 12649041, 12649034, 12649037, 12649029, 12649025, 12649019, 12649025, 12649038, 12649041, 12649044, 12649027, 12649035, 12649042, 12649027, 12649022, 12649040, 12649033};
                long [] endOffsets17 =  {13249028, 13249035, 13249040, 13249031, 13249042, 13249031, 13249031, 13249031, 13249024, 13249028, 13249031, 13249022, 13249030, 13249040, 13249033, 13249036, 13249028, 13249024, 13249018, 13249024, 13249037, 13249040, 13249043, 13249026, 13249034, 13249041, 13249026, 13249021, 13249039, 13249032};
                updateStartEndOffset(startOffsets17, endOffsets17);
                break;
            case 18:
                long [] startOffsets18 = {13249029, 13249036, 13249041, 13249032, 13249043, 13249032, 13249032, 13249032, 13249025, 13249029, 13249032, 13249023, 13249031, 13249041, 13249034, 13249037, 13249029, 13249025, 13249019, 13249025, 13249038, 13249041, 13249044, 13249027, 13249035, 13249042, 13249027, 13249022, 13249040, 13249033};
                long [] endOffsets18 =  {13849028, 13849035, 13849040, 13849031, 13849042, 13849031, 13849031, 13849031, 13849024, 13849028, 13849031, 13849022, 13849030, 13849040, 13849033, 13849036, 13849028, 13849024, 13849018, 13849024, 13849037, 13849040, 13849043, 13849026, 13849034, 13849041, 13849026, 13849021, 13849039, 13849032};
                updateStartEndOffset(startOffsets18, endOffsets18);
                break;
            case 19:
                long [] startOffsets19 = {13849029, 13849036, 13849041, 13849032, 13849043, 13849032, 13849032, 13849032, 13849025, 13849029, 13849032, 13849023, 13849031, 13849041, 13849034, 13849037, 13849029, 13849025, 13849019, 13849025, 13849038, 13849041, 13849044, 13849027, 13849035, 13849042, 13849027, 13849022, 13849040, 13849033};
                long [] endOffsets19 =  {14449028, 14449035, 14449040, 14449031, 14449042, 14449031, 14449031, 14449031, 14449024, 14449028, 14449031, 14449022, 14449030, 14449040, 14449033, 14449036, 14449028, 14449024, 14449018, 14449024, 14449037, 14449040, 14449043, 14449026, 14449034, 14449041, 14449026, 14449021, 14449039, 14449032};
                updateStartEndOffset(startOffsets19, endOffsets19);
                break;
            case 20:
                long [] startOffsets20 = {14449029, 14449036, 14449041, 14449032, 14449043, 14449032, 14449032, 14449032, 14449025, 14449029, 14449032, 14449023, 14449031, 14449041, 14449034, 14449037, 14449029, 14449025, 14449019, 14449025, 14449038, 14449041, 14449044, 14449027, 14449035, 14449042, 14449027, 14449022, 14449040, 14449033};
                long [] endOffsets20 =  {15049028, 15049035, 15049040, 15049031, 15049042, 15049031, 15049031, 15049031, 15049024, 15049028, 15049031, 15049022, 15049030, 15049040, 15049033, 15049036, 15049028, 15049024, 15049018, 15049024, 15049037, 15049040, 15049043, 15049026, 15049034, 15049041, 15049026, 15049021, 15049039, 15049032};
                updateStartEndOffset(startOffsets20, endOffsets20);
                break;
            case 21:
                long [] startOffsets21 = {15049029, 15049036, 15049041, 15049032, 15049043, 15049032, 15049032, 15049032, 15049025, 15049029, 15049032, 15049023, 15049031, 15049041, 15049034, 15049037, 15049029, 15049025, 15049019, 15049025, 15049038, 15049041, 15049044, 15049027, 15049035, 15049042, 15049027, 15049022, 15049040, 15049033};
                long [] endOffsets21 =  {15649028, 15649035, 15649040, 15649031, 15649042, 15649031, 15649031, 15649031, 15649024, 15649028, 15649031, 15649022, 15649030, 15649040, 15649033, 15649036, 15649028, 15649024, 15649018, 15649024, 15649037, 15649040, 15649043, 15649026, 15649034, 15649041, 15649026, 15649021, 15649039, 15649032};
                updateStartEndOffset(startOffsets21, endOffsets21);
                break;
            case 22:
                long [] startOffsets22 = {15649029, 15649036, 15649041, 15649032, 15649043, 15649032, 15649032, 15649032, 15649025, 15649029, 15649032, 15649023, 15649031, 15649041, 15649034, 15649037, 15649029, 15649025, 15649019, 15649025, 15649038, 15649041, 15649044, 15649027, 15649035, 15649042, 15649027, 15649022, 15649040, 15649033};
                long [] endOffsets22 =  {16249028, 16249035, 16249040, 16249031, 16249042, 16249031, 16249031, 16249031, 16249024, 16249028, 16249031, 16249022, 16249030, 16249040, 16249033, 16249036, 16249028, 16249024, 16249018, 16249024, 16249037, 16249040, 16249043, 16249026, 16249034, 16249041, 16249026, 16249021, 16249039, 16249032};
                updateStartEndOffset(startOffsets22, endOffsets22);
                break;
            case 23:
                long [] startOffsets23 = {16249029, 16249036, 16249041, 16249032, 16249043, 16249032, 16249032, 16249032, 16249025, 16249029, 16249032, 16249023, 16249031, 16249041, 16249034, 16249037, 16249029, 16249025, 16249019, 16249025, 16249038, 16249041, 16249044, 16249027, 16249035, 16249042, 16249027, 16249022, 16249040, 16249033};
                long [] endOffsets23 =  {16849028, 16849035, 16849040, 16849031, 16849042, 16849031, 16849031, 16849031, 16849024, 16849028, 16849031, 16849022, 16849030, 16849040, 16849033, 16849036, 16849028, 16849024, 16849018, 16849024, 16849037, 16849040, 16849043, 16849026, 16849034, 16849041, 16849026, 16849021, 16849039, 16849032};
                updateStartEndOffset(startOffsets23, endOffsets23);
                break;
            case 24:
                long [] startOffsets24 = {16849029, 16849036, 16849041, 16849032, 16849043, 16849032, 16849032, 16849032, 16849025, 16849029, 16849032, 16849023, 16849031, 16849041, 16849034, 16849037, 16849029, 16849025, 16849019, 16849025, 16849038, 16849041, 16849044, 16849027, 16849035, 16849042, 16849027, 16849022, 16849040, 16849033};
                long [] endOffsets24 =  {17229930, 17229920, 17229946, 17229949, 17228963, 17229958, 17229953, 17229945, 17229933, 17229925, 17229933, 17229951, 17229952, 17229952, 17229956, 17229963, 17229924, 17229941, 17229940, 17229925, 17229906, 17229887, 17229905, 17229926, 17229923, 17229933, 17229924, 17229916, 17229926, 17229930};
                updateStartEndOffset(startOffsets24, endOffsets24);
                break;
                default:
                    throw new RuntimeException("input 3 to 24");

        }

        //final offset
        //static long [] endOffsets =   {17221006, 17221032, 17221022, 17220998, 17221028, 17221015, 17221004, 17221029, 17220997, 17220994, 17221015, 17220036, 17221013, 17221017, 17221013, 17221005, 17221039, 17221024, 17220976, 17221018, 17221028, 17221011, 17221026, 17221008, 17220995, 17221049, 17221022, 17220991, 17221036, 17221033};

    }

    private static void updateStartEndOffset(long[] startOffsetsL, long[] endOffsetsL) {
        for (int i = 0 ; i < startOffsetsL.length; i++){
            startOffsets[i] = startOffsetsL[i];
            endOffsets[i] = endOffsetsL[i];
        }

    }

    private static Consumer<Long, String> createConsumer() {


        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG,
                UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        // Create the consumer using props.
        final Consumer<Long, String> consumer =
                new KafkaConsumer<>(props);
        return consumer;
    }

    static void consume(Consumer<Long, String> consumer) throws IOException {
        final int giveUp = 1000;   int noRecordsCount = 0;
        while (true) {
            final ConsumerRecords<Long, String> consumerRecords =
                    consumer.poll(1000);
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            consumerRecords.forEach(record -> {
                switch (sinkType) {
                    case PRINT:
                        print(record);
                        break;
                    case WRITE_TO_FILE:
                        writeInFile(record);
                        break;
                }
            });
            consumer.commitAsync();

        }
        if (sinkType == Sink.WRITE_TO_FILE) buffWriter.flush();
        consumer.close();
        System.out.println("DONE");
    }

    static void consumFromStartToEndOffset(Consumer<Long, String> consumer) throws InterruptedException, IOException {

        final int giveUp = 1000;   int noRecordsCount = 0;

        while (true) {
            final ConsumerRecords<Long, String> consumerRecords =
                    consumer.poll(1000);
            if (endOffsetReached) break;
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            consumerRecords.forEach(record -> {
                if (record.offset() >= endOffsets[record.partition()]) {
                    endOffsetOfPartitions[record.partition()] = true;
                    endOffsetReached = checkEndPartition();
                }
                else{
                    recordCount++;
                    switch (sinkType) {
                        case PRINT:
                            print(record);
                            break;
                        case WRITE_TO_FILE:
                            writeInFile(record);
                            break;
                    }
                }
            } );
            consumer.commitAsync();

        }

        if (sinkType == Sink.WRITE_TO_FILE) buffWriter.flush();

        consumer.close();
        System.out.println("DONE");
    }

    private static boolean checkEndPartition() {
        boolean endOfPartitionReached = true;

        int numChecks =0;

        for (boolean partition : endOffsetOfPartitions) {

            if(partition) {
                numChecks++;
            }
        }
        if (numChecks == 30) return endOfPartitionReached;

        if (numChecks > 28) {
            if(recordCount > 17999900 ) return endOfPartitionReached;
        }
        return false;
    }

    private static void print(ConsumerRecord<Long, String> record) {
        System.out.println(record.value());
    }

    private static void writeInFile(ConsumerRecord<Long, String> record) {
        try {
            buffWriter.write(record.value() + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String samp = "{\"eventType\":\"DiscoveryContentImpression\",\"ndid\":\"5003007\",\"visit_ids\":\"7b52310807ca5b03dd4e421bb2f1fa94-1589858145637\",\"cart_fids\":null,\"ts_date_hour\":\"2020051908\",\"timestamp\":1589858379283,\"evnt\":\"BAU\",\"mpid\":\"FLIPKART\",\"vert\":\"Handset\",\"sc\":\"Mobile\",\"fm\":\"ORGANIC\",\"mmodel\":\"NA\",\"counit_ids\":null,\"brand\":\"NA\",\"cocmplt_ids\":null,\"fids\":null,\"devid\":\"7b52310807ca5b03dd4e421bb2f1fa94\",\"subcat\":\"Handset\",\"dev_ids\":\"7b52310807ca5b03dd4e421bb2f1fa94\",\"account_id\":\"ACC8CBB40117A3546FB8A684FC0982608E23\",\"bu\":\"Mobile\",\"ns_fids\":null,\"fid_visits\":null,\"mc\":\"DIRECT\",\"coinit_ids\":null,\"lckin\":\"ACTIVE\",\"oos_fids\":null,\"platform\":\"APP\",\"cat\":\"Mobile\",\"pin\":\"600044\",\"zone\":\"NA\",\"fsn\":\"NA\"}";
        JsonNode tree = mapper.readTree(samp);
        System.out.println(tree);
        System.out.println(tree.get("sc"));
    }

    private static void subscribeConsumer(Consumer<Long, String> consumer) {
        consumer.subscribe(Collections.singletonList(TOPIC));
    }

    private static void seekToStartOffset(Consumer<Long, String> consumer) {
        /**
         * Before you can seek() you first need to subscribe() to a topic
         * Also the subscribe() is a lazy operation thus here, making a dummy call to poll
         */
        consumer.poll(1000);
        int partition =0;
        for (long offset : startOffsets) {
            consumer.seek(new TopicPartition(TOPIC, partition), offset);
            partition++;
        }
    }

    enum Sink {
        PRINT,
        WRITE_TO_FILE
    }
    private static void runConsumerForOffsetRange(Consumer<Long, String> consumer) throws IOException, InterruptedException {
        validate();
        if(sinkType==Sink.WRITE_TO_FILE) {
            try {
                buffWriter = new BufferedWriter(new FileWriter(FILE_PATH));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        subscribeConsumer(consumer);
        seekToStartOffset(consumer);
        consumFromStartToEndOffset(consumer);
    }


    private static void validate() {
        File f = new File(FILE_PATH);
        if (f.exists()) throw new RuntimeException("Already ran since file exist " +FILE_PATH);

        if (endOffsets.length == startOffsets.length
                && startOffsets.length == endOffsetOfPartitions.length
                && startOffsets[0] < endOffsets[0]) return;
        throw new RuntimeException("incorrect start/end offset configs");
    }

    private static void runConsumer(Consumer<Long, String> consumer) throws IOException {
        subscribeConsumer(consumer);
        consume(consumer);

    }

    public static void utilGetEndOffset(int add) {
        int i = 0;
        for(long offset : startOffsets) {
            i++;
            if (i!=startOffsets.length)
                System.out.print(offset + add + ", ");
            else
                System.out.print(offset + add );

        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {
//        for(int i =1; i < 23 ;i++) {
//            System.out.println("case "+(i+2)+":");
//            System.out.print("\tlong [] startOffsets"+(i+2)+" = {");
//            utilGetEndOffset((600000*i)+1);
//            System.out.println("};");
//            System.out.print("\tlong [] endOffsets"+(i+2)+" =  {");
//            utilGetEndOffset(600000*(i+1));
//            System.out.println("};");
//            System.out.println("\tupdateStartEndOffset(startOffsets"+(i+2)+", endOffsets"+(i+2)+");");
//            System.out.println("\tbreak;");
//        }

        int runid = Integer.parseInt(args[0]);
        sinkType = Sink.WRITE_TO_FILE;
        updateOffsets(runid);
        FILE_PATH = FILE_PATH + startOffsets[0] + "_"+endOffsets[0];
        Consumer<Long, String> consumer = createConsumer();
        runConsumerForOffsetRange(consumer);

    }


}
