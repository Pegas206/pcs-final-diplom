import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BooleanSearchEngine implements SearchEngine {
    private Set<String> words;
    private List<Path> listPDF;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы

        // Найти все PDF-документ в папке pdfs.
        listFiles(pdfsDir.toPath());

        // Перебор всех документов из списка и чтение
        Iterator<Path> itr = listPDF.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());

            var doc = new PdfDocument(new PdfReader(itr.next().toFile()));
            // Получаю количество страниц
            int number_of_pages = doc.getNumberOfPages();
            // Перебираю все страницы и перевожу в мапу
            for (int i = 0; i < number_of_pages; i++) {

                PdfPage page = doc.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");
                System.out.println(words);

                Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
            }
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
        return null;
        // тут реализуйте поиск по слову
//        words = new HashSet<>(List.of(word.split("\\P{IsAlphabetic}+")));
//
////        return words.contains(word);
//        return Collections.emptyList();
//    }
//
//    public boolean hasWord(String word) {
//        return words.contains(word);
    }
}
