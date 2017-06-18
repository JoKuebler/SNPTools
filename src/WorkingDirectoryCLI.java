import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Scanner;

/**
 * Created by kuebler on 12/2/15.
 * Functionality and Command Line Interface to chose and change working directory of SNPTools
 */
public class WorkingDirectoryCLI {

    private static final String CLASS_NAME = "snptools working directory [options]";
    String workingDirectory;

    public WorkingDirectoryCLI(String[] args) {

        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show help page");
        Options options = new Options();
        options.addOption("d", "change directory", true, "change working directory");

        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();


        try {
            CommandLine cmd = parser.parse(helpOptions, args);
            if (cmd.hasOption('h')) {
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }
        } catch (ParseException e1) {
        }

        try {

            if (args.length < 2) {
                helpFormatter.printHelp(CLASS_NAME,options);
                System.exit(0);
            }

            CommandLine cmd = parser.parse(options, args);

            workingDirectory = cmd.getOptionValue("d");

            File dir = new File(workingDirectory);

            if(!dir.exists() || !dir.isDirectory()) {
                dir.mkdir();
            } else {
                System.err.println("Directory already exists, overwrite (Y/N?)");
                Scanner scanner1 = new Scanner(System.in);
                String resp = scanner1.nextLine();
                if(resp.equals("y") || resp.equals("Y")) {
                    FileUtils.deleteQuietly(dir);
                    dir.mkdir();
                    helpFormatter.printHelp(CLASS_NAME,options);
                } else {
                    helpFormatter.printHelp(CLASS_NAME, options);
                    System.exit(0);
                }
            }


        } catch (ParseException e2) {

        }


    }


}
