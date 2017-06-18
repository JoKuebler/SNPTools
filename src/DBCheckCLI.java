import org.apache.commons.cli.*;

import java.util.ArrayList;

/**
 * Created by kuebler on 1/11/16.
 * Command Line Interface for Database Check
 */
public class DBCheckCLI extends DBCheck {

    private static final String CLASS_NAME = "snptools database check [options]";

    private String inputFile;
    private String outputFile;
    private String outputDirectory;
    private String databaseFile;

    public DBCheckCLI(String[] args) {

        //help page of data base check
        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show help page");
        Options options = new Options();
        options.addOption("i", "input file", true, "input vcf file containing snps");
        options.addOption("c", "database check file", true, "set database file to check");
        options.addOption("d", "output directory", true, "set output directory [current directory]");
        options.addOption("f", "output file", true, "set output file [terminal]");

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

            //reads value of param for input file
            inputFile = cmd.getOptionValue("i");

            if (inputFile == null) {
                System.err.println("no input file");
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }

            //reads in param for output directory
            outputDirectory = cmd.getOptionValue("d");

            //reads in param for output file
            outputFile = cmd.getOptionValue("f");

            //reads in param for database file
            databaseFile = cmd.getOptionValue("c");

            if (databaseFile == null) {
                System.err.println("no database to check");
                helpFormatter.printHelp(CLASS_NAME,options);
                System.exit(0);
            }


            //only runs if input file and database file are available
            if(inputFile != null & databaseFile != null) {
                runDBCheck();
            } else {
                System.err.println("Input File/Database File is missing");
            }


        } catch (ParseException e2) {

        }


    }

    //runs the functionality class
    public void runDBCheck() {

        Reader r1 = new Reader();
        Helpers h1 = new Helpers();
        ArrayList<SNPObject> inputSNPs = r1.getSNPs(inputFile);
        h1.writedbCheckHeader(bw2,outputDirectory,outputFile);

        for (int i = 0; i < inputSNPs.size() ; i++) {
            databaseCheck(outputDirectory,outputFile, inputSNPs.get(i), databaseFile);
        }


    }


}
