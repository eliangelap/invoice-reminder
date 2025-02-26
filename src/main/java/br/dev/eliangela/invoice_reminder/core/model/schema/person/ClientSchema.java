package br.dev.eliangela.invoice_reminder.core.model.schema.person;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tblclients")
public class ClientSchema {

    @Id
    @Column(name = "userid")
    private Integer userId;

    private String company;
    private String vat;

    @Column(name = "phonenumber")
    private String phoneNumber;
    private Integer country;
    private String city;
    private String zip;
    private String state;
    private String address;
    private String website;

    @Column(name = "datecreated", nullable = false)
    private LocalDateTime dateCreated;

    private Integer active;
    private Integer leadid;
    private String billingStreet;
    private String billingCity;
    private String billingState;
    private String billingZip;
    private Integer billingCountry;
    private String shippingStreet;
    private String shippingCity;
    private String shippingState;
    private String shippingZip;
    private Integer shippingCountry;
    private String longitude;
    private String latitude;
    private String defaultLanguage;
    private Integer defaultCurrency;
    private Integer showPrimaryContact;
    private String stripeId;
    private Integer registrationConfirmed;

    @Column(name = "addedfrom", nullable = false)
    private Integer addedFrom;
}
