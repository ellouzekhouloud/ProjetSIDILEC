package tn.sidilec.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.sidilec.Entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRoleAndIsReadFalse(String role);
    List<Notification> findByRole(String role);
    void deleteByBlId(Long blId);
}