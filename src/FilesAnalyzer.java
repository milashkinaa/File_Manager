import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Класс для анализа файлов и построения графика по ним
 */
public class FilesAnalyzer {
    private final Map<String, FileGraphNode> graphNodeMap;

    public FilesAnalyzer() {
        this.graphNodeMap = new HashMap<>();
    }

    /**
     * Рекурсивное добавление файлов в граф
     * @param path путь к узлу
     */
    public void recursiveBuild(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            throw new RuntimeException("Неправильный путь: " + dir.getPath());
        }
        if (dir.isFile()) {
            FileGraphNode node;
            if (!graphNodeMap.containsKey(dir.getPath())) {
                node = new FileGraphNode(dir.getPath());
                graphNodeMap.put(dir.getPath(), node);
            } else {
                node = graphNodeMap.get(dir.getPath());
            }
            analyzeFile(node);
        } else {
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                recursiveBuild(file.getPath());
            }
        }
    }

    /**
     * Анализ содержимого файла и добавление новых ребер к графу
     * @param node узел графа для анализа
     */
    private void analyzeFile(FileGraphNode node) {
        try (BufferedReader br = new BufferedReader(new FileReader(node.getFilePath().toFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (Pattern.matches("require '.+'", line)) {
                    String parentPath = line.substring(9, line.length() - 1);
                    if (Path.of(parentPath).toFile().exists()) {
                        if (!graphNodeMap.containsKey(parentPath)) {
                            graphNodeMap.put(parentPath, new FileGraphNode(parentPath));
                        }
                        graphNodeMap.get(parentPath).getChildrenList().add(node);
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Возникло исключение при чтении файла: {}", ex);
        }
    }

    public Collection<FileGraphNode> getValues() {
        return graphNodeMap.values();
    }
}