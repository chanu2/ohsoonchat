package chatversion3.ohsoonchat.repo;



import chatversion3.ohsoonchat.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {

    @Override
    Optional<Member> findById(Long id);


    Optional<Member> findByName(String name);
}