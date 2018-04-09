package com.restfulapi.repository.aircondition;

import com.restfulapi.entity.aircondition.AirCtlResp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface AirCtlRespRepository extends JpaRepository<AirCtlResp,Long> {
    boolean existsByMacAndCtime(String mac, Date date);
}
