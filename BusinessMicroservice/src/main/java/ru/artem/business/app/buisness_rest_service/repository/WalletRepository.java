package ru.artem.business.app.buisness_rest_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.artem.business.app.buisness_rest_service.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {

}
