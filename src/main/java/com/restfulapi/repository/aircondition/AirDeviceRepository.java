package com.restfulapi.repository.aircondition;

import com.restfulapi.entity.aircondition.AirDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirDeviceRepository extends JpaRepository<AirDevice,Long> {
     boolean existsByMac(String mac);
}
