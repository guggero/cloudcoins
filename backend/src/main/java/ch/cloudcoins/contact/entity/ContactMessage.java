package ch.cloudcoins.contact.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contactmessage_seq")
    @SequenceGenerator(name = "contactmessage_seq", sequenceName = "contactmessage_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    @Column(length = 4096)
    private String message;

    public ContactMessage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
