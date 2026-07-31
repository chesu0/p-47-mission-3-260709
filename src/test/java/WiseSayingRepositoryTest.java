import org.example.WiseSaying;
import org.example.repository.WiseSayingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class WiseSayingRepositoryTest {

    @Test
    @DisplayName("생성 시 저장 폴더 생성")
    void t1() {
        String testDirectoryPath = "testDb/wiseSaying";
        File testDirectory = new File(testDirectoryPath);

        testDirectory.delete();

        new WiseSayingRepository(testDirectoryPath);

        assertThat(testDirectory.exists()).isTrue();
        assertThat(testDirectory.isDirectory()).isTrue();

        testDirectory.delete();
        new File("testDb").delete();
    }

    @Test
    @DisplayName("명언 저장 시 json 파일 생성")
    void t2() throws IOException {
        WiseSayingRepository repository =
                new WiseSayingRepository("testDb/wiseSaying");

        WiseSaying wiseSaying =
                new WiseSaying(1, "명언1", "작가1");

        repository.save(wiseSaying);

        File file = new File("testDb/wiseSaying/1.json");

        assertThat(file.exists()).isTrue();
    }

    @Test
    @DisplayName("명언 저장 시 json 파일에 내용 저장")
    void t3() throws IOException {
        WiseSayingRepository repository =
                new WiseSayingRepository("testDb/wiseSaying");

        WiseSaying wiseSaying =
                new WiseSaying(1, "명언1", "작가1");

        repository.save(wiseSaying);

        String fileContent = Files.readString(
                Path.of("testDb/wiseSaying/1.json")
        );

        assertThat(fileContent)
                .contains("\"id\": 1")
                .contains("\"content\": \"명언1\"")
                .contains("\"author\": \"작가1\"");
    }

    @Test
    @DisplayName("수정을 진행하면 해당 json 파일 내용이 수정")
    void t4() throws IOException {
        WiseSayingRepository repository =
                new WiseSayingRepository("testDb/wiseSaying");

        WiseSaying wiseSaying =
                new WiseSaying(1, "명언1", "작가1");

        repository.save(wiseSaying);

        wiseSaying.setContent("수정된 명언");
        wiseSaying.setAuthor("수정된 작가");

        repository.save(wiseSaying);

        String fileContent = Files.readString(
                Path.of("testDb/wiseSaying/1.json")
        );

        assertThat(fileContent)
                .contains("\"content\": \"수정된 명언\"")
                .contains("\"author\": \"수정된 작가\"");
    }

    @Test
    @DisplayName("data.json 파일이 생성")
    void t10() throws IOException {
        WiseSayingRepository repository =
                new WiseSayingRepository("testDb/wiseSaying");

        repository.save(new WiseSaying(1, "명언1", "작가1"));
        repository.save(new WiseSaying(2, "명언2", "작가2"));

        repository.build();

        String rs = Files.readString(
                Path.of("testDb/wiseSaying/data.json")
        );

        assertThat(rs).contains("\"id\": 1");
        assertThat(rs).contains("\"content\": \"명언1\"");
        assertThat(rs).contains("\"author\": \"작가1\"");

        assertThat(rs).contains("\"id\": 2");
        assertThat(rs).contains("\"content\": \"명언2\"");
        assertThat(rs).contains("\"author\": \"작가2\"");
    }
}