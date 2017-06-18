import org.apache.commons.cli.*;


import java.io.File;
import java.util.ArrayList;


/**
 * Created by kuebler on 12/3/15.
 * Command Line Interface for distance calculation
 */
public class DistanceCLI {

    private static final String CLASS_NAME = "snptools distance [options]";
    String inputFile;
    String outputDirectory;
    String inputGffFile;
    String outputFile;

    public DistanceCLI(String[] args) {

        //help page for distance functionality
        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show help page");
        Options options = new Options();
        options.addOption("i", "input file", true, "input vcf file to calculate distances on");
        options.addOption("g", "gff file", true, "input gff file");
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

            //reads in param for inputfile
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

            //reads in param for input gff file
            inputGffFile = cmd.getOptionValue("g");



        } catch (ParseException e2) {

        }

        //runs it only if input file and gff file are available
        if(inputFile != null & inputGffFile != null) {
            runDistance();
        } else {
            System.err.println("Input File/GFF File is missing");
        }



    }

    /**
     * runs the distance functionality
     */
    public void runDistance() {
        Filter f1 = new Filter();
        Reader r1 = new Reader();
        Helpers h1 = new Helpers();
        ArrayList<SNPObject> inputSNPs = r1.getSNPs(inputFile);
        String filteredFile = f1.filterGenes(outputDirectory, inputGffFile);
        String sortedFile = r1.sortFile(outputDirectory, filteredFile);
        h1.writeDistanceHeader(outputDirectory,outputFile);

        for (int i = 0; i < inputSNPs.size() ; i++) {
            Distance.getSNPDist(inputSNPs.get(i), sortedFile, outputDirectory, outputFile);
        }

        File fileToDelete1 = new File(outputDirectory + "/tmpGeneFile.gff3");
        fileToDelete1.delete();
        File fileToDelete2 = new File(outputDirectory + "/tmpGffFile.gff3");
        fileToDelete2.delete();






    }


}
