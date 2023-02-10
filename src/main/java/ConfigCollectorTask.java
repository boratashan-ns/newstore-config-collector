import com.google.gson.*;
import kong.unirest.HttpResponse;
import nwscore.Credential;
import nwscore.NwsContext;
import nwscore.NwsEnvironment;
import nwscore.io.DefaultRestClient;
import nwscore.io.InvalidCredentialsException;
import nwscore.io.RestClient;
import nwscore.io.RestClientException;
import nwscore.utils.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

public class ConfigCollectorTask implements TaskRunnable {
    private SingleTaskExecutor executor;
    private String inputFolder;

    private static final int DEFAULT_SLEEP = 1000;

    private String activeTaskUuid;
    private boolean isInProgress  = false;
    private NwsContext context;
    private RestClient restClient;
    private String inputFile;
    private String outputFolder;

    private Gson gson;

    private JsonElement singleFileElem;
    private final boolean isSingleOutput;
    private String singleOutFileName;

    List<CSVRecord> listOfRecords;

    public ConfigCollectorTask(ApplicationParams params) {
        String username = params.getUsername();
        String password = params.getPassword();
        String tenant = params.getTenant();
        String environment = params.getEnvironment();
        singleOutFileName = params.getSingleFileName();
        this.inputFile = params.getInputFile();
        this.outputFolder = params.getOutputFolder();
        this.context = NwsContext.Builder
                .start()
                .withCredentials(new Credential(username, password))
                .withTenant(tenant)
                .withEnvironment(NwsEnvironment.fromString(environment))
                .withNwsUrl("newstore.net")
                .build();
        this.restClient = new DefaultRestClient(context);
        gson = new GsonBuilder().setPrettyPrinting().create();
        executor = new SingleTaskExecutor();
        isSingleOutput = params.isSingleOutput();
        if (params.isSingleOutput()) {
            singleFileElem = new JsonArray();
        }

    }

    @Override
    public boolean isFinished() {
        return executor.isFinished();
    }

    @Override
    public void interrupt() {
        executor.interrupt();
    }

    @Override
    public void join() {
        executor.join();
    }


    @Override
    public void run() {
        executor.execute(status -> {
            ConsoleUtils.printlnLine();
            ConsoleUtils.println("Start collecting configurations");
            try {
                 boolean keepRunning = true;
                 listInputCsv(this.inputFile);
                 int totalNrOfLines = listOfRecords.size() - 1;
                 ConsoleUtils.println(String.format("Numbers of configurations to be collected %d", totalNrOfLines));
                 for (int i = 1; i<listOfRecords.size(); i++) {
                         if (status.isInterrupted()) {
                             break;
                         }
                         this.collectSingleConfiguration(listOfRecords.get(i));
                         Thread.sleep(DEFAULT_SLEEP);
                 }
                 if (isSingleOutput) {
                     File outFile = new File(IOUtils.joinPaths(this.outputFolder, this.singleOutFileName));
                     FileWriter fw = new FileWriter(outFile);
                     this.gson.toJson(singleFileElem, fw);
                     fw.flush();
                 }
            } catch (Exception e)   {
                ConsoleUtils.printLnException(e);
                ConsoleUtils.println("EXITING, please check error message!");
            }
        });
    }


    public void listInputCsv(String csvFileName) throws IOException {
        Reader in = new FileReader(csvFileName);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        listOfRecords = new ArrayList<>();
        for (CSVRecord record : records) {
            listOfRecords.add(record);
        }
    }


public Optional<HttpResponse<String>> collectSingleConfiguration(CSVRecord record) {
    try {
        String id = record.get(0);
        String name = record.get(1);
        String path = record.get(2);
        String method = record.get(3);
        org.apache.commons.io.FileUtils.forceMkdir(new File(this.outputFolder));
        HttpResponse<String> response = this.restClient.get(path, null, null);
        if (response.isSuccess()) {
            File outFile = new File(IOUtils.joinPaths(this.outputFolder, name+".json"));
            String result = response.getBody();
            JsonElement  element =  JsonParser.parseString(result);
            if (this.isSingleOutput) {
                JsonObject object = new JsonObject();
                object.add(name, element);
             singleFileElem.getAsJsonArray().add(object);
            } else {
                FileWriter fw = new FileWriter(outFile);
                this.gson.toJson(element, fw);
                fw.flush();
            }
        }
        else {
            ConsoleUtils.println(response.getStatusText());
        }


        return Optional.of(response);
    } catch (RestClientException e) {
        ConsoleUtils.printLnException(e);
    } catch (InvalidCredentialsException e) {
        ConsoleUtils.printLnException(e);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return Optional.empty();
}






}
