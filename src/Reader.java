/**
 * A class to read and parse vcf files
 * @author kuebler
 */

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {

    static HashMap<Integer, HashMap<Integer, ArrayList<SNPObject>>> chromMap = new HashMap<Integer, HashMap<Integer, ArrayList<SNPObject>>>();

    /**
     * parse input file and save snps in an arraylist of snpObjects
     * @param inputFile
     * @return inputSNPs
     */
    public static ArrayList<SNPObject> getSNPs(String inputFile) {

        ArrayList<SNPObject> inputSNPs = new ArrayList<SNPObject>();
        BufferedReader br;
        String line;
        String delimiter = "\t";

        try {

            br = new BufferedReader(((new FileReader(inputFile))));

            while((line = br.readLine()) != null) {
                if (!line.contains("#")) {

                    String[] dataArray = line.split(delimiter);
                    SNPObject snp = new SNPObject();
                    dataArray[0] = dataArray[0].replaceAll("[^0-9,X,Y,MT?]", "");
                    String chrom = dataArray[0];


                    if (chrom.equals("X")) {
                        snp.chrom = 23;
                    } else if (chrom.equals("Y")) {
                        snp.chrom = 24;
                    } else if (chrom.equals("MT") || chrom.equals("M")) {
                        snp.chrom = 25;
                    } else {
                        snp.chrom = Integer.parseInt(chrom);
                    }


                    snp.pos = Integer.parseInt(dataArray[1]);
                    snp.id = dataArray[2];
                    snp.ref = dataArray[3];
                    snp.alt = dataArray[4];
                    snp.qual = dataArray[5];
                    snp.filter = dataArray[6];
                    snp.info = dataArray[7];
                    inputSNPs.add(snp);
                }

            }



        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return inputSNPs;


    }

    /**
     * gets windows (start/end pos) and the amount of snps out of file and saves it in respective chromosomes
     * @param filename
     * @return ArrayList of ChromosomeObjects
     */
    public static ArrayList<ChromosomeObject> getWindows(String filename) {

        ArrayList<ChromosomeObject> chromosomes= new ArrayList<ChromosomeObject>();
        BufferedReader br;
        String line;
        String delimiter = "\t";

        try {

            br = new BufferedReader((new FileReader(filename)));

            int chrom = 1;
            ChromosomeObject chromo = new ChromosomeObject();

            while((line = br.readLine()) != null) {
                WindowsObject window = new WindowsObject();
                String[] dataArray = line.split(delimiter);
                window.chrom = Integer.parseInt(dataArray[0]);
                window.start = Integer.parseInt(dataArray[1]);
                window.end = Integer.parseInt(dataArray[2]);
                window.snpCount = Integer.parseInt(dataArray[3]);

                if(window.chrom == chrom) {
                    chromo.windows.add(window);
                } else {
                    chromosomes.add(chromo);
                    chromo = new ChromosomeObject();
                    chromo.windows.add(window);
                    chrom++;
                }

            }

            chromosomes.add(chromo);

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return chromosomes;

    }

    /**
     * method to parse GFF file into feature objects
     * @param filename
     * @return
     */
    public static ArrayList<FeatureObject> parseGff(String filename) {

        ArrayList<FeatureObject> features = new ArrayList<FeatureObject>();
        BufferedReader br = null;
        String line = "#";
        String delimiter = "\t";
        String[] dataArray;
        String[] infoField;

        try {
            br = new BufferedReader((new FileReader(filename)));


            while ((line = br.readLine()) != null) {
                if (!line.contains("#")) {
                    dataArray = line.split(delimiter);
                    if (dataArray[0].length() > 8) {
                        br.readLine();
                    } else {
                        dataArray[0] = dataArray[0].replaceAll("[^0-9,X,Y,M]", "");

                        if (dataArray[0].equals("X")) {
                            dataArray[0] = "23";
                        } else if (dataArray[0].equals("Y")) {
                            dataArray[0] = "24";
                        } else if (dataArray[0].equals("M")) {
                            dataArray[0] = "25";
                        }

                        FeatureObject feature = new FeatureObject();
                        feature.chrom = Integer.parseInt(dataArray[0]);
                        feature.annot = dataArray[2];
                        feature.start = Integer.parseInt(dataArray[3]);
                        feature.end = Integer.parseInt(dataArray[4]);
                        infoField = dataArray[8].split(";");
                        feature.name = infoField[0];

                        for (int i = 0; i < infoField.length ; i++) {
                            if(infoField[i].startsWith("ID")) {
                                feature.ID = infoField[i];
                            }
                        }

                        features.add(feature);


                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return features;

    }

    /**
     * sorts file increasing according to chromosome
     * @param outputDirectory
     * @param filename
     * @return
     */
    public String sortFile(String outputDirectory, String filename) {

        BufferedReader br;
        BufferedWriter bw;
        int chrom = 1;
        String line = "#";
        String[] dataArray;
        String delimiter = "\t";


        try {

            if(outputDirectory == null) {
                String current = new File(".").getCanonicalPath();
                outputDirectory = current;
            }

            br = new BufferedReader((new FileReader(filename)));
            bw = new BufferedWriter((new FileWriter(outputDirectory + "/" + "tmpGffFile" + ".gff3")));

            while (chrom < 23) {
                while ((line = br.readLine()) != null) {
                    if (!line.contains("#")) {
                        dataArray = line.split(delimiter);
                        if (dataArray[0].equals("chr" + Integer.toString(chrom))) {
                            bw.write(line + "\n");
                        }

                    }
                }

                chrom++;
                br = new BufferedReader((new FileReader(filename)));

            }


            br = new BufferedReader(new FileReader(filename));

            while ((line = br.readLine()) != null) {
                if (!line.contains("#")) {
                    dataArray = line.split(delimiter);
                    if(dataArray[0].contains("X") || dataArray[0].contains("Y") || dataArray[0].contains("M")) {
                        bw.write(line + "\n");
                    }
                }
            }

            bw.close();

        }  catch (IOException e) {
            e.printStackTrace();
        }

        return outputDirectory + "/" + "tmpGffFile" + ".gff3";

    }





}



/*
    //creates object for each snp if index file isnt available
    public void parseVCF(String filename, int[] chromData) throws IOException {

    BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filename));
            String line;
            String delimiter = "\t";

            while ((line = br.readLine()) != null) {
                if (!line.contains("#")) {
                    String[] dataArray;
                    dataArray = line.split(delimiter);

                    if(dataArray[0].contains("MT")){
                        dataArray[0] = Integer.toString(25);
                    }

                    if(dataArray[0].contains("Y")){
                        dataArray[0] = Integer.toString(24);
                    }

                    if(dataArray[0].contains("X")) {
                        dataArray[0] = Integer.toString(23);
                    }


                    dataArray[0] = dataArray[0].replaceAll("[^0-9]", "");
                    if(dataArray[0].equals(Integer.toString(chromData[0]))) {
                        SNPObject snp = new SNPObject();
                        snp.pos = Integer.parseInt(dataArray[1]);
                        if (snp.pos >= chromData[1]) {
                            if(snp.pos <= chromData[2]) {
                                snp.chrom = Integer.parseInt(dataArray[0]);
                                snp.id = dataArray[2];
                                snp.ref = dataArray[3];
                                snp.alt = dataArray[4];
                                snp.qual = dataArray[5];
                                snp.filter = dataArray[6];
                                snp.info = dataArray[7];

                                HashMap<Integer, ArrayList<SNPObject>> posMap = chromMap.get(snp.getChrom());


                                if (posMap == null) {
                                    posMap = new HashMap<Integer, ArrayList<SNPObject>>();
                                    ArrayList<SNPObject> snps = new ArrayList<SNPObject>();
                                    snps.add(snp);
                                    posMap.put(snp.getPos(), snps);
                                    chromMap.put(snp.getChrom(), posMap);
                                } else {
                                    ArrayList<SNPObject> snps = posMap.get(snp.getPos());
                                    if (snps == null) {
                                        snps = new ArrayList<SNPObject>();
                                        snps.add(snp);
                                        posMap.put(snp.getPos(), snps);
                                    } else {
                                        snps.add(snp);
                                    }

                                }

                            }
                        }

                    }
                }


            }
            br.close();

            if(chromMap.isEmpty()) {
                System.out.println("No SNPs found!");
                System.exit(0);
            }

        } catch (IOException e) {
            System.out.println("No File found");
            readFilePath();
        }

    }*/





