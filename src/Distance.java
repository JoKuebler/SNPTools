import java.io.*;
import java.util.HashMap;

/**
 * Created by kuebler on 11/18/15.
 * Functionality for distance calculation
 */
public class Distance {

    /**
     * calculates distance of input snp to next gene downstream
     * @param snp
     * @param gffFile
     * @param outputDirectory
     * @param outputFile
     */
    public static void getSNPDist(SNPObject snp, String gffFile, String outputDirectory, String outputFile) {

        BufferedReader br2;
        BufferedWriter bw;
        String line2;
        String delimiter = "\t";
        int distance;
        String[] geneNames;
        String ensemblID;
        int gffChrom;

        try {

            //read in gff file
            br2 = new BufferedReader(new FileReader(gffFile));

            //if no output file print it to terminal
            if(outputFile == null) {
                bw = new BufferedWriter(new OutputStreamWriter(System.out));
            } else {
                bw = new BufferedWriter(new FileWriter(outputDirectory + "/" + outputFile + ".tsv", true));
            }

            line2 = br2.readLine();

            while(line2.contains("#")) {
                line2 = br2.readLine();
            }

            while (line2 != null) {
                String[] dataArray2 = line2.split(delimiter);
                dataArray2[0] = dataArray2[0].replaceAll("[^0-9,X,Y,MT?]", "");

                if (dataArray2[0].equals("X")) {
                    gffChrom = 23;
                } else if (dataArray2[0].equals("Y")) {
                    gffChrom = 24;
                } else if (dataArray2[0].equals("MT") || dataArray2[0].equals("M")) {
                    gffChrom = 25;
                } else {
                    gffChrom = Integer.parseInt(dataArray2[0]);
                }

                int gffStartPos = Integer.parseInt(dataArray2[3]);
                //int gffEndPos = Integer.parseInt(dataArray2[4]);
                // strand = dataArray2[6];

                //searches for next gene downstream in gff file
                if (snp.chrom == gffChrom) {
                    if (snp.pos > gffStartPos) {
                        line2 = br2.readLine();
                    } else {
                        geneNames = dataArray2[8].split(";");
                        ensemblID = geneNames[0];
                        distance = gffStartPos - snp.pos;
                        //writes calculations in output file
                        bw.write(snp.chrom + "\t" + snp.id + "\t" +snp.pos + "\t" + distance + "\t" + ensemblID + "\n");
                        break;
                    }

                //functionality for +/- strand is available but not yet tested enough
                /*if (snp.chrom == gffChrom) {
                    if(strand.equals("+")) {
                        if (snp.pos > gffStartPos) {
                            line2 = br2.readLine();
                        } else {
                            geneNames = dataArray2[8].split(";");
                            ensemblID = geneNames[0];
                            distance = gffStartPos - snp.pos;
                            bw.write(snp.chrom + "\t" + snp.id + "\t" + snp.pos + "\t" + strand +  "\t"  + distance + "\t" + ensemblID + "\n");
                            break;
                        }
                    } else if(strand.equals("-")) {
                        if(snp.pos > gffEndPos) {
                            line2 = br2.readLine();
                        } else {
                            geneNames = dataArray2[8].split(";");
                            ensemblID = geneNames[0];
                            distance = snp.pos - gffEndPos;
                            bw.write(snp.chrom + "\t" + snp.id + "\t" + snp.pos + "\t" + strand +  "\t" + distance + "\t" + ensemblID + "\n");
                            break;
                        }

                    } else {
                        bw.write("NotInGene + \n");
                    }*/


                } else {
                    line2 = br2.readLine();
                }

            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * hashMap based functionality to calculate distance of snp to next gene (not used)
     * @param inputFile
     * @param gffFile
     * @param chrom
     * @return
     */
    public static HashMap<Integer,Integer> findDist(String inputFile, String gffFile, int chrom) {

        BufferedReader br1;
        BufferedReader br2;
        BufferedWriter bw;
        String line1 = "#";
        String line2;
        String delimiter = "\t";
        int distance;
        String chromosome;
        if(chrom == 23)
            chromosome = "chrX";
        else if (chrom == 24)
            chromosome = "chrY";
        else if (chrom == 25)
            chromosome = "chrMT";
        else
            chromosome = "chr" + Integer.toString(chrom);

        HashMap<Integer,Integer> dist = new HashMap();

        try {
            br1 = new BufferedReader(new FileReader(inputFile));
            br2 = new BufferedReader(new FileReader(gffFile));
            bw = new BufferedWriter(new FileWriter("test.txt"));
            line2 = br2.readLine();

            while(line1.contains("#")) {
                line1 = br1.readLine();
            }

            while(line2.contains("#")) {
                line2 = br2.readLine();
            }


            String[] dataArray1 = line1.split(delimiter);
            String[] dataArray2 = line2.split(delimiter);



            while(line1 != null) {

                if(dataArray1[0].equals(chromosome)) {
                    if (dataArray2[0].equals(chromosome)) {

                        while (Integer.parseInt(dataArray1[1]) <= Integer.parseInt(dataArray2[3])) {

                            distance = Integer.parseInt(dataArray2[3]) - Integer.parseInt(dataArray1[1]);
                            line1 = br1.readLine();
                            if (dist.get(distance) == null) {
                                dist.put(distance, 1);
                            }

                            int tmp = dist.get(distance);
                            dist.put(distance, tmp + 1);

                            if (line1 != null) {
                                dataArray1 = line1.split(delimiter);
                            } else {
                                break;
                            }

                        }

                        if (Integer.parseInt(dataArray1[1]) > Integer.parseInt(dataArray2[3])) {
                            while (Integer.parseInt(dataArray1[1]) <= Integer.parseInt(dataArray2[4])) {
                                line1 = br1.readLine();
                                if (line1 != null) {
                                    dataArray1 = line1.split(delimiter);
                                } else {
                                    break;
                                }
                            }
                        }

                        line2 = br2.readLine();
                        if (line2 != null) {
                            dataArray2 = line2.split(delimiter);

                        } else {
                            break;
                        }

                    } else {
                        line2 = br2.readLine();
                        if (line2 != null) {
                            dataArray2 = line2.split(delimiter);

                        } else {
                            break;
                        }

                    }

                } else {
                    line1 = br1.readLine();
                    if (line1 != null) {
                        dataArray1 = line1.split(delimiter);
                    } else {
                        break;
                    }
                }

            }

            br1.close();
            br2.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }



        return dist;
    }
}
