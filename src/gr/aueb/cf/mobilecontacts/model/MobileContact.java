package gr.aueb.cf.mobilecontacts.model;

import java.util.Objects;

public class MobileContact extends AbstractEntity implements IdentifiableEntity {
    private UserDetails userDetails;
    private String phoneNumber;

    public MobileContact() {

    }

    public MobileContact(long id, UserDetails userDetails, String phoneNumber) {
        this.setId(id);
        this.userDetails = new UserDetails(userDetails);
        this.phoneNumber = phoneNumber;
    }

    public MobileContact(MobileContact mobileContact) {
        this.setId(mobileContact.getId());
        this.userDetails = new UserDetails(mobileContact.getUserDetails());
        this.phoneNumber = mobileContact.getPhoneNumber();
    }

    public UserDetails getUserDetails() {
        return new UserDetails(userDetails);
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = new UserDetails(userDetails);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Firstname: " + userDetails.getFirstname() +
                "Lastname: " + userDetails.getLastname() +
                "Phone number: " + getPhoneNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobileContact that = (MobileContact) o;
        return Objects.equals(getUserDetails(), that.getUserDetails()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getUserDetails());
        result = 31 * result + Objects.hashCode(getPhoneNumber());
        return result;
    }
}
