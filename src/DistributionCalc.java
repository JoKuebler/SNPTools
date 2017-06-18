

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kuebler on 11/18/15.
 * Functionality for Window Calculation to get SNP distribution
 */
public class DistributionCalc {

    /**
     * calculates SNPs in variable windowlength with variable shift over the whole file and writes it into tsv file
     *
     * @param input,
     * @param windowLength
     * @param shift
     */
    public void WindowCalculator(String input, String outputDirectory, String outputFile, int windowLength, int shift) {

        BufferedReader br1;
        BufferedWriter bw1;

        //initialize chrom length (hg19)
        int chromlengthArray[] = {249250621, 243199373, 198022430, 191154276, 180915260, 171115067, 159138663,
                146364022, 141213431, 135534747, 135006516, 133851895, 115169878, 107349540, 102531392,
                90354753, 81195210, 78077248, 59128983, 63025520, 48129895, 51304566, 155270560, 59373566, 16569};

        try {
            br1 = new BufferedReader(new FileReader(input));

            //if no outputdirectory chose current one
            if(outputDirectory == null) {
                String current = new File(".").getCanonicalPath();
                outputDirectory = current;
            }

            //initialize buffered writer for writing output file
            if(outputFile == null) {
                bw1 = new BufferedWriter(new OutputStreamWriter(System.out));
            } else {
                bw1 = new BufferedWriter((new FileWriter(outputDirectory + "/" + "SNPTools" + outputFile + "W"
                        + windowLength + "S" + shift +  ".tsv",true)));
            }

            bw1.write("#CHROM\t" + "START\t" + "END\t" + "SNP\n");
            String line;
            String delimiter = "\t";
            int currentPos = 1;
            int currentStart = 0;
            int currentEnd = windowLength;
            int currentChrom = 0;
            int lastChrom = 1;
            List<Integer> workingWindow = new LinkedList<Integer>();


            //counts first window
            while ((line = br1.readLine()) != null) {

                //skips header
                if (!line.contains("#")) {
                    String[] dataArray = line.split(delimiter);
                    currentPos = Integer.parseInt(dataArray[1]);
                    dataArray[0] = dataArray[0].replaceAll("[^0-9]", "");
                    currentChrom = Integer.parseInt(dataArray[0]);

                    //counts first window of genom and writes it in output file
                    if (currentPos < windowLength) {
                        workingWindow.add(currentPos);
                    } else {
                        bw1.write(currentChrom + "\t" + currentStart + "\t" + currentEnd + "\t" + workingWindow.size() + "\n");
                        break;
                    }

                }

            }

            //first shift
            currentStart += shift;
            currentEnd += shift;

            //goes through database file and calculates windows
            while (line != null) {

                String[] dataArray = line.split(delimiter);
                currentPos = Integer.parseInt(dataArray[1]);
                dataArray[0] = dataArray[0].replaceAll("[^0-9,X,Y,MT]", "");

                //to change
                if (dataArray[0].equals("X")) {
                    dataArray[0] = "23";
                } else if (dataArray[0].equals("Y")) {
                    dataArray[0] = "24";
                } else if (dataArray[0].equals("MT")) {
                    dataArray[0] = "25";
                }

                currentChrom = Integer.parseInt(dataArray[0]);

                //removes all SNPs left from the current window
                while (workingWindow.size() != 0 && workingWindow.get(0) < currentStart) {
                    workingWindow.remove(0);
                }


                //  if (currentPos > currentEnd) {

                //checks if currentSNP is already in next window and if so write the size of the current working window in file
                if (currentPos > currentStart + windowLength) {
                    bw1.write(currentChrom + "\t" + currentStart + "\t" + currentEnd + "\t" + workingWindow.size() + "\n");
                    bw1.flush();
                    currentStart += shift;
                    currentEnd += shift;
                    if(currentEnd > chromlengthArray[currentChrom-1]) {
                        currentEnd = chromlengthArray[currentChrom-1];
                    }

                    //if current SNP is still in currentWindow then add it to file and look at next line
                } else if (currentPos <= currentStart + windowLength && currentPos >= currentStart) {  //
                    workingWindow.add(currentPos);
                    line = br1.readLine();

                } else if (currentPos < currentStart) {

                    lastChrom++;
                    //last window of currentChrom
                    bw1.write((currentChrom - 1) + "\t" + currentStart + "\t" + currentEnd + "\t" + workingWindow.size() + "\n");

                    //adds windows after last snp until last pos of chromosome is reached
                        while (currentEnd < chromlengthArray[currentChrom - 2]) {
                            currentStart += shift;
                            currentEnd += shift;
                            if (currentEnd > chromlengthArray[currentChrom - 2]) {
                                currentEnd = chromlengthArray[currentChrom - 2];
                            }

                            while (workingWindow.size() != 0 && workingWindow.get(0) < currentStart) {
                                workingWindow.remove(0);
                            }

                            bw1.write((currentChrom - 1) + "\t" + currentStart + "\t" + currentEnd + "\t" + workingWindow.size() + "\n");
                            bw1.flush();

                        }



                    //empty working window for new chromosome
                    workingWindow.clear();

                    currentStart = 0;
                    currentEnd = windowLength;


                    //first window of new chromosome
                    while (currentPos < windowLength) {
                        workingWindow.add(currentPos);
                        line = br1.readLine();
                        if (line == null) {
                            break;
                        }
                        dataArray = line.split(delimiter);
                        currentPos = Integer.parseInt(dataArray[1]);

                    }

                    if(currentEnd > chromlengthArray[24] && currentChrom == 25) {
                        bw1.write(currentChrom + "\t" + currentStart + "\t" + chromlengthArray[24] + "\t" + workingWindow.size() + "\n");
                    } else {
                        //write first window of new chromosome in file
                        bw1.write(currentChrom + "\t" + currentStart + "\t" + currentEnd + "\t" + workingWindow.size() + "\n");
                        //first shift of new chromosome
                        currentStart += shift;
                        currentEnd += shift;
                    }

                }

            }

            if(currentEnd < chromlengthArray[24]) {
                //last window of last chromosome
                bw1.write(currentChrom + "\t" + currentStart + "\t" + chromlengthArray[24] + "\t" + workingWindow.size() + "\n");
            }

            bw1.flush();
            bw1.close();
            br1.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
