import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Directory {

    private static final int TABLES = 9; // Total tables
    private static final int TABLE_SIZE = 20; // Slots in each table

    static class Record {

        String name;
        String phone;
        String address;
        Status status; //EMPTY / OCCUPIED / DELETED

        Record(String name, String phone, String address, Status status) {
            this.name = name;
            this.phone = phone;
            this.address = address;
            this.status = status;
        }
    }

    enum Status {
        EMPTY,
        OCCUPIED,
        DELETED
    }

    private Record[][] tables = new Record[TABLES][TABLE_SIZE];

    public Directory() {

        for(int i = 0; i < TABLES; i++) {

            for(int j = 0; j < TABLE_SIZE; j++) {

                tables[i][j] = new Record(
                        "",
                        "",
                        "",
                        Status.EMPTY
                );

            }
        }

    }

    private int getTableIndex(String phone) {        

        return (phone.charAt(0) - '0') - 1;
    }

    private int hash(String phone) {

        String lastSix = phone.substring(1);
        int num = Integer.parseInt(lastSix);
        return (num % 20);

    }


    private boolean validPhone(String phone){

        if(phone ==  null || phone.length() !=7){
            System.out.println("Invalid Number Length");
            return false;
        }

        for(char ch : phone.toCharArray()){
            if(!Character.isDigit(ch)){
                System.out.println("Invalid Number: Has Other Characters");
                return false;
            }
        }

        if(phone.charAt(0) == '0'){
            System.out.println("Invalid First Digit");
            return false;
        }

        return true;
    }

    public boolean insert(String name, String phone, String address) {


        if (!validPhone(phone)) return false; // Check phone validity
        int index = getTableIndex(phone); // Find table
        int hashValue = hash(phone); // Compute hash
        if(search(phone,index,hashValue)!=-1){  // Check duplicate phone
            System.out.println("Phone already exists.");
            return false;
        }

        //Linear Probing
        for(int i = 0; i<TABLE_SIZE; i++){
            int idx = (hashValue+i)%TABLE_SIZE;
            if(tables[index][idx].status == Status.EMPTY || tables[index][idx].status == Status.DELETED){
                tables[index][idx] = new Record(name,phone,address,Status.OCCUPIED);
                return true;
            }
        }

        System.out.println("Table's full");
        return false; // Insertion Unsuccessful
    }

    public boolean delete(String phone) {

        if (!validPhone(phone)) return false; // Check phone validity
        int index = getTableIndex(phone); // Find table
        int hashValue = hash(phone); // Compute hash
        int slot = search(phone,index,hashValue);
        
        if(slot == -1){
            System.out.println("Record unavailable");
            return false;
        }

        tables[index][slot].status = Status.DELETED;

        return true;
    }

    public boolean modify(String oldPhone, String newAddress, String newPhone) {

        if (!validPhone(oldPhone)) return false; // Check phone validity
        int index = getTableIndex(oldPhone); // Find table
        int hashValue = hash(oldPhone); // Compute hash
        int slot = search(oldPhone,index,hashValue);
        
        if(slot == -1){
            System.out.println("Record unavailbale");
            return false;
        }

        if(newPhone == null || newPhone.isEmpty()){
            tables[index][slot].address = newAddress;
            return true;
        }
        else{
            if (!validPhone(newPhone)) return false; // Check phone validity
            String oldName = tables[index][slot].name; // Store the old name
            // If only the phone numer is changed, pass address as null or empty
            String address = (newAddress == null || newAddress.isEmpty()) ? tables[index][slot].address : newAddress;
            int newIndex = getTableIndex(newPhone); // Find table
            int newHashValue = hash(newPhone); // Compute hash
            if(search(newPhone,newIndex,newHashValue)!=-1){  // Check duplicate phone
                System.out.println("Phone already exists.");
                return false;
            }

            delete(oldPhone);
            if (insert(oldName, newPhone, address)) {
                return true;
            }
        }
        return false;
    }

    private int search(String phone, int index, int hashValue) {

        for(int i=0 ; i<TABLE_SIZE; i++){

            int curr = (hashValue+i)%TABLE_SIZE;

            if(tables[index][curr].status == Status.EMPTY) return -1; // No Record

            if(tables[index][curr].status == Status.OCCUPIED){
                if(tables[index][curr].phone.equals(phone)) return curr; // Record Found
            } 

            // If status is DELETED then just move to the next slot and wrap around but only 20 times

        }

        return -1;
    }

    public void display() {
        for (int i = 0; i < TABLES; i++) {
            System.out.println("\n Table T" + (i + 1));
            for (int j = 0; j < TABLE_SIZE; j++) {
                System.out.print("Slot " + j + " : ");
                if (tables[i][j].status == Status.EMPTY) {
                    System.out.println("EMPTY");
                }
                else if (tables[i][j].status == Status.DELETED) {
                    System.out.println("DELETED");
                }
                else {
                    System.out.println( "Name: " + tables[i][j].name + ", Phone: " + tables[i][j].phone + ", Address: " + tables[i][j].address );
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        Directory directory = new Directory();

        Scanner sc = new Scanner(new File("input.txt"));
        PrintWriter out = new PrintWriter("output.txt");

        while(sc.hasNextLine()) {

            String line = sc.nextLine();
            String[] section = line.split(" ");

            switch(section[0].toLowerCase()) {

                case "insert":{
                    boolean inserted = directory.insert(section[1], section[2], section[3]);
                    out.println(inserted);

                    break;
                }
                case "delete":{
                    boolean delete = directory.delete(section[1]);
                    out.println(delete);

                    break;
                }

                case "modify":{
                    String newPhone = "";
                    if(section.length == 4){ // If new phone number is passed
                        newPhone = section[3];
                    }
                    boolean modified = directory.modify(section[1], section[2], newPhone);
                    out.println(modified);

                    break;
                }

                case "debug":

                    directory.display();

                    break;
            }
        }
        sc.close();
        out.close();
    }
}