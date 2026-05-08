package ru.artem.business.app.buisness_rest_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artem.business.app.buisness_rest_service.enums.CategoryEnum;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "transaction_amount", nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "categories", nullable = false)
    private CategoryEnum categories;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "time_transaction")
    private LocalDateTime transactionTime;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public Transaction(BigDecimal transactionAmount, CategoryEnum categories, String description, Wallet wallet) {
        this.transactionAmount = transactionAmount;
        this.categories = categories;
        this.description = description;
        this.transactionTime = LocalDateTime.now();
        this.wallet = wallet;
    }

}
