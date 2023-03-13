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
    private HashMap<String, List<PageEntry>> finalListOfWords = new HashMap<String, List<PageEntry>>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        listFiles(pdfsDir.toPath());
        // Перебор всех документов из списка и чтение
        for (int k = 0; k < listPDF.size(); k++) {
            var doc = new PdfDocument(new PdfReader(listPDF.get(k).toFile()));
            // Получаю количество страниц
            int numberOfPages = doc.getNumberOfPages();
            // Перебираю все страницы и перевожу в мапу
            for (int i = 1; i <= numberOfPages; i++) {
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
                for (Map.Entry<String, Integer> entry: freqs.entrySet())

                {   // get key
                    String keyItr = entry.getKey();
                    // get value
                    int valueItr = entry.getValue();

                    //Проверяю есть ли повторяющиеся слова, если есть добавляю в List
                    if (finalListOfWords.containsKey(keyItr)) {
                        List<PageEntry> ListValues = finalListOfWords.get(keyItr);

                        // Получаю предыдущее значение списка данного слова
                        ListValues.add(new PageEntry(String.valueOf(listPDF.get(k)), i, valueItr));
                        ListValues.sort((o1, o2) -> {
                            if (o1.getCount() == o2.getCount()) {
                                return 0;
                            } else if (o1.getCount() < o2.getCount()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        });
                        //  И здесь я добавляю в ключ новое значение и старое
                        finalListOfWords.put(keyItr, ListValues);
                    } else {
                        finalListOfWords.put(keyItr, new ArrayList(Collections.singleton(new PageEntry(String.valueOf(listPDF.get(k)), i, valueItr))));
                        System.out.println();
                    }
                }
            }
        }
        System.out.println("Документы обработаны");
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
    public Object search(String word) {

        if (finalListOfWords.containsKey(word)) {
            List<PageEntry> ListKey = finalListOfWords.get(word);
return ListKey;

        }
        return null;
    }
}

