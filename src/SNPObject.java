/**
 * class to represent a SNP and its attributes
 */
public class SNPObject {

    int chrom;
    int pos;
    String id;
    String ref;
    String alt;
    String qual;
    String filter;
    String info;

    public SNPObject(int chrom, int pos, String id, String ref, String alt,
                     String qual, String filter, String info) {
        this.chrom = chrom;
        this.pos = pos;
        this.id = id;
        this.ref = ref;
        this.alt = alt;
        this.qual = qual;
        this.filter = filter;
        this.info = info;
    }

    public SNPObject() {
        this.chrom = -1;
        this.pos = -1;
        this.id = null;
        this.ref = null;
        this.alt = null;
        this.qual = null;
        this.filter = null;
        this.info = null;

    }

    public int getChrom() {
        return chrom;
    }

    public void setChrom(int chrom) {
        this.chrom = chrom;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {this.id = id;}

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getQual() {
        return qual;
    }

    public void setQual(String qual) {
        this.qual = qual;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
