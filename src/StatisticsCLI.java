import org.apache.commons.cli.*;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by kuebler on 11/26/15.
 */
public class StatisticsCLI {

    private static final String CLASS_NAME = "snptools statistics [options]";
    private String inputFile;
    private String outputFile;
    private String inputDirectory;
    private ArrayList<String> statisticFiles = new ArrayList<>();
    private String outputDirectory;

    public StatisticsCLI(String[] args) {

        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show help page");
        Options options = new Options();
        options.addOption("i", "input file", true, "set input file to calculate statistics on");
        options.addOption("s", "statistic files", true, "set directory containing statistic files");
        options.addOption("o", "output file", true, "set output file [terminal]");
        options.addOption("d", "output directory", true, "set output directory [current directory]");

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

            inputFile = cmd.getOptionValue("i");

            /*if (inputFile == null) {
                System.err.println("no input file");
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }*/

            inputDirectory = cmd.getOptionValue("s");

            File f = new File(inputDirectory);
            File[] list = f.listFiles();
            Helpers helpers = new Helpers();

            if(list.length == 0) {
                System.out.println("No files here!");
            }

            for (int i = 0; i < list.length; i++) {
                String path = list[i].getAbsolutePath();
                String filename = helpers.getFolderName(path);
                if(filename.startsWith("SNPTools")) {
                    statisticFiles.add(path);

                }
            }

           if (statisticFiles.size() == 0) {
                System.err.println("no statistic files");
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);

            }

            outputFile = cmd.getOptionValue("o");

            if (outputFile == null) {
                System.err.println("no output file");
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }

            outputDirectory = cmd.getOptionValue("d");

        } catch (ParseException e2) {

        }

        if(inputFile != null && inputDirectory != null) {
            runStatistics();
        } else {
            System.err.println("Input File/Statistic File is missing");
        }

    }

    public void runStatistics() {

        Statistics st1 = new Statistics();
        Helpers h1 = new Helpers();
        Reader r1 = new Reader();
        ArrayList<SNPObject> inputSNPs = r1.getSNPs(inputFile);
        h1.writeHeader(outputDirectory, outputFile);


        for (int j = 0; j <= statisticFiles.size()-1 ; j++) {
            String currentFile = statisticFiles.get(j);
            String currentDB = h1.getDBName(statisticFiles.get(j));
            int snps = h1.countSNPs(statisticFiles.get(j));


            for (int i = 0; i < inputSNPs.size(); i++) {
                st1.binomial(inputSNPs.get(i), currentFile, outputFile, outputDirectory, snps, currentDB);
            }
        }




        /*for (int i = 0; i < statisticFiles.size()-1; i++) {
            for (int j = 0; j < inputSNPs.size(); i++) {
            int snps = h1.countSNPs(statisticFiles.get(i));
            String currentFile = statisticFiles.get(i);
                st1.binomial(inputSNPs.get(j), currentFile, outputFile, outputDirectory, snps);
            }
        }*/
    }

}



