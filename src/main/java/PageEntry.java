public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {
//
//    if (getSurname().length() < o.surname.length()) {
//        return 1;
//    } else if (getSurname().length() > o.surname.length()) {
//        return -1;
//    } else if (getAge() < o.age){
//        return 1;
//    }else if (getAge() > o.age){
//        return -1;
//    }
        return 0;
}

}
