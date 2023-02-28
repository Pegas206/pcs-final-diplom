import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BooleanSearchEngine implements SearchEngine {
    private List<Path> listPDF;
    private HashMap<Object, List<PageEntry>> wordItog = new HashMap<Object, List<PageEntry>>();
    private int k = 0;
private String itogKey;
    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы

        // Найти все PDF-документ в папке pdfs.
        listFiles(pdfsDir.toPath());

        // Перебор всех документов из списка и чтение
        Iterator<Path> itr = listPDF.iterator();
        while (itr.hasNext()) {
            var doc = new PdfDocument(new PdfReader(itr.next().toFile()));
            // Получаю количество страниц
            int number_of_pages = doc.getNumberOfPages();
            // Перебираю все страницы и перевожу в мапу
            for (int i = 1; i <= number_of_pages; i++) {

                PdfPage page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }


                // в чем хранить
                Iterator<Map.Entry<String, Integer>> itrMap = freqs.entrySet().iterator();
                while (itrMap.hasNext()) {
                    Map.Entry<String, Integer> entry = itrMap.next();
                    // get key
                    String keyItr = entry.getKey();
                    // get value
                    int valueItr = entry.getValue();
//Проверяю есть ли повторяющиеся слова, если есть добавляю в List
                    if (wordItog.containsKey(keyItr)) {
                        List<List<PageEntry>> ListofValues = null;
//                        Получаю предыдущее значение списка данного слова
                         ListofValues = Collections.singletonList(wordItog.get(keyItr));
//                         И здесь я добавляю в ключ новое значение и старое
                        wordItog.put(keyItr, List.of(new PageEntry(String.valueOf(listPDF.get(k)), i, valueItr), new PageEntry(String.valueOf(listPDF.get(k)), i, valueItr)));
//                                .toArray(new Set[]{Collections.singleton(new PageEntry(String.valueOf(listPDF.get(k)), i, valueItr))});
                    } else {
    wordItog.put(keyItr, new ArrayList(Collections.singleton(new PageEntry(String.valueOf(listPDF.get(k)), i, valueItr))));
System.out.println();
}
                }
            }
        }
        System.out.println("Документы обработаны");
        System.out.println(wordItog);

    }

    public static void updateValue(Map<String, Integer> map, String key, Integer value) {
        if (map.containsKey(key)) {
            map.put(key, map.get(key) + value);
        } else {
            map.put(key, value);
        }
    }
    // данный метод ищет все PDF-документ в папке pdfs и заносит в список.
    public List<Path> listFiles(Path path) throws IOException {


        try (Stream<Path> walk = Files.walk(path)) {
            listPDF = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }
        return listPDF;

    }

    @Override
    public List<PageEntry> search(String word) {

        // тут реализуйте поиск по слову

        return null;
    }
}
