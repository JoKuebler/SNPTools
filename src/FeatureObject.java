import java.io.Serializable;

/**
 * Class to represent a feature and its attributes.
 */
public class FeatureObject implements Serializable {

    String annot;
    String name;
    String ID;
    int start;
    int end;
    int chrom;

    public FeatureObject() {
        this.annot = "";
        this.name = "";
        this.ID = "";
        this.start = -1;
        this.end = -1;
        this.chrom = -1;
    }
}
