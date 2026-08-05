# Telephone Directory using Hash Tables

## Overview

This program creates a telephone directory using **9 hash tables** with **linear probing** for collision resolution. Each record stores a person's **name**, **phone number**, and **address**.

---

## Prerequisites

* Java JDK 8 or later

---

## Compilation

Compile all Java source files:

```bash
javac Directory.java
```

---

## Running the Program

Run the program using:

```bash
java Directory.java
```

---

## Menu Options

```
Directory

1. Insert
2. Delete
3. Modify
4. Display
5. Exit
```

Enter the corresponding option number to perform the desired operation.

---

## User Prompts

### Insert

```
Enter Name:
Enter Phone Number:
Enter Address:
```

### Delete

```
Enter Phone Number:
```

### Modify

```
Enter Existing Phone Number:
```

Choose one of the following:

* **Modify Address Only**

  ```
  Enter New Address:
  ```

* **Modify Phone Number**

  ```
  Enter New Phone Number:
  Enter New Address (Press Enter to keep the existing address):
  ```

### Display

Displays all hash tables along with the records stored in each slot.

---

## Phone Number Validation

A valid phone number must:

* Contain exactly **7 digits**
* Contain only numeric characters
* Begin with a digit between **1 and 9**

Invalid phone numbers are rejected before any operation is performed.

---

## Notes

* Duplicate phone numbers are not allowed.
* Deleted records are marked as **DELETED** and reused during future insertions.
* Linear probing is used for insertion, searching, deletion, and modification.
