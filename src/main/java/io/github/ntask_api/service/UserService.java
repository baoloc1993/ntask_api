package io.github.ntask_api.service;

import io.github.ntask_api.domain.Authority;
import io.github.ntask_api.domain.Event;
import io.github.ntask_api.domain.User;
import io.github.ntask_api.repository.AuthorityRepository;
import io.github.ntask_api.repository.EventRepository;
import io.github.ntask_api.repository.TaskRepository;
import io.github.ntask_api.repository.UserRepository;
import io.github.ntask_api.security.AuthoritiesConstants;
import io.github.ntask_api.security.SecurityUtils;
import io.github.ntask_api.service.dto.AdminUserDTO;
import io.github.ntask_api.service.dto.EventDTO;
import io.github.ntask_api.service.dto.TaskDTO;
import io.github.ntask_api.service.dto.UserDTO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import io.github.ntask_api.web.rest.errors.NotFoundException;
import io.github.ntask_api.web.rest.vm.KeyAndPasswordVM;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final EventRepository eventRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final FolderProvider folderProvider;

    public UserDTO findById(Long id) {
        return userRepository.findById(id).map(UserDTO::new).orElseThrow(NotFoundException::new);
    }

    public Page<EventDTO> findEventsBy(Long uid, Pageable pageable) {
        return eventRepository.findAllBy(uid, pageable).map(EventDTO::new);
    }

    public Page<TaskDTO> findTasksBy(Long uid, Long eid, Pageable pageable) {
        return taskRepository.findAllBy(uid, eid, pageable).map(TaskDTO::new);
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> pwResetStep1(String key) {
        return userRepository
                .findOneByResetKey(key)
                .map(u -> {
                    u.setEligibleResettingPw(Boolean.TRUE);
                    this.clearUserCaches(u);
                    return u;
                });
    }

    public Optional<User> completePasswordReset(KeyAndPasswordVM vm) {
        String key = vm.getKey();
        String newPassword = vm.getNewPassword();
//        String currentPassword = vm.getCurrentPassword();
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .map(user -> {
//                String currentEncryptedPassword = user.getPassword();
//                if (!passwordEncoder.matches(currentPassword, currentEncryptedPassword)) {
//                    throw new InvalidPasswordException();
//                }
                if(user.getEligibleResettingPw()) {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setEligibleResettingPw(Boolean.FALSE);
                    this.clearUserCaches(user);
                    return user;
                }

                return null;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setName(userDTO.getName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER_ID).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

//    public User createUser(AdminUserDTO userDTO) {
//        User user = new User();
//        user.setLogin(userDTO.getLogin().toLowerCase());
//        user.setName(userDTO.getName());
//        if (userDTO.getEmail() != null) {
//            user.setEmail(userDTO.getEmail().toLowerCase());
//        }
//
//        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
//        user.setPassword(encryptedPassword);
//        user.setResetKey(RandomUtil.generateResetKey());
//        user.setActivated(true);
//        if (userDTO.getAuthorities() != null) {
//            Set<Authority> authorities = userDTO
//                .getAuthorities()
//                .stream()
//                .map(authorityRepository::findByName)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toSet());
//            user.setAuthorities(authorities);
//        }
//        userRepository.save(user);
//        this.clearUserCaches(user);
//        log.debug("Created Information for User: {}", user);
//        return user;
//    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
//    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
//        return Optional
//            .of(userRepository.findById(userDTO.getId()))
//            .filter(Optional::isPresent)
//            .map(Optional::get)
//            .map(user -> {
//                this.clearUserCaches(user);
//                user.setName(userDTO.getName());
//                user.setActivated(userDTO.isActivated());
//                Set<Authority> managedAuthorities = user.getAuthorities();
//                managedAuthorities.clear();
//                userDTO
//                    .getAuthorities()
//                    .stream()
//                    .map(authorityRepository::findByName)
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .forEach(managedAuthorities::add);
//                this.clearUserCaches(user);
//                log.debug("Changed Information for User: {}", user);
//                return user;
//            })
//            .map(AdminUserDTO::new);
//    }

    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional
                .of(userRepository.findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    this.clearUserCaches(user);
                    user.setName(userDTO.getName());
                    byte[] avatar = userDTO.getAvatar();
                    if(avatar != null && avatar.length > 0) {
                        String filename = UUID.randomUUID().toString();
                        Path avtPath = folderProvider.getFolder().resolve(filename);
                        try (OutputStream output = Files.newOutputStream(avtPath)){
                            IOUtils.copy(new ByteArrayInputStream(avatar), output);
                            user.setAvatarUrl(filename);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    user.setBio(userDTO.getBio());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO
                            .getRoles()
                            .stream()
                            .map(authorityRepository::findByName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .forEach(managedAuthorities::add);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param name name of user.
     * @param email     email id of user.
     */
    public void updateUser(String name, String email) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setName(name);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllPublicUsers(String q, Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(q, pageable).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public UserDTO bookmarkEvents(Long uid, List<Long> eventIds) {
        User u = userRepository.findById(uid).orElseThrow(NotFoundException::new);

        Set<Event> bookmarkEvents = u.getBookmarkEvents();
        if(bookmarkEvents != null) {
            bookmarkEvents.clear();
        }

        List<Event> allById = eventRepository.findAllById(eventIds);
        u.setBookmarkEvents(new HashSet<>(allById));

        return new UserDTO(u);
    }

    public List<EventDTO> getBookmarkEvents(Long uid) {
        User u = userRepository.findById(uid).orElseThrow(NotFoundException::new);
        return u.getBookmarkEvents().stream().map(EventDTO::new).collect(Collectors.toList());
    }
}
