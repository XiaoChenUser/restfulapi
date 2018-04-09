package com.restfulapi.repository.dehumidifier;

import com.restfulapi.entity.dehumidifier.DehumidifierOuterUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface DehumidifierOuterUnitRepository extends JpaRepository<DehumidifierOuterUnit,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
