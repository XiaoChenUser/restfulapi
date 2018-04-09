package com.restfulapi.repository.aircondition;

import com.restfulapi.entity.aircondition.AirInnerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface AirInnerStatusRepository extends JpaRepository<AirInnerStatus,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
