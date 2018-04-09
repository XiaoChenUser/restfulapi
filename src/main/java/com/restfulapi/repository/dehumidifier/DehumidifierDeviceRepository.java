package com.restfulapi.repository.dehumidifier;

import com.restfulapi.entity.dehumidifier.DehumidifierDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DehumidifierDeviceRepository extends JpaRepository<DehumidifierDevice,Long> {
     boolean existsByMac(String mac);
}
