import java.util.ArrayList;

/**
 * Class to represent a chromosome and its attributes
 * Represents a chromosome and it's windows + features
 */
public class ChromosomeObject {

    int length;
    ArrayList<WindowsObject> windows = new ArrayList<WindowsObject>();
    ArrayList<FeatureObject> features = new ArrayList<FeatureObject>();


    public ChromosomeObject() {
        this.length = -1;
        this.windows = new ArrayList<WindowsObject>();
        this.features = new ArrayList<FeatureObject>();
    }

}


