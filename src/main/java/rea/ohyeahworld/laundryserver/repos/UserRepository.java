package rea.ohyeahworld.laundryserver.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import rea.ohyeahworld.laundryserver.entities.Users;

public interface UserRepository extends JpaRepository<Users, String> {
}
