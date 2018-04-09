package com.restfulapi.repository.aircondition;

import com.restfulapi.entity.aircondition.AirInnerUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface AirInnerUnitRepository extends JpaRepository<AirInnerUnit,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
