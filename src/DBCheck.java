import java.io.*;

/**
 * Created by kuebler on 1/11/16.
 * Functionality for Database Check
 */
public class DBCheck {

    public static BufferedWriter bw;
    public static BufferedWriter bw2;
    public static BufferedReader br;

    /**
     * checks if input snp is already in any of the databases
     * @param snp
     */
    public static void databaseCheck(String outputDirectory, String outputFile, SNPObject snp, String databaseFile) {

        String line;
        String delimiter = "\t";
        String[] dataArray;

        try {

            //reads in database file
            br = new BufferedReader(new FileReader(databaseFile));

            if(outputDirectory == null || !outputDirectory.endsWith("/")) {
                String current = new File(".").getCanonicalPath();
                outputDirectory = current;
            }

            if (outputFile == null) {
                bw = new BufferedWriter(new OutputStreamWriter(System.out));
                bw2 = new BufferedWriter(new OutputStreamWriter(System.out));

            } else {
                bw = new BufferedWriter(new FileWriter(outputDirectory + "/" + outputFile + "Check.tsv", true));
                bw2 = new BufferedWriter(new FileWriter(outputDirectory + "/" + outputFile + "NotFound.tsv", true));
            }

            line = br.readLine();

            //skips header
            while(line.contains("#")) {
                line = br.readLine();
            }

            //checks if snp is in database
            while(line != null) {


                dataArray = line.split(delimiter);
                dataArray[0] = dataArray[0].replaceAll("[^0-9,X,Y,M]", "");

                if (dataArray[0].equals("X")) {
                    dataArray[0] = "23";
                } else if (dataArray[0].equals("Y")) {
                    dataArray[0] = "24";
                } else if (dataArray[0].equals("MT") || dataArray[0].equals("M") ) {
                    dataArray[0] = "25";
                }

                int currChrom = Integer.parseInt(dataArray[0]);
                int currPos = Integer.parseInt(dataArray[1]);

                //checks only snp of chrom
                if(currChrom == snp.chrom) {
                    if (snp.pos == currPos) {
                        bw.write(line + "\n");
                        bw.flush();
                        break;

                    } else if(currPos > snp.pos) {
                        bw2.write(snp.chrom + "\t" + snp.pos + "\t" + snp.id
                                + "\t" + snp.ref + "\t" + snp.alt + "\t" + snp.filter
                                + "\t" + snp.info + "\n");
                        bw2.flush();
                        break;

                    } else {
                        line = br.readLine();
                    }
                } else if(currChrom > snp.chrom){
                    break;

                } else {
                    line = br.readLine();
                }

            }

            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


