package com.restfulapi.repository.dehumidifier;

import com.restfulapi.entity.dehumidifier.DehumidifierInnerUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface DehumidifierInnerUnitRepository extends JpaRepository<DehumidifierInnerUnit,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
