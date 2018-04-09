package com.restfulapi.repository.aircondition;

import com.restfulapi.entity.aircondition.AirOuterUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface AirOuterUnitRepository extends JpaRepository<AirOuterUnit,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
