package be.businesstraining.repository;

import be.businesstraining.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RolesRepository extends JpaRepository<Role,Long> {

    Role findByRole(String role);

}
