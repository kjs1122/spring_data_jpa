package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    List<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    void deleteByMnoLessThan(Long num);

    @Query("select m from Memo m order by m.mno desc ")
    List<Memo> getListDesce();

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText =:#{#param.memoText} where m.mno = :#{#param.memo}")
    int updateMemoTextV2(@Param("param") Memo memo);

    @Query(value = "select m from Memo m where m.mno > :mno",
            countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Object[]> getListWithQuery(Long mno, Pageable pageable);


    @Query(value = "select m.mno, m.memoText, current_date from Memo m where m.mno > :mno",
            countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Memo> getListWithQueryObject(Long mno, Pageable pageable);

    @Query(value = "select * from memo where mno > 0", nativeQuery = true)
    List<Object[]> getNativeResulte();
}
