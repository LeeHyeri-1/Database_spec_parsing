import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileReader {
    private File filename;

    public FileReader(String filePath) {
        this.filename = new File(filePath);
    }

    public String readFile() {  // private에서 public으로 변경
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}