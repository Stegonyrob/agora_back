package de.stella.agora_web.security;

import de.stella.agora_web.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

  UserRepository repository;

  public JpaUserDetailsService(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    return repository
      .findByUsername(username)
      .map(SecurityUser::new)
      .orElseThrow(() ->
        new UsernameNotFoundException("User not found" + username)
      );
  }
}
// @Override
// public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//     User user = userRepository.findByUsername(username)
//         .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//     return new org.springframework.security.core.userdetails.User(
//         user.getUsername(),
//         user.getPassword(),
//         getAuthorities(new ArrayList<>(user.getRoles()))
//     );
// }
// private List<GrantedAuthority> getAuthorities(List<Role> roles) {
//     List<GrantedAuthority> authorities = new ArrayList<>();
//     for (Role role : roles) {
//         authorities.add(new SimpleGrantedAuthority(role.getName()));
//     }
//     return authorities;
// }
