import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;


/**
 * Created by kuebler on 11/24/15.
 * Command Line Interface for Window Calculation to get SNP distribution
 */
public class DistributionCalcCLI extends DistributionCalc {

    private static final String CLASS_NAME = "snptools distribution calculator [options]";
    int windowlength = 10000;
    int shift = 10000;
    String inputFile;
    String outputFile;
    String outputDirectory;

    public DistributionCalcCLI(String[] args) {

        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show help page");
        Options options = new Options();
        options.addOption("i", "input file", true, "set input database file to calculate windows on");
        options.addOption("o", "output directory", true, "set output directory [current directory]");
        options.addOption("n", "database", true, "set name for database output file");
        options.addOption("w", "windowlength", true, "set window length [10000]");
        options.addOption("s", "shift", true, "set shift [10000]");


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

            CommandLine cmd = parser.parse(options, args);

            inputFile = cmd.getOptionValue("i");

            if (inputFile == null) {
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }

            outputDirectory = cmd.getOptionValue("o");

            outputFile = cmd.getOptionValue("n");

            String windowlengthString = cmd.getOptionValue("w");

            if(windowlengthString != null) {
                if(isInteger(windowlengthString)) {
                    this.windowlength = Integer.parseInt(windowlengthString);

                } else {
                    System.err.println("Windowlength has to be an Integer");
                    helpFormatter.printHelp(CLASS_NAME, options);
                    System.exit(0);
                }
            }


            String ShiftString = cmd.getOptionValue("s");

            if(ShiftString != null) {

                if (isInteger(ShiftString)) {
                    this.shift = Integer.parseInt(ShiftString);

                } else {
                    System.err.println("Shift has to be an Integer");
                    helpFormatter.printHelp(CLASS_NAME, options);
                    System.exit(0);
                }
            }

            if(shift > windowlength) {
                System.err.println("Shift has to be equal/smaller then windowlength");
                helpFormatter.printHelp(CLASS_NAME,options);
                System.exit(0);
            }



        } catch (ParseException e2) {

        }

        if(inputFile != null) {
            runShifter();
        } else {
            System.err.println("No input file selected");
        }

    }

    public void runShifter() {

        DistributionCalc sh1 = new DistributionCalc();
        Helpers h1 = new Helpers();
        h1.writeDBName(outputDirectory,outputFile,windowlength,shift);
        sh1.WindowCalculator(inputFile,outputDirectory, outputFile, windowlength, shift);

        try {
            File path = new File(".");
            String current = path.getCanonicalPath() + "/SNPTools/Test";
            File folder = new File(current);

        } catch(IOException e1) {
        }


    }

    public static Boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(Exception e){
            return false;
        }
    }


}
