# Simon Swift 🎮
Formative Task 2 – Group Project (CS1704)

## Overview
This project replicates the classic *Simon* memory game using a SwiftBot and Java.  
The program displays a sequence of colors via the SwiftBot LEDs, and the player must repeat the pattern using the SwiftBot buttons.

## Features
- Four LEDs mapped to A/B/X/Y buttons  
- Increasing difficulty after each correct round  
- Score tracking and round display  
- Optional quit every 5 rounds  
- Celebration dive for scores ≥ 5  
- Command-line interface  

## Group Members
- Nafiul Alam – Project Manager
- Noorjahan Hasan – Project Manager
- Aaron Bartholomew – Developer
- Arkin Rana – Developer
- Dhruvesh Shatish Babu – Developer
- Nehemina Odoom – Developer
- Saskia Pinto – Developer
- Emmanuel Matthew – Developer
- Naimul Alif – Developer

---

## 🕹️ How to Run

### 1. Prerequisites
Ensure you have the following installed:
- **Java 17** or higher  
- **Apache Maven 3.8+**

### 2. Verify Installation:
```bash
java -version
```

### 3. Clone the Repository
```bash
git clone https://github.com/Gl1tchzz/Simon-Swift.git
cd Simon-Swift
```

### 4. Compile the Project
```bash
mkdir -p bin
javac -cp "lib/SwiftBot-API-6.0.0.jar -d bin src/*.java
```

### 5. Run the Application
```bash
java -cp "lib/SwiftBot-API-6.0.0.jar:bin" Main
```
