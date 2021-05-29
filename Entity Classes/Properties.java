/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.AssignmentOne;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "properties")

public class Properties implements Serializable {

    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)  
    private Long id;
    private String street;
    private String city;
    private Integer listing_Num;
    private Integer style_Id;
    private Integer type_Id;
    private Integer bedrooms;
    private Float bathrooms;
    private Integer squarefeet;
    private String ber_Rating;
    @Lob
    private String description;
    private String lotsize;
    private Short garagesize;
    private Integer garage_Id;
    private Integer agent_Id;
    private String photo;
    private Double price;
    @Temporal(TemporalType.DATE)
    private Date date_Added;
}
