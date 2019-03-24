package com.mfava.akcetask.repo;

import com.mfava.akcetask.model.Audit_Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<Audit_Log, Long> {

}
