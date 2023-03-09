import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    private int k = 0;

    public BooleanSearchEngine(File pdfsDir) throws IOException {

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
            k++;
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
    public String search(String word) {
        String json = null;
        if (finalListOfWords.containsKey(word)) {
            List<PageEntry> ListKey = finalListOfWords.get(word);
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            try {
                json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ListKey);
                System.out.println(json);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        return json;
    }
}

