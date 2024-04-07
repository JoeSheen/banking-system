package com.sheen.joe.bankingsystem.repository;

import com.sheen.joe.bankingsystem.entity.AuditView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuditViewRepository extends PagingAndSortingRepository<AuditView, Long> {

    Optional<AuditView> findByCommitId(Long commitId);

    Page<AuditView> findAllByEntityIdAndAuthor(String entityId, String author, Pageable pageable);

}
