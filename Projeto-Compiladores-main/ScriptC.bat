@echo off
javac -cp %cd% *.java
java -cp %cd% Main.java

echo Compilando codigo
gcc Code.c
echo Excutando codigo
a.exe
