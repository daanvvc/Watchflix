//package fontys.IA.repositories.fakeimpl;
//
//import fontys.IA.domain.User;
//import fontys.IA.domain.enums.UserRole;
//import fontys.IA.repositories.IUserRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//
//public class UserRepositoryFakeImpl implements IUserRepository {
//    private List<User> users;
//
//    public UserRepositoryFakeImpl() {
//        users = new ArrayList<>();
//
//        User userOne = new User(UUID.fromString("6df588f1-854d-4939-b800-bc6eb5cc450c"),
//                "96DdrQFtjgHn2NXvIRbjKSvt3ufS3OnkU5ayadZsqATHcK50", "john.doe", UserRole.USER);
//        User userTwo = new User(UUID.fromString("4bf2a1b1-4550-44ad-9f90-0f2a6f2862d8"),
//                "+a7UrVlskxLG2M7lKSboIWTn3aTPS0ytKNcT4UO0n95ZLgrK7CIn", "daanvervaecke", UserRole.ADMIN);
//
//        users.add(userOne);
//        users.add(userTwo);
//    }
//
//    public Optional<User> findById(UUID userId) {
//        for (User user : users) {
//            if (user.getId().equals(userId)) {
//                return Optional.of(user);
//            }
//        }
//
//        return Optional.empty();
//    }
//
//    public Optional<String> findRoleById(UUID userId) {
//        for (User user : users) {
//            if (user.getId().equals(userId)) {
//                return Optional.of(user.getRole().toString());
//            }
//        }
//
//        return Optional.empty();
//    }
//    public User save(User user) {
//        users.add(user);
//
//        return user;
//    }
//}
