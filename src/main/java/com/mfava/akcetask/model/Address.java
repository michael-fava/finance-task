package com.mfava.akcetask.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "address_line1", length = 255, nullable = false)
    private String addressLine1;

    @Column(name = "address_line2", length = 255, nullable = true)
    private String addressLine2;

    @Column(name = "city", length = 255, nullable = false)
    private String city;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "country", referencedColumnName = "country_id", nullable = false)
    private Country country;
}
