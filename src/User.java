import java.util.Scanner;

/**
 * A class to give to user the possibility to choose data (not used)
 * @author kuebler
 *
 */
public class User {

    public static String readFilePath() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path (1 for dbSNP/2 for Clinvar): ");
        System.out.flush();
        String filename = scanner.nextLine();
        if (filename.equals("1"))
            filename = "/home/kueblerj/phixshare/kueblerj/dbsnp_137.b37.vcf";
        if (filename.equals("2"))
            filename = "/home/kueblerj/phixshare/kueblerj/grch37.vcf";


        return filename;

    }

    public static SNPObject readSNP() {

        SNPObject snp = new SNPObject();
        Scanner scanner = new Scanner(System.in);
       /* System.out.println("Reference Base: ");
        System.out.flush();
        String ref = scanner.nextLine();
        snp.ref = ref;

        System.out.println("Alternate Allele: ");
        System.out.flush();
        String alt = scanner.nextLine();
        snp.alt = alt;*/


        System.out.println("Chromosome: ");
        System.out.flush();
        String chrom = scanner.nextLine();
        if (chrom.equals("X")) {
            snp.chrom = Integer.parseInt("23");
        } else if (chrom.equals("Y")) {
            snp.chrom = Integer.parseInt("24");
        } else if (chrom.equals("MT")) {
            snp.chrom = Integer.parseInt("25");
        } else {
            snp.chrom = Integer.parseInt(chrom);
        }



        System.out.println("Position: ");
        System.out.flush();
        String pos = scanner.nextLine();
        snp.pos = Integer.parseInt(pos);

        return snp;

    }


    public static int getWindowLength() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter window length: ");
        System.out.flush();
        String windowLength = scanner.nextLine();

        return Integer.parseInt(windowLength);

    }


    public static int getShift() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter shift: ");
        System.out.flush();
        String shift = scanner.nextLine();

        return Integer.parseInt(shift);

    }

}
