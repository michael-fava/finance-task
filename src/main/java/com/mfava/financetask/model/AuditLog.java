package com.mfava.financetask.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "audit_log")
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name="url")
    private String url;

    @Column(name="requestMethod")
    private String requestMethod;

    @Column(name="request")
    private String request;

    @Column(name="response", columnDefinition = "TEXT")
    private String response;

}
