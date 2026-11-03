# Smart Password Generator

A web app that generates random passwords and rates their strength. The backend is written in Java and the frontend is a simple HTML page.

## Features

- Choose password length (4 to 32)
- Choose character types: uppercase, lowercase, numbers, symbols
- Shows strength: Weak, Medium or Strong
- Copy the password with one click

## Technologies Used

- Java (built-in HTTP server)
- HTML, CSS, JavaScript

## Files

- `Server.java`: starts the server and handles requests
- `PasswordGenerator.java`: creates the password and checks its strength
- `index.html`: the web page

## How to Run

1. Install JDK 8 or later.
2. Open a terminal in the project folder and run:

```
javac Server.java PasswordGenerator.java
java Server
```

3. Open http://localhost:9090 in your browser.
