package com.restfulapi.repository.dehumidifier;

import com.restfulapi.entity.dehumidifier.DehumidifierRealDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DehumidifierRealDeviceRepository extends JpaRepository<DehumidifierRealDevice,Long> {
    boolean existsByMac(String mac);
}
