
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.*;
import java.util.ArrayList;

/**
 * Class for basic statistic methods
 */

public class Statistics {


    public static int chromlengthArray[] = {249250621, 243199373, 198022430, 191154276, 180915260, 171115067, 159138663,
            146364022, 141213431, 135534747, 135006516, 133851895, 115169878, 107349540, 102531392,
            90354753, 81195210, 78077248, 59128983, 63025520, 48129895, 51304566, 155270560, 59373566, 16569};
    public final long dbSNPCount = 45978243;
    public final int clinVarCount = 85151;

    /**
     * calculates pvalue
     * @param snp
     * @param windowFile
     * @param outputFile
     * @param outputDirectory
     * @param sumSNPs
     * @param currentDB
     */

    public void binomial(SNPObject snp, String windowFile, String outputFile, String outputDirectory, int sumSNPs, String currentDB) {


        BufferedReader br;
        BufferedWriter bw;
        String delimiter = "\t";
        String line;
        int windowlength = 0;
        int x = 0;
        int pos = snp.pos;
        int snps = 0;
        int winStart = 0;
        int winEnd = 0;
        int firstLineStart = 0;
        int shift = 0;


        try {

            //if no output directory chose current one
            if(outputDirectory == null) {
                String current = new File(".").getCanonicalPath();
                outputDirectory = current;
            }


            br = new BufferedReader(new FileReader(windowFile));


            //if no outputfile print to terminal
            if(outputFile == null) {
                bw = new BufferedWriter(new OutputStreamWriter(System.out));
            } else {
                bw = new BufferedWriter(new FileWriter(outputDirectory + "/" + outputFile + ".tsv", true));
            }

            line = br.readLine();

            while(line.contains("#")) {
                line = br.readLine();
            }

            //gets window size und window of current snp
            while(line != null) {


                String[] dataArray = line.split(delimiter);

                //find shift and windowlength of current file
                if(x < 2) {
                    int secondLineStart = Integer.parseInt(dataArray[1]);
                    if(x == 1) {
                        shift = secondLineStart - firstLineStart;
                    }
                    firstLineStart = Integer.parseInt(dataArray[1]);
                    windowlength = Integer.parseInt(dataArray[2]) - Integer.parseInt(dataArray[1]);
                    x++;
                }


                //finds window of current snp in window file
                if (snp.chrom == Integer.parseInt(dataArray[0])) {
                    if (pos >= Integer.parseInt(dataArray[1]) && pos <= Integer.parseInt(dataArray[2])) {
                        snps = Integer.parseInt(dataArray[3]);
                        winStart = Integer.parseInt(dataArray[1]);
                        winEnd = Integer.parseInt(dataArray[2]);


                    }
                }

                line = br.readLine();


            }

            br.close();


            double pvalue = 0;
            double prod1 = 0;
            double prod2 = 0;
            double prod3 = 0;
            double tmp = 0;
            double genomeLength = genomeLength();

            double prob = sumSNPs/genomeLength;

            //pvalue calculation
            for (int i = snps; i <= windowlength; i++) {

                //calculates each product at once
                prod1 = approxLogFactorial(windowlength) - ((approxLogFactorial(i) + approxLogFactorial(windowlength-i)));
                prod2 = i*Math.log(prob);
                prod3 = (windowlength-i)*Math.log(1-prob);


                tmp = Math.exp(prod1 + prod2 + prod3);

                pvalue += tmp;

            }

            //rounds the pvalue
            String pString = Double.toString(pvalue);
            pString = pString.substring(0,4) + pString.substring(pString.length()-5,pString.length());

            //writes in outputfile
            bw.write(snp.chrom + "\t" + snp.id + "\t" + snp.pos + "\t" + winStart + "-" +  winEnd +
                    "\t" + snps + "\t" + pString + "\t" + currentDB.substring(1) + "\t" + windowlength + "\t" + shift + "\n");
            bw.close();

        } catch(Exception e) {

        }
    }

    /**
     * gets the percentage of ClinVar SNPs in dbSNP
     *
     * @param chromosomesdbSNP
     * @param chromosomesClinVar
     * @return ArrayList which contains percentage of every window of each chromosome
     */
    public static ArrayList<ArrayList<Double>> getPercentage(ArrayList<ChromosomeObject> chromosomesdbSNP, ArrayList<ChromosomeObject> chromosomesClinVar) {

        double onePercent = 0.0;
        double percentage = 0.0;
        ArrayList<Double> singleWindows;

        ArrayList<ArrayList<Double>> chromosomeWindows = new ArrayList<ArrayList<Double>>();

        for (int i = 0; i < chromosomesdbSNP.size(); i++) {
            singleWindows = new ArrayList<Double>();

            for (int j = 0; j < chromosomesdbSNP.get(i).windows.size(); j++) {
                if (j >= chromosomesClinVar.get(i).windows.size()) {
                    singleWindows.add(0.0);
                } else {
                    onePercent = round((double) chromosomesdbSNP.get(i).windows.get(j).snpCount / 100, 2);
                    percentage = chromosomesClinVar.get(i).windows.get(j).snpCount / onePercent;
                    if (Double.isNaN(percentage)) {
                        singleWindows.add(0.0);
                    } else {
                        singleWindows.add(round(chromosomesClinVar.get(i).windows.get(j).snpCount / onePercent, 3));
                    }

                }
            }

            chromosomeWindows.add(i, singleWindows);


        }

        return chromosomeWindows;


    }

    /**
     * gets frequency of snps for each chromosome
     *
     * @param chromosomes
     * @return array of frequencies
     */
    public static double[] getFrequency(ArrayList<ChromosomeObject> chromosomes) {


        double[] frequencies = new double[chromlengthArray.length];
        double[] SNPs = new double[chromlengthArray.length];

        for (int i = 0; i <= chromosomes.size() - 1; i++) {
            for (int j = 0; j < chromosomes.get(i).windows.size(); j++) {
                SNPs[i] += chromosomes.get(i).windows.get(j).snpCount;

            }

        }

        for (int i = 0; i <= frequencies.length - 1; i++) {
            frequencies[i] = chromlengthArray[i] / SNPs[i];
        }

        return frequencies;

    }

    /**
     * gets mean of snps in a window for each chromosome
     *
     * @param chromosomes
     * @return array of means
     */
    public static double[] getMean(ArrayList<ChromosomeObject> chromosomes) {

        double[] SNPs = new double[chromlengthArray.length];
        double[] means = new double[chromlengthArray.length];

        for (int i = 0; i <= chromosomes.size() - 1; i++) {
            for (int j = 0; j < chromosomes.get(i).windows.size(); j++) {
                SNPs[i] += chromosomes.get(i).windows.get(j).snpCount;


            }
        }

        for (int i = 0; i <= means.length - 1; i++) {
            means[i] = SNPs[i] / chromosomes.get(i).windows.size();
        }


        return means;

    }

    /**
     * rounds a double variable
     *
     * @param value
     * @param places
     * @return
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;

    }

    public static double logb( double a, double b ) {
        return Math.log(a) / Math.log(b);
    }



    public static double approxLogFactorial(int N) {

        double multi = 0;


        if (N >= 1000) {
            multi = (N * Math.log(N) - N) + 0.5 * Math.log(2 * Math.PI * N) + (1 / 12) * N;
        } else {
            for (int i = 1; i <= N; i++) {
                multi += Math.log(i);

            }
        }

        return multi;
    }


    public long genomeLength() {

        long genomLength = 0;

        for (int i = 0; i < chromlengthArray.length; i++) {
            genomLength += chromlengthArray[i];
        }

        return genomLength;

    }


}




