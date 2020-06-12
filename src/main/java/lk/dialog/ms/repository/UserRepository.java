/**
 * The UserRepository use to handle user related CRUD operations.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.repository;

import java.util.List;
import java.util.Optional;

import lk.dialog.ms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	    /**
    	 *  Find user by name
    	 * @param username name of user
    	 * @return @{@link User}
    	 */
    	@Transactional
    	Optional<User> findByUsername(String username);

    	/**
    	 * Check whether user is exists by name
    	 * @param username name of user
    	 * @return is exists true else false
    	 */
    	@Transactional
    	Boolean existsByUsername(String username);

    	/**
    	 * Check whether user is exists by email
    	 * @param email
    	 * @return is exists true else false
    	 */
    	@Transactional
    	Boolean existsByEmail(String email);

    	/**
    	 * Save or update User
    	 * @param @{@link User}
    	 * @return @{@link User}
    	 */
    	@Transactional
    	@Override
    	User save(User s);

    	/**
    	 * Find all users
    	 * @return @{@link List}@{@link User}
    	 */
    	@Transactional
    	@Override
    	List<User> findAll();
}
