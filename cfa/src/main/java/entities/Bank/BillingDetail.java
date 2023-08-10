package entities.Bank;

import entities.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "billing_details")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BillingDetail extends BaseEntity {

    private String number;
    private User user;

    public BillingDetail() {

    }

    @Column(unique = true)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
