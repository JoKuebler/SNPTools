/**
 * Main Class of SNPTools
 */

import org.apache.commons.cli.*;
import java.util.Arrays;


public class SNPTools {

    private static final String CLASS_NAME = "snptools";
    private static final String VERSION = "1.0";
    private static final String INFO = "Tools to get SNP information";
    public static Options options = new Options();

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        HelpFormatter helpFormatter = new HelpFormatter();

        //overview for all available functions in snptools
        Option calculator = new Option("d", "snp distribution", false, "calculates distribution of input snps in shifting windows iterating over genome (database)");
        Option check = new Option("b", "database check", false, "checks if input database contains input snps");
        Option annotate = new Option("a", "annotate", false, "annotates input snp regarding it's genomic feature");
        Option distance = new Option("c", "distance", false, "calculates distances of input snps to next gene downstream");
        Option statistics = new Option("s", "statistics", false, "gets statistics about region of input snps");
        Option working = new Option("f", "data folder", false, "sets data folder to save files");
        Option existingFiles = new Option("e", "existing files", false, "views existing files in working directory");


        options.addOption(calculator);
        options.addOption(check);
        options.addOption(annotate);
        options.addOption(statistics);
        options.addOption(working);
        options.addOption(existingFiles);
        options.addOption(distance);


        if (args.length < 1) {
            printHelp();
            helpFormatter.printHelp(CLASS_NAME, options);
            System.exit(0);

        }

        String[] newCommands = Arrays.copyOfRange(args, 1, args.length);

        CommandLineParser parser = new DefaultParser();

        if (!(args[0].equals("-d") || args[0].equals("-a") || args[0].equals("-c") || args[0].equals("-s") || args[0].equals("-f") || args[0].equals("-e") || args[0].equals("-b"))) {
            System.err.println("Option not available");
            helpFormatter.printHelp(CLASS_NAME, options);
            System.exit(0);
        }

        String[] firstArgument = new String[]{args[0]};

        //parses argument and finds correct functionality to run
        CommandLine cmd = parser.parse(options, firstArgument);

        if (cmd.hasOption("snp distribution")) {
            DistributionCalcCLI shifterCLI = new DistributionCalcCLI(newCommands);
        } else if (cmd.hasOption("database check")) {
            DBCheckCLI dbCheckCLI = new DBCheckCLI((newCommands));

        } else if (cmd.hasOption("annotate")) {
            AnnotateCLI annotateCLI = new AnnotateCLI(newCommands);

        } else if (cmd.hasOption("distance")) {
            DistanceCLI distanceCLI = new DistanceCLI(newCommands);

        } else if (cmd.hasOption("statistics")) {
            StatisticsCLI statisticsCLI = new StatisticsCLI(newCommands);

        } else if (cmd.hasOption("data folder")) {
            WorkingDirectoryCLI workingDirectoryCLI = new WorkingDirectoryCLI((newCommands));

        } else if (cmd.hasOption("existing files")) {
            ExistingFilesCLI existingFilesCLI = new ExistingFilesCLI((newCommands));
        } else {
            helpFormatter.printHelp(CLASS_NAME, options);
            System.exit(0);
        }

    }

    //prints help page
  private static void printHelp() {

        StringBuffer helpInfo = new StringBuffer();

        helpInfo.append("Program: " + CLASS_NAME);
        helpInfo.append(" (" + INFO + ")");
        helpInfo.append("\n");
        helpInfo.append("Version: ");
        helpInfo.append(VERSION + "\n\n");
        System.out.println(helpInfo.toString());

    }

}



