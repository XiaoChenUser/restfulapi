package com.restfulapi.repository.dehumidifier;

import com.restfulapi.entity.dehumidifier.DehumidifierInnerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface DehumidifierInnerStatusRepository extends JpaRepository<DehumidifierInnerStatus,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
