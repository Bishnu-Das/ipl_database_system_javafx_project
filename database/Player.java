package database;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String country;
    private int age;
    private double height;
    private String club;
    private String position;
    private int number;
    private int weeklySalary;

    Player() {

    }

    public Player(String name, String country, int age, double height, String club, String position, int number,
            int weeklySalary) {
        this.name = name;
        this.country = country;
        this.age = age;
        this.height = height;
        this.club = club;
        this.position = position;
        this.number = number;
        this.weeklySalary = weeklySalary;
    }

    public void displayDetails() {

        System.out.println("---------------------");
        System.out.println("Name: " + name);
        System.out.println("Country: " + country);
        System.out.println("Age: " + age);
        System.out.println("Height: " + height);
        System.out.println("Club: " + club);
        System.out.println("Position: " + position);
        if (this.number != -1)
            System.out.println("Jersey number: " + number);
        else
            System.out.println("Jersey number: Unknown");
        System.out.println("Weekly Salary: " + weeklySalary);
        System.out.println("---------------------");
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public String getClub() {
        return club;
    }

    public String getPosition() {
        return position;
    }

    public int getNumber() {
        return number;
    }

    public double getWeeklySalary() {
        return weeklySalary;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setClub(String club) {
        this.club = club;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Player player = (Player) obj;
        return Objects.equals(this.name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" + "Country: " + country + "\nAge: " + age + "\nHeight: " + height + "\nclub"
                + club + "\nPosition" + position + "\nJersy number: " + number + "\nWeekly Salary: " + weeklySalary;
    }
}
