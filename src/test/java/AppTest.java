import org.example.App;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    @BeforeEach
    void beforeEach() {
        // 이전 테스트에서 생성된 DB 파일 제거
        deleteDirectory(new File("db"));
    }

    private void deleteDirectory(File file) {
        if (!file.exists()) {
            return;
        }

        File[] childFiles = file.listFiles();

        if (childFiles != null) {
            for (File childFile : childFiles) {
                deleteDirectory(childFile);
            }
        }

        file.delete();
    }

    private String runAppTest(String input) throws IOException {
        Scanner sc = new Scanner(
                new ByteArrayInputStream(input.getBytes())
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(outputStream));

            App app = new App(sc);
            app.run();

            return outputStream.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    @DisplayName("명언을 한번 등록하면 번호가 1번등록된걸로 출력")
    void t1() throws IOException{
        String input = """
            등록
            나 자신을 알라
            소크라테스
            종료
            """;

        String output = runAppTest(input);

        assertThat(output)
                .contains("1번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("명언을 두 번 등록하면 번호가 1번, 2번으로 증가 확인")
    void t2() throws IOException{
        String input = """
            등록
            나 자신을 알라
            소크라테스
            등록
            천 리 길도 한 걸음부터
            노자
            종료
            """;

        String output = runAppTest(input);

        assertThat(output)
                .contains("1번 명언이 등록되었습니다.")
                .contains("2번 명언이 등록되었습니다.");
    }

    @Test
    @DisplayName("등록된 명언 목록이 출력")
    void t3() throws IOException{
        String input = """
            등록
            나 자신을 알라
            소크라테스
            등록
            천 리 길도 한 걸음부터
            노자
            목록
            종료
            """;

        String output = runAppTest(input);

        assertThat(output)
                .contains("번호 / 작가 / 명언")
                .contains("2 / 노자 / 천 리 길도 한 걸음부터")
                .contains("1 / 소크라테스 / 나 자신을 알라");
    }

    @Test
    @DisplayName("명언 목록은 최신 등록순으로 출력")
    void t4() throws IOException{
        String input = """
            등록
            나 자신을 알라
            소크라테스
            등록
            천 리 길도 한 걸음부터
            노자
            목록
            종료
            """;

        String output = runAppTest(input);

        int second = output.indexOf("2 / 노자 / 천 리 길도 한 걸음부터");
        int first = output.indexOf("1 / 소크라테스 / 나 자신을 알라");

        assertThat(second).isLessThan(first);
    }

    @Test
    @DisplayName("명언 삭제하기")
    void t5() throws IOException{
        String input = """
            등록
            현재를 사랑하라.
            작자미상
            등록
            과거에 집착하지 마라.
            작자미상
            삭제?id=1
            종료
            """;

        String output = runAppTest(input);

        assertThat(output)
                .contains("1번 명언이 삭제되었습니다.");
    }

    @Test
    @DisplayName("없는 명언은 삭제할 수 없다")
    void t6() throws IOException{
        String input = """
            등록
            현재를 사랑하라.
            작자미상
            삭제?id=1
            삭제?id=1
            종료
            """;

        String output = runAppTest(input);

        assertThat(output)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @Test
    @DisplayName("삭제된 명언은 목록에 출력되지 않는다.")
    void t7() throws IOException{
        String input = """
            등록
            현재를 사랑하라.
            작자미상
            등록
            과거에 집착하지 마라.
            작자미상
            삭제?id=1
            목록
            종료
            """;

        String output = runAppTest(input);

        assertThat(output)
                .contains("2 / 작자미상 / 과거에 집착하지 마라.")
                .doesNotContain("1 / 작자미상 / 현재를 사랑하라.");
    }

    @Test
    @DisplayName("수정기능 확인하기")
    void t8() throws IOException{
        String input = """
            등록
            현재를 사랑하라.
            작자미상
            수정?id=1
            현재와 자신을 사랑하라.
            홍길동
            목록
            종료
            """;

        String output = runAppTest(input);

        assertThat(output)
                .contains("현재와 자신을 사랑하라.")
                .contains("홍길동");
    }

    @Test
    @DisplayName("명언을 삭제하면 저장된 json 파일도 삭제")
    void t9() throws IOException {
        String input = """
            등록
            현재를 사랑하라.
            작자미상
            삭제?id=1
            종료
            """;

        runAppTest(input);

        File savedFile =
                new File("db/wiseSaying/1.json");

        assertThat(savedFile.exists()).isFalse();
    }

    @Test
    @DisplayName("빌드 명령을 입력하면 data.json 파일 생성")
    void t10() throws IOException {
        String input = """
            등록
            현재를 사랑하라.
            작자미상
            빌드
            종료
            """;

        String output = runAppTest(input);

        File dataFile =
                new File("db/wiseSaying/data.json");

        assertThat(output)
                .contains("data.json 파일의 내용이 갱신되었습니다.");

        assertThat(dataFile.exists()).isTrue();
    }

    @Test
    @DisplayName("빌드 명령을 입력하면 data.json 파일 생성")
    void t11() throws IOException {
        String input = """
            등록
            현재를 사랑하라.
            작자미상
            빌드
            종료
            """;

        String output = runAppTest(input);

        File dataFile = new File("db/wiseSaying/data.json");

        assertThat(output)
                .contains("data.json 파일의 내용이 갱신되었습니다.");

        assertThat(dataFile.exists()).isTrue();
    }
}