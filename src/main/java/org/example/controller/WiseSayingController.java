package org.example.controller;

import org.example.WiseSaying;
import org.example.service.WiseSayingService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class WiseSayingController {

    private final Scanner sc;
    private final WiseSayingService service;

    public WiseSayingController(
            Scanner sc,
            WiseSayingService service
    ) {
        this.sc = sc;
        this.service = service;
    }

    // 명언 등록 화면
    public void actionCreate() throws IOException {
        System.out.print("명언 : ");
        String content = sc.nextLine();

        System.out.print("작가 : ");
        String author = sc.nextLine();

        WiseSaying createdWiseSaying =
                service.create(content, author);

        System.out.println(
                createdWiseSaying.getId()
                        + "번 명언이 등록되었습니다."
        );
    }

    // 명언 목록 화면
    public void actionList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayingList = service.findAll();

        for (WiseSaying wiseSaying : wiseSayingList) {
            System.out.println(
                    wiseSaying.getId()
                            + " / "
                            + wiseSaying.getAuthor()
                            + " / "
                            + wiseSaying.getContent()
            );
        }
    }

    // 명언 삭제 화면
    public void actionDelete(String[] commandParts) throws IOException {
        int deleteId = Integer.parseInt(commandParts[1]);

        boolean isDeleted = service.delete(deleteId);

        if (!isDeleted) {
            System.out.println(
                    deleteId + "번 명언은 존재하지 않습니다."
            );
            return;
        }

        System.out.println(
                deleteId + "번 명언이 삭제되었습니다."
        );
    }

    // 명언 수정 화면
    public void actionUpdate(String[] commandParts) throws IOException {
        int updateId = Integer.parseInt(commandParts[1]);

        WiseSaying foundWiseSaying =
                service.findById(updateId);

        if (foundWiseSaying == null) {
            System.out.println(
                    updateId + "번 명언은 존재하지 않습니다."
            );
            return;
        }

        System.out.println(
                "명언(기존) : " + foundWiseSaying.getContent()
        );
        System.out.print("명언 : ");
        String changedContent = sc.nextLine();

        System.out.println(
                "작가(기존) : " + foundWiseSaying.getAuthor()
        );
        System.out.print("작가 : ");
        String changedAuthor = sc.nextLine();

        service.update(
                updateId,
                changedContent,
                changedAuthor
        );
    }

    // data.json 파일 생성 처리
    public void actionBuild() throws IOException {
        service.build();

        System.out.println(
                "data.json 파일의 내용이 갱신되었습니다."
        );
    }
}