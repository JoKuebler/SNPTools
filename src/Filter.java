import htsjdk.samtools.util.StringUtil;

import java.io.*;

/**
 * Created by kuebler on 11/18/15.
 * some filters used for filtering database vcf files
 */
public class Filter {


    /**
     * filters indels out of a vcf file
     * @param filename returned by User class method
     * @throws IOException
     */
    public static void indelFilter(String filename) throws IOException {

        String delimiter = "\t";
        try {

            File inFile = new File(filename);

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(filename));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = "#";
            String[] dataArray;
            boolean isSNP = false;

            while ((line = br.readLine()).contains("#")) {
                pw.println(line);
                pw.flush();

            }

            //Read from the original file and write to new
            //Unless Indels
            while ((line = br.readLine()) != null) {
                    dataArray = line.split(delimiter);
                    String ref = dataArray[3];
                    String alt = dataArray[4];

                    for (String alternative : alt.split(",")) {
                        int distance = StringUtil.levenshteinDistance(ref, alternative, 10, 1, 10, 10);
                        if (distance > 1) {
                            isSNP = false;
                        }
                    }

                    if(isSNP == true) {
                        pw.println(line);
                    } else {
                        isSNP=true;
                    }
                }


            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");

            pw.flush();
            pw.close();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * filter genes out of gff file for distance calculations
     * @param outputDirectory
     * @param filename
     * @return
     */
    public String filterGenes(String outputDirectory, String filename) {

        BufferedReader br;
        BufferedWriter bw;
        String line = "#";
        String delimiter = "\t";
        String[] dataArray;

        try {

            if(outputDirectory == null) {
                String current = new File(".").getCanonicalPath();
                outputDirectory = current;
            }

            br = new BufferedReader((new FileReader(filename)));
            bw = new BufferedWriter((new FileWriter(outputDirectory + "/" + "tmpGeneFile" + ".gff3")));
            bw.write("#all genes from gff file" + "\n");


            while ((line = br.readLine()) != null) {
                if (!line.contains("#")) {
                    dataArray = line.split(delimiter);
                    if(dataArray[2].contains("gene") && dataArray[0].length() <= 5) {
                        bw.write(line + "\n");
                    }
                }

            }

            bw.flush();
            bw.close();

        }  catch (IOException e) {
            e.printStackTrace();
        }

        return outputDirectory + "/" + "tmpGeneFile" + ".gff3";

    }

}
