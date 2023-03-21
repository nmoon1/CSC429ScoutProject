# Compiling and Running From the Command Line

Directory Structure:
Project(root)/
	classes/
	database/
    model/
	…
	Main.java
	mariadb-java-client-3.0.3.jar

We will be using the java and javac commands from the command line. This guide assumes you are working from a windows command prompt.

## Compiling

To compile we will use the javac command. It has the following format:
javac <options> <files>
<options> - any options to pass to the command (read the documentation online for more info)
<files> - a list of files to compile

You can simply type the paths to files that you wish to compile manually if you wish but this is tedious and annoying so we will use what is called an argFile. The argFile is simply a file that contains the paths to all of our source files (anything with a .java extension). To create this file run the following command from the root directory of your project:

dir /s /B *.java > sources.txt

This will output the complete path to all files under your root directory with a .java extension and write the output to a file called sources.txt (it will create the file if it doesn’t exist).

** Note if any directories have spaces in the folder name you will need to wrap the path in quotes to prevent the compiler from treating the spaces as delimiters **

For example, if your project directory is named CSC429 Assignment1 (notice the space) then you will need to go into your sources.txt file and wrap each path in quotes.

CSC429 Assignment1/path/to/file.java -> “CSC429 Assignment1/path/to/file.java” (notice the quotes)

Now that you have a reference to all of your source files you can compile them. Run the following command from the root directory of your project:

javac -d classes @sources.txt

The -d option tells the compiler where to output the compiled .class files. In this case they will placed in the classes directory. Note that this directory must exist but can be empty. The compiler will create the correct directory structure based off the package structure in your project.
The @sources.txt will tell the compiler to read the sources.txt file and compile each file that is listed there.
If you didn’t get any errors here you should check your classes folder and you should see all of the compiled .class files.

JavaFX:
There is an extra step needed in order to compile our source files when using javafx. First you need to find where your javafx sdk is installed. Once you locate that package you need to reference all of the .jar files under the javafx-sdk/lib folder when compiling. Just as we made an argFile for our sources we can make one for our options. Make a file called options.txt at the root of the project. You can specify the options you want to pass each on a new line. We need to add the -cp (class path) option in order to tell the compiler where to find the javafx files. The options.txt file should look something like this:
-d classes
-cp “C:\\Program Files\\Java\\javafx-sdk-19.0.2.1\\lib\\javafx.base.jar”;"add the other paths"

You need to specify each of the .jar files under the lib folder in your javafx sdk. Separate each path with a semicolon ( ; ) and wrap the path in double quotes if there are spaces anywhere in the path.

You can now compile your source files with the following command:

javac @options.txt @sources.txt

## Running

To run the program we will use the java command. It has the format:

java <options> <main_class>

Run the following command from the root directory of your project:

java -cp mariadb-java-client-3.0.3.jar;classes; Main

The -cp option stands for “class path” and indicates where to find the compiled classes. Note that we also need to specify where to find the database driver here. Each class path is separated by a semi-colon.
The second part, Main, specifies the name of the main class to run. In our case that is just “Main”. Note the space between the class paths and the Main class.

JavaFX:
When running JavaFX programs we also need to add the javafx modules. To do this we will use the 
--module-path and the --add-modules options. The module-path option should be passed the path to the lib folder of your javafx sdk. The add-modules option will be the same for everyone so run the following command, replacing the path to the javafx lib folder with your own path:

java -cp mariadb-java-client-3.0.3.jar;classes; --module-path “c:/Program Files/Java/javafx-sdk-19.0.2.1/lib” --add-modules=javafx.controls,javafx.fxml,javafx.graphics Main

Automation:
To make running and compiling simple you can create .bat files to run these commands. I would recommend making three .bat files and place them in the root directory of your project:

1.	compile.bat -> compiles only
2.	run-only.bat -> runs only
3.	run.bat -> compiles and runs

In your compile.bat file copy the command you used to compile from above:

javac @options.txt @sources.txt

In your run-only.bat file copy the command you used to run from above:

java -cp mariadb-java-client-3.0.3.jar;classes; --module-path “c:/Program Files/Java/javafx-sdk-19.0.2.1/lib” --add-modules=javafx.controls,javafx.fxml,javafx.graphics Main

In your run.bat file put the following:

compile && run-only

Then to execute the files enter the file name into the command line from the root directory of your project. I.e type “run” into the command line (from the root of your project) and hit enter. You should see output in the console and your program should run.