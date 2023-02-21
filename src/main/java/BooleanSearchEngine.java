import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private Set<String> words;

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        // прочтите тут все pdf и сохраните нужные данные,
        // тк во время поиска сервер не должен уже читать файлы
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        words = new HashSet<>(List.of(word.split("\\P{IsAlphabetic}+")));

//        return words.contains(word);
        return Collections.emptyList();
            }
    public boolean hasWord(String word) {
        return words.contains(word);
    }
}
