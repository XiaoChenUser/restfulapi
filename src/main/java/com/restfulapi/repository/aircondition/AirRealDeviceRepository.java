package com.restfulapi.repository.aircondition;

import com.restfulapi.entity.aircondition.AirRealDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirRealDeviceRepository extends JpaRepository<AirRealDevice,Long> {
    boolean existsByMac(String mac);
}
