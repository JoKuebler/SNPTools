import org.apache.commons.cli.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jonas on 24.11.15.
 * Command Line Interface for Annotation
 */
public class AnnotateCLI extends Annotate {

    private static final String CLASS_NAME = "snptools annotate [options]";
    private String inputFile;
    private String inputGffFile;
    private String outputFile;
    private String outputDirectory;


    public AnnotateCLI(String[] args) throws IOException {

        //helpage of annotation function
        Options helpOptions = new Options();
        helpOptions.addOption("h", "help", false, "show help page");
        Options options = new Options();
        options.addOption("i", "input file", true, "input vcf file containing snps");
        options.addOption("g", "gff file", true, "input gff file for annotation");
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
                helpFormatter.printHelp(CLASS_NAME,options);
                System.exit(0);
            }

            CommandLine cmd = parser.parse(options, args);

            //reads value of param for input file
            inputFile = cmd.getOptionValue("i");

            //no input file
            if (inputFile == null) {
                System.err.println("missing input file");
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }

            //reads value of param for input gff file
            inputGffFile = cmd.getOptionValue("g");

            //no gff file
            if(inputGffFile == null) {
                System.err.println("missing gff file");
                helpFormatter.printHelp(CLASS_NAME, options);
                System.exit(0);
            }

            //reads param for output directory
            outputDirectory = cmd.getOptionValue("d");

            //reads param for outputFile
            outputFile = cmd.getOptionValue("f");

        } catch (ParseException e2) {

        }

        //annotation will only start if input SNPs and GFF file are available
        if(inputFile != null & inputGffFile != null) {
            runAnnotate();
        } else {
            System.err.println("Input File/GFF File is missing");
        }


    }

    //runs the annotation
    public void runAnnotate() throws IOException {

        Reader r1 = new Reader();
        Helpers h1 = new Helpers();
        Annotate a1 = new Annotate();
        ArrayList<SNPObject> inputSNPs = r1.getSNPs(inputFile);
        BufferedWriter bw = a1.initBW(outputDirectory,outputFile);
        h1.writeAnnotateHeader(bw,outputDirectory,outputFile);
        ArrayList<FeatureObject> features = r1.parseGff(inputGffFile);

        for (int i = 0; i < inputSNPs.size(); i++) {
            findFeature(bw,inputSNPs.get(i),features);
       }

    }


}
