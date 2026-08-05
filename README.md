# Telephone Directory using Hash Tables

## Overview

This program creates a telephone directory using **9 hash tables** with **linear probing** for collision resolution. Each record stores a person's **name**, **phone number**, and **address**. The program reads commands from **input.txt** and writes the output to **output.txt**.

---

## Prerequisites

* Java JDK 8 or later

---

## Compilation

Compile all Java source files:

```
javac Directory.java
```

---

## Running the Program

Run the program using:

```
java Directory
```

---

## Input File

The program reads commands from `input.txt`.

Supported commands are:

```
Insert <Name> <PhoneNumber> <Address>
Delete <PhoneNumber>
Modify <OldPhoneNumber> <NewAddress>
Modify <OldPhoneNumber> <NewAddress> <NewPhoneNumber>
```

Example:

``` 
Insert Bob 5482930 456_Oak_Street
Insert Alice 5482362 452_Oak_Street
Modify 5482362 452_Brown_Street
Delete 5482362
```

---

## Output

The program writes the return value of each command to `output.txt`.

Example:

```
true
true
true
false
```

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
* Deleted records are marked as **DELETED** and those slots are reused during future insertions.
* Linear probing is used for insertion, searching, deletion, and modification.
