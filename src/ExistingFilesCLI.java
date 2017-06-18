import org.apache.commons.cli.*;

import java.io.File;

/**
 * Created by kuebler on 12/2/15.
 * Functionality and Command Line Interface to check and view all existing files created by the distributionCalc class
 */
public class ExistingFilesCLI {

    private static final String CLASS_NAME = "snptools existing files [options]";
    String directory;

    public ExistingFilesCLI(String[] args) {

        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show help page");
        Options options = new Options();
        options.addOption("v", "view files in directory", true, "view existing files in directory");

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
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }

            CommandLine cmd = parser.parse(options, args);


            directory = cmd.getOptionValue("v");

            if(directory == null) {
                helpFormatter.printHelp(CLASS_NAME,options);
                System.exit(0);
            }

            File f = new File(directory);
            File[] list = f.listFiles();
            Helpers helpers = new Helpers();

            if(list.length == 0) {
                System.out.println("No files here!");
            }

            for (int i = 0; i < list.length; i++) {
                String path = list[i].getAbsolutePath();
                String filename = helpers.getFolderName(path);
                if(filename.contains("tsv")) {
                    System.out.println(filename);
                }
            }


        } catch (ParseException e2) {

        }

    }
}





