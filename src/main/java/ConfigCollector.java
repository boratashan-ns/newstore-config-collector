import org.apache.commons.cli.*;
import nwscore.utils.ConsoleUtils;
import nwscore.utils.TaskRunnable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.*;



public class ConfigCollector {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final int EXITCODE_SUCCESS = 0;
    private static final int EXITCODE_ARGUMENT_ERROR = 1;
    private static final int EXITCODE_FILE_NOT_FOUND = 2;
    private static final int EXITCODE_USER_INTERRUPTED = 3;


    public static void main(String[] args) throws IOException, ParseException {

        ConsoleUtils.startRecording();

        // create Options object
        Options options = new Options();
        /*
        Options pattern  -tenant -environment -username -password -inputfile -outputfolder
         */

        // add t option
        options.addOption("help", false, "Print help");
        options.addOption("tenant", true, "Tenant");
        options.addOption("env", true, "Environment");
        options.addOption("u", true, "Username");
        options.addOption("p", true, "Password");
        options.addOption("in", true, "Input file");
        options.addOption("of", true, "Output folder");
        options.addOption("single", true, "Build output in one single file, specify output file name");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args);

        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();

            final PrintWriter writer = new PrintWriter(System.out);
            formatter.printUsage(writer,80,"Newstore config collector.", options);
            writer.flush();
            System.exit(0);
        }
        ApplicationParams params = new ApplicationParams();
        try {
            params.setTenant(cmd.getOptionValue("tenant"));
            params.setEnvironment(cmd.getOptionValue("env"));
            params.setUsername(cmd.getOptionValue("u"));
            params.setPassword(cmd.getOptionValue("p"));
            params.setInputFile(cmd.getOptionValue("in"));
            params.setOutputFolder(cmd.getOptionValue("of"));
            params.setSingleOutput(cmd.hasOption("single"));
            if (params.isSingleOutput()) {
                params.setSingleFileName(cmd.getOptionValue("single"));
            }
            ConsoleUtils.println(params.toString());
            params.checkIfValid();
        } catch (Exception e) {
            ConsoleUtils.println("Can not parse input arguments...");
            ConsoleUtils.printLnException(e);
            System.exit(EXITCODE_ARGUMENT_ERROR);
        }

        int exitCode = EXITCODE_SUCCESS;
        try {
            try {
                ConfigCollectorTask task = new ConfigCollectorTask(params);
                task.run();
                boolean isInterrupted = waitTaskOrKeyToFinish(task, 'q');
                if (isInterrupted) {
                    exitCode = EXITCODE_SUCCESS;
                    ConsoleUtils.println("Process is interrupted by the user");
                    //LOGGER.info("Process is interrupted by the user");
                    return;
                }
            } catch (IOException e) {
                if (e instanceof FileNotFoundException)
                    exitCode = EXITCODE_FILE_NOT_FOUND;
                else if (e instanceof IOException)
                    exitCode = EXITCODE_ARGUMENT_ERROR;
            } catch (Exception e) {
                exitCode = EXITCODE_ARGUMENT_ERROR;
            }
        } finally {
            ConsoleUtils.println("Application ends...");
            System.exit(exitCode);
        }
    }

    private static boolean waitTaskOrKeyToFinish(TaskRunnable task, char keyToQuit) throws IOException {
        boolean isInterrupted = false;
        while (!(task.isFinished())) {
            if (System.in.available() > 0) {
                int c = System.in.read();
                if (c == 'q') {
                    task.interrupt();
                    isInterrupted = true;
                }
            }
        }
        if (isInterrupted)
            task.join();
        return isInterrupted;
    }



}
