package org.example.repository;

import org.example.WiseSaying;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class WiseSayingRepository {

    private final String directoryPath;

    public WiseSayingRepository() {
        this("db/wiseSaying");
    }

    private void init() {
        File directory = new File(directoryPath);
        directory.mkdirs();
    }

    public WiseSayingRepository(String directoryPath) {
        this.directoryPath = directoryPath;
        init();
    }

    public void save(WiseSaying wiseSaying) throws IOException {
        File file = new File(directoryPath + "/" + wiseSaying.getId() + ".json");
        file.createNewFile();

        String json = """
                {
                "id": %d,
                "content": "%s",
                "author": "%s"
                }
                """.formatted(
                wiseSaying.getId(),
                wiseSaying.getContent(),
                wiseSaying.getAuthor()
        );
        Files.writeString(file.toPath(), json);
    }

    public void saveLastId(int lastId) throws IOException {
        File file = new File(directoryPath + "/lastId.txt");

        Files.writeString(file.toPath(), String.valueOf(lastId));
    }

    public int loadLastId() throws IOException {
        File file = new File(directoryPath + "/lastId.txt");

        if (!file.exists()) {
            return 0;
        }
        String text = Files.readString(file.toPath());
        int lastId = Integer.parseInt(text);
        return lastId;
    }

    public void build() throws IOException {

        File dir = new File(directoryPath);

        File[] files = dir.listFiles((d, name) ->
                name.endsWith(".json") &&
                        !name.equals("data.json"));

        Arrays.sort(files);
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < files.length; i++) {
            String json = Files.readString(files[i].toPath());
            sb.append(json);

            if (i != files.length - 1) {
                sb.append(",\n");
            }
        }

        sb.append("\n]");

        Files.writeString(
                Path.of(directoryPath, "data.json"),
                sb.toString()
        );
    }

    // 해당 번호의 명언 파일 삭제
    public void delete(int id) throws IOException {
        Path filePath = Path.of(
                directoryPath,
                id + ".json"
        );

        Files.deleteIfExists(filePath);
    }
}