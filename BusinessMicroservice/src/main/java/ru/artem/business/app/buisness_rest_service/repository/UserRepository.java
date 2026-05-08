package ru.artem.business.app.buisness_rest_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.artem.business.app.buisness_rest_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.wallet w JOIN FETCH w.transactions WHERE u.id = :userId ")
    // Берем всего юзера, затем соединяем кошелек этого юзера, затем транзакции
    // этого юзера для юзера с нужным id
    Optional<User> findAllTransactionsFromUserById(@Param("userId") String id);

}