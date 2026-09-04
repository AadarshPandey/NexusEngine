package com.nexusengine.core.repository;
import com.nexusengine.core.model.CmsSubject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
public interface CmsSubjectRepository extends JpaRepository<CmsSubject, Long>, JpaSpecificationExecutor<CmsSubject> {
    List<CmsSubject> findByCategoryId(Long categoryId, Pageable pageable);
    List<CmsSubject> findByTitleContaining(String title, Pageable pageable);
}
