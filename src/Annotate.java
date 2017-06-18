
import java.io.*;
import java.util.ArrayList;

/**
 * Created by kuebler on 11/18/15.
 * Functionality for Annotation
 */
public class Annotate {

    BufferedWriter bw = null;

    /**
     * @param outputDirectory
     * @param outputFile
     * initialize bufferedWriter to write output file
     *
     */
    public BufferedWriter initBW(String outputDirectory, String outputFile) throws IOException {


        if (outputDirectory == null) {
            String current = new File(".").getCanonicalPath();
            outputDirectory = current;
        }

        if (outputFile == null) {
            bw = new BufferedWriter(new OutputStreamWriter(System.out));
        } else {
            bw = new BufferedWriter((new FileWriter(outputDirectory + "/" + outputFile + ".tsv", true)));
        }

        return bw;
    }

    /**
     * finds feature in GFF file for input snp and writes the information in the output file
     *
     * @param snp
     * @param features
     */
    public static void findFeature(BufferedWriter bw, SNPObject snp, ArrayList<FeatureObject> features) {

        try {

            //searches for feature of snp in all available features
            for (int i = 0; i < features.size(); i++) {
                //chrom of feature has to be the same as chrom of snp
                if (snp.chrom == features.get(i).chrom) {
                    //snp pos has to be between start- and endpos of feature
                    if (snp.pos >= features.get(i).start && snp.pos <= features.get(i).end) {
                        //writes calculations to output file
                        bw.write(features.get(i).chrom  + "\t" + snp.pos + "\t" + snp.id + "\t" + features.get(i).start
                                + "\t" + features.get(i).end + "\t" + features.get(i).annot + "\t"
                                + features.get(i).ID + "\n");

                    }
                }
            }

            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}