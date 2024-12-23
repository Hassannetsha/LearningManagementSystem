package org.example.lmsproject.userPart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Response {
    @Id
    private Long id;
    @Column(nullable=false)
    private int state;

    public Response(Long id, int state) {
        this.id = id;
        this.state = state;
    }

    public Response() {

    }
}
