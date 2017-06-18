import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.vcf.VCFFileReader;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kuebler on 10/21/15.
 * Write some stuff for testing (not used)
 */
public class Writer extends Statistics {


    static int index = 0;


    /**
     * calculates and writes information such as window,amount snps in respective database,mean,frequency etc. into output file
     * @param snp
     * @param chromosomesdbSNP
     * @param chromosomesClinVar
     */
    public void writeInfoAboutSnp(SNPObject snp, ArrayList<ChromosomeObject> chromosomesdbSNP, ArrayList<ChromosomeObject> chromosomesClinVar) {

        BufferedWriter bw = null;

        double[] meanClinVar = getMean(chromosomesClinVar);
        double[] meandbSNP = getMean(chromosomesdbSNP);
        ArrayList<ArrayList<Double>> percentages = getPercentage(chromosomesdbSNP,chromosomesClinVar);

        double[] freqClinVar = getFrequency(chromosomesClinVar);
        double[] freqdbSNP = getFrequency(chromosomesdbSNP);

        int windowOneCountClinVar;
        int windowTwoCountClinVar;
        int windowThreeCountClinVar;
        int windowOneCountdbSNP = 0;
        int windowTwoCountdbSNP = 0;
        int windowThreeCountdbSNP = 0;
        int startOne = 0;
        int startTwo = 0;
        int startThree = 0;
        int endOne = 0;
        int endTwo = 0;
        int endThree = 0;
        double meanDevdbSNPOne = 0;
        double meanDevdbSNPTwo = 0;
        double meanDevdbSNPThree = 0;
        double meanDevClinVarOne = 0;
        double meanDevClinVarTwo = 0;
        double meanDevClinVarThree = 0;


        try {
            bw = new BufferedWriter((new FileWriter("Output.csv", true)));
            bw.write("Chromosome\t" + "Windows\t" + "SNPs_in_ClinVar\t" + "SNPs_in_dbSNP\t" + "Dev_Mean_Clinvar(" + round(meanClinVar[snp.chrom - 1], 3) + ")\t" + "Dev_Mean_dbSNP(" + round(meandbSNP[snp.chrom - 1], 3) + ")\t"
                    + "Percentage\t" + "Frequency_ClinVar\t" + "Frequency_dbSNP\t" + "\n");


            for (int i = 0; i < chromosomesdbSNP.get(snp.chrom - 1).windows.size(); i++) {
                if (snp.pos < chromosomesdbSNP.get(snp.chrom - 1).windows.get(i).start) {
                    index = i;
                    break;
                }

            }

            if (index > chromosomesClinVar.get(snp.chrom - 1).windows.size()-2) {
                windowOneCountClinVar = 0;
                windowOneCountdbSNP = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 2).snpCount;
                startOne = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 2).start;
                endOne = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 2).end;
                if(index > chromosomesdbSNP.get(snp.chrom-1).windows.size()-2) {
                    windowOneCountdbSNP = 0;
                    startOne = 0;
                    endOne = 0;
                }
            } else {
                windowOneCountClinVar = chromosomesClinVar.get(snp.chrom - 1).windows.get(index - 2).snpCount;
                windowOneCountdbSNP = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 2).snpCount;
                startOne = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 2).start;
                endOne = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 2).end;

            }

            if (index > chromosomesClinVar.get(snp.chrom - 1).windows.size() - 1) {
                windowTwoCountClinVar = 0;
                windowTwoCountdbSNP = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 1).snpCount;
                startTwo = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 1).start;
                endTwo = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 1).end;
                if(index > chromosomesdbSNP.get(snp.chrom-1).windows.size()-1) {
                    windowTwoCountdbSNP = 0;
                    startTwo = 0;
                    endTwo = 0;
                }
            } else {
                windowTwoCountClinVar = chromosomesClinVar.get(snp.chrom - 1).windows.get(index - 1).snpCount;
                windowTwoCountdbSNP = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 1).snpCount;
                startTwo = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 1).start;
                endTwo = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 1).end;
            }

            if (index > chromosomesClinVar.get(snp.chrom - 1).windows.size()) {
                windowThreeCountClinVar = 0;
                windowThreeCountdbSNP = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index).snpCount;
                startThree = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index).start;
                endThree = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index).end;
                if(index > chromosomesdbSNP.get(snp.chrom - 1).windows.size()) {
                    windowThreeCountdbSNP = 0;
                    startThree = 0;
                    endThree = 0;
                }
            } else {
                windowThreeCountClinVar = chromosomesClinVar.get(snp.chrom - 1).windows.get(index).snpCount;
                windowThreeCountdbSNP = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index).snpCount;
                startThree = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index).start;
                endThree = chromosomesdbSNP.get(snp.chrom - 1).windows.get(index).end;
            }

             meanDevdbSNPTwo = round((Math.abs(meandbSNP[snp.chrom - 1] - windowTwoCountdbSNP) / (meandbSNP[snp.chrom - 1]) * 100), 2);
             meanDevClinVarTwo = round((Math.abs(meanClinVar[snp.chrom - 1] - windowTwoCountClinVar) / (meanClinVar[snp.chrom - 1]) * 100), 2);
             meanDevdbSNPOne = round((Math.abs(meandbSNP[snp.chrom - 1] - windowOneCountdbSNP) / (meandbSNP[snp.chrom - 1]) * 100), 2);
             meanDevClinVarOne = round((Math.abs(meanClinVar[snp.chrom - 1] - windowOneCountClinVar) / (meanClinVar[snp.chrom - 1]) * 100), 2);
             meanDevdbSNPThree = round((Math.abs(meandbSNP[snp.chrom - 1] - windowThreeCountdbSNP) / (meandbSNP[snp.chrom - 1]) * 100), 2);
             meanDevClinVarThree = round((Math.abs(meanClinVar[snp.chrom - 1] - windowThreeCountClinVar) / (meanClinVar[snp.chrom - 1]) * 100), 2);

            if(meanDevClinVarOne == 100.00)
                meanDevClinVarOne = 0;
            if(meanDevClinVarTwo == 100.00)
                meanDevClinVarTwo = 0;
            if(meanDevClinVarThree == 100.00)
                meanDevClinVarThree = 0;
            if(meanDevdbSNPOne == 100.00)
                meanDevdbSNPOne = 0;
            if(meanDevdbSNPTwo == 100.00)
                meanDevdbSNPTwo = 0;
            if(meanDevdbSNPThree == 100.00)
                meanDevdbSNPThree = 0;


           if(index == 0) {
               index=1;
           }

            if (index > 1) {

                bw.write(snp.chrom + "\t" + startOne + "-" + endOne + "\t"
                        + windowOneCountClinVar + "\t" + windowOneCountdbSNP + "\t"
                        + meanDevClinVarOne + "%\t" + meanDevdbSNPOne + "%\t" + percentages.get(snp.chrom-1).get(index-2) + "%\t" + round(freqClinVar[snp.chrom - 1], 2) + "\t"
                        + round(freqdbSNP[snp.chrom - 1], 2) + "\t");

                bw.newLine();

            }

            bw.write(snp.chrom + "\t" + startTwo + "-" + endTwo + "\t"
                    + windowTwoCountClinVar + "\t" + windowTwoCountdbSNP + "\t"
                    + meanDevClinVarTwo + "%\t" + meanDevdbSNPTwo + "%\t" + percentages.get(snp.chrom-1).get(index-1) + "%\t" + round(freqClinVar[snp.chrom-1],2) + "\t"
                    + round(freqdbSNP[snp.chrom-1],2) + "\t");

            bw.newLine();

            if(index > 1) {

                bw.write(snp.chrom + "\t" + startThree + "-" + endThree + "\t"
                        + windowThreeCountClinVar + "\t" + windowThreeCountdbSNP + "\t"
                        + meanDevClinVarThree + "%\t" + meanDevdbSNPThree + "%\t"  + percentages.get(snp.chrom-1).get(index) + "%\t" + round(freqClinVar[snp.chrom - 1], 2) + "\t"
                        + round(freqdbSNP[snp.chrom - 1], 2) + "\t" + "\n");

            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void writePercentages(ArrayList<ArrayList<Double>> percentages) {


        int count = 0;
        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter("Percentages.csv"));

            for (int i = 0; i < percentages.size(); i++) {
                for (int j = 0; j < percentages.get(i).size(); j++) {
                    count++;
                    bw.write(count + "\t" + percentages.get(i).get(j).toString() + "\n");
                }
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }



    /*public static void variableScore(SNPObject snp) {

        int ClinVarCount = chromosomesClinVar.get(snp.chrom - 1).windows.get(index - 2).snpCount + chromosomesClinVar.get(snp.chrom - 1).windows.get(index - 1).snpCount
                            + chromosomesClinVar.get(snp.chrom - 1).windows.get(index).snpCount;
        int dbSNPCount =  chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 2).snpCount + chromosomesdbSNP.get(snp.chrom - 1).windows.get(index - 1).snpCount
                        + chromosomesdbSNP.get(snp.chrom - 1).windows.get(index).snpCount;

        for (int i = 0; i < chromosomesdbSNP.get(snp.chrom - 1).windows.size(); i++) {
            if (snp.pos < chromosomesdbSNP.get(snp.chrom - 1).windows.get(i).start) {
                index = i;
                break;
            }

        }*/








}
