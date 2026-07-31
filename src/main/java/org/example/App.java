package org.example;

import org.example.controller.WiseSayingController;
import org.example.repository.WiseSayingRepository;
import org.example.service.WiseSayingService;

import java.io.IOException;
import java.util.Scanner;

public class App {

    private final Scanner sc;
    private final WiseSayingController controller;

    public App() {
        this(new Scanner(System.in));
    }

    public App(Scanner sc) {
        this.sc = sc;

        // 저장을 담당하는 리포지터리 생성
        WiseSayingRepository repository =
                new WiseSayingRepository();

        // 데이터 처리를 담당하는 서비스 생성
        WiseSayingService service =
                new WiseSayingService(repository);

        // 사용자 입력과 출력을 담당하는 컨트롤러 생성
        this.controller =
                new WiseSayingController(sc, service);
    }

    public void run() throws IOException {
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine();

            // 명령어와 id 값 분리
            String[] commandParts = command.split("\\?id=");
            String action = commandParts[0];

            switch (action) {
                case "등록":
                    controller.actionCreate();
                    break;

                case "목록":
                    controller.actionList();
                    break;

                case "삭제":
                    controller.actionDelete(commandParts);
                    break;

                case "수정":
                    controller.actionUpdate(commandParts);
                    break;

                case "빌드":
                    controller.actionBuild();
                    break;

                case "종료":
                    return;

                default:
                    System.out.println("잘못된 명령어입니다.");
                    break;
            }
        }
    }
}