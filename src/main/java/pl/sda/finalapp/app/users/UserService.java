package pl.sda.finalapp.app.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public void registerUser(UserRegistrationDTO dto) {
        userRepository.findByEMail(dto.geteMail())
                .ifPresent(e -> {
                    throw new EmailAlreadyExistsException("Email: " + dto.geteMail() + " jest już zarejestrowany.");
                });
        String passwordHash = passwordEncoder.encode(dto.getPassword());
        User user = User.applyDTO(dto, passwordHash);
        user.addRole(roleRepository.findByRoleName(Role.USER));
        userRepository.save(user);
    }
}
