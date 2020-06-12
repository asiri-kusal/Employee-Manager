/**
 * The RoleRepository use to handle role related CRUD operations.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.repository;

import java.util.List;
import java.util.Optional;

import lk.dialog.ms.models.ERole;
import lk.dialog.ms.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	        /**
        	 * Find user role by name
        	 * @param name of the role
        	 * @return @{@link Role}
        	 */
        	@Transactional
        	Optional<Role> findByName(ERole name);

        	/**
        	 * Save or update user role
        	 * @param @{@link Role}
        	 * @return @{@link Role}
        	 */
        	@Transactional
        	@Override
        	Role save(Role s);

        	/**
        	 * Find all roles
        	 * @return @{@link List} @{@link Role}
        	 */
        	@Transactional
        	@Override
        	List<Role> findAll();
}