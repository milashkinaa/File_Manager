import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Здравствуйте! Введите, пожалуйста, путь в формате C:\\Users\\user\\path\\file.txt");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        FilesAnalyzer filesAnalyzer = new FilesAnalyzer();
        try {
            filesAnalyzer.recursiveBuild(path);
            GraphOperations.printByTopologicalSorted(filesAnalyzer.getValues());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}