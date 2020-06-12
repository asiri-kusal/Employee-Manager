/**
 * The UserDetailsServiceImpl class for fetch user information.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.security.services;

import lk.dialog.ms.models.User;
import lk.dialog.ms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

        @Autowired
    	UserRepository userRepository;

    	/**
    	 * Fetch user by name
    	 * @param username
    	 * @return @{@link UserDetails}
    	 * @throws UsernameNotFoundException
    	 */
    	@Override
    	@Transactional
    	public UserDetails loadUserByUsername(String username) {
    		User user = userRepository.findByUsername(username)
    								  .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    		return UserDetailsImpl.build(user);
    	}
}