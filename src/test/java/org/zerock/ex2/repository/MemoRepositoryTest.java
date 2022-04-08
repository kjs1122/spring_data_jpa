package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemoRepositoryTest {

    @Autowired
    MemoRepository memoRepository;

    @Test
    void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);
        }) ;
    }

//    @Transactional
//    @Test
//    void testSelect() {
//
//        Long mno = 100L;
//
//        Memo memo = memoRepository.getOne(mno);
//        System.out.println("==========================================");
//
// /*       if (result.isPresent()) {
//            Memo memo = result.get();*/
//            System.out.println("memo = " + memo);
//    }

    @Test
    void testUpdate() {

        Memo memo = Memo.builder().mno(100l).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    void testDelete() {

        Long mno = 100L;

        memoRepository.deleteById(mno);
    }

    @Test
    void testPageDefault() {

        // Spring Data JPA를 이용할 때 페이지 처리는 반드시 '0'부터 시작한다
        // 1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println("result = " + result);

        System.out.println("---------------------------------------");

        System.out.println("Total Pages : " + result.getTotalPages()); // 총 몇 페이지
        System.out.println("Total Elements : " + result.getTotalElements()); // 전체 개수
        System.out.println("Page Number : " + result.getNumber()); // 현재 페이지 번호
        System.out.println("Page Size : " + result.getSize()); // 페이지당 데이터 개수
        System.out.println("has next page?  : " + result.hasNext()); // 다음 페이지 존재 여부
        System.out.println("has prev page?  : " + result.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("first page?  : " + result.isFirst()); // 시작 페이지(0) 존재 여부

        System.out.println("---------------------------------------");

        for (Memo memo : result.getContent()) {
            System.out.println("memo = " + memo);
        }
    }

    @Test
    void testSort() {

        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println("memo = " + memo);
        });
    }

    @Test
    void testQueryMethod() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        for (Memo memo : memoRepository.findByMnoBetween(70L, 80L, pageable)) {
            System.out.println("memo = " + memo);
        }
    }

    @Commit
    @Transactional
    @Test
    void testDeleteQueryMethod() {
        memoRepository.deleteByMnoLessThan(10L);
    }
}