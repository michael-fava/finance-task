package com.mfava.financetask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long transactionId;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "debit_account", referencedColumnName = "account_id", nullable = false)
    private Account debitAccount;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "credit_account", referencedColumnName = "account_id", nullable = false)
    private Account creditAccount;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "message", length = 200, nullable = false)
    private String message;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false)
    private Date dateCreated;

}
