package de.szut.lf8_starter.exceptionHandling;

public class EmployeeAlreadyInproject extends RuntimeException {
  public EmployeeAlreadyInproject(String message) {
    super(message);
  }
}
