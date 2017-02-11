package net.couchdev.android.layoutsandbox.model;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Tim on 04.01.2017.
 */

public class Userdata {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Calendar dateOfBirth;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String country;
    private String state;
    private String city;
    private String zip;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        //Log.d("Userdata", "username: " + username);
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        //Log.d("Userdata", "firstName: " + firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        //Log.d("Userdata", "lastName: " + lastName);
        this.lastName = lastName;
    }

    public Calendar getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDateOfBirthString() {
        int day = dateOfBirth.get(Calendar.DAY_OF_MONTH);
        int month = dateOfBirth.get(Calendar.MONTH) + 1;
        int year = dateOfBirth.get(Calendar.YEAR);
        return String.format("%02d.%02d.%d", day, month, year);
    }

    public void setDateOfBirth(String dateOfBirth) {
        //Log.d("Userdata", "dateOfBirth: " + dateOfBirth);
        if(dateOfBirth != null) {
            String[] values = dateOfBirth.split("[.]");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(values[0]));
            cal.set(Calendar.MONTH, Integer.parseInt(values[1]) - 1);
            cal.set(Calendar.YEAR, Integer.parseInt(values[2]));
            this.dateOfBirth = cal;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        //Log.d("Userdata", "email: " + email);
        this.email = email;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        //Log.d("Userdata", "addressLine1: " + addressLine1);
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        //Log.d("Userdata", "addressLine2: " + addressLine2);
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        //Log.d("Userdata", "addressLine3: " + addressLine3);
        this.addressLine3 = addressLine3;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        //Log.d("Userdata", "country: " + country);
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        //Log.d("Userdata", "state: " + state);
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        //Log.d("Userdata", "zip: " + zip);
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        //Log.d("Userdata", "city: " + city);
        this.city = city;
    }

}
