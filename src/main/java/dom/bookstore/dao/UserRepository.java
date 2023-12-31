package dom.bookstore.dao;

import dom.bookstore.domain.Book;
import dom.bookstore.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query(value = "SELECT * FROM users u WHERE u.first_name = :firstName " +
            "AND u.last_name = :lastName AND u.address_line_1 = :addressLine1 " +
            "AND u.address_line_2 = :addressLine2 AND u.post_code = :postCode", nativeQuery=true)
    Optional<Users> findUserByUserDetailsIgnoreCase(@Param("firstName") String firstName,
                                                    @Param("lastName") String lastName,
                                                    @Param("addressLine1") String addressLine1,
                                                    @Param("addressLine2") String addressLine2,
                                                    @Param("postCode") String postCode);

}
