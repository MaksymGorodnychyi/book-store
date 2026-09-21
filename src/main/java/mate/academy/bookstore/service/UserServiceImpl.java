package mate.academy.bookstore.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.RoleName;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.RoleRepository;
import mate.academy.bookstore.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user. Email already exists: "
                    + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() ->
                        new RegistrationException("Can't find role: " + RoleName.ROLE_USER));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        shoppingCartService.createShoppingCart(savedUser);
        return userMapper.toDto(savedUser);
    }
}
