import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс, где собраны все операции с графами
 */
public class GraphOperations {
    private final static Map<FileGraphNode, FileGraphNode> parent;
    static {
        parent = new HashMap<>();
    }
    private static FileGraphNode cycleStart;
    private static FileGraphNode cycleEnd;

    /**
     * Алгоритм DFS, который проверяет наличие циклов в графе
     * @param node узел графа
     * @return есть ли цикл
     */
    private static boolean dfsCheckCycles(FileGraphNode node) {
        node.setUsed(1);
        for (FileGraphNode to : node.getChildrenList()) {
            if (to.getFilePath().toFile().exists()) {
                if (to.usedEquals(1)) {
                    cycleEnd = node;
                    cycleStart = to;
                    return true;
                } else {
                    if (to.usedEquals(0)) {
                        parent.put(node, to);
                        return dfsCheckCycles(to);
                    }
                }
            }
        }
        node.setUsed(2);
        return false;
    }

    /**
     * Алгоритм топологической сортировки
     * @param node узел графа
     * @param answer список топологически отсортированного графа
     */
    private static void dfsTopologicalSort(FileGraphNode node, List<FileGraphNode> answer) {
        node.setUsed(1);
        for (FileGraphNode to : node.getChildrenList()) {
            if (to.usedEquals(0)) {
                dfsTopologicalSort(to, answer);
            }
        }
        answer.add(node);
    }

    private static void init(Collection<FileGraphNode> graph) {
        for (FileGraphNode node : graph) {
            node.setUsed(0);
        }
        parent.clear();
        cycleStart = null;
        cycleEnd = null;
    }

    /**
     * Проверка на цикличность
     * @param graph граф
     * @return при нахождении цикла
     */
    public static boolean checkCycles(Collection<FileGraphNode> graph) {
        init(graph);

        for (FileGraphNode node : graph) {
            if (dfsCheckCycles(node)) {
                break;
            }
        }

        if (cycleStart == null) {
            return false;
        } else {
            System.out.println("Нашелся цикл:");
            System.out.print(cycleStart.getFilePath() + " ");
            while (cycleStart != cycleEnd) {
                cycleStart = parent.get(cycleStart);
                System.out.print(cycleStart.getFilePath() + " ");
            }
            System.out.println();
            return true;
        }
    }

    /**
     * Топологическая сортировка и печать файлов
     * @param graph граф
     */
    public static void printByTopologicalSorted(Collection<FileGraphNode> graph) {
        if (checkCycles(graph)) {
            return;
        }
        for (FileGraphNode node : graph) {
            node.setUsed(0);
        }
        List<FileGraphNode> answer = new ArrayList<>();
        for (FileGraphNode node : graph) {
            if (node.usedEquals(0)) {
                dfsTopologicalSort(node, answer);
            }
        }
        Collections.reverse(answer);
        for (FileGraphNode node : answer) {
            try (BufferedReader br = new BufferedReader(new FileReader(node.getFilePath().toFile()))) {
                String line;
                System.out.println(node.getFilePath());
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException ex) {
                throw new RuntimeException("Возникло исключение при печати файла {}", ex);
            }
        }
    }
}