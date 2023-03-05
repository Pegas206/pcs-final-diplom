import org.json.simple.JSONObject;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName.substring(5);
        this.page = page;
        this.count = count;
    }


    @Override
    public int compareTo(PageEntry o) {

        return 0;
    }

}
