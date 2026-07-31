package org.example.service;

import org.example.WiseSaying;
import org.example.repository.WiseSayingRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingService {

    // 등록된 명언을 저장할 배열
    private final WiseSaying[] wiseSayingList = new WiseSaying[100];

    // 현재까지 등록된 명언 개수
    private int count = 0;

    private final WiseSayingRepository repository;

    public WiseSayingService(WiseSayingRepository repository) {
        this.repository = repository;
    }

    // 명언 등록
    public WiseSaying create(String content, String author) throws IOException {
        int newId = count + 1;

        WiseSaying newWiseSaying = new WiseSaying(
                newId,
                content,
                author
        );

        wiseSayingList[count] = newWiseSaying;
        count++;

        // 등록된 명언과 마지막 번호를 파일에 저장
        repository.save(newWiseSaying);
        repository.saveLastId(count);

        return newWiseSaying;
    }

    // 등록된 명언을 최신순으로 조회
    public List<WiseSaying> findAll() {
        List<WiseSaying> resultList = new ArrayList<>();

        for (int i = count - 1; i >= 0; i--) {
            if (wiseSayingList[i] != null) {
                resultList.add(wiseSayingList[i]);
            }
        }

        return resultList;
    }

    // 번호에 해당하는 명언 조회
    public WiseSaying findById(int id) {
        if (id <= 0 || id > count) {
            return null;
        }

        return wiseSayingList[id - 1];
    }

    // 명언 삭제
    public boolean delete(int id) throws IOException {
        WiseSaying foundWiseSaying = findById(id);

        if (foundWiseSaying == null) {
            return false;
        }

        // 메모리에서 삭제
        wiseSayingList[id - 1] = null;

        // 저장된 명언 파일도 함께 삭제
        repository.delete(id);

        return true;
    }

    // 명언 수정
    public boolean update(
            int id,
            String changedContent,
            String changedAuthor
    ) throws IOException {

        WiseSaying foundWiseSaying = findById(id);

        if (foundWiseSaying == null) {
            return false;
        }

        foundWiseSaying.setContent(changedContent);
        foundWiseSaying.setAuthor(changedAuthor);

        // 수정된 내용을 기존 파일에 다시 저장
        repository.save(foundWiseSaying);

        return true;
    }

    // 전체 명언을 data.json 파일로 생성
    public void build() throws IOException {
        repository.build();
    }
}