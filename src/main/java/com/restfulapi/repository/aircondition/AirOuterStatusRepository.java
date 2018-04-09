package com.restfulapi.repository.aircondition;

import com.restfulapi.entity.aircondition.AirOuterStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface AirOuterStatusRepository extends JpaRepository<AirOuterStatus,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
