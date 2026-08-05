import java.util.Scanner;

/**
 * Directory
 * ADSA Assignment-1
 *
 * TODO:
 * 1. Implement hash tables
 * 2. Implement insertion
 * 3. Implement deletion
 * 4. Implement modification
 */
public class Directory {

    // -----------------------------
    // Constants
    // -----------------------------

    private static final int TABLES = 9; // Total tables
    private static final int TABLE_SIZE = 20; // Slots in each table


    // -----------------------------
    // Record Class
    // -----------------------------
    // Represents one contact in the directory
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


    // -----------------------------
    // Slot Status
    // -----------------------------
    enum Status {
        EMPTY,
        OCCUPIED,
        DELETED
    }


    // -----------------------------
    // 9 Hash Tables
    // -----------------------------
    // tables[0] -> T1
    // tables[1] -> T2
    // ...
    // tables[8] -> T9
    private Record[][] tables = new Record[TABLES][TABLE_SIZE];


    // -----------------------------
    // Constructor
    // -----------------------------
    public Directory() {

        // Initialize every slot as EMPTY

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


    // =====================================================
    // Helper Functions
    // =====================================================

    /**
     * Returns table index (0-8)
     * from first digit of phone number.
     *
     * Example:
     * 5482910
     * ->
     * tableIndex = 4 (T5)
     */
    private int getTableIndex(String phone) {        

        return (phone.charAt(0) - '0') - 1;
    }


    /**
     * Returns hash value
     * x % 20
     *
     * x = last six digits
     */
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


    // =====================================================
    // Required Operations
    // =====================================================

    /**
     * Insert a new record.
     *
     * Steps:
     * 1. Check duplicate phone
     * 2. Find table
     * 3. Compute hash
     * 4. Linear probing
     * 5. Insert record
     */
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


    /**
     * Delete a record.
     *
     * Steps:
     * 1. Search record
     * 2. Mark slot as DELETED
     */
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


    /**
     * Modify record.
     *
     * Case 1:
     * Address only
     *
     * Case 2:
     * Phone changes
     */
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
            System.out.println("Only address is modified");
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
                System.out.println("Phone number and address modified.");
                return true;
            }
        }
        return false;
    }


    /**
     * Searches a phone number.
     *
     * Helpful for delete and modify.
     */
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


    /**
     * Prints all records.
     *
     * Useful for debugging.
     */
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


    // =====================================================
    // Driver
    // =====================================================

    public static void main(String[] args) {

        Directory directory =
                new Directory();

        Scanner sc = new Scanner(System.in);

        while(true) {

            System.out.println("\n Directory ");

            System.out.println("1. Insert");
            System.out.println("2. Delete");
            System.out.println("3. Modify");
            System.out.println("4. Display");
            System.out.println("5. Exit");

            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {

                case 1:{
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Phone Number: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter Address: ");
                    String address = sc.nextLine();
                    boolean inserted = directory.insert(name, phone, address);
                    if (inserted) System.out.println("Record inserted successfully.");
                    else System.out.println("Insertion failed.");

                    break;
                }
                case 2:{
                    System.out.print("Enter Phone Number: ");
                    String phone = sc.nextLine();
                    boolean delete = directory.delete(phone);
                    if (delete) System.out.println("Record deleted successfully.");
                    else System.out.println("Deletion failed.");

                    break;
                }

                case 3:{
                    System.out.print("Enter Old Phone Number: ");
                    String oldPhone = sc.nextLine();
                    System.out.print("Enter New Address: ");
                    String newAddress = sc.nextLine();
                    System.out.print("Enter New Phone Number: ");
                    String newPhone = sc.nextLine();
                    boolean modified = directory.modify(oldPhone, newAddress, newPhone);
                    if (modified) System.out.println("Record modified successfully.");
                    else System.out.println("Modification failed.");

                    break;
                }

                case 4:

                    directory.display();

                    break;

                case 5:

                    System.out.println("The End!");
                    return;

                default:

                    System.out.println("Invalid Choice");
            }
        }
    }
}