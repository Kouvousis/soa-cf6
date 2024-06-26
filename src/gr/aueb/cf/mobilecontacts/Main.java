package gr.aueb.cf.mobilecontacts;

import gr.aueb.cf.mobilecontacts.dao.IMobileContactDAO;
import gr.aueb.cf.mobilecontacts.dao.MobileContactDAOImpl;
import gr.aueb.cf.mobilecontacts.dto.*;
import gr.aueb.cf.mobilecontacts.model.MobileContact;
import gr.aueb.cf.mobilecontacts.model.UserDetails;
import gr.aueb.cf.mobilecontacts.service.IMobileContactService;
import gr.aueb.cf.mobilecontacts.service.MobileContactServiceImpl;
import gr.aueb.cf.mobilecontacts.service.exceptions.ContactNotFoundException;
import gr.aueb.cf.mobilecontacts.service.exceptions.PhoneNumberAlreadyExistsException;
import gr.aueb.cf.mobilecontacts.service.exceptions.UserIdAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    // Wiring
    private final static IMobileContactDAO dao = new MobileContactDAOImpl();
    private final static IMobileContactService service = new MobileContactServiceImpl(dao);

    public static void main(String[] args) {
        String choice = "";

        while (true) {
            printMenu();
            System.out.println("Please insert a choice.");
            choice = getChoice();

            if (choice.matches("[qQ]")) {
                System.out.println("Goodbye. Thanks for using our app.");
                break;
            }

            if (!choice.matches("[1-5]")) {
                System.out.println("Error in choice.");
                continue;
            }

            processWithChoice(choice);
        }
    }

    public static void processWithChoice(String choice) {
        MobileContact contact;
        long id;
        String phoneNumber;

        switch (choice) {
            case "1":
                contact = getMobileContact();
                insertContact(contact);
                break;
            case "2":
                System.out.println("Please insert the id of the contact you wish to update.");
                id = scanner.nextLong();
                System.out.println("Please insert the details of the new contact.");
                contact = getMobileContact();
                updateContact(id, contact);
                break;
            case "3":
                System.out.println("Please insert the id of the contact you wish to delete.");
                if (scanner.hasNextLong()) {
                    id = scanner.nextLong();
                    scanner.nextLine(); // Consume the newline
                    deleteContact(id);
                } else {
                    System.out.println("Invalid input. Please enter a valid numeric ID.");
                    scanner.nextLine(); // Clear the invalid input
                }
                break;
            case "4":
                System.out.println("Please insert the phone number of the contact you wish to search for.");
                phoneNumber = scanner.nextLine().trim();
                MobileContactReadOnlyDTO dto = getMobileContactByPhoneNumber(phoneNumber);
                if (dto == null) {
                    System.out.println("Contact not found.");
                } else {
                    System.out.println(dto);
                }
                break;
            case "5":
                List<MobileContactReadOnlyDTO> contacts = getMobileContacts();
                printContacts(contacts);
                break;
            default:
                System.out.println("Generic error");
                break;

        }
    }

    private static void printContacts(List<MobileContactReadOnlyDTO> contacts) {
        contacts.forEach(System.out::println);
    }

    public static void insertContact(MobileContact mobileContact) {
        try {
            MobileContactInsertDTO insertDTO = mapToContactInsertDto(mobileContact);
            MobileContact mc = service.insertMobileContact(insertDTO);
            System.out.println("Successful insert: " + mc);
        } catch (PhoneNumberAlreadyExistsException | UserIdAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateContact(long id, MobileContact mobileContact) {
        try {
            MobileContactUpdateDTO updateDTO = mapToContactUpdateDto(mobileContact);
            MobileContact mc = service.updateMobileContact(id, updateDTO);
            System.out.println("Successful insert: " + mc);
        } catch (PhoneNumberAlreadyExistsException | UserIdAlreadyExistsException | ContactNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteContact(long id) {
        try {
            service.deleteMobileContactById(id);
            System.out.println("Deletion was successful");
        } catch (ContactNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static MobileContactReadOnlyDTO getMobileContactByPhoneNumber(String phoneNumber) {
        MobileContact mc = null;

        try {
            mc = service.getMobileContactByPhoneNumber(phoneNumber);

        } catch (ContactNotFoundException e) {
            System.out.println(e.getMessage());
        }
        if (mc == null) return null;
        return mapToContactReadOnlyDto(mc);
    }

    public static List<MobileContactReadOnlyDTO> getMobileContacts() {
        List<MobileContact> contacts = service.getAllMobileContacts();
        return mapToContactsReadOnlyDto(contacts);
    }

    public static MobileContact getMobileContact() {
        MobileContact mobileContact = new MobileContact();
        System.out.println("Please insert the contact id.");
        mobileContact.setId(scanner.nextLong());


        UserDetails userDetails = new UserDetails();
        System.out.println("Please insert the user id.");
        userDetails.setId(scanner.nextLong());
        scanner.nextLine();

        System.out.println("Please insert the first name.");
        userDetails.setFirstname(scanner.nextLine());

        System.out.println("Please insert the last name.");
        userDetails.setLastname(scanner.nextLine());

        System.out.println("Please insert the phone number.");
        mobileContact.setPhoneNumber(scanner.nextLine());

        mobileContact.setUserDetails(userDetails);
        return mobileContact;
    }

    private static UserDetailsInsertDTO mapToUserDetailsInsertDto(UserDetails userDetails) {
        return new UserDetailsInsertDTO(userDetails.getId(), userDetails.getFirstname(), userDetails.getLastname());
    }

    private static MobileContactInsertDTO mapToContactInsertDto(MobileContact mobileContact) {
        return  new MobileContactInsertDTO((mobileContact.getId()),
                mapToUserDetailsInsertDto(mobileContact.getUserDetails()), mobileContact.getPhoneNumber());
    }

    private static MobileContactUpdateDTO mapToContactUpdateDto(MobileContact mobileContact) {
        return new MobileContactUpdateDTO(mobileContact.getId(), mapToUserDetailsDto(mobileContact.getUserDetails()), mobileContact.getPhoneNumber());
    }

    private static UserDetailsUpdateDTO mapToUserDetailsDto(UserDetails userDetails) {
        return  new UserDetailsUpdateDTO(userDetails.getId(), userDetails.getFirstname(), userDetails.getLastname());
    }

    private static MobileContactReadOnlyDTO mapToContactReadOnlyDto(MobileContact mobileContact) {
        return new MobileContactReadOnlyDTO(mobileContact.getId(), mapToContactReadOnlyDto(mobileContact.getUserDetails()), mobileContact.getPhoneNumber());
    }

    private static UserDetailsReadOnlyDTO mapToContactReadOnlyDto(UserDetails userDetails) {
        return  new UserDetailsReadOnlyDTO(userDetails.getId(), userDetails.getFirstname(), userDetails.getLastname());
    }

    private static List<MobileContactReadOnlyDTO> mapToContactsReadOnlyDto(List<MobileContact> contacts) {
        List<MobileContactReadOnlyDTO> contactReadOnlyDTOS = new ArrayList<>();
        for (MobileContact contact : contacts) {
            contactReadOnlyDTOS.add(mapToContactReadOnlyDto(contact));
        }
        return contactReadOnlyDTOS;
    }

    public static void printMenu() {
        System.out.println("Choose one of the following");
        System.out.println("1. Insert contact.");
        System.out.println("2. Update contact.");
        System.out.println("3. Delete contact.");
        System.out.println("4. Search contact with phone number.");
        System.out.println("5. Print all contacts.");
        System.out.println("6. Q/q for exit.");
    }

    public static String getChoice() {
        return scanner.nextLine().trim();
    }

}
