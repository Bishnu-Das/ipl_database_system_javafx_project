package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManagement implements Serializable{
    List<Player> players = new ArrayList<>();
    List<Club> clubs = new ArrayList<>();
    List<String> clubNamesList = new ArrayList<>();
    List<String> countryNameList = new ArrayList<>();
    private HashMap<String, String> clubPasswords = new HashMap<>();

    List<List<Player>> playerCountryWise = new ArrayList<>();
    List<List<Player>> playerClubWise = new ArrayList<>();

    PlayerManagement() throws FileNotFoundException, IOException {
        String filename = "database\\resource\\players.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length != 8) {
                    continue;
                }
                try {
                    String name = fields[0].trim();
                    String country = fields[1].trim();
                    int age = Integer.parseInt(fields[2].trim());
                    double height = Double.parseDouble(fields[3].trim());
                    String club = fields[4].trim();
                    String position = fields[5].trim();
                    int number = -1;
                    if (!fields[6].trim().isEmpty()) {
                        number = Integer.parseInt(fields[6].trim());
                    }
                    int salary = Integer.parseInt((fields[7].trim()));
                    players.add(new Player(name, country, age, height, club, position, number, salary));
                } catch (NumberFormatException e) {
                    System.out.println("Number format error in line: " + line);
                    e.printStackTrace();
                }

            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader("database\\resource\\clubs.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] fields = line.split(",");
                if (fields.length != 2) {
                    continue;
                }
                clubNamesList.add(fields[0].trim());
                clubPasswords.put(fields[0].trim(), fields[1].trim());
            }
        }
        setCountryNameList();
        setClubNameList();
        setClubList();
        organizeByClub();
        organizeByCountries();
    }

    boolean findStringInList(List<String> list, String str) {
        for (String s : list) {
            if (s.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void setClubNameList() {
        for (Player player : players) {
            if (!findStringInList(clubNamesList, player.getClub())/*  && !player.getClub().equalsIgnoreCase("none")*/) {
                clubNamesList.add(player.getClub());
            }
        }
    }
    private void setCountryNameList() {
        for (Player player : players) {
            if (!findStringInList(countryNameList, player.getCountry())) {
                countryNameList.add(player.getCountry());
            }
        }
    }

    private void setClubList() {
        clubs.clear();
        for (String clubName : clubNamesList) {
            clubs.add(new Club(clubName, players));
        }
    }

    //adding a new player
    public boolean addPlayer(Player player) {
        boolean status = false;
        for(Player player2:players){
            if(player2.getName().equals(player.getName())){
                return status;
            }
        }
        players.add(player);
        for (Club club : clubs) {
            if (club.getName().equals(player.getClub())) {
                club.addPlayer(player);
            }
        }
        status = true;
        setClubNameList();
        setClubList();
        organizeByCountries();
        organizeByClub();
        return status;
    }
    public void addPlayerFromUser(String name, String country, int age, double height, String club, String position,
            int num, int salary) {
        Player newPlayer = new Player(name, country, age, height, club, position, num, salary);

        String fileName = "TestJavaFX\\src\\singlescene\\database\\resource\\players.txt";
        String line = "\n" + name + "," + country + "," + age + "," + height + "," + club + "," + position + "," + num
                + "," + salary;

        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(line);
        } catch (IOException e) {
            System.out.println("file not opening here");
        }

        for (Player player : players) {
            if (name.equalsIgnoreCase(player.getName())) {
                System.out.println("Player with same name exist");
                return;
            }
        }

        players.add(newPlayer);
        for (Club club1 : clubs) {
            if (club1.getName().equals(club)) {
                club1.addPlayer(newPlayer);
            }
        }
        setClubNameList();
        setClubList();
        organizeByClub();
        organizeByCountries();
    }


    public void removePlayer(Player player) {
        players.remove(player);
        for (Club club : clubs) {
            if (club.getName().equals(player.getClub())) {
                club.removePlayer(player);
            }
        }
        setClubNameList();
        setClubList();
        organizeByClub();
    }
    public void changeClub(Player player, String newClub){
        for(Player p: players){
            if(p.getName().equals(player.getName())){
                p.setClub(newClub);
            }
        }
        setClubNameList();
        setClubList();
        organizeByClub();
    }

    //getters are here
    public List<Player> getAllPlayers() {
        return players;
    }
    public List<Club> getAllClubs() {
        return clubs;
    }
    public HashMap<String, String> getClubPasswords() {
        return clubPasswords;
    }
    public Club getClub(String clubName) {
        for (Club club : clubs) {
            if (club.getName().equals(clubName)) {
                return club;
            }
        }
        return null;
    }

    public void writePlayerInFile(List<Player> players2) {
        String filename = "database\\resource\\players.txt";
        System.out.println("trying to write player in file");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Player player : players2) {
                String line = String.format("%s,%s,%d,%.2f,%s,%s,%d,%.0f",
                        player.getName(),
                        player.getCountry(),
                        player.getAge(),
                        player.getHeight(),
                        player.getClub(),
                        player.getPosition(),
                        player.getNumber(),
                        player.getWeeklySalary());
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Player details successfully written to the file.");
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
        
    }




    private void organizeByClub() {
        setClubNameList();
        while (playerClubWise.size() < clubNamesList.size()) {
            playerClubWise.add(new ArrayList<>());
        }
        for (Player player : players) {
            int clubIndex = clubNamesList.indexOf(player.getClub());
            if (clubIndex != -1) {
                playerClubWise.get(clubIndex).add(player);
            } else {
                System.err.println("Error: Club not found in clubNamesList");
            }
        }
    }
    private void clearPlayerCountryWiseList() {
        for(List<Player> list: playerCountryWise) {
            list.clear();
        }
    }
    private void organizeByCountries() {
        setCountryNameList();
        clearPlayerCountryWiseList();
        while(playerCountryWise.size() < countryNameList.size()) {
            playerCountryWise.add(new ArrayList<>());
        }
        for(Player player: players) {
            int countryIndex = countryNameList.indexOf(player.getCountry());
            if(countryIndex != -1) {
                playerCountryWise.get(countryIndex).add(player);
            } else {
                System.err.println("Error: Country not found in countryNameList");
            }
        }
    }


    //all search functions are here
    public Player searchByName(String name) {
        for (Player player : players) {
            if (name.equalsIgnoreCase(player.getName())) {
                return player;
            }
        }
        return null;
    }
    public List<Player> searchByClubAndCountry(String club, String country) {
        List<Player> newPlayers = new ArrayList<>();
        if (club.equalsIgnoreCase("any")) {
            for (Player player : playerCountryWise.get(isContain(countryNameList, country))) {
                newPlayers.add(player);
            }
        } else {
            for (Player player : players) {
                if (player.getClub().equalsIgnoreCase(club) && player.getCountry().equalsIgnoreCase(country)) {
                    newPlayers.add(player);
                }
            }
        }
        return newPlayers;

    }
    public HashMap<String, Integer> countryWiseCount() {
        HashMap<String, Integer> hash = new HashMap<>();
        for (String country : countryNameList) {
            //System.out.println(country + " : " + playerCountryWise.get(countryNameList.indexOf(country)).size());
            hash.put(country, playerCountryWise.get(countryNameList.indexOf(country)).size());
        }
        return hash;
    }
    public List<Player> searchInRangeSalary(int low, int high) {
        List<Player> newPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getWeeklySalary() >= low && player.getWeeklySalary() <= high) {
                newPlayers.add(player);
            }
        }
        return newPlayers;
    }
    public List<Player> searchByPosition(String position) {
        List<Player> newPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getPosition().equalsIgnoreCase(position)) {
                newPlayers.add(player);
            }
        }
        return newPlayers;
    }


    public int isContain(List<String> list, String key) {
        int i = 0;
        for (String ele : list) {
            if (ele.equalsIgnoreCase(key)) {
                return i;
            }
            i++;
        }
        return -1;
    }

   public void writePasswordOnFile() {
        File file = new File("database\\resource\\clubs.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> entry : clubPasswords.entrySet()) {
                String clubName = entry.getKey();
                String password = entry.getValue();
                writer.write(clubName + "," + password);
                writer.newLine();
            }
            //System.out.println("Passwords successfully written to file.");
        } catch (IOException e) {
            System.err.println("Error writing passwords to file: " + e.getMessage());
            e.printStackTrace();
        }
}

    

}