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
    private int k = 0;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы

        // Найти все PDF-документ в папке pdfs.
        listFiles(pdfsDir.toPath());

        // Перебор всех документов из списка и чтение
        Iterator<Path> itr = listPDF.iterator();
        while (itr.hasNext()) {
            System.out.println(listPDF.get(k));
            k++;
            var doc = new PdfDocument(new PdfReader(itr.next().toFile()));
            // Получаю количество страниц
            int number_of_pages = doc.getNumberOfPages();
            // Перебираю все страницы и перевожу в мапу
            for (int i = 1; i <= number_of_pages; i++) {
                System.out.println("Страница " + i);

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
                System.out.println(freqs);

                // в чем хранить мапу?????
//                Iterator<Map.Entry<String, Integer>> itrMap = freqs.entrySet().iterator();
//                while(itrMap.hasNext()) {
//                    Map.Entry<String, Integer> entry = itrMap.next();
//                    // get key
//                    String key = entry.getKey();
//                    // get value
//                    int value = entry.getValue();


//                }
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
    public List<PageEntry> search(String word) {

        // тут реализуйте поиск по слову
System.out.println(word);
        return null;
    }
}
