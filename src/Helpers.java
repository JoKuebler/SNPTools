import java.io.*;

/**
 * Created by kuebler on 12/1/15.
 * some usefull helpers
 */
public class Helpers {

    public String getFolderName(String path) {

        String[] dataArray = path.split("/");
        String foldername = dataArray[dataArray.length - 1];

        return foldername;


    }

    public int countSNPs(String file) {

        BufferedReader br;
        String line;
        int snps = 0;
        String delimiter = "\t";
        String[] dataArray;

        try {

            br = new BufferedReader(new FileReader(file));

            line = br.readLine();

            while (line.contains("#")) {
                line = br.readLine();
            }

            while (line != null) {

                dataArray = line.split(delimiter);
                snps += Integer.parseInt(dataArray[3]);

                line = br.readLine();
            }

            br.close();

        } catch (Exception e) {

        }

        return snps;
    }

    public void writeHeader(String outputDirectory, String outputFile) {

        BufferedWriter bw;

        try {
            bw = new BufferedWriter(new FileWriter(outputDirectory + "/" + outputFile + ".tsv", false));
            bw.write("#CHROM" + "\t" + "SNP_ID" + "\t" + "POS" + "\t" + "WINDOW" + "\t" + "SNPS_FOUND" +
                    "\t" + "P-VALUE" + "\t" + "DB" + "\t" + "W" + "\t" + "S" + "\n");
            bw.flush();
            bw.close();


        } catch (IOException e) {

        }

    }

    public void writeDistanceHeader(String outputDirectory, String outputFile) {

        BufferedWriter bw;

        try {

            bw = new BufferedWriter(new FileWriter(outputDirectory + "/" + outputFile + ".tsv", true));
            bw.write("#CHROM" + "\t" + "SNP_ID" + "\t" + "POS" + "\t" + "DIST" + "\t" + "GENE" + "\n");
            bw.flush();
            bw.close();

        } catch (IOException e) {

        }

    }

    public void writeAnnotateHeader(BufferedWriter bw, String outputDirectory, String outputFile) {

        try {

            bw.write("#SNP_POS" + "\t" + "CHROM" + "\t" + "FEATURE_STARTPOS" + "\t" + "FEATURE_ENDPOS" + "\t" + "FEATURE" + "\t" + "ENSEMBLID" + "\n");
            bw.flush();

        } catch (IOException e) {

        }

    }

    public void writeDBName(String outputDirectory, String outputFile, int windowLength, int shift) {

        BufferedWriter bw;

        try {
            if (outputFile == null) {
                bw = new BufferedWriter(new OutputStreamWriter(System.out));
            } else {
                bw = new BufferedWriter((new FileWriter(outputDirectory + "/" + "SNPTools" + outputFile + "W"
                        + windowLength + "S" + shift + ".tsv", false)));
            }


            bw.write("#" + outputFile + "\n");

            bw.flush();
            bw.close();


        } catch (IOException e) {

        }

    }

    public String getDBName(String file) {

        BufferedReader br;
        String name = "";

        try {

            br = new BufferedReader(new FileReader(file));
            name = br.readLine();

        } catch (Exception e) {

        }

        return name;


    }

    public void countExons(String file) {

        BufferedReader br;
        BufferedWriter bw;
        String line;
        String delimiter = "\t";
        String[] dataArray;

        try {

            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter("exons.tsv"));

            line = br.readLine();

            while (line != null) {

                dataArray = line.split(delimiter);
                if (dataArray[5].equals("exon")) {
                    bw.write(line);
                    bw.flush();
                }


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void writedbCheckHeader(BufferedWriter bw, String outputDirectory, String outputFile) {

        try {

            bw.write("#SNPs not found in database\n");
            bw.write("#CHROM  POS     ID      REF     ALT     QUAL    FILTER  INFO");
            bw.flush();

        } catch (IOException e) {

        }


    }

    public void FileEditor(String file, String secondFile) {

        BufferedReader br;
        BufferedReader br2;
        BufferedWriter bw;
        String line;
        String line2;
        String[] dataArray;
        String[] dataArray2;
        String delimiter = "\t";
        String currentSNP;
        String lastSNP = " ";
        int CDS = 0;
        int snps = 0;

        try {

            br = new BufferedReader(new FileReader(file));
             br2 = new BufferedReader(new FileReader(secondFile));
            bw = new BufferedWriter(new FileWriter("/Users/Jonas/Desktop/NotinGenes.tsv"));
            line = br.readLine();

            while (line.contains("#")) {
                line = br.readLine();
            }

            while (line != null) {

                br2 = new BufferedReader(new FileReader(secondFile));
                line2 = br2.readLine();
                dataArray = line.split(delimiter);
                dataArray2 = line2.split(delimiter);
                currentSNP = dataArray[2];
               lastSNP = dataArray2[2];

               while(line2 != null) {

                   if(currentSNP.equals(lastSNP)) {
                       break;
                   } else {
                       line2 = br2.readLine();
                       if (line2!=null) {
                           dataArray2 = line2.split(delimiter);
                           lastSNP = dataArray2[2];
                       } else {
                           bw.write(line + "\n");
                       }

                   }

                }


                line = br.readLine();
                br2.close();
                bw.flush();



               /* if (dataArray[5].equals("gene")) {
                    bw.write(line+"\n");
                   // CDS++;
                  String nextSNP = currentSNP;
                  while(currentSNP.equals(nextSNP) && line!= null) {
                      line = br.readLine();
                      dataArray = line.split(delimiter);
                      currentSNP = dataArray[2];
                   }
                   }*/

        }

        br.close();
        bw.close();

    }

    catch(
    FileNotFoundException e
    )

    {
        e.printStackTrace();
    }

    catch(
    IOException e
    )

    {
        e.printStackTrace();
    }





    }

}
