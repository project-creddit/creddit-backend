package com.creddit.credditmainserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;



public enum Authority {
   ROLE_USER, ROLE_ADMIN
}
