package com.restfulapi.repository.dehumidifier;

import com.restfulapi.entity.dehumidifier.DehumidifierCtlResp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface DehumidifierCtlRespRepository extends JpaRepository<DehumidifierCtlResp,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
