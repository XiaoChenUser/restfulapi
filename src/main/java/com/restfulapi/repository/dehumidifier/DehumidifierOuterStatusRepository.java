package com.restfulapi.repository.dehumidifier;

import com.restfulapi.entity.dehumidifier.DehumidifierOuterStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface DehumidifierOuterStatusRepository extends JpaRepository<DehumidifierOuterStatus,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
