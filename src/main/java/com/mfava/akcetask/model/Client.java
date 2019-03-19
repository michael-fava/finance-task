package com.mfava.akcetask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private long clientId;

    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "surname", length = 200, nullable = false)
    private String surname;

    @OneToOne
    @JoinColumn(name = "primary_address", referencedColumnName = "address_id", nullable = false)
    private Address primaryAddress;

    @OneToOne
    @JoinColumn(name = "secondary_address", referencedColumnName = "address_id", nullable = true)
    private Address secondaryAddress;

    @ElementCollection(targetClass = Account.class)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "client_accounts",
            joinColumns = {@JoinColumn(name = "client_id", referencedColumnName = "client_id")},
            inverseJoinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "account_id", unique = true)})
    private List<Account> accountList;

}
